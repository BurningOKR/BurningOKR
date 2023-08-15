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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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
  void saveTaskBoard_shouldSaveAllTasks() throws Exception {
    //assemble
    TaskBoard taskBoard = new TaskBoard();
    TaskState state = new TaskState();
    taskBoard.setAvailableStates(List.of(state));
    doReturn(taskBoard).when(taskBoardRepository).save(taskBoard);
    doReturn(state).when(taskStateRepository).saveAll(any());

    //act
    taskBoard = this.taskBoardService.saveTaskBoard(taskBoard);

    //assetion
    Assertions.assertNotNull(taskBoard);
    Assertions.assertNotNull(state);
    Assertions.assertNotNull(taskBoard.getAvailableStates());
    Assertions.assertEquals(taskBoard, taskBoard.getAvailableStates());
  }

  @Test
  void cloneTaskBoard() {
     OkrDepartment copiedOkrDepartment = new OkrDepartment();
     TaskBoard taskBoard = new TaskBoard();

//     doReturn();
    TaskBoard clonedTaskBoard = this.taskBoardService.copyTaskBoardWithParentOkrUnitOnly(copiedOkrDepartment);
  }

  @Test
  void copyTaskStateListAndSetTaskBoard() {
  }

  @Test
  void findFinishedState() {
  }

  @Test
  void findUnfinishedTasks() {
  }
}

