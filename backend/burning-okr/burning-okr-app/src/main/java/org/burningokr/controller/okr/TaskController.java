package org.burningokr.controller.okr;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.TaskDto;
import org.burningokr.mapper.okr.TaskMapper;
import org.burningokr.model.okr.Task;
import org.burningokr.service.okr.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

@RestApiController
public class TaskController {
  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @Autowired
  public TaskController(TaskService taskService, TaskMapper taskMapper) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
  }

  @GetMapping("/unit/{unitId}/tasks")
  public ResponseEntity<Collection<TaskDto>> getTasksForUnit(@PathVariable long unitId) {
    Collection<Task> tasks = taskService.findTaskForOkrUnit(unitId);
    return ResponseEntity.ok(taskMapper.mapEntitiesToDtos(tasks));
  }
}
