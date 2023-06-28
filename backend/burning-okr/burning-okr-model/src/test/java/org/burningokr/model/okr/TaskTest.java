package org.burningokr.model.okr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskTest {
  private Task originalTask;
  private Task expectedTask;
  private Task emptyTask;

  @BeforeEach
  public void init() {
    TaskState taskState = new TaskState();
    TaskBoard taskBoard = new TaskBoard();
    Task prevTask = new Task();
    Collection<UUID> userIds = new ArrayList<>();
    UUID userId1 = new UUID(1L, 1L);
    userIds.add(userId1);
    KeyResult keyResult = new KeyResult();

    originalTask = new Task();
    originalTask.setId(100L);
    originalTask.setTitle("Task Title");
    originalTask.setDescription("Task Description");
    originalTask.setTaskState(taskState);
    originalTask.setParentTaskBoard(taskBoard);
    originalTask.setPreviousTask(prevTask);
    originalTask.setAssignedUserIds(userIds);
    originalTask.setAssignedKeyResult(keyResult);
    originalTask.setVersion(1L);
    expectedTask = originalTask;

    emptyTask = new Task();
  }

  @Test
  public void hasAssignedUserIds_shouldNotBeEmpty() {
   assertTrue(originalTask.hasAssignedUserIds());
  }

  @Test
  public void hasAssignedUserIds_shouldBeEmpty() {
    assertFalse(emptyTask.hasAssignedUserIds());
  }

  @Test
  public void hasAssignedKeyResult_shouldBeTrue() {
    assertTrue(originalTask.hasAssignedKeyResult());
  }

  @Test
  public void hasAssignedKeyResult_shouldBeFalse() {
    assertFalse(emptyTask.hasAssignedKeyResult());
  }

  @Test
  public void hasPreviousTask_shouldBeTrue() {
    assertTrue(originalTask.hasPreviousTask());
  }

  @Test
  public void hasPreviousTask_shouldBeFalse() {
    assertFalse(emptyTask.hasPreviousTask());
  }

  @Test
  public void copyTask_shouldNotBeNull() {
    assertNotNull(originalTask.copyWithNoRelations());
  }

  @Test
  public void copyTask_shouldNotHaveSameId() {
    assertNotEquals(expectedTask.getId(), originalTask.copyWithNoRelations().getId());
  }

  @Test
  public void copyTask_shouldNotHaveSameVersion() {
    assertNotEquals(expectedTask.getVersion(), originalTask.copyWithNoRelations().getVersion());
  }

  @Test
  public void copyTask_shouldNotHaveSameAssignedKeyResult() {
    assertNotEquals(expectedTask.getAssignedKeyResult(), originalTask.copyWithNoRelations().getAssignedKeyResult());
  }

  @Test
  public void copyTask_shouldHaveSameTitle() {
    assertEquals(expectedTask.getTitle(), originalTask.copyWithNoRelations().getTitle());
  }

  @Test
  public void copyTask_shouldHaveSameDescription() {
    assertEquals(expectedTask.getDescription(), originalTask.copyWithNoRelations().getDescription());
  }

  @Test
  public void copyTask_shouldHaveSameTaskState() {
    assertEquals(expectedTask.getTaskState().getTitle(), originalTask.copyWithNoRelations().getTaskState().getTitle());
  }

  @Test
  public void copyTask_shouldHaveSameAssignedUserIds() {
    assertEquals(expectedTask.getAssignedUserIds(), originalTask.copyWithNoRelations().getAssignedUserIds());
  }

  @Test
  public void copyTask_shouldHaveSameParentTaskBoard() {
    assertNotEquals(expectedTask.getParentTaskBoard(), originalTask.copyWithNoRelations().getParentTaskBoard());
  }

  @Test
  public void copyTask_shouldHaveSamePreviousTask() {
    assertEquals(expectedTask.getPreviousTask(), originalTask.copyWithNoRelations().getPreviousTask());
  }
}
