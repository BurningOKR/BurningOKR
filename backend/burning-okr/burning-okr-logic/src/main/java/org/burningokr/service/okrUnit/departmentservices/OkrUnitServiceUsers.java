package org.burningokr.service.okrUnit.departmentservices;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnit.OkrUnitService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.okrUnitUtil.ParentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;

@Service("okrUnitServiceUsers")
public class OkrUnitServiceUsers<T extends OkrChildUnit> implements OkrUnitService<T> {

  protected final Logger logger = LoggerFactory.getLogger(OkrUnitServiceUsers.class);
  protected UnitRepository<T> unitRepository;
  protected ObjectiveRepository objectiveRepository;
  protected OkrTopicDraftRepository topicDraftRepository;
  protected ActivityService activityService;
  ParentService parentService;
  private EntityCrawlerService entityCrawlerService;

  @Autowired
  OkrUnitServiceUsers(
      ParentService parentService,
      UnitRepository<T> unitRepository,
      ObjectiveRepository objectiveRepository,
      OkrTopicDraftRepository topicDraftRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService) {
    this.parentService = parentService;
    this.unitRepository = unitRepository;
    this.objectiveRepository = objectiveRepository;
    this.topicDraftRepository = topicDraftRepository;
    this.activityService = activityService;
    this.entityCrawlerService = entityCrawlerService;
  }

  @Override
  public T findById(long UnitId) {
    return unitRepository.findByIdOrThrow(UnitId);
  }

  @Override
  public Collection<Objective> findObjectivesOfUnit(long departmentId) {
    T department = findById(departmentId);
    return objectiveRepository.findByUnitAndOrderBySequence(department);
  }

  @Override
  public Collection<KeyResult> findKeyResultsOfUnit(long departmentId) {
    T department = findById(departmentId);
    Collection<KeyResult> keyResults = new ArrayList<>();
    Collection<Objective> objectives = department.getObjectives();
    for (Objective objective : objectives) {
      keyResults.addAll(objective.getKeyResults());
    }
    return keyResults;
  }

  @Override
  public T updateUnit(T updatedUnit, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  @Override
  public void deleteUnit(Long unitId, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  @Override
  public OkrChildUnit createChildUnit(Long parentUnitId, OkrChildUnit subDepartment, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  @Override
  public Objective createObjective(Long unitId, Objective objective, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  @Override
  public OkrTopicDraft createTopicDraft(Long unitId, OkrTopicDraft topicDraft, User user) {
    T parentUnit = unitRepository.findByIdOrThrow(unitId);

    throwIfCycleForDepartmentIsClosed(parentUnit);

    topicDraft.setParentUnit(parentUnit);

    topicDraft = topicDraftRepository.save(topicDraft);
    logger.info(
        "Created Topic Draft: "
            + topicDraft.getName()
            + " into department "
            + parentUnit.getName()
            + "(id:"
            + unitId
            + ")");

    activityService.createActivity(user, topicDraft, Action.CREATED);

    return topicDraft;
  }

  void throwIfCycleForDepartmentIsClosed(OkrUnit okrUnitToCheck) {
    if (entityCrawlerService.getCycleOfUnit(okrUnitToCheck).getCycleState() == CycleState.CLOSED) {
      throw new ForbiddenException(
          "Cannot modify this resource on a OkrDepartment in a closed cycle.");
    }
  }
}
