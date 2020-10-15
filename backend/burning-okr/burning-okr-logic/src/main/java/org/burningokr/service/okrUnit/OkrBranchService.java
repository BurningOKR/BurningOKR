package org.burningokr.service.okrUnit;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okrUnit.OkrBranchRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.IdDeviationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OkrBranchService {

  private final OkrBranchRepository okrBranchRepository;
  private final ActivityService activityService;
  private final Logger logger = LoggerFactory.getLogger(OkrBranchService.class);

  @Autowired
  public OkrBranchService(
      OkrBranchRepository okrBranchRepository, ActivityService activityService) {
    this.okrBranchRepository = okrBranchRepository;
    this.activityService = activityService;
  }

  public OkrBranch findById(Long id) {
    return this.okrBranchRepository.findByIdOrThrow(id);
  }

  /**
   * Create an OkrUnit.
   *
   * @param okrBranch a {@link OkrBranch} object
   * @param user an {@link User} object
   * @return a {@link OkrBranch} object
   */
  public OkrBranch create(OkrBranch okrBranch, User user) {
    OkrBranch entity = this.okrBranchRepository.save(okrBranch);
    logger.info("Created OkrBranch " + okrBranch.getName() + " (id: " + okrBranch.getId() + ")");
    activityService.createActivity(user, okrBranch, Action.CREATED);
    return entity;
  }

  /**
   * Update an OkrUnit.
   *
   * @param branchId a long value
   * @param okrBranch a {@link OkrBranch} object
   * @param user an {@link User} object
   * @return
   */
  public OkrBranch update(long branchId, OkrBranch okrBranch, User user) {
    throwIdDeviationExceptionIfIdsAreDeviating(branchId, okrBranch);
    OkrBranch persistedEntity = okrBranchRepository.findByIdOrThrow(branchId);
    persistedEntity.setName(okrBranch.getName());
    persistedEntity.setParentOkrUnit(okrBranch.getParentOkrUnit());
    persistedEntity.setOkrChildUnits(okrBranch.getOkrChildUnits());
    persistedEntity.setObjectives(okrBranch.getObjectives());
    okrBranchRepository.save(persistedEntity);
    logger.info("Update OkrBranch " + okrBranch.getName() + " (id: " + okrBranch.getId() + ")");
    activityService.createActivity(user, okrBranch, Action.EDITED);
    return persistedEntity;
  }

  private void throwIdDeviationExceptionIfIdsAreDeviating(long branchId, OkrBranch okrBranch) {
    if (okrBranch.getId() != branchId) {
      throw new IdDeviationException("IDs are deviating, but they have to be equal.");
    }
  }

  /**
   * Deletes OkrUnit.
   *
   * @param id a long value
   * @param user an {@link User} object
   */
  public void delete(Long id, User user) {
    OkrBranch okrBranch = okrBranchRepository.findByIdOrThrow(id);
    okrBranchRepository.delete(okrBranch);
    logger.info("Deleted OkrBranch with id: " + id);
    activityService.createActivity(user, okrBranch, Action.DELETED);
  }
}
