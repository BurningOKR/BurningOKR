package org.burningokr.service.okrUnit.departmentservices;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.okrUnitUtil.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("okrUnitServiceManagers")
public class OkrUnitServiceManagers<T extends OkrChildUnit> extends OkrUnitServiceUsers<T> {

  @Autowired
  OkrUnitServiceManagers(
      ParentService parentService,
      UnitRepository<T> unitRepository,
      ObjectiveRepository objectiveRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService) {
    super(
        parentService,
        unitRepository,
        objectiveRepository,
        activityService,
        entityCrawlerService);
  }

  @Override
  @Transactional
  public T updateUnit(T updatedUnit, User user) {
    T referencedUnit = unitRepository.findByIdOrThrow(updatedUnit.getId());

    throwIfCycleForDepartmentIsClosed(referencedUnit);

    if (updatedUnit instanceof OkrDepartment) {
      ((OkrDepartment) referencedUnit)
          .setOkrMemberIds(((OkrDepartment) updatedUnit).getOkrMemberIds());

      referencedUnit = unitRepository.save(referencedUnit);
      logger.info(
          "Updated OkrDepartment "
              + referencedUnit.getName()
              + "(id:"
              + referencedUnit.getId()
              + ")");
      activityService.createActivity(user, referencedUnit, Action.EDITED);
    }

    return referencedUnit;
  }

  @Override
  @Transactional
  public Objective createObjective(Long unitId, Objective objective, User user) {
    T department = unitRepository.findByIdOrThrow(unitId);

    throwIfCycleForDepartmentIsClosed(department);

    for (Objective otherObjective : department.getObjectives()) {
      otherObjective.setSequence(otherObjective.getSequence() + 1);
      objectiveRepository.save(otherObjective);
    }

    objective.setParentOkrUnit(department);

    Objective parentObjective = null;
    if (objective.hasParentObjective()) {
      parentObjective = objectiveRepository.findByIdOrThrow(objective.getParentObjective().getId());
      parentService.validateParentObjective(objective, parentObjective);
    }
    objective.setParentObjective(parentObjective);

    objective = objectiveRepository.save(objective);
    logger.info(
        "Created Objective: "
            + objective.getName()
            + " into department "
            + department.getName()
            + "(id:"
            + unitId
            + ")");
    activityService.createActivity(user, objective, Action.CREATED);

    return objective;
  }
}
