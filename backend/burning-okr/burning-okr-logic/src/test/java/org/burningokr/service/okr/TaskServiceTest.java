package org.burningokr.service.okr;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.repositories.okr.TaskRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    TaskState taskStateNotMatchingOriginal = new TaskState();
    taskStateNotMatchingOriginal.setId(201L);
    taskStateNotMatchingOriginal.setTitle("Not the same State Title");
    taskStateNotMatchingOriginal.setParentTaskBoard(null);

    TaskState taskStateMatchingOriginal = new TaskState();
    taskStateNotMatchingOriginal.setId(202L);
    taskStateNotMatchingOriginal.setTitle("State Title");
    taskStateNotMatchingOriginal.setParentTaskBoard(null);

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
    copiedStates.add(taskStateMatchingOriginal);
    copiedStates.add(taskStateNotMatchingOriginal);
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

  @Test
  void copyTasksAndSetNewStates_shouldCopyTasksAndSetNewParentBoardAndStates() {
    //assemble
    TaskState originalState1 = taskMock.getTaskState();
    TaskState originalState2 = TaskState.builder()
      .title("Second State")
      .build();

    Task taskMock1 = this.taskMock;
    Task taskMock2 = Task.builder()
      .id(200L)
      .title("Second Task Title")
      .description("Description")
      .taskState(originalState2)
      .assignedUserIds(taskMock.getAssignedUserIds())
      .parentTaskBoard(taskMock.getParentTaskBoard())
      .assignedKeyResult(taskMock.getAssignedKeyResult())
      .previousTask(null)
      .version(1L)
    .build();

    Collection<Task> unfinishedTasks = List.of(taskMock1, taskMock2);

    TaskState newStateMatchingOriginal1 = TaskState.builder()
      .title(originalState1.getTitle())
      .build();
    TaskState newStateMatchingOriginal2 = TaskState.builder()
      .title(originalState2.getTitle())
      .build();

    Collection<TaskState> copiedTaskStates = List.of(newStateMatchingOriginal1, newStateMatchingOriginal2);

    TaskBoard taskBoard = new TaskBoard();


    //act
    Collection<Task> results = this.taskService.copyTasksAndSetNewStates(unfinishedTasks, copiedTaskStates, taskBoard);


    //assert
    Assertions.assertEquals(2, results.size());
    Optional<Task> taskOptional1 = results.stream().filter(t -> t.getTitle().equals(taskMock1.getTitle())).findFirst();
    Optional<Task> taskOptional2 = results.stream().filter(t -> t.getTitle().equals(taskMock2.getTitle())).findFirst();

    Assertions.assertTrue(taskOptional1.isPresent());
    Assertions.assertTrue(taskOptional2.isPresent());

    Task task1 = taskOptional1.get();
    Task task2 = taskOptional2.get();

    //assert Task1
    Assertions.assertNotSame(taskMock1, task1);
    Assertions.assertEquals(taskMock1.getTitle(), task1.getTitle());
    Assertions.assertEquals(taskMock1.getDescription(), task1.getDescription());
    //todo lazevedo 4.9.23 ask team whether behaviour is correct
    //Assertions.assertEquals(taskMock.getVersion(), task1.getVersion());

    Assertions.assertEquals(taskMock1.getPreviousTask(), task1.getPreviousTask());
    //todo lazevedo 4.9.23 ask team whether behaviour is correct
    //Assertions.assertEquals(taskMock.getAssignedKeyResult(), task1.getAssignedKeyResult());
    Assertions.assertEquals(taskMock1.getAssignedUserIds(), task1.getAssignedUserIds());

    Assertions.assertSame(newStateMatchingOriginal1, task1.getTaskState());
    Assertions.assertSame(taskBoard, task1.getParentTaskBoard());

    //assert Task2
    Assertions.assertNotSame(taskMock2, task2);
    Assertions.assertEquals(taskMock2.getTitle(), task2.getTitle());
    Assertions.assertEquals(taskMock2.getDescription(), task2.getDescription());
    //todo lazevedo 4.9.23 ask team whether behaviour is correct
    //Assertions.assertEquals(taskMock.getVersion(), task2.getVersion());

    Assertions.assertEquals(taskMock2.getPreviousTask(), task2.getPreviousTask());
    //todo lazevedo 4.9.23 ask team whether behaviour is correct
    //Assertions.assertEquals(taskMock2.getAssignedKeyResult(), task2.getAssignedKeyResult());
    Assertions.assertEquals(taskMock2.getAssignedUserIds(), task2.getAssignedUserIds());

    Assertions.assertSame(newStateMatchingOriginal2, task2.getTaskState());
    Assertions.assertSame(taskBoard, task2.getParentTaskBoard());
  }
}
