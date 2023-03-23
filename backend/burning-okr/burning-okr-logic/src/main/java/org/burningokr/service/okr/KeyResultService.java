package org.burningokr.service.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.*;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.NoteKeyResultRepository;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class KeyResultService {

  private final Logger logger = LoggerFactory.getLogger(KeyResultService.class);

  private final KeyResultRepository keyResultRepository;
  private final NoteRepository noteRepository;
  private final NoteKeyResultRepository noteKeyResultRepository;
  private final ActivityService activityService;
  private final EntityCrawlerService entityCrawlerService;
  private final ObjectiveService objectiveService;
  private final KeyResultMilestoneService keyResultMilestoneService;
  private final KeyResultHistoryService keyResultHistoryService;
  private final TaskService taskService;

  public KeyResult findById(long keyResultId) {
    return keyResultRepository.findByIdOrThrow(keyResultId);
  }

  public Collection<NoteKeyResult> findNotesOfKeyResult(long keyResultId) {
    KeyResult keyResult = findById(keyResultId);
    return keyResult.getNotes();
  }

  /**
   * Updates a Key Result and updates or creates a KeyResultHistory.
   *
   * @param updatedKeyResult a {@link KeyResult} object
   * @param IUser            an {@link IUser} object
   * @return a {@link KeyResult} object
   */
  @Transactional
  public KeyResult updateKeyResult(KeyResult updatedKeyResult, IUser IUser) {
    KeyResult referencedKeyResult = findById(updatedKeyResult.getId());
    throwIfCycleOfKeyResultIsClosed(referencedKeyResult);

    boolean keyResultProgressChanged = createdOrProgressChanged(referencedKeyResult, updatedKeyResult);

    referencedKeyResult.setName(updatedKeyResult.getName());
    referencedKeyResult.setDescription(updatedKeyResult.getDescription());
    referencedKeyResult.setStartValue(updatedKeyResult.getStartValue());
    referencedKeyResult.setCurrentValue(updatedKeyResult.getCurrentValue());
    referencedKeyResult.setTargetValue(updatedKeyResult.getTargetValue());
    referencedKeyResult.setUnit(updatedKeyResult.getUnit());

    referencedKeyResult = keyResultMilestoneService.updateMilestones(updatedKeyResult, IUser);

    referencedKeyResult = keyResultRepository.save(referencedKeyResult);
    if (keyResultProgressChanged) {
      keyResultHistoryService.updateKeyResultHistory(IUser, referencedKeyResult);
    }
    logger.info(
      "Updated Key Result "
        + referencedKeyResult.getName()
        + "(id:"
        + referencedKeyResult.getId()
        + ")");
    activityService.createActivity(IUser, referencedKeyResult, Action.EDITED);
    return referencedKeyResult;
  }

  /**
   * Deletes a Key Result.
   *
   * @param keyResultId a long value
   * @param IUser       an {@link IUser} object
   */
  @Transactional
  public void deleteKeyResult(Long keyResultId, IUser IUser) throws Exception {

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

    // remove references to this kr from tasks
    Collection<Task> tasksForKeyResult = taskService.findTasksForKeyResult(referencedKeyResult);
    for (Task task : tasksForKeyResult) {
      task.setAssignedKeyResult(null);
      taskService.updateTask(task, IUser);
    }

    keyResultRepository.deleteById(keyResultId);
    activityService.createActivity(IUser, referencedKeyResult, Action.DELETED);
  }

  /**
   * Creates a Note for a Key Result.
   *
   * @param keyResultId   a long value
   * @param noteKeyResult a {@link NoteKeyResult} object
   * @param IUser         an {@link IUser} object
   * @return a {@link Note} object
   */
  @Transactional
  public NoteKeyResult createNote(long keyResultId, NoteKeyResult noteKeyResult, IUser IUser) {
    noteKeyResult.setId(null);
    noteKeyResult.setUserId(IUser.getId());
    noteKeyResult.setDate(LocalDateTime.now());

    noteKeyResult = noteKeyResultRepository.save(noteKeyResult);
    logger.info(
      "Added Note with id "
        + noteKeyResult.getId()
        + " from User "
        + IUser.getGivenName()
        + " "
        + IUser.getSurname()
        + " to KeyResult "
        + keyResultId);
    activityService.createActivity(IUser, noteKeyResult, Action.CREATED);

    return noteKeyResult;
  }

  @Transactional
  public NoteKeyResult updateNote(NoteKeyResult updatedNoteKeyResult) {
    NoteKeyResult referencedNoteKeyResult =
      noteKeyResultRepository.findByIdOrThrow(updatedNoteKeyResult.getId());

    referencedNoteKeyResult.setUserId(updatedNoteKeyResult.getUserId());
    referencedNoteKeyResult.setText(updatedNoteKeyResult.getText());
    referencedNoteKeyResult.setDate(updatedNoteKeyResult.getDate());
    referencedNoteKeyResult.setParentKeyResult(updatedNoteKeyResult.getParentKeyResult());

    referencedNoteKeyResult = noteKeyResultRepository.save(referencedNoteKeyResult);

    return referencedNoteKeyResult;
  }

  public Collection<KeyResult> findKeyResultsOfUnit(long unitId) {
    return keyResultRepository.findKeyResultsOfUnit(unitId);
  }


  /**
   * Updates a Sequence.
   *
   * @param objectiveId  a long value
   * @param sequenceList a {@link Collection} of long values
   * @param IUser        an {@link IUser} object
   * @throws Exception if Sequence is invalid
   */
  @Transactional
  public void updateSequence(Long objectiveId, Collection<Long> sequenceList, IUser IUser)
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
          activityService.createActivity(IUser, keyResult, Action.EDITED);
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

  private boolean createdOrProgressChanged(KeyResult oldKeyResult, KeyResult updatedKeyResult) {
    return oldKeyResult == null || oldKeyResult.getCurrentValue() != updatedKeyResult.getCurrentValue() ||
      oldKeyResult.getStartValue() != updatedKeyResult.getStartValue() ||
      oldKeyResult.getTargetValue() != updatedKeyResult.getTargetValue();
  }
}
