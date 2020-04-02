package org.burningokr.service.okr;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KeyResultService {

  private final Logger logger = LoggerFactory.getLogger(KeyResultService.class);

  private KeyResultRepository keyResultRepository;
  private NoteRepository noteRepository;
  private ActivityService activityService;
  private EntityCrawlerService entityCrawlerService;
  private ObjectiveService objectiveService;

  /**
   * Initializes KeyResultService.
   *
   * @param keyResultRepository a {@link KeyResultRepository} object
   * @param noteRepository a {@link NoteRepository} object
   * @param activityService an {@link ActivityService} object
   * @param entityCrawlerService an {@link EntityCrawlerService} object
   * @param objectiveService an {@link ObjectiveService} object
   */
  @Autowired
  public KeyResultService(
      KeyResultRepository keyResultRepository,
      NoteRepository noteRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService,
      ObjectiveService objectiveService) {
    this.keyResultRepository = keyResultRepository;
    this.noteRepository = noteRepository;
    this.activityService = activityService;
    this.entityCrawlerService = entityCrawlerService;
    this.objectiveService = objectiveService;
  }

  public KeyResult findById(long keyResultId) {
    return keyResultRepository.findByIdOrThrow(keyResultId);
  }

  public Collection<Note> findNotesOfKeyResult(long keyResultId) {
    KeyResult keyResult = findById(keyResultId);
    return keyResult.getNotes();
  }

  /**
   * Updates a Key Result.
   *
   * @param updatedKeyResult a {@link KeyResult} object
   * @param user an {@link User} object
   * @return a {@link KeyResult} object
   */
  @Transactional
  public KeyResult updateKeyResult(KeyResult updatedKeyResult, User user) {
    KeyResult referencedKeyResult = findById(updatedKeyResult.getId());
    throwIfCycleOfKeyResultIsClosed(referencedKeyResult);

    referencedKeyResult.setName(updatedKeyResult.getName());
    referencedKeyResult.setDescription(updatedKeyResult.getDescription());
    referencedKeyResult.setStartValue(updatedKeyResult.getStartValue());
    referencedKeyResult.setCurrentValue(updatedKeyResult.getCurrentValue());
    referencedKeyResult.setTargetValue(updatedKeyResult.getTargetValue());
    referencedKeyResult.setUnit(updatedKeyResult.getUnit());

    referencedKeyResult = keyResultRepository.save(referencedKeyResult);
    logger.info(
        "Updated Key Result "
            + referencedKeyResult.getName()
            + "(id:"
            + referencedKeyResult.getId()
            + ")");
    activityService.createActivity(user, referencedKeyResult, Action.EDITED);
    return referencedKeyResult;
  }

  /**
   * Deletes a Key Result.
   *
   * @param keyResultId a long value
   * @param user an {@link User} object
   */
  @Transactional
  public void deleteKeyResult(Long keyResultId, User user) {
    KeyResult referencedKeyResult = keyResultRepository.findByIdOrThrow(keyResultId);
    throwIfCycleOfKeyResultIsClosed(referencedKeyResult);

    if (referencedKeyResult.getParentObjective() != null) {
      Collection<KeyResult> keyResultList =
          referencedKeyResult.getParentObjective().getKeyResults();
      for (KeyResult keyResult : keyResultList) {
        if (keyResult.getSequence() > referencedKeyResult.getSequence()) {
          keyResult.setSequence(keyResult.getSequence() - 1);
          keyResultRepository.save(keyResult);
        }
      }
    }

    keyResultRepository.deleteById(keyResultId);
    activityService.createActivity(user, referencedKeyResult, Action.DELETED);
  }

  /**
   * Creates a Note.
   *
   * @param keyResultId a long value
   * @param note a {@link Note} object
   * @param user an {@link User} object
   * @return a {@link Note} object
   */
  @Transactional
  public Note createNote(long keyResultId, Note note, User user) {
    note.setParentKeyResult(findById(keyResultId));
    note.setUserId(user.getId());
    note.setDate(LocalDateTime.now());

    note = noteRepository.save(note);
    logger.info(
        "Added Note with id "
            + note.getId()
            + " from User "
            + user.getGivenName()
            + " "
            + user.getSurname()
            + " to KeyResult "
            + keyResultId);
    activityService.createActivity(user, note, Action.CREATED);

    return note;
  }

  /**
   * Updates a Sequence.
   *
   * @param objectiveId a long value
   * @param sequenceList a {@link Collection} of long values
   * @param user an {@link User} object
   * @throws Exception if Sequence is invalid
   */
  @Transactional
  public void updateSequence(Long objectiveId, Collection<Long> sequenceList, User user)
      throws Exception {
    Objective objective = objectiveService.findById(objectiveId);
    throwIfSequenceInvalid(objective, sequenceList);

    ArrayList<Long> sequenceArray = new ArrayList<>(sequenceList);
    objective
        .getKeyResults()
        .forEach(
            keyResult -> {
              int currentOrder = sequenceArray.indexOf(keyResult.getId());
              keyResult.setSequence(currentOrder);
              keyResultRepository.save(keyResult);
              activityService.createActivity(user, keyResult, Action.EDITED);
              logger.info("Update sequence of KeyResult with id " + keyResult.getId());
            });
  }

  private void throwIfSequenceInvalid(Objective objective, Collection<Long> sequenceList)
      throws Exception {
    Collection<KeyResult> keyResultList = objective.getKeyResults();

    if (keyResultList.size() != sequenceList.size()) {
      throw new IllegalArgumentException(
          "Size of KeyResult List and Sequence List has to be equal");
    }

    ArrayList<Long> keyResultIdList = new ArrayList<>();
    for (KeyResult currentKeyResult : keyResultList) {
      keyResultIdList.add(currentKeyResult.getId());
    }

    while (keyResultIdList.size() != 0) {
      int currentId = keyResultIdList.size() - 1;
      if (sequenceList.contains(keyResultIdList.get(currentId))) {
        keyResultIdList.remove(currentId);
      } else {
        throw new Exception("KeyResult sequence list does not contain correct ID's");
      }
    }
  }

  private void throwIfCycleOfKeyResultIsClosed(KeyResult keyResultToCheck) {
    if (entityCrawlerService.getCycleOfKeyResult(keyResultToCheck).getCycleState()
        == CycleState.CLOSED) {
      throw new ForbiddenException("Cannot modify this resource on a KeyResult in a closed cycle.");
    }
  }
}
