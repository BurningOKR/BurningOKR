package org.burningokr.service.okr;

import org.burningokr.model.okr.DefaultTaskBoardState;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.repositories.okr.DefaultTaskStateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DefaultTaskStateServiceTest {
  @Test
  public void getDefaultTaskStatesForNewTaskBoard_shouldCreateAllDefaultValuesFromDatabaseEmptyList() {
    DefaultTaskStateRepository repo = Mockito.mock(DefaultTaskStateRepository.class);
    DefaultTaskStateService sut = new DefaultTaskStateService(repo);
    when(repo.findAll()).thenReturn(new ArrayList<>());

    Collection<TaskState> result = sut.getDefaultTaskStatesForNewTaskBoard(new TaskBoard());

    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void getDefaultTaskStatesForNewTaskBoard_shouldCreateAllDefaultValuesFromDatabase() {
    DefaultTaskStateRepository repo = Mockito.mock(DefaultTaskStateRepository.class);
    DefaultTaskStateService sut = new DefaultTaskStateService(repo);
    TaskBoard parentBoard = new TaskBoard();
    ArrayList<DefaultTaskBoardState> taskBoards = new ArrayList<>();
    DefaultTaskBoardState taskBoard = new DefaultTaskBoardState();
    taskBoard.setId(2L);
    taskBoard.setTitle("My Title");
    taskBoards.add(taskBoard);
    when(repo.findAll()).thenReturn(taskBoards);

    Collection<TaskState> result = sut.getDefaultTaskStatesForNewTaskBoard(parentBoard);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertSame(parentBoard, result.toArray(new TaskState[0])[0].getParentTaskBoard());
    assertEquals(taskBoard.getTitle(), result.toArray(new TaskState[0])[0].getTitle());
    assertNull(result.toArray(new TaskState[0])[0].getId());
  }

  @Test
  public void getDefaultTaskStatesForNewTaskBoard_shouldCreateAllDefaultValuesFromDatabaseMultiple() {
    DefaultTaskStateRepository repo = Mockito.mock(DefaultTaskStateRepository.class);
    DefaultTaskStateService sut = new DefaultTaskStateService(repo);
    TaskBoard parentBoard = new TaskBoard();
    ArrayList<DefaultTaskBoardState> taskBoards = new ArrayList<>();
    for(int i = 0; i < 10; i++)
    {
      taskBoards.add(new DefaultTaskBoardState());
    }
    when(repo.findAll()).thenReturn(taskBoards);

    Collection<TaskState> result = sut.getDefaultTaskStatesForNewTaskBoard(parentBoard);

    assertNotNull(result);
    assertEquals(10, result.size());
    for(int i = 0; i < 10; i++)
    {
      assertSame(parentBoard, result.toArray(new TaskState[0])[i].getParentTaskBoard());
    }
  }
}
