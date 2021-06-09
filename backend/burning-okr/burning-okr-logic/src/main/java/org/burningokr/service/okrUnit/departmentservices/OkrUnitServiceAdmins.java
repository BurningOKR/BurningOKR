package org.burningokr.service.okrUnit.departmentservices;

import java.util.Collection;
import java.util.UUID;
import org.burningokr.model.activity.Action;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.model.configuration.ConfigurationName;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrParentUnit;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.ConfigurationChangedEvent;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.InvalidDeleteRequestException;
import org.burningokr.service.okr.TaskBoardService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.okrUnitUtil.ParentService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("okrUnitServiceAdmins")
public class OkrUnitServiceAdmins<T extends OkrChildUnit> extends OkrUnitServiceManagers<T> {

  private final UnitRepository<OkrUnit> superUnitRepository;
  private final OkrTopicDescriptionRepository okrTopicDescriptionRepository;

  private final TaskBoardService taskBoardService;

  /**
   * Initialize DepartmentServiceAdmins.
   *
   * @param parentService a {@link ParentService} object
   * @param unitRepository a {@link OkrDepartmentRepository} object
   * @param objectiveRepository an {@link ObjectiveRepository} object
   * @param activityService an {@link ActivityService} object
   * @param entityCrawlerService an {@link EntityCrawlerService} object
   */
  public OkrUnitServiceAdmins(
      ParentService parentService,
      UnitRepository<T> unitRepository,
      UnitRepository<OkrUnit> superUnitRepository,
      ObjectiveRepository objectiveRepository,
      OkrTopicDraftRepository topicDraftRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService,
      OkrTopicDescriptionRepository okrTopicDescriptionRepository,
      TaskBoardService taskBoardService) {
    super(
        parentService,
        unitRepository,
        objectiveRepository,
        topicDraftRepository,
        activityService,
        entityCrawlerService);

    this.superUnitRepository = superUnitRepository;
    this.okrTopicDescriptionRepository = okrTopicDescriptionRepository;

    this.taskBoardService = taskBoardService;
  }

  @EventListener(ConfigurationChangedEvent.class)
  public void onConfigurationChangedEvent(ConfigurationChangedEvent event) {
    final Configuration changedConfiguration = event.getChangedConfiguration();

    if (changedConfiguration.getName().equals(ConfigurationName.TOPIC_SPONSORS_ACTIVATED.getName())
        && changedConfiguration.getValue().equals("false")) {
      for (OkrUnit unit : getAllOkrDepartments()) {
        if (unit instanceof OkrDepartment) {
          degradeTopicSponsor((OkrDepartment) unit);
        }
      }
    }
  }

  @Override
  @Transactional
  public T updateUnit(T updatedUnit, User user) {
    T referencedUnit = unitRepository.findByIdOrThrow(updatedUnit.getId());

    throwIfCycleForDepartmentIsClosed(referencedUnit);

    referencedUnit.setName(updatedUnit.getName());
    referencedUnit.setLabel(updatedUnit.getLabel());
    referencedUnit.setActive(updatedUnit.isActive());

    if (updatedUnit instanceof OkrDepartment) {
      OkrDepartment referencedOkrDepartment = (OkrDepartment) referencedUnit;
      OkrDepartment updatedOkrDepartment = (OkrDepartment) updatedUnit;

      referencedOkrDepartment.setOkrMasterId(updatedOkrDepartment.getOkrMasterId());
      referencedOkrDepartment.setOkrTopicSponsorId(updatedOkrDepartment.getOkrTopicSponsorId());
      referencedOkrDepartment.setOkrMemberIds(updatedOkrDepartment.getOkrMemberIds());
    }

    referencedUnit = unitRepository.save(referencedUnit);
    logger.info(
        "Updated OkrDepartment "
            + referencedUnit.getName()
            + "(id:"
            + referencedUnit.getId()
            + ")");
    activityService.createActivity(user, referencedUnit, Action.EDITED);

    return referencedUnit;
  }

  @Override
  public void deleteUnit(Long unitId, User user) {
    T referencedUnit = unitRepository.findByIdOrThrow(unitId);

    throwIfCycleForDepartmentIsClosed(referencedUnit);

    if (!(referencedUnit instanceof OkrParentUnit)
        || ((OkrParentUnit) referencedUnit).getOkrChildUnits().isEmpty()) {
      activityService.createActivity(user, referencedUnit, Action.DELETED);
      unitRepository.deleteById(unitId);
      logger.info("Deleted OkrDepartment with id: " + unitId);
      activityService.createActivity(user, referencedUnit, Action.DELETED);
      //      if (referencedUnit instanceof OkrDepartment) {
      //        okrTopicDescriptionService.safeDeleteOkrTopicDescription(
      //            ((OkrDepartment) referencedUnit).getOkrTopicDescription().getId(), user);
      //      }

    } else {
      logger.info(
          "Could not delete department with id: "
              + unitId
              + ". The department contains sub-departments.");
      throw new InvalidDeleteRequestException(
          "You can not delete departments which contains sub-departments.");
    }
  }

  @Override
  @Transactional
  public OkrChildUnit createChildUnit(Long parentUnitId, OkrChildUnit subDepartment, User user) {
    OkrUnit parentOkrUnit = superUnitRepository.findByIdOrThrow(parentUnitId);

    throwIfCycleForDepartmentIsClosed(parentOkrUnit);
    TaskBoard taskBoard = null;

    subDepartment.setParentOkrUnit(parentOkrUnit);

    if (subDepartment instanceof OkrDepartment) {
      OkrDepartment okrDepartment = (OkrDepartment) subDepartment;

      OkrTopicDescription description = new OkrTopicDescription(okrDepartment.getName());
      description = okrTopicDescriptionRepository.save(description);
      okrDepartment.setOkrTopicDescription(description);

      taskBoard = taskBoardService.createNewTaskBoardWithDefaultStates();
    }

    subDepartment = superUnitRepository.save(subDepartment);

    if (taskBoard != null) {
      taskBoard.setParentOkrDepartment((OkrDepartment) subDepartment);

      this.taskBoardService.saveTaskBoard(taskBoard);
    }

    logger.info(
        "Created subdepartment: "
            + subDepartment.getName()
            + " into OkrDepartment "
            + parentOkrUnit.getName()
            + "(id:"
            + parentUnitId
            + ")");
    activityService.createActivity(user, subDepartment, Action.CREATED);
    return subDepartment;
  }

  private Iterable<T> getAllOkrDepartments() {
    return unitRepository.findAll();
  }

  private void degradeTopicSponsor(OkrDepartment department) {
    moveTopicSponsorToMembers(department);
    department.setOkrTopicSponsorId(null);

    superUnitRepository.save(department);
  }

  private void moveTopicSponsorToMembers(OkrDepartment department) {
    final Collection<UUID> memberIds = department.getOkrMemberIds();
    final UUID topicSponsorId = department.getOkrTopicSponsorId();

    if (!memberIds.contains(topicSponsorId)) {
      if (memberIds.add(topicSponsorId)) {
        department.setOkrMemberIds(memberIds);
      } else {
        logger.warn(
            String.format(
                "Couldn't add topic sponsor %s to member of department %d",
                topicSponsorId, department.getId()));
      }
    }
  }
}
