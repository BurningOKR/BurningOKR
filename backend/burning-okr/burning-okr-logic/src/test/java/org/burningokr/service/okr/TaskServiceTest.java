package org.burningokr.service.okr;

import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.repositories.okr.TaskRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

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
  @Spy
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

  @Test
  void deleteTaskById_shouldDeleteTaskWithoutPreviousOrNextTask() {
    //assemble
    long inputTaskId = 1L;
    long inputUnitId = 2L;

    OkrUnit unitMock = mock(OkrDepartment.class);
    doReturn(unitMock).when(this.okrDepartmentRepository).findByIdOrThrow(inputUnitId);

    Cycle activeCycleMock = mock(Cycle.class);
    doReturn(CycleState.PREPARATION).when(activeCycleMock).getCycleState();
    doReturn(activeCycleMock).when(this.entityCrawlerService).getCycleOfUnit(unitMock);

    Task taskToDelete = new Task();
    taskToDelete.setId(1L);
    doReturn(taskToDelete).when(this.taskRepository).findByIdOrThrow(inputTaskId);
    doReturn(null).when(this.taskRepository)
      .findByPreviousTask(eq(taskToDelete), nullable(TaskState.class), nullable(TaskBoard.class));

    doNothing().when(this.taskRepository).deleteById(taskToDelete.getId());
    doNothing().when(this.activityService).createActivity(taskToDelete, Action.DELETED);

    //act
    Collection<Task> results = this.taskService.deleteTaskById(inputTaskId, inputUnitId);

    //assert
    Assertions.assertEquals(0, results.size());
    verify(this.taskRepository, times(0)).save(any());
    verify(this.taskRepository).deleteById(taskToDelete.getId());
  }

  @Test
  void deleteTaskById_shouldDeleteTaskWithPreviousTask() {
    //assemble
    long inputTaskId = 1L;
    long inputUnitId = 2L;

    OkrUnit unitMock = mock(OkrDepartment.class);
    doReturn(unitMock).when(this.okrDepartmentRepository).findByIdOrThrow(inputUnitId);

    Cycle activeCycleMock = mock(Cycle.class);
    doReturn(CycleState.PREPARATION).when(activeCycleMock).getCycleState();
    doReturn(activeCycleMock).when(this.entityCrawlerService).getCycleOfUnit(unitMock);

    Task prevTask = new Task();
    Task taskToDelete = new Task();
    taskToDelete.setId(1L);
    taskToDelete.setPreviousTask(prevTask);
    doReturn(taskToDelete).when(this.taskRepository).findByIdOrThrow(inputTaskId);
    doReturn(taskToDelete).when(this.taskRepository).save(taskToDelete);
    doReturn(null).when(this.taskRepository)
      .findByPreviousTask(eq(taskToDelete), nullable(TaskState.class), nullable(TaskBoard.class));

    doNothing().when(this.taskRepository).deleteById(taskToDelete.getId());
    doNothing().when(this.activityService).createActivity(taskToDelete, Action.DELETED);

    //act
    Collection<Task> results = this.taskService.deleteTaskById(inputTaskId, inputUnitId);

    //assert
    Assertions.assertEquals(1, results.size());
    Task resultingTask = results.stream().findFirst().orElse(null);
    Assertions.assertSame(prevTask, resultingTask);

    verify(this.taskRepository, times(1)).save(taskToDelete);
    verify(this.taskRepository).deleteById(taskToDelete.getId());
    verify(this.activityService).createActivity(taskToDelete, Action.DELETED);
  }

  @Test
  void deleteTaskById_shouldDeleteTaskWithSuccessorTask() {
    //assemble
    long inputTaskId = 1L;
    long inputUnitId = 2L;

    OkrUnit unitMock = mock(OkrDepartment.class);
    doReturn(unitMock).when(this.okrDepartmentRepository).findByIdOrThrow(inputUnitId);

    Cycle activeCycleMock = mock(Cycle.class);
    doReturn(CycleState.PREPARATION).when(activeCycleMock).getCycleState();
    doReturn(activeCycleMock).when(this.entityCrawlerService).getCycleOfUnit(unitMock);

    Task succTask = new Task();
    Task taskToDelete = new Task();
    taskToDelete.setId(1L);
    succTask.setPreviousTask(taskToDelete);

    doReturn(taskToDelete).when(this.taskRepository).findByIdOrThrow(inputTaskId);
    doReturn(succTask).when(this.taskRepository)
      .findByPreviousTask(taskToDelete, taskToDelete.getTaskState(), taskToDelete.getParentTaskBoard());
    doReturn(succTask).when(this.taskRepository).save(succTask);

    doNothing().when(this.taskRepository).deleteById(taskToDelete.getId());
    doNothing().when(this.activityService).createActivity(taskToDelete, Action.DELETED);

    //act
    Collection<Task> results = this.taskService.deleteTaskById(inputTaskId, inputUnitId);

    //assert
    Assertions.assertEquals(1, results.size());
    Task resultingTask = results.stream().findFirst().orElse(null);
    Assertions.assertSame(succTask, resultingTask);
    Assertions.assertNull(succTask.getPreviousTask());

    verify(this.taskRepository, times(1)).save(succTask);
    verify(this.taskRepository).deleteById(taskToDelete.getId());
    verify(this.activityService).createActivity(taskToDelete, Action.DELETED);
  }


  @Test
  void deleteTaskById_shouldDeleteTaskWithPreviousAndSuccessorTask() {
    //assemble
    long inputTaskId = 1L;
    long inputUnitId = 2L;

    OkrUnit unitMock = mock(OkrDepartment.class);
    doReturn(unitMock).when(this.okrDepartmentRepository).findByIdOrThrow(inputUnitId);

    Cycle activeCycleMock = mock(Cycle.class);
    doReturn(CycleState.PREPARATION).when(activeCycleMock).getCycleState();
    doReturn(activeCycleMock).when(this.entityCrawlerService).getCycleOfUnit(unitMock);

    Task prevTask = new Task();
    prevTask.setTitle("Previous Task");
    Task taskToDelete = new Task();
    taskToDelete.setPreviousTask(prevTask);
    taskToDelete.setId(1L);
    Task succTask = new Task();
    succTask.setPreviousTask(taskToDelete);
    succTask.setTitle("Successor Task");

    doReturn(taskToDelete).when(this.taskRepository).findByIdOrThrow(inputTaskId);
    doReturn(taskToDelete).when(this.taskRepository).save(taskToDelete);
    doReturn(succTask).when(this.taskRepository)
      .findByPreviousTask(taskToDelete, taskToDelete.getTaskState(), taskToDelete.getParentTaskBoard());
    doReturn(succTask).when(this.taskRepository).save(succTask);

    doNothing().when(this.taskRepository).deleteById(taskToDelete.getId());
    doNothing().when(this.activityService).createActivity(taskToDelete, Action.DELETED);

    //act
    Collection<Task> results = this.taskService.deleteTaskById(inputTaskId, inputUnitId);

    //assert
    Assertions.assertEquals(2, results.size());
    Task resultingPrevTask = results.stream().filter(t -> t.getTitle().equals("Previous Task")).findFirst().orElse(null);
    Task resultingSuccTask = results.stream().filter(t -> t.getTitle().equals("Successor Task")).findFirst().orElse(null);

    Assertions.assertSame(prevTask, resultingPrevTask);

    Assertions.assertSame(succTask, resultingSuccTask);
    Assertions.assertEquals(resultingPrevTask, succTask.getPreviousTask());

    verify(this.taskRepository, times(2)).save(any());
    verify(this.taskRepository, times(1)).save(succTask);
    verify(this.taskRepository, times(1)).save(taskToDelete);
    verify(this.taskRepository).deleteById(taskToDelete.getId());
    verify(this.activityService).createActivity(taskToDelete, Action.DELETED);
  }

  @Test
  void createTask_shouldSaveTheVeryFirstTaskOfABoard() {
    //assemble
    Task invalidPrevTask = spy(Task.class);
    Task taskToCreate = spy(new Task());
    taskToCreate.setPreviousTask(invalidPrevTask); //expected to be set to null
    Long inputOkrUnitId = 1L;

    TaskBoard board = new TaskBoard();
    OkrDepartment unit = new OkrDepartment();
    unit.setTaskBoard(board);

    doReturn(unit).when(this.okrDepartmentRepository).findByIdOrThrow(inputOkrUnitId);
    doNothing().when(this.taskService).throwIfCycleOfTaskIsNotActive(unit);
    doReturn(null).when(this.taskRepository).findFirstTaskOfList(board, null);
    doReturn(taskToCreate).when(this.taskRepository).save(taskToCreate);

    //act
    Collection<Task> result = this.taskService.createTask(taskToCreate, inputOkrUnitId);

    //assert
    Assertions.assertEquals(1, result.size());
    Task resTask = result.stream().findFirst().orElse(null);
    Assertions.assertNotNull(resTask);
    Assertions.assertSame(taskToCreate, resTask);
    Assertions.assertNull(resTask.getPreviousTask());
    Assertions.assertSame(board, resTask.getParentTaskBoard());

    ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
    verify(this.taskRepository).save(taskCaptor.capture());
    Assertions.assertSame(resTask, taskCaptor.getValue());
    Assertions.assertNull(taskCaptor.getValue().getPreviousTask());
    Assertions.assertEquals(board, taskCaptor.getValue().getParentTaskBoard());
  }

  @Test
  void createTask_shouldSaveSecondTaskOfABoard() {
    //assemble
    Task existingTask = spy(Task.class);
    existingTask.setTitle("existing task");
    Task taskToCreate = spy(Task.class);
    taskToCreate.setTitle("task to create");
    Long inputOkrUnitId = 1L;

    TaskBoard board = new TaskBoard();
    OkrDepartment unit = new OkrDepartment();
    unit.setTaskBoard(board);

    doReturn(unit).when(this.okrDepartmentRepository).findByIdOrThrow(inputOkrUnitId);
    doNothing().when(this.taskService).throwIfCycleOfTaskIsNotActive(unit);
    doReturn(existingTask).when(this.taskRepository).findFirstTaskOfList(board, null);
    doReturn(existingTask).when(this.taskRepository).save(existingTask);
    doReturn(taskToCreate).when(this.taskRepository).save(taskToCreate);

    //act
    Collection<Task> result = this.taskService.createTask(taskToCreate, inputOkrUnitId);

    //assert
    Assertions.assertEquals(2, result.size());
    Task resCreatedTask = result.stream().filter(t->t.getTitle().equals(taskToCreate.getTitle())).findFirst().orElse(null);
    Assertions.assertNotNull(resCreatedTask);
    Assertions.assertSame(taskToCreate, resCreatedTask);
    Assertions.assertNull(resCreatedTask.getPreviousTask());
    Assertions.assertSame(board, resCreatedTask.getParentTaskBoard());

    Task resExistingTask = result.stream().filter(t->t.getTitle().equals(existingTask.getTitle())).findFirst().orElse(null);
    Assertions.assertNotNull(resExistingTask);
    Assertions.assertSame(existingTask, resExistingTask);
    Assertions.assertSame(resCreatedTask, resExistingTask.getPreviousTask());


    ArgumentCaptor<Task> tasksCaptor = ArgumentCaptor.forClass(Task.class);
    verify(this.taskRepository, times(2)).save(tasksCaptor.capture());

    Task capturedCreatedTask = tasksCaptor.getAllValues().get(0);
    Task capturedExistingTask = tasksCaptor.getAllValues().get(1);

    Assertions.assertSame(resCreatedTask, capturedCreatedTask);
    Assertions.assertEquals(resCreatedTask, capturedCreatedTask);
    Assertions.assertEquals(resCreatedTask.getParentTaskBoard(), capturedCreatedTask.getParentTaskBoard());
    Assertions.assertNull(capturedCreatedTask.getPreviousTask());

    Assertions.assertSame(resExistingTask, capturedExistingTask);
    Assertions.assertEquals(resExistingTask, capturedExistingTask);
    Assertions.assertEquals(resCreatedTask, capturedExistingTask.getPreviousTask());

    verifyNoMoreInteractions(this.taskRepository);
  }

  @Test
  void createTask_shouldCreateActivity() {
    //assemble
    Task taskToCreate = spy(new Task());
    Long inputOkrUnitId = 1L;

    TaskBoard board = new TaskBoard();
    OkrDepartment unit = new OkrDepartment();
    unit.setTaskBoard(board);

    doReturn(unit).when(this.okrDepartmentRepository).findByIdOrThrow(inputOkrUnitId);
    doNothing().when(this.taskService).throwIfCycleOfTaskIsNotActive(unit);
    doReturn(null).when(this.taskRepository).findFirstTaskOfList(board, null);
    doReturn(taskToCreate).when(this.taskRepository).save(taskToCreate);

    //act
    this.taskService.createTask(taskToCreate, inputOkrUnitId);

    //assert
    verify(this.activityService).createActivity(taskToCreate, Action.CREATED);
  }

  @Test
  void throwIfCycleOfTaskIsNotActive_shouldNotThrowForbiddenException(){
    //assemble
    OkrUnit unit = new OkrDepartment();
    Cycle cycle = new Cycle();
    cycle.setCycleState(CycleState.ACTIVE);

    doReturn(cycle).when(this.entityCrawlerService).getCycleOfUnit(unit);

    //act + assert
    Assertions.assertDoesNotThrow(
      () -> this.taskService.throwIfCycleOfTaskIsNotActive(unit)
    );
  }

  @Test
  void throwIfCycleOfTaskIsNotActive_shouldThrowForbiddenException() {
    //assemble
    OkrUnit unit = new OkrDepartment();
    Cycle cycle = new Cycle();
    cycle.setCycleState(CycleState.CLOSED);

    doReturn(cycle).when(this.entityCrawlerService).getCycleOfUnit(unit);

    //act + assert
    ForbiddenException exception = Assertions.assertThrows(
      ForbiddenException.class,
      () -> this.taskService.throwIfCycleOfTaskIsNotActive(unit)
    );

    Assertions.assertEquals("Cannot modify task because it belongs to a closed cycle.", exception.getMessage());
  }
}
