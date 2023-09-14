package org.burningokr.service.okr;

import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.repositories.okr.TaskBoardRepository;
import org.burningokr.repositories.okr.TaskStateRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TaskStateServiceTest {

  @InjectMocks
  private TaskStateService taskStateService;

  @Mock
  private TaskStateRepository taskStateRepositoryMock;
  @Mock
  private OkrDepartmentRepository okrDepartmentRepositoryMock;

  @Test
  void copyTaskStates() {
    //assemble
    TaskBoard taskBoard = new TaskBoard();
    TaskState state1 = new TaskState(null, "State 1", taskBoard);
    TaskState state2 = new TaskState(null, "State 2", taskBoard);
    taskBoard.setAvailableStates(List.of(state1, state2));

    //act
    Collection<TaskState> result = this.taskStateService.copyTaskStates(taskBoard);

    //assert
    Assertions.assertEquals(2, result.size());
    Assertions.assertTrue(result.stream().anyMatch(s -> s.equals(state1)));
    Assertions.assertTrue(result.stream().anyMatch(s -> s.equals(state2)));
    Assertions.assertTrue(result.stream().noneMatch(s -> s == state1 || s == state2));
  }
}
