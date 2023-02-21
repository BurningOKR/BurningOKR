package org.burningokr.service.okr;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.KeyResultMilestone;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultMilestoneRepository;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.service.activity.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeyResultMilestoneServiceTest {

  @Mock
  private KeyResultMilestoneRepository keyResultMilestoneRepository;

  @Mock
  private KeyResultRepository keyResultRepository;

  @Mock
  private ActivityService activityService;

  @Mock
  private User user;

  @InjectMocks
  private KeyResultMilestoneService keyResultMilestoneService;

  private KeyResult keyResult;
  private KeyResultMilestone milestone;

  @BeforeEach
  public void setUp() {
    keyResult = new KeyResult();
    milestone = new KeyResultMilestone();

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.findById(anyLong())).thenReturn(Optional.of(keyResult));
    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
  }

  @Test
  public void createKeyResultMilestone_expectMilestoneCreated() {
    keyResult.setId(10L);
    milestone.setId(12L);

    keyResultMilestoneService.createKeyResultMilestone(10L, milestone, user);

    verify(keyResultMilestoneRepository).save(any());
  }

  @Test
  public void createKeyResultMilestone_expectParentKeyResultIsSet() {
    keyResult.setId(10L);
    milestone.setId(12L);

    keyResultMilestoneService.createKeyResultMilestone(10L, milestone, user);

    assertEquals(milestone.getParentKeyResult(), keyResult);
  }

  @Test
  public void createKeyResultMilestone_expectActivityIsCreated() {
    keyResult.setId(10L);
    milestone.setId(12L);

    keyResultMilestoneService.createKeyResultMilestone(10L, milestone, user);

    verify(activityService).createActivity(eq(user), eq(milestone), eq(Action.CREATED));
  }

  @Test
  public void createKeyResultMilestone_expectEntityNotFoundExceptionWhenKeyResultDoesNotExist() {
    when(keyResultRepository.findByIdOrThrow(any(Long.class)))
      .thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> {
      keyResultMilestoneService.createKeyResultMilestone(10L, milestone, user);
    });
  }

  @Test
  public void updateKeyResultMilestone_expectNameIsUpdated() {
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setName("test");
    updateMilestone.setId(10L);

    keyResultMilestoneService.updateKeyResultMilestone(updateMilestone, user);

    assertEquals(updateMilestone.getName(), milestone.getName());
  }

  @Test
  public void updateKeyResultMilestone_expectValueIsUpdated() {
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setValue(3L);
    updateMilestone.setId(10L);

    keyResultMilestoneService.updateKeyResultMilestone(updateMilestone, user);

    assertEquals(updateMilestone.getValue(), milestone.getValue());
  }

  @Test
  public void updateKeyResultMilestone_expectParentKeyResultIsUpdated() {
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setParentKeyResult(keyResult);
    updateMilestone.setId(10L);

    keyResultMilestoneService.updateKeyResultMilestone(updateMilestone, user);

    assertEquals(updateMilestone.getParentKeyResult(), milestone.getParentKeyResult());
  }

  @Test
  public void updateKeyResultMilestone_expectMilestoneIsSaved() {
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setId(10L);

    keyResultMilestoneService.updateKeyResultMilestone(updateMilestone, user);

    verify(keyResultMilestoneRepository).save(any());
  }

  @Test
  public void updateKeyResultMilestone_expectActivityIsCreated() {
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setId(10L);

    keyResultMilestoneService.updateKeyResultMilestone(updateMilestone, user);

    verify(activityService).createActivity(eq(user), eq(milestone), eq(Action.EDITED));
  }

  @Test
  public void updateKeyResultMilestone_expectExceptionWhenMilestoneDoesNotExist() {
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setId(10L);

    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong()))
      .thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> {
      keyResultMilestoneService.updateKeyResultMilestone(updateMilestone, user);
    });
  }

  @Test
  public void deleteKeyResultMilestone_expectMilestoneIsDeleted() {
    keyResultMilestoneService.deleteKeyResultMilestone(10L, user);

    verify(keyResultMilestoneRepository).deleteById(anyLong());
  }

  @Test
  public void deleteKeyResultMilestone_expectMilestoneIsRemovedFromParentKeyResultsMilestoneList() {
    milestone.setParentKeyResult(keyResult);

    List<KeyResultMilestone> milestoneList = new ArrayList<>();
    milestoneList.add(milestone);

    keyResult.setMilestones(milestoneList);

    keyResultMilestoneService.deleteKeyResultMilestone(10L, user);

    assertTrue(keyResult.getMilestones().isEmpty());
  }

  @Test
  public void
  deleteKeyResultMilestone_expectOnlyMilestoneIsRemovedFromParentKeyResultsMilestoneList() {
    milestone.setParentKeyResult(keyResult);

    List<KeyResultMilestone> milestoneList = new ArrayList<>();
    milestoneList.add(milestone);
    milestoneList.add(new KeyResultMilestone());

    keyResult.setMilestones(milestoneList);

    keyResultMilestoneService.deleteKeyResultMilestone(10L, user);

    assertEquals(1, keyResult.getMilestones().size());
  }

  @Test
  public void deleteKeyResultMilestone_expectActivityIsCreated() {
    keyResultMilestoneService.deleteKeyResultMilestone(10L, user);

    verify(activityService).createActivity(eq(user), eq(milestone), eq(Action.DELETED));
  }

  @Test
  public void deleteKeyResultMilestone_expectExceptionWhenMilestoneDoesNotExist() {
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong()))
      .thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> {
      keyResultMilestoneService.deleteKeyResultMilestone(10L, user);
    });
  }

  @Test
  public void updateMilestones_doesNothingWhenThereAreNoMilestones() {
    KeyResult updateKeyResult = new KeyResult();
    updateKeyResult.setMilestones(Collections.emptyList());
    updateKeyResult.setId(10L);

    keyResult.setMilestones(Collections.emptyList());

    KeyResult testKeyResult = keyResultMilestoneService.updateMilestones(updateKeyResult, user);

    assertEquals(Collections.emptyList(), updateKeyResult.getMilestones());
    assertEquals(Collections.emptyList(), keyResult.getMilestones());
    assertEquals(Collections.emptyList(), testKeyResult.getMilestones());

    verify(activityService, never()).createActivity(any(), any(), any());
  }

  @Test
  public void updateMilestones_deletesMilestonesWhenTheyDoNotExistInTheNewKeyResult() {
    KeyResult updateKeyResult = new KeyResult();
    updateKeyResult.setMilestones(Collections.emptyList());
    updateKeyResult.setId(10L);

    KeyResultMilestone milestone1 = new KeyResultMilestone();
    KeyResultMilestone milestone2 = new KeyResultMilestone();
    KeyResultMilestone milestone3 = new KeyResultMilestone();
    milestone1.setId(1L);
    milestone2.setId(2L);
    milestone3.setId(3L);

    List<KeyResultMilestone> existingMilestones = new ArrayList<>();
    existingMilestones.add(milestone1);
    existingMilestones.add(milestone2);
    existingMilestones.add(milestone3);
    keyResult.setMilestones(existingMilestones);

    KeyResult testKeyResult = keyResultMilestoneService.updateMilestones(updateKeyResult, user);

    assertEquals(Collections.emptyList(), updateKeyResult.getMilestones());
    assertEquals(Collections.emptyList(), testKeyResult.getMilestones());

    verify(activityService, times(3)).createActivity(any(), any(), eq(Action.DELETED));
  }

  @Test
  public void updateMilestones_createsMilestonesWhenTheyDoNotExistInTheOldKeyResult() {
    List<KeyResultMilestone> existingMilestones = new ArrayList<>();
    existingMilestones.add(new KeyResultMilestone());
    existingMilestones.add(new KeyResultMilestone());
    existingMilestones.add(new KeyResultMilestone());

    KeyResult updateKeyResult = new KeyResult();
    updateKeyResult.setMilestones(existingMilestones);
    updateKeyResult.setId(10L);

    keyResult.setMilestones(Collections.emptyList());

    KeyResult testKeyResult = keyResultMilestoneService.updateMilestones(updateKeyResult, user);

    assertEquals(3, testKeyResult.getMilestones().size());
    verify(activityService, times(3)).createActivity(any(), any(), eq(Action.CREATED));
  }

  @Test
  public void updateMilestones_createsUpdatesAndDeletes() {
    KeyResultMilestone milestone1 = new KeyResultMilestone();
    KeyResultMilestone milestone2 = new KeyResultMilestone();
    KeyResultMilestone milestone2updated = new KeyResultMilestone();
    KeyResultMilestone milestone3 = new KeyResultMilestone();
    milestone1.setParentKeyResult(keyResult);
    milestone2.setId(2L);
    milestone2.setValue(10L);
    milestone2.setParentKeyResult(keyResult);
    milestone2updated.setId(2L);
    milestone2updated.setValue(20L);
    milestone2updated.setParentKeyResult(keyResult);
    milestone3.setId(3L);

    List<KeyResultMilestone> existingMilestones = new ArrayList<>();
    existingMilestones.add(milestone2);
    existingMilestones.add(milestone3);

    List<KeyResultMilestone> updateMilestones = new ArrayList<>();
    updateMilestones.add(milestone1);
    updateMilestones.add(milestone2updated);

    keyResult.setMilestones(existingMilestones);

    KeyResult updateKeyResult = new KeyResult();
    updateKeyResult.setId(10L);
    updateKeyResult.setMilestones(updateMilestones);

    keyResultMilestoneService.updateMilestones(updateKeyResult, user);

    verify(activityService, times(1)).createActivity(any(), any(), eq(Action.CREATED));
    verify(activityService, times(1)).createActivity(any(), any(), eq(Action.EDITED));
    verify(activityService, times(1)).createActivity(any(), any(), eq(Action.DELETED));
  }
}
