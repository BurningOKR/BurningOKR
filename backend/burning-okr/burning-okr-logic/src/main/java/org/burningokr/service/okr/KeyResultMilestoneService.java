package org.burningokr.service.okr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.KeyResultMilestone;
import org.burningokr.repositories.okr.KeyResultMilestoneRepository;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.service.activity.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyResultMilestoneService {

  private final KeyResultMilestoneRepository keyResultMilestoneRepository;
  private final KeyResultRepository keyResultRepository;
  private final ActivityService activityService;


  /**
   * Creates a new KeyResultMilestone for a KeyResult
   *
   * @param keyResultId the id of the KeyResult to create the milestone for
   * @param milestone   the milestone to create
   * @return the created KeyResultMilestone. Use this object for further operations.
   */
  @Transactional
  public KeyResultMilestone createKeyResultMilestone(
    long keyResultId, KeyResultMilestone milestone
  ) {
    KeyResult keyResult = keyResultRepository.findByIdOrThrow(keyResultId);
    milestone.setParentKeyResult(keyResult);

    KeyResultMilestone referencedMilestone = keyResultMilestoneRepository.save(milestone);
    log.debug("Created KeyResultMilestone with id %d for KeyResult %s (id: %d).".formatted(referencedMilestone.getId(), keyResult.getName(), keyResult.getId()));

    activityService.createActivity(referencedMilestone, Action.CREATED);
    return referencedMilestone;
  }

  /**
   * updates a keyResultMilestone
   *
   * @param milestone the keyResultMilestone to update
   * @return the updated keyResultMilestone. Use this object for further operations.
   */
  @Transactional
  public KeyResultMilestone updateKeyResultMilestone(KeyResultMilestone milestone) {
    KeyResultMilestone referencedMilestone =
      keyResultMilestoneRepository.findByIdOrThrow(milestone.getId());
    referencedMilestone.setName(milestone.getName());
    referencedMilestone.setValue(milestone.getValue());
    referencedMilestone.setParentKeyResult(milestone.getParentKeyResult());

    referencedMilestone = keyResultMilestoneRepository.save(referencedMilestone);

    log.debug("Updated KeyResultMilestone %s (id: %d)"
            + referencedMilestone.getName()
            + "(id: "
            + referencedMilestone.getId()
            + ")");
    activityService.createActivity(referencedMilestone, Action.EDITED);
    return referencedMilestone;
  }

  /**
   * deletes a keyResultMilestone
   *
   * @param milestoneId the id of the milestone to delete.
   */
  @Transactional
  public void deleteKeyResultMilestone(Long milestoneId) {
    KeyResultMilestone referencedMilestone =
      keyResultMilestoneRepository.findByIdOrThrow(milestoneId);

    if (referencedMilestone.getParentKeyResult() != null) {
      KeyResult parent = referencedMilestone.getParentKeyResult();
      parent.getMilestones().remove(referencedMilestone);

      keyResultRepository.save(parent);
    }

    keyResultMilestoneRepository.deleteById(milestoneId);
    activityService.createActivity(referencedMilestone, Action.DELETED);
  }

  /**
   * Updates the milestones of a KeyResult
   *
   * @param keyResult the Keyresult of which the milestones should be updated
   */
  @Transactional
  public KeyResult updateMilestones(KeyResult keyResult) {
    KeyResult oldKeyResult;

    if (keyResult.getId() != null) {
      oldKeyResult = keyResultRepository.findById(keyResult.getId()).orElseGet(KeyResult::new);
    } else {
      oldKeyResult = new KeyResult();
    }

    // Find all milestones in the oldKeyResult that no longer exist in the keyResult.
    Collection<KeyResultMilestone> milestonesToDelete =
      oldKeyResult.getMilestones().stream()
        .filter(
          milestone ->
            keyResult.getMilestones().stream()
              .noneMatch(
                existingMilestone ->
                  milestone.getId().equals(existingMilestone.getId())))
        .toList();

    // Find all milestones that exist in the oldKeyResult and in the keyResult.
    Collection<KeyResultMilestone> milestonesToUpdate =
      keyResult.getMilestones().stream()
        .filter(
          milestone ->
            oldKeyResult.getMilestones().stream()
              .anyMatch(
                existingMilestone ->
                  existingMilestone.getId().equals(milestone.getId())))
        .collect(Collectors.toList());

    // Find all milestones that have no id.
    Collection<KeyResultMilestone> milestonesToCreate =
      keyResult.getMilestones().stream()
        .filter(existingMilestone -> existingMilestone.getId() == null)
        .collect(Collectors.toList());

    milestonesToCreate =
      milestonesToCreate.stream()
        .map(milestone -> createKeyResultMilestone(keyResult.getId(), milestone))
        .collect(Collectors.toList());

    milestonesToUpdate =
      milestonesToUpdate.stream()
        .map(this::updateKeyResultMilestone)
        .collect(Collectors.toList());

    milestonesToDelete.forEach(milestone -> deleteKeyResultMilestone(milestone.getId()));

    keyResult.setMilestones(
      Stream.concat(milestonesToCreate.stream(), milestonesToUpdate.stream())
        .collect(Collectors.toList()));

    return keyResult;
  }
}
