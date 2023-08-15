package org.burningokr.service.okr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteKeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.NoteKeyResultRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyResultService {

  private final KeyResultRepository keyResultRepository;
  private final NoteKeyResultRepository noteKeyResultRepository;
  private final ActivityService activityService;
  private final EntityCrawlerService entityCrawlerService;
  private final ObjectiveService objectiveService;
  private final KeyResultMilestoneService keyResultMilestoneService;
  private final KeyResultHistoryService keyResultHistoryService;
  private final TaskService taskService;
  private final OkrChildUnitService<OkrChildUnit> okrChildUnitService;
  private final AuthenticationUserContextService authenticationUserContextService;

  public KeyResult findById(long keyResultId) {
    return keyResultRepository.findByIdOrThrow(keyResultId);
  }

  public Collection<NoteKeyResult> findNotesOfKeyResult(long keyResultId) {
    KeyResult keyResult = findById(keyResultId);
    return keyResult.getNotes();
  }

  @Transactional
  public KeyResult updateKeyResult(KeyResult updatedKeyResult) {
    KeyResult referencedKeyResult = findById(updatedKeyResult.getId());
    throwIfCycleOfKeyResultIsClosed(referencedKeyResult);

    boolean keyResultProgressChanged = createdOrProgressChanged(referencedKeyResult, updatedKeyResult);

    referencedKeyResult.setName(updatedKeyResult.getName());
    referencedKeyResult.setDescription(updatedKeyResult.getDescription());
    referencedKeyResult.setStartValue(updatedKeyResult.getStartValue());
    referencedKeyResult.setCurrentValue(updatedKeyResult.getCurrentValue());
    referencedKeyResult.setTargetValue(updatedKeyResult.getTargetValue());
    referencedKeyResult.setUnit(updatedKeyResult.getUnit());

    referencedKeyResult = keyResultMilestoneService.updateMilestones(updatedKeyResult);

    referencedKeyResult = keyResultRepository.save(referencedKeyResult);
    if (keyResultProgressChanged) {
      keyResultHistoryService.updateKeyResultHistory(referencedKeyResult);
    }
    log.debug("Updated Key Result %s (id: %d).".formatted(referencedKeyResult.getName(), referencedKeyResult.getId()));
    activityService.createActivity(referencedKeyResult, Action.EDITED);
    return referencedKeyResult;
  }

  /**
   * Deletes a Key Result.
   *
   * @param keyResultId a long value
   */
  @Transactional
  public void deleteKeyResult(Long keyResultId) throws Exception {

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
      taskService.updateTask(task);
    }

    keyResultRepository.deleteById(keyResultId);
    activityService.createActivity(referencedKeyResult, Action.DELETED);
  }

  @Transactional
  public NoteKeyResult createNote(NoteKeyResult noteKeyResult) {
    noteKeyResult.setId(null);
    noteKeyResult.setUserId(authenticationUserContextService.getAuthenticatedUser().getId());
    noteKeyResult.setDate(LocalDateTime.now());

    noteKeyResult = noteKeyResultRepository.save(noteKeyResult);

    activityService.createActivity(noteKeyResult, Action.CREATED);

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
    var keyResultList = new LinkedList<KeyResult>();
    okrChildUnitService.findById(unitId)
      .getObjectives()
      .forEach(objective -> keyResultList.addAll(objective.getKeyResults()));
    return keyResultList;
  }


  /**
   * Updates a Sequence.
   *
   * @param objectiveId  a long value
   * @param sequenceList a {@link Collection} of long values
   * @throws Exception if Sequence is invalid
   */
  @Transactional
  public void updateSequence(Long objectiveId, Collection<Long> sequenceList)
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
          activityService.createActivity(keyResult, Action.EDITED);
          log.debug("Update sequence of KeyResult with id %d.".formatted(keyResult.getId()));
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
