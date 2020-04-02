package org.burningokr.service.structure;

import org.burningokr.model.activity.Action;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.users.User;
import org.burningokr.repositories.structre.CorporateObjectiveStructureRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.IdDeviationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorporateObjectiveStructureService {

  private final CorporateObjectiveStructureRepository corporateObjectiveStructureRepository;
  private final ActivityService activityService;
  private final Logger logger = LoggerFactory.getLogger(CorporateObjectiveStructureService.class);

  @Autowired
  public CorporateObjectiveStructureService(
      CorporateObjectiveStructureRepository corporateObjectiveStructureRepository,
      ActivityService activityService) {
    this.corporateObjectiveStructureRepository = corporateObjectiveStructureRepository;
    this.activityService = activityService;
  }

  public CorporateObjectiveStructure findById(Long id) {
    return this.corporateObjectiveStructureRepository.findByIdOrThrow(id);
  }

  /**
   * Create a Corporate Objective Structure.
   *
   * @param corporateObjectiveStructure a {@link CorporateObjectiveStructure} object
   * @param user an {@link User} object
   * @return a {@link CorporateObjectiveStructure} object
   */
  public CorporateObjectiveStructure create(
      CorporateObjectiveStructure corporateObjectiveStructure, User user) {
    CorporateObjectiveStructure entity =
        this.corporateObjectiveStructureRepository.save(corporateObjectiveStructure);
    logger.info(
        "Created CorporateObjectiveStructure "
            + corporateObjectiveStructure.getName()
            + " (id: "
            + corporateObjectiveStructure.getId()
            + ")");
    activityService.createActivity(user, corporateObjectiveStructure, Action.CREATED);
    return entity;
  }

  /**
   * Update a Corporate Objective Structure.
   *
   * @param corporateObjectiveStructureId a long value
   * @param corporateObjectiveStructure a {@link CorporateObjectiveStructure} object
   * @param user an {@link User} object
   * @return
   */
  public CorporateObjectiveStructure update(
      long corporateObjectiveStructureId,
      CorporateObjectiveStructure corporateObjectiveStructure,
      User user) {
    throwIdDeviationExceptionIfIdsAreDeviating(
        corporateObjectiveStructureId, corporateObjectiveStructure);
    CorporateObjectiveStructure persistedEntity =
        corporateObjectiveStructureRepository.findByIdOrThrow(corporateObjectiveStructureId);
    persistedEntity.setName(corporateObjectiveStructure.getName());
    persistedEntity.setParentStructure(corporateObjectiveStructure.getParentStructure());
    persistedEntity.setDepartments(corporateObjectiveStructure.getDepartments());
    persistedEntity.setObjectives(corporateObjectiveStructure.getObjectives());
    corporateObjectiveStructureRepository.save(persistedEntity);
    logger.info(
        "Update CorporateObjectiveStructure "
            + corporateObjectiveStructure.getName()
            + " (id: "
            + corporateObjectiveStructure.getId()
            + ")");
    activityService.createActivity(user, corporateObjectiveStructure, Action.EDITED);
    return persistedEntity;
  }

  private void throwIdDeviationExceptionIfIdsAreDeviating(
      long corporateObjectiveStructureId, CorporateObjectiveStructure corporateObjectiveStructure) {
    if (corporateObjectiveStructure.getId() != corporateObjectiveStructureId) {
      throw new IdDeviationException("IDs are deviating, but they has to be equal.");
    }
  }

  /**
   * Deletes Corporate Objective Structure.
   *
   * @param id a long value
   * @param user an {@link User} object
   */
  public void delete(Long id, User user) {
    CorporateObjectiveStructure corporateObjectiveStructure =
        corporateObjectiveStructureRepository.findByIdOrThrow(id);
    corporateObjectiveStructureRepository.delete(corporateObjectiveStructure);
    logger.info("Deleted CorporateObjectiveStructure with id: " + id);
    activityService.createActivity(user, corporateObjectiveStructure, Action.DELETED);
  }
}
