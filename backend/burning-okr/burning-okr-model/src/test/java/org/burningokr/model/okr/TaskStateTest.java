package org.burningokr.model.okr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskStateTest {
  TaskState taskState;

  @BeforeEach
  public void init() {
    taskState = new TaskState();

  }

  @Test
  public void copy_shouldCopy() {
    assertEquals(taskState, taskState.copy());
  }

}
