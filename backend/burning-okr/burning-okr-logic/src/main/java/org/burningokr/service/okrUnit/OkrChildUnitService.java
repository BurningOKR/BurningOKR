package org.burningokr.service.okrUnit;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okrUnit.OkrBranchRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.repositories.okrUnit.OkrUnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okr.TaskBoardService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.hibernate.Hibernate;
import org.hibernate.TypeMismatchException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class OkrChildUnitService<T extends OkrChildUnit> {
  private final OkrUnitRepository<T> okrUnitRepository;
  private final OkrDepartmentRepository okrDepartmentRepository;
  private final OkrBranchRepository okrBranchRepository;
  private final ObjectiveRepository objectiveRepository;
  private final EntityCrawlerService entityCrawlerService;
  private final ActivityService activityService;
  private final OkrUnitRepository<OkrUnit> parentOkrUnitRepository;
  private final TaskBoardService taskBoardService;

  // TODO (C.K. 07.08.2023): refactor. this can not be the solution
  public T findById(long unitId) {
    return (T) Hibernate.unproxy(okrUnitRepository.findById(unitId).orElseThrow(() -> {
      log.warn("Could not find OkrUnit with id %d".formatted(unitId));
      return new EntityNotFoundException();
    }));
  }

  public Collection<Objective> findObjectivesOfUnit(long unitId) {
    var okrUnit = findById(unitId);
    return objectiveRepository.findByUnitAndOrderBySequence(okrUnit);
  }

  @PreAuthorize("@childUnitAuthorizationService.hasManagerPrivilegesForChildUnit(#updatedUnit.getId())")
  public T updateUnit(T updatedUnit) throws ForbiddenException {
    T referencedUnit = okrUnitRepository.findByIdOrThrow(updatedUnit.getId());

    throwIfCycleForOkrChildUnitIsClosed(referencedUnit);

    if (updatedUnit instanceof OkrDepartment updatedOkrDepartment) {
      updateOkrDepartment(updatedOkrDepartment, (OkrDepartment) referencedUnit);
    } else if (updatedUnit instanceof OkrBranch updatedOkrBranch) {
      updateOkrBranch(updatedOkrBranch, (OkrBranch) referencedUnit);
    } else {
      log.warn("Cannot update Unit with id: %d, unknown ChildUnitType".formatted(updatedUnit.getId()));
      throw new TypeMismatchException("Cannot update Unit with id: %d, unknown ChildUnitType".formatted(updatedUnit.getId()));
    }

    activityService.createActivity(
      referencedUnit,
      Action.EDITED
    );

    return referencedUnit;
  }

  @PreAuthorize("@childUnitAuthorizationService.hasManagerPrivilegesForChildUnit(#unitId)")
  public void deleteChildUnit(Long unitId) {
    T referencedUnit = okrUnitRepository.findByIdOrThrow(unitId);
    throwIfCycleForOkrChildUnitIsClosed(referencedUnit);

    okrUnitRepository.deleteById(unitId);
    log.debug("Deleted OkrDepartment (id: %d) and its children".formatted(unitId));
    activityService.createActivity(referencedUnit, Action.DELETED);
  }

  @PreAuthorize("@childUnitAuthorizationService.hasManagerPrivilegesForChildUnit(#parentUnitId)")
  public OkrChildUnit createChildUnit(Long parentUnitId, OkrChildUnit okrChildUnit) {
    okrChildUnit.setId(null);
    OkrUnit parentOkrUnit = parentOkrUnitRepository.findByIdOrThrow(parentUnitId);

    throwIfCycleForOkrChildUnitIsClosed(parentOkrUnit);

    okrChildUnit.setParentOkrUnit(parentOkrUnit);

    if (okrChildUnit instanceof OkrDepartment okrDepartment) {
      initializeOkrDepartment(okrDepartment);
    }

    okrChildUnit = parentOkrUnitRepository.save(okrChildUnit);

    log.debug(
      "Created subdepartment: %s (id: %d) into OkrDepartment %s (id: %d)"
        .formatted(
          okrChildUnit.getName(),
          okrChildUnit.getId(),
          okrChildUnit.getParentOkrUnit().getName(),
          okrChildUnit.getParentOkrUnit().getId()
        ));

    activityService.createActivity(okrChildUnit, Action.CREATED);
    return okrChildUnit;
  }

  private void initializeOkrDepartment(OkrDepartment department) {
    department.setOkrTopicDescription(new OkrTopicDescription(department.getName()));
    department.setTaskBoard(taskBoardService.createNewTaskBoardWithDefaultStates(department));
  }

  private void updateOkrBranch(OkrBranch updatedOkrDepartment, OkrBranch databaseOkrBranch) {
    databaseOkrBranch.setName(updatedOkrDepartment.getName());
    databaseOkrBranch.setLabel(updatedOkrDepartment.getLabel());
    databaseOkrBranch.setActive(updatedOkrDepartment.isActive());

    okrBranchRepository.save(databaseOkrBranch);
    log.debug("Updated OkrBranch %s (id: %d)"
      .formatted(
        databaseOkrBranch.getName(),
        databaseOkrBranch.getId()
      )
    );
  }

  private void updateOkrDepartment(OkrDepartment updatedOkrDepartment, OkrDepartment databaseOkrDepartment) {
    databaseOkrDepartment.setName(updatedOkrDepartment.getName());
    databaseOkrDepartment.setPhoto(updatedOkrDepartment.getPhoto());
    databaseOkrDepartment.setLabel(updatedOkrDepartment.getLabel());
    databaseOkrDepartment.setActive(updatedOkrDepartment.isActive());
    databaseOkrDepartment.setOkrMasterId(updatedOkrDepartment.getOkrMasterId());
    databaseOkrDepartment.setOkrTopicSponsorId(updatedOkrDepartment.getOkrTopicSponsorId());
    databaseOkrDepartment.setOkrMemberIds(updatedOkrDepartment.getOkrMemberIds());

    okrDepartmentRepository.save(databaseOkrDepartment);
    log.debug("Updated OkrDepartment %s (id: %d)"
      .formatted(
        databaseOkrDepartment.getName(),
        databaseOkrDepartment.getId()
      )
    );
  }


  public void throwIfCycleForOkrChildUnitIsClosed(OkrUnit okrUnitToCheck) throws ForbiddenException {
    if (entityCrawlerService.getCycleOfUnit(okrUnitToCheck).getCycleState() == CycleState.CLOSED) {
      throw new ForbiddenException(
        "Cannot modify this resource on a OkrDepartment in a closed cycle.");
    }
  }
}
