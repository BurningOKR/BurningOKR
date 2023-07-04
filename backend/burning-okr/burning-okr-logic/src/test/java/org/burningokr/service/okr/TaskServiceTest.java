package org.burningokr.service.okr;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.repositories.okr.TaskRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
  private Collection<Task> notFinishedTasks;
  private Collection<TaskState> copiedStates;
  private TaskBoard copiedTaskBoard;

  @Mock
  private TaskRepository taskRepository;
  @Mock
  private OkrDepartmentRepository okrDepartmentRepository;
  @Mock
  private ActivityService activityService;
  @Mock
  private EntityCrawlerService entityCrawlerService;

  @InjectMocks
  private TaskService taskService;

  private final Task taskMock = new Task();

  @BeforeEach
  public void init() {
    notFinishedTasks = new ArrayList<>();
    copiedStates = new ArrayList<>();

    ArrayList<UUID> userIds = new ArrayList<>();
    userIds.add(UUID.randomUUID());
    userIds.add(UUID.randomUUID());
    userIds.add(UUID.randomUUID());

    TaskState taskStateOriginal = new TaskState();
    taskStateOriginal.setId(200L);
    taskStateOriginal.setTitle("State Title");
    taskStateOriginal.setParentTaskBoard(null);

    TaskState taskStateNew1 = new TaskState();
    taskStateNew1.setId(201L);
    taskStateNew1.setTitle("Not the same State Title");
    taskStateNew1.setParentTaskBoard(null);

    TaskState taskStateNew2 = new TaskState();
    taskStateNew2.setId(202L);
    taskStateNew2.setTitle("State Title");
    taskStateNew2.setParentTaskBoard(null);

    KeyResult keyResult = new KeyResult();
    keyResult.setId(300L);
    keyResult.setName("Key Result Name");
    keyResult.setDescription("Key Result Description");

    taskMock.setId(100L);
    taskMock.setTitle("Task Title");
    taskMock.setDescription("Description");
    taskMock.setTaskState(taskStateOriginal);
    taskMock.setAssignedUserIds(userIds);
    taskMock.setParentTaskBoard(null);
    taskMock.setAssignedKeyResult(keyResult);
    taskMock.setPreviousTask(null);
    taskMock.setVersion(1L);

    notFinishedTasks.add(taskMock);
    copiedStates.add(taskStateNew1);
    copiedStates.add(taskStateNew2);
  }


  @Test
  public void copyTask_shouldNotHaveSameId() {
    assertNotEquals(taskMock.getId(), taskService.copyTask(taskMock, copiedStates, copiedTaskBoard).getId());
  }

  @Test
  public void copyTask_shouldNotHaveSameVersion() {
    assertNotEquals(taskMock.getVersion(), taskService.copyTask(taskMock, copiedStates, copiedTaskBoard).getVersion());
  }

  @Test
  public void copyTask_shouldNotHaveSameAssignedKeyResult() {
    assertNotEquals(taskMock.getAssignedKeyResult(), taskService.copyTask(taskMock, copiedStates, copiedTaskBoard).getAssignedKeyResult());
  }

  @Test
  public void copyTask_shouldHaveSameTitle() {
    assertEquals(taskMock.getTitle(), taskService.copyTask(taskMock, copiedStates, copiedTaskBoard).getTitle());
  }

  @Test
  public void copyTask_shouldHaveSameDescription() {
    assertEquals(taskMock.getDescription(), taskService.copyTask(taskMock, copiedStates, copiedTaskBoard).getDescription());
  }

  @Test
  public void copyTask_shouldHaveSameTaskState() {
    assertEquals(taskMock.getTaskState().getTitle(), taskService.copyTask(taskMock, copiedStates, copiedTaskBoard).getTaskState().getTitle());
  }

  @Test
  public void copyTask_shouldHaveSameAssignedUserIds() {
    assertEquals(taskMock.getAssignedUserIds(), taskService.copyTask(taskMock, copiedStates, copiedTaskBoard).getAssignedUserIds());
  }

  @Test
  public void copyTask_shouldHaveSameParentTaskBoard() {
    assertEquals(taskMock.getParentTaskBoard(), taskService.copyTask(taskMock, copiedStates, copiedTaskBoard).getParentTaskBoard());
  }

  @Test
  public void copyTask_shouldHaveSamePreviousTask() {
    assertEquals(taskMock.getPreviousTask(), taskService.copyTask(taskMock, copiedStates, copiedTaskBoard).getPreviousTask());
  }
}
