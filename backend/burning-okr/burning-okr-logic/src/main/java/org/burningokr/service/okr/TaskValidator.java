package org.burningokr.service.okr;

import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.okr.Task;
import org.burningokr.repositories.okr.TaskRepository;

@Slf4j
public class TaskValidator {

  /**
   * Checks, whether a task is self-referencing or if its predecessor is located in another column
   * or has a different state
   */
  public Task validateTask(Task task, TaskRepository taskRepository) throws Exception {
    if (!task.hasPreviousTask()) {
      return null;
    }
    if (task.getPreviousTask().getId().equals(task.getId())) {
      throw new Exception("Task is its own predecessor.");
    }

    Task prevTask = taskRepository.findByIdOrThrow(task.getPreviousTask().getId());
    log.debug("Validation - Previous and current task have different ids");
    if (!(task.getTaskState().getId().equals(prevTask.getTaskState().getId()))) {
      throw new Exception("Tasks are in different states");
    }

    return prevTask;
  }
}
