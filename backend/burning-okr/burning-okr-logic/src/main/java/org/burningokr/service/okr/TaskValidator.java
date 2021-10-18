package org.burningokr.service.okr;

import org.burningokr.model.okr.Task;
import org.burningokr.repositories.okr.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskValidator {
  private final Logger logger = LoggerFactory.getLogger(TaskService.class);

  /**
   * Checks, whether a task is self-referencing or if its predecessor is located in another collumn
   * or has a different state
   */
  public Task validateTask(Task task, TaskRepository taskRepository) throws Exception {
    if (!task.hasPreviousTask()) {
      return null;
    }
    if (task.getPreviousTask().getId().equals(task.getId())) {
      throw new Exception("Aufgabe ist sein eigener Vorgänger");
    }

    Task newTask = taskRepository.findByIdOrThrow(task.getPreviousTask().getId());
    logger.info("Validation - previous and current task have different ids");
    if (!(task.getTaskState().getId().equals(newTask.getTaskState().getId()))) {
      throw new Exception("Aufgaben sind in unterschiedlichen Spalten/Zuständen");
    }

    return newTask;
  }
}
