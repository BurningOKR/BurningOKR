package org.burningokr.service.okr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.activity.Action;
import org.burningokr.model.configuration.ConfigurationName;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteObjective;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.NoteObjectiveRepository;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.configuration.ConfigurationService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.exceptions.KeyResultOverflowException;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.okrUnitUtil.ParentService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ObjectiveService {
  private final ParentService parentService;
  private final ObjectiveRepository objectiveRepository;
  private final KeyResultRepository keyResultRepository;
  private final KeyResultHistoryService keyResultHistoryService;
  private final ActivityService activityService;
  private final EntityCrawlerService entityCrawlerService;
  private final ConfigurationService configurationService;
  private final OkrChildUnitService<OkrChildUnit> okrChildUnitService;
  private final KeyResultMilestoneService keyResultMilestoneService;
  private final NoteObjectiveRepository noteObjectiveRepository;
  private final AuthenticationUserContextService authenticationUserContextService;

  @PreAuthorize("@childUnitAuthorizationService.hasMemberPrivilegesForChildUnit(#unitId)")
  public Objective createObjective(Long unitId, Objective objective) {
    OkrChildUnit objectivesParentOkrChildUnit = okrChildUnitService.findById(unitId);

    okrChildUnitService.throwIfCycleForOkrChildUnitIsClosed(objectivesParentOkrChildUnit);

    objective.setSequence(objectivesParentOkrChildUnit.getObjectives().size());
    objective.setParentOkrUnit(objectivesParentOkrChildUnit);

    if (objective.hasParentObjective()) {
      var parentObjective = objectiveRepository.findByIdOrThrow(objective.getParentObjective().getId());
      parentService.validateParentObjective(objective, parentObjective);
      objective.setParentObjective(parentObjective);
    }

    objective = objectiveRepository.save(objective);
    log.debug("Created Objective: %s (id: %d) into department %s (id: %d)"
        .formatted(
            objective.getName(),
            objective.getId(),
            objectivesParentOkrChildUnit.getName(),
            unitId
        ));

    activityService.createActivity(objective, Action.CREATED);

    return objective;
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

  public Collection<Objective> findChildObjectivesOfObjective(long objectiveId) {
    return objectiveRepository.findByParentObjectiveId(objectiveId);
  }

  /**
   * Updates an Objective.
   *
   * @param updatedObjective an {@link Objective} object
   * @return an {@link Objective} object
   */
  @Transactional
  public Objective updateObjective(Objective updatedObjective) {
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


    log.debug("Updated objective: %s (id: %d)".formatted(updatedObjective.getName(), updatedObjective.getId()));

    activityService.createActivity(referencedObjective, Action.EDITED);
    return referencedObjective;
  }

  /**
   * Deletes the Objective with the given ID.
   *
   * @param objectiveId a long value
   */
  @Transactional
  public void deleteObjectiveById(Long objectiveId) {
    Objective referencedObjective = objectiveRepository.findByIdOrThrow(objectiveId);
    throwIfCycleForObjectiveIsClosed(referencedObjective);
    for (Objective subObjective : referencedObjective.getSubObjectives()) {
      subObjective.setParentObjective(null);
      objectiveRepository.save(subObjective);
      log.debug("Removed parent objective from %s (id: %d)".formatted(referencedObjective.getName(), referencedObjective.getId()));
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
    activityService.createActivity(referencedObjective, Action.DELETED);
  }

  @Transactional
  public KeyResult createKeyResult(Long objectiveId, KeyResult keyResult)
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
                    keyResultMilestoneService.createKeyResultMilestone(id, milestone))
            .collect(Collectors.toList()));

    log.debug("Created Key Result %s in Objective %s (id: %d)".formatted(keyResult.getName(), referencedObjective.getName(), objectiveId));
    activityService.createActivity(keyResult, Action.CREATED);
    keyResultHistoryService.createKeyResultHistory(keyResult);
    return keyResult;
  }

  /**
   * Updates a Sequence.
   *
   * @param unitId       a long value
   * @param sequenceList a {@link Collection} of long values
   * @throws Exception if Sequence is invalid
   */
  @Transactional
  public void updateSequence(Long unitId, Collection<Long> sequenceList)
      throws Exception {
    OkrChildUnit childUnit = okrChildUnitService.findById(unitId);
    throwIfSequenceInvalid(childUnit, sequenceList);

    ArrayList<Long> sequenceArray = new ArrayList<>(sequenceList);
    childUnit
        .getObjectives()
        .forEach(
            objective -> {
              int currentOrder = sequenceArray.indexOf(objective.getId());
              objective.setSequence(currentOrder);
              objectiveRepository.save(objective);
              activityService.createActivity(objective, Action.EDITED);
              log.debug("Update sequence of Objective with id " + objective.getId());
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

    while (!objectiveIdList.isEmpty()) {
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
      log.error(
          "Can not add more Key Results to Objective: %s (id: %d). Max number of Key Results in one Objective is: %f."
                  .formatted(objective.getName(), objective.getId(), maxKeyResultsPerObjective));
      throw new KeyResultOverflowException(
          "No more Key Results can be appended to Objective with id %d. Max value of Key Results are %f."
                  .formatted(objective.getId(), maxKeyResultsPerObjective));
    }
  }

  private void throwIfCycleForObjectiveIsClosed(Objective objectiveToCheck) {
    if (entityCrawlerService.getCycleOfObjective(objectiveToCheck).getCycleState()
        == CycleState.CLOSED) {
      throw new ForbiddenException("Cannot modify this resource on a Objective in a closed cycle.");
    }
  }


  public NoteObjective createNote(NoteObjective noteObjective) {

    noteObjective.setUserId(authenticationUserContextService.getAuthenticatedUser().getId());
    noteObjective.setDate(LocalDateTime.now());

    noteObjective = noteObjectiveRepository.save(noteObjective);

    activityService.createActivity(noteObjective, Action.CREATED);

    return noteObjective;
  }

  @Transactional
  public NoteObjective updateNote(NoteObjective updatedNoteObjective) {
//    Objective objective = findById(objectiveId);
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
