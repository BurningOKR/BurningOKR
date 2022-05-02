package org.burningokr.service.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.*;
import org.burningokr.model.okr.histories.KeyResultHistory;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultHistoryRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class KeyResultService {

  private final Logger logger = LoggerFactory.getLogger(KeyResultService.class);

  private final KeyResultRepository keyResultRepository;
  private final KeyResultHistoryRepository keyResultHistoryRepository;
  private final NoteRepository noteRepository;
  private final NoteKeyResultRepository noteKeyResultRepository;
  private final ActivityService activityService;
  private final EntityCrawlerService entityCrawlerService;
  private final ObjectiveService objectiveService;
  private final KeyResultMilestoneService keyResultMilestoneService;
  private final TaskService taskService;

  public KeyResult findById(long keyResultId) {
    return keyResultRepository.findByIdOrThrow(keyResultId);
  }

  public Collection<NoteKeyResult> findNotesOfKeyResult(long keyResultId) {
    KeyResult keyResult = findById(keyResultId);
    return keyResult.getNotes();
  }

  /**
   * Updates a Key Result.
   *
   * @param updatedKeyResult a {@link KeyResult} object
   * @param user             an {@link User} object
   * @return a {@link KeyResult} object
   */
  @Transactional
  public KeyResult updateKeyResult(KeyResult updatedKeyResult, User user) {
    KeyResult referencedKeyResult = findById(updatedKeyResult.getId());
    throwIfCycleOfKeyResultIsClosed(referencedKeyResult);

    boolean keyResultProgressChanged = progessChanged(referencedKeyResult, updatedKeyResult);

    referencedKeyResult.setName(updatedKeyResult.getName());
    referencedKeyResult.setDescription(updatedKeyResult.getDescription());
    referencedKeyResult.setStartValue(updatedKeyResult.getStartValue());
    referencedKeyResult.setCurrentValue(updatedKeyResult.getCurrentValue());
    referencedKeyResult.setTargetValue(updatedKeyResult.getTargetValue());
    referencedKeyResult.setUnit(updatedKeyResult.getUnit());

    referencedKeyResult = keyResultMilestoneService.updateMilestones(updatedKeyResult, user);

    referencedKeyResult = keyResultRepository.save(referencedKeyResult);
    if (keyResultProgressChanged) {
      updateKeyResultHistory(user, referencedKeyResult);
    }
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
   * @param user        an {@link User} object
   */
  @Transactional
  public void deleteKeyResult(Long keyResultId, User user) throws Exception {

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
      taskService.updateTask(task, user);
    }

    keyResultRepository.deleteById(keyResultId);
    activityService.createActivity(user, referencedKeyResult, Action.DELETED);
  }

  /**
   * Creates a Note for a Key Result.
   *
   * @param keyResultId   a long value
   * @param noteKeyResult a {@link NoteKeyResult} object
   * @param user          an {@link User} object
   * @return a {@link Note} object
   */
  @Transactional
  public NoteKeyResult createNote(long keyResultId, NoteKeyResult noteKeyResult, User user) {
    noteKeyResult.setId(null);
    noteKeyResult.setUserId(user.getId());
    noteKeyResult.setDate(LocalDateTime.now());

    noteKeyResult = noteKeyResultRepository.save(noteKeyResult);
    logger.info(
      "Added Note with id "
        + noteKeyResult.getId()
        + " from User "
        + user.getGivenName()
        + " "
        + user.getSurname()
        + " to KeyResult "
        + keyResultId);
    activityService.createActivity(user, noteKeyResult, Action.CREATED);

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

  /**
   * Updates a Sequence.
   *
   * @param objectiveId  a long value
   * @param sequenceList a {@link Collection} of long values
   * @param user         an {@link User} object
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

  private boolean progessChanged(KeyResult oldKeyResult, KeyResult updatedKeyResult) {
    return oldKeyResult.getCurrentValue() != updatedKeyResult.getCurrentValue() || oldKeyResult.getStartValue() != updatedKeyResult.getStartValue() || oldKeyResult.getTargetValue() != updatedKeyResult.getTargetValue();
  }

  private void updateKeyResultHistory(User user, KeyResult updatedKeyResult) {
    KeyResultHistory keyResultHistory = keyResultHistoryRepository.findByKeyResultOrderByDateChangedDesc(
      updatedKeyResult).get(0);

    if (keyResultHistory.getDateChanged().equals(LocalDate.now())) {
      keyResultHistory.setStartValue(updatedKeyResult.getStartValue());
      keyResultHistory.setCurrentValue(updatedKeyResult.getCurrentValue());
      keyResultHistory.setTargetValue(updatedKeyResult.getTargetValue());
      KeyResultHistory updatedHistory = keyResultHistoryRepository.save(keyResultHistory);
      activityService.createActivity(user, updatedHistory, Action.EDITED);
    } else {
      KeyResultHistory newKeyResultHistory = new KeyResultHistory();
      newKeyResultHistory.setKeyResult(updatedKeyResult);
      newKeyResultHistory.setDateChanged(LocalDate.now());
      newKeyResultHistory.setStartValue(updatedKeyResult.getStartValue());
      newKeyResultHistory.setCurrentValue(updatedKeyResult.getCurrentValue());
      newKeyResultHistory.setTargetValue(updatedKeyResult.getTargetValue());
      KeyResultHistory createdHistory = keyResultHistoryRepository.save(newKeyResultHistory);
      activityService.createActivity(user, createdHistory, Action.CREATED);
    }
  }
}
