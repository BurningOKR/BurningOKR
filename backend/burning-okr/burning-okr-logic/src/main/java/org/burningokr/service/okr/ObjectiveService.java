package org.burningokr.service.okr;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.burningokr.model.activity.Action;
import org.burningokr.model.configuration.ConfigurationName;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteObjective;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.NoteObjectiveRepository;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.configuration.ConfigurationService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.exceptions.KeyResultOverflowException;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceUsers;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.okrUnitUtil.ParentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ObjectiveService {

  private final Logger logger = LoggerFactory.getLogger(ObjectiveService.class);

  private final ParentService parentService;
  private final ObjectiveRepository objectiveRepository;
  private final KeyResultRepository keyResultRepository;
  private final KeyResultHistoryService keyResultHistoryService;
  private final ActivityService activityService;
  private final EntityCrawlerService entityCrawlerService;
  private final ConfigurationService configurationService;
  private final OkrUnitServiceUsers<OkrChildUnit> unitService;
  private final KeyResultMilestoneService keyResultMilestoneService;
  private final NoteObjectiveRepository noteObjectiveRepository;

  /**
   * Initialize ObjectiveService.
   *  @param parentService a {@link ParentService} object
   * @param objectiveRepository an {@link ObjectiveRepository} object
   * @param keyResultRepository a {@link KeyResultRepository} object
   * @param keyResultHistoryService a {@link KeyResultHistoryService} object
   * @param activityService an {@link ActivityService} object
   * @param entityCrawlerService an {@link EntityCrawlerService} object
   * @param configurationService a {@link ConfigurationService} object
   * @param unitService a {@link OkrUnitServiceUsers} object
   */
  @Autowired
  public ObjectiveService(
    ParentService parentService,
    ObjectiveRepository objectiveRepository,
    KeyResultRepository keyResultRepository,
    KeyResultHistoryService keyResultHistoryService, ActivityService activityService,
    EntityCrawlerService entityCrawlerService,
    ConfigurationService configurationService,
    KeyResultMilestoneService keyResultMilestoneService,
    NoteObjectiveRepository noteObjectiveRepository,
    @Qualifier("okrUnitServiceUsers") OkrUnitServiceUsers<OkrChildUnit> unitService) {
    this.parentService = parentService;
    this.objectiveRepository = objectiveRepository;
    this.keyResultRepository = keyResultRepository;
    this.keyResultHistoryService = keyResultHistoryService;
    this.activityService = activityService;
    this.entityCrawlerService = entityCrawlerService;
    this.configurationService = configurationService;
    this.unitService = unitService;
    this.keyResultMilestoneService = keyResultMilestoneService;
    this.noteObjectiveRepository = noteObjectiveRepository;
  }

  public Objective findById(Long objectiveId) {
    return objectiveRepository.findByIdOrThrow(objectiveId);
  }

  public Collection<KeyResult> findKeyResultsOfObjective(long objectiveId) {
    Objective objective = findById(objectiveId);
    return keyResultRepository.findByObjectiveAndOrderBySequence(objective);
  }

  public Collection<NoteObjective> findNotesOfObjective(long objectiveId) {
    Objective objective = findById(objectiveId);
    return objective.getNotes();
  }

  /**
   * Updates an Objective.
   *
   * @param updatedObjective an {@link Objective} object
   * @param user an {@link User} object
   * @return an {@link Objective} object
   */
  @Transactional
  public Objective updateObjective(Objective updatedObjective, User user) {
    Objective referencedObjective = findById(updatedObjective.getId());

    if (entityCrawlerService.getCycleOfObjective(referencedObjective).getCycleState()
        == CycleState.CLOSED) {
      referencedObjective.setReview(updatedObjective.getReview());
    } else {
      referencedObjective.setName(updatedObjective.getName());
      referencedObjective.setDescription(updatedObjective.getDescription());
      referencedObjective.setRemark(updatedObjective.getRemark());
      referencedObjective.setContactPersonId(updatedObjective.getContactPersonId());
      referencedObjective.setActive(updatedObjective.isActive());

      Objective newParentObjective = null;
      if (updatedObjective.hasParentObjective()) {
        newParentObjective = findById(updatedObjective.getParentObjective().getId());
        parentService.validateParentObjective(referencedObjective, newParentObjective);
      }
      referencedObjective.setParentObjective(newParentObjective);
    }

    referencedObjective = objectiveRepository.save(referencedObjective);

    logger.info(
        "Updated objective: "
            + updatedObjective.getName()
            + "(id:"
            + updatedObjective.getId()
            + ").");
    activityService.createActivity(user, referencedObjective, Action.EDITED);
    return referencedObjective;
  }

  /**
   * Deletes the Objective with the given ID.
   *
   * @param objectiveId a long value
   * @param user an {@link User} object
   */
  @Transactional
  public void deleteObjectiveById(Long objectiveId, User user) {
    Objective referencedObjective = objectiveRepository.findByIdOrThrow(objectiveId);
    throwIfCycleForObjectiveIsClosed(referencedObjective);
    for (Objective subObjective : referencedObjective.getSubObjectives()) {
      subObjective.setParentObjective(null);
      objectiveRepository.save(subObjective);
      logger.info(
          "Removed parent objective from "
              + referencedObjective.getName()
              + "(id:"
              + referencedObjective.getId()
              + ").");
    }

    if (referencedObjective.getParentOkrUnit() != null) {
      for (Objective otherObjective : referencedObjective.getParentOkrUnit().getObjectives()) {
        if (otherObjective.getSequence() > referencedObjective.getSequence()) {
          otherObjective.setSequence(otherObjective.getSequence() - 1);
          objectiveRepository.save(otherObjective);
        }
      }
    }

    objectiveRepository.deleteById(objectiveId);
    activityService.createActivity(user, referencedObjective, Action.DELETED);
  }

  /**
   * Creates a Key Result of an Objective.
   *
   * @param objectiveId a long value
   * @param keyResult a {@link KeyResult} object
   * @param user an {@link User} object
   * @return a {@link KeyResult} object
   * @throws KeyResultOverflowException if Key Result limit is hit
   */
  @Transactional
  public KeyResult createKeyResult(Long objectiveId, KeyResult keyResult, User user)
      throws KeyResultOverflowException {
    Objective referencedObjective = findById(objectiveId);
    throwIfKeyResultLimitIsHit(referencedObjective);
    throwIfCycleForObjectiveIsClosed(referencedObjective);

    keyResult.setSequence(referencedObjective.getKeyResults().size());

    keyResult.setParentObjective(referencedObjective);

    keyResult = keyResultRepository.save(keyResult);

    long id = keyResult.getId();
    keyResult.setMilestones(
        keyResult.getMilestones().stream()
            .map(
                milestone ->
                    keyResultMilestoneService.createKeyResultMilestone(id, milestone, user))
            .collect(Collectors.toList()));

    logger.info(
        "Created Key Result "
            + keyResult.getName()
            + " in Objective "
            + referencedObjective.getName()
            + "(id:"
            + objectiveId
            + ").");
    activityService.createActivity(user, keyResult, Action.CREATED);
    keyResultHistoryService.createKeyResultHistory(user, keyResult);
    return keyResult;
  }

  /**
   * Updates a Sequence.
   *
   * @param unitId a long value
   * @param sequenceList a {@link Collection} of long values
   * @param user an {@link User} object
   * @throws Exception if Sequence is invalid
   */
  @Transactional
  public void updateSequence(Long unitId, Collection<Long> sequenceList, User user)
      throws Exception {
    OkrChildUnit childUnit = unitService.findById(unitId);
    throwIfSequenceInvalid(childUnit, sequenceList);

    ArrayList<Long> sequenceArray = new ArrayList<>(sequenceList);
    childUnit
        .getObjectives()
        .forEach(
            objective -> {
              int currentOrder = sequenceArray.indexOf(objective.getId());
              objective.setSequence(currentOrder);
              objectiveRepository.save(objective);
              activityService.createActivity(user, objective, Action.EDITED);
              logger.info("Update sequence of Objective with id " + objective.getId());
            });
  }

  private void throwIfSequenceInvalid(OkrChildUnit childUnit, Collection<Long> sequenceList)
      throws Exception {
    Collection<Objective> objectiveList = childUnit.getObjectives();

    if (objectiveList.size() != sequenceList.size()) {
      throw new IllegalArgumentException(
          "Size of Objective List and Sequence List has to be equal");
    }

    ArrayList<Long> objectiveIdList = new ArrayList<>();
    for (Objective currentObjective : objectiveList) {
      objectiveIdList.add(currentObjective.getId());
    }

    while (objectiveIdList.size() != 0) {
      int currentId = objectiveIdList.size() - 1;
      if (sequenceList.contains(objectiveIdList.get(currentId))) {
        objectiveIdList.remove(currentId);
      } else {
        throw new Exception("Objective sequence list does not contain correct ID's");
      }
    }
  }

  private void throwIfKeyResultLimitIsHit(Objective objective) throws KeyResultOverflowException {
    float maxKeyResultsPerObjective =
        Float.parseFloat(
            this.configurationService
                .getConfigurationByName(ConfigurationName.MAX_KEY_RESULTS.getName())
                .getValue());
    if (objective.getKeyResults().size() >= maxKeyResultsPerObjective) {
      logger.error(
          "Can not add more Key Results to Objective: "
              + objective.getName()
              + "(id:"
              + objective.getId()
              + ")."
              + " Max number of Key Results in one Objective is: "
              + maxKeyResultsPerObjective);
      throw new KeyResultOverflowException(
          "No more Key Results can be appended to Objective with id "
              + objective.getId()
              + ". Max value of Key Results are "
              + maxKeyResultsPerObjective
              + ".");
    }
  }

  private void throwIfCycleForObjectiveIsClosed(Objective objectiveToCheck) {
    if (entityCrawlerService.getCycleOfObjective(objectiveToCheck).getCycleState()
        == CycleState.CLOSED) {
      throw new ForbiddenException("Cannot modify this resource on a Objective in a closed cycle.");
    }
  }

  /**
   * creates a note for an objective.
   *
   * @param objectiveId id from objective
   * @param noteObjective the objective
   * @param user the user which wants to add the note
   * @return the created Note
   */
  public NoteObjective createNote(long objectiveId, NoteObjective noteObjective, User user) {

    noteObjective.setUserId(user.getId());
    noteObjective.setDate(LocalDateTime.now());

    noteObjective = noteObjectiveRepository.save(noteObjective);
    logger.info(
        "Added Note with id "
            + noteObjective.getId()
            + " from User "
            + user.getGivenName()
            + " "
            + user.getSurname()
            + " to KeyResult "
            + objectiveId);

    activityService.createActivity(user, noteObjective, Action.CREATED);

    return noteObjective;
  }

  @Transactional
  public NoteObjective updateNote(NoteObjective updatedNoteObjective) {
    NoteObjective referencedNoteObjective =
        noteObjectiveRepository.findByIdOrThrow(updatedNoteObjective.getId());

    referencedNoteObjective.setUserId(updatedNoteObjective.getUserId());
    referencedNoteObjective.setText(updatedNoteObjective.getText());
    referencedNoteObjective.setDate(updatedNoteObjective.getDate());
    referencedNoteObjective.setParentObjective(updatedNoteObjective.getParentObjective());

    referencedNoteObjective = noteObjectiveRepository.save(referencedNoteObjective);

    return referencedNoteObjective;
  }
}
