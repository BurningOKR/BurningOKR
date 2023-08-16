package org.burningokr.service.okr;

import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.repositories.okr.TaskBoardRepository;
import org.burningokr.repositories.okr.TaskRepository;
import org.burningokr.repositories.okr.TaskStateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TaskBoardServiceTest {

  @InjectMocks
  private TaskBoardService taskBoardService;

  @Mock
  private TaskBoardRepository taskBoardRepository;
  @Mock
  private DefaultTaskStateService defaultTaskStateService;
  @Mock
  private TaskStateRepository taskStateRepository;
  @Mock
  private TaskService taskService;
  @Mock
  private TaskRepository taskRepository;


  @BeforeEach
  void setUp() {
  }

  @Test
  void createNewTaskBoardWithDefaultStates_shouldReturnBoardWithSetParent() {
    //assemble
    OkrDepartment department = new OkrDepartment();
    doReturn(List.of(new TaskState())).when(defaultTaskStateService).getDefaultTaskStatesForNewTaskBoard(any());

    //act
    TaskBoard resultTaskboard = this.taskBoardService.createNewTaskBoardWithDefaultStates(department);

    //assert -> pruefen
    Assertions.assertNotNull(resultTaskboard.getParentOkrDepartment());
    Assertions.assertSame(department, resultTaskboard.getParentOkrDepartment());
  }

  @Test
  void copyTaskBoardWithParentOkrUnitOnly() {
    //assemble
    OkrDepartment department = new OkrDepartment();

    //act
    TaskBoard board = this.taskBoardService.copyTaskBoardWithParentOkrUnitOnly(department);

    //assert
    Assertions.assertNotNull(board.getParentOkrDepartment());
  }

  @Test
  void saveTaskBoard_shouldSaveTaskboardAndAllTasks() {
    //assemble
    TaskState state = new TaskState();
    TaskBoard taskBoard = new TaskBoard();
    Collection<TaskState> states = List.of(state);
    taskBoard.setAvailableStates(states);

    doReturn(taskBoard).when(taskBoardRepository).save(taskBoard);
    doReturn(states).when(taskStateRepository).saveAll(states);

    //act
    TaskBoard resultTaskBoard = this.taskBoardService.saveTaskBoard(taskBoard);

    //assert
    Assertions.assertNotNull(taskBoard);
    Assertions.assertEquals(states.size(), resultTaskBoard.getAvailableStates().size());
    Assertions.assertSame(taskBoard, resultTaskBoard);
    Assertions.assertSame(state, taskBoard.getAvailableStates().stream().findFirst().get());

    InOrder inOrder = Mockito.inOrder(taskBoardRepository, taskStateRepository);
    inOrder.verify(taskBoardRepository).save(taskBoard);
    inOrder.verify(taskStateRepository).saveAll(taskBoard.getAvailableStates());
  }

//  @Test
//  void cloneTaskBoard() {
//    //assemble
//
//    doReturn(new ArrayList<>()).when(this.taskBoardService).findFinishedState(any());
//    doReturn(null).when(this.taskStateRepository).saveAll(any());
//    doReturn(null).when(this.taskBoardRepository).save(any());
//    doReturn(null).when(this.taskService).save(any());
//    //act
//
//    //assert
////     OkrDepartment copiedOkrDepartment = new OkrDepartment();
////     TaskBoard taskBoard = new TaskBoard();
////
//////     doReturn();
////    TaskBoard clonedTaskBoard = this.taskBoardService.copyTaskBoardWithParentOkrUnitOnly(copiedOkrDepartment);
//  }

  @Test
  void copyTaskStateListAndSetTaskBoard_shouldReturnListOfCopiedTaskStates() {
    //assemble
    TaskBoard taskBoard = new TaskBoard();
    TaskState state1 = new TaskState();
    TaskState state2 = new TaskState();
    Collection<TaskState> states = List.of(state1,state2);


    //act
    Collection<TaskState> result = this.taskBoardService.copyTaskStateListAndSetTaskBoard(states, taskBoard);

    //assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(states.size(),result.size());
    Assertions.assertFalse(result.stream().anyMatch(s -> s == state1));
    Assertions.assertFalse(result.stream().anyMatch(s -> s == state2));
    Assertions.assertTrue(result.stream().allMatch(s -> s.getParentTaskBoard() == taskBoard));

  }

  @Test
  void findFinishedState_shouldReturnState() {
    //assemble
    TaskState taskState = new TaskState();
    taskState.setTitle("Finished");
    Collection<TaskState> states = List.of(taskState);

    //act
    TaskState result = this.taskBoardService.findFinishedState(states);

    //assert
    Assertions.assertNotNull(result);
    Assertions.assertSame(taskState, result);
    Assertions.assertEquals(taskState, result);
  }

  @Test
  void findFinishedState_shouldReturnNull() {
    //assemble
    TaskState taskState = new TaskState();
    taskState.setTitle("Not Finished");
    Collection<TaskState> states = List.of(taskState);

    //act
    TaskState result = this.taskBoardService.findFinishedState(states);

    //assert
    Assertions.assertNull(result);
  }

}

