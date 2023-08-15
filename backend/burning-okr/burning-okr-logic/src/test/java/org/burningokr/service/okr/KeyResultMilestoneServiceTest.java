package org.burningokr.service.okr;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.KeyResultMilestone;
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

  @InjectMocks
  private KeyResultMilestoneService keyResultMilestoneService;

  private KeyResult keyResult;
  private KeyResultMilestone milestone;

  @BeforeEach
  public void setUp() {
    keyResult = new KeyResult();
    milestone = new KeyResultMilestone();
  }

  @Test
  public void createKeyResultMilestone_shouldCreateMilestone() {
    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    keyResult.setId(10L);
    milestone.setId(12L);

    keyResultMilestoneService.createKeyResultMilestone(10L, milestone);

    verify(keyResultMilestoneRepository).save(any());
  }

  @Test
  public void createKeyResultMilestone_shouldCheckIfParentKeyResultIsSet() {
    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    keyResult.setId(10L);
    milestone.setId(12L);

    keyResultMilestoneService.createKeyResultMilestone(10L, milestone);

    assertEquals(milestone.getParentKeyResult(), keyResult);
  }

  @Test
  public void createKeyResultMilestone_shouldCreateActivity() {
    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    keyResult.setId(10L);
    milestone.setId(12L);

    keyResultMilestoneService.createKeyResultMilestone(10L, milestone);

    verify(activityService).createActivity(eq(milestone), eq(Action.CREATED));
  }

  @Test
  public void createKeyResultMilestone_shouldThrowEntityNotFoundExceptionWhenKeyResultDoesNotExist() {
    when(keyResultRepository.findByIdOrThrow(any(Long.class)))
            .thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> keyResultMilestoneService.createKeyResultMilestone(10L, milestone));
  }

  @Test
  public void updateKeyResultMilestone_shouldUpdateName() {
    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setName("test");
    updateMilestone.setId(10L);

    keyResultMilestoneService.updateKeyResultMilestone(updateMilestone);

    assertEquals(updateMilestone.getName(), milestone.getName());
  }

  @Test
  public void updateKeyResultMilestone_shouldUpdateValue() {
    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setValue(3L);
    updateMilestone.setId(10L);

    keyResultMilestoneService.updateKeyResultMilestone(updateMilestone);

    assertEquals(updateMilestone.getValue(), milestone.getValue());
  }

  @Test
  public void updateKeyResultMilestone_shouldUpdateParentKeyResult() {
    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setParentKeyResult(keyResult);
    updateMilestone.setId(10L);

    keyResultMilestoneService.updateKeyResultMilestone(updateMilestone);

    assertEquals(updateMilestone.getParentKeyResult(), milestone.getParentKeyResult());
  }

  @Test
  public void updateKeyResultMilestone_shouldSaveMilestone() {
    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setId(10L);

    keyResultMilestoneService.updateKeyResultMilestone(updateMilestone);

    verify(keyResultMilestoneRepository).save(any());
  }

  @Test
  public void updateKeyResultMilestone_shouldCreateActivity() {
    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setId(10L);

    keyResultMilestoneService.updateKeyResultMilestone(updateMilestone);

    verify(activityService).createActivity(eq(milestone), eq(Action.EDITED));
  }

  @Test
  public void updateKeyResultMilestone_shouldThrowExceptionWhenMilestoneDoesNotExist() {
    KeyResultMilestone updateMilestone = new KeyResultMilestone();
    updateMilestone.setId(10L);

    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong()))
            .thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> keyResultMilestoneService.updateKeyResultMilestone(updateMilestone));
  }

  @Test
  public void deleteKeyResultMilestone_shouldDeleteMilestone() {
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    keyResultMilestoneService.deleteKeyResultMilestone(10L);

    verify(keyResultMilestoneRepository).deleteById(anyLong());
  }

  @Test
  public void deleteKeyResultMilestone_shouldRemoveMilestoneFromParentKeyResultsMilestoneList() {
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    milestone.setParentKeyResult(keyResult);

    List<KeyResultMilestone> milestoneList = new ArrayList<>();
    milestoneList.add(milestone);

    keyResult.setMilestones(milestoneList);

    keyResultMilestoneService.deleteKeyResultMilestone(10L);

    assertTrue(keyResult.getMilestones().isEmpty());
  }

  @Test
  public void deleteKeyResultMilestone_shouldRemoveSingleMilestoneFromParentKeyResultsMilestoneList() {
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    milestone.setParentKeyResult(keyResult);

    List<KeyResultMilestone> milestoneList = new ArrayList<>();
    milestoneList.add(milestone);
    milestoneList.add(new KeyResultMilestone());

    keyResult.setMilestones(milestoneList);

    keyResultMilestoneService.deleteKeyResultMilestone(10L);

    assertEquals(1, keyResult.getMilestones().size());
  }

  @Test
  public void deleteKeyResultMilestone_shouldCreateActivity() {
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    keyResultMilestoneService.deleteKeyResultMilestone(10L);

    verify(activityService).createActivity(eq(milestone), eq(Action.DELETED));
  }

  @Test
  public void deleteKeyResultMilestone_shouldThrowExceptionWhenMilestoneDoesNotExist() {
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong()))
            .thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> keyResultMilestoneService.deleteKeyResultMilestone(10L));
  }

  @Test
  public void updateMilestones_shouldDoNothingWhenThereAreNoMilestones() {
    KeyResult updateKeyResult = new KeyResult();
    updateKeyResult.setMilestones(Collections.emptyList());
    updateKeyResult.setId(10L);

    keyResult.setMilestones(Collections.emptyList());

    KeyResult testKeyResult = keyResultMilestoneService.updateMilestones(updateKeyResult);

    assertEquals(Collections.emptyList(), updateKeyResult.getMilestones());
    assertEquals(Collections.emptyList(), keyResult.getMilestones());
    assertEquals(Collections.emptyList(), testKeyResult.getMilestones());

    verify(activityService, never()).createActivity(any(), any());
  }

  @Test
  public void updateMilestones_shouldDeleteMilestonesWhenTheyDoNotExistInTheNewKeyResult() {
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
    when(keyResultRepository.findById(anyLong())).thenReturn(Optional.of(keyResult));
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    keyResult.setMilestones(existingMilestones);

    KeyResult testKeyResult = keyResultMilestoneService.updateMilestones(updateKeyResult);

    assertEquals(Collections.emptyList(), updateKeyResult.getMilestones());
    assertEquals(Collections.emptyList(), testKeyResult.getMilestones());

    verify(activityService, times(3)).createActivity(any(), eq(Action.DELETED));
  }

  @Test
  public void updateMilestones_shouldCreateMilestonesWhenTheyDoNotExistInTheOldKeyResult() {
    List<KeyResultMilestone> existingMilestones = new ArrayList<>();
    existingMilestones.add(new KeyResultMilestone());
    existingMilestones.add(new KeyResultMilestone());
    existingMilestones.add(new KeyResultMilestone());

    KeyResult updateKeyResult = new KeyResult();
    updateKeyResult.setMilestones(existingMilestones);
    updateKeyResult.setId(10L);

    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    keyResult.setMilestones(Collections.emptyList());

    KeyResult testKeyResult = keyResultMilestoneService.updateMilestones(updateKeyResult);

    assertEquals(3, testKeyResult.getMilestones().size());
    verify(activityService, times(3)).createActivity(any(), eq(Action.CREATED));
  }

  @Test
  public void updateMilestones_shouldCreateUpdateAndDelete() {
    KeyResultMilestone milestone1 = new KeyResultMilestone();
    KeyResultMilestone milestone2 = new KeyResultMilestone();
    KeyResultMilestone milestone2updated = new KeyResultMilestone();
    KeyResultMilestone milestone3 = new KeyResultMilestone();
    when(keyResultRepository.findById(anyLong())).thenReturn(Optional.of(keyResult));
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

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultMilestoneRepository.save(any())).thenReturn(milestone);
    when(keyResultMilestoneRepository.findByIdOrThrow(anyLong())).thenReturn(milestone);
    keyResult.setMilestones(existingMilestones);

    KeyResult updateKeyResult = new KeyResult();
    updateKeyResult.setId(10L);
    updateKeyResult.setMilestones(updateMilestones);

    keyResultMilestoneService.updateMilestones(updateKeyResult);

    verify(activityService, times(1)).createActivity(any(), eq(Action.CREATED));
    verify(activityService, times(1)).createActivity(any(), eq(Action.EDITED));
    verify(activityService, times(1)).createActivity(any(), eq(Action.DELETED));
  }
}
