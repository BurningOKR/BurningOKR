package org.burningokr.controller.okr;

import jakarta.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.TaskDto;
import org.burningokr.mapper.okr.TaskMapper;
import org.burningokr.model.okr.Task;
import org.burningokr.service.okr.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity<Collection<TaskDto>> getTasksForUnit(
    @PathVariable long unitId
  ) {
    Collection<Task> tasks = taskService.findTaskForOkrUnit(unitId);
    return ResponseEntity.ok(taskMapper.mapEntitiesToDtos(tasks));
  }

  @PostMapping("/unit/{unitId}/tasks")
  public ResponseEntity<Collection<TaskDto>> addTask(
    @PathVariable long unitId,
    @Valid
    @RequestBody
    TaskDto taskDto
  ) {
    Task newTask = taskMapper.mapDtoToEntity(taskDto);
    Collection<Task> createdAndUpdatedTasks = taskService.createTask(newTask, unitId);

    return ResponseEntity.ok(taskMapper.mapEntitiesToDtos(createdAndUpdatedTasks));
  }

  @PutMapping("/unit/{unitId}/tasks/{taskId}")
  public ResponseEntity<Collection<TaskDto>> updateTask(
    @PathVariable long unitId,
    @PathVariable Long taskId,
    @Valid
    @RequestBody
    TaskDto taskDto
  )
    throws Exception {
    Task updatedTask = taskMapper.mapDtoToEntity(taskDto);
    Collection<Task> updatedTasks = taskService.updateTask(updatedTask);
    return ResponseEntity.ok(taskMapper.mapEntitiesToDtos(updatedTasks));
  }

  @DeleteMapping("/unit/{unitId}/tasks/{taskId}")
  public ResponseEntity deleteTask(
    @PathVariable long unitId,
    @PathVariable Long taskId
  ) {
    this.taskService.deleteTaskById(taskId, unitId);
    return ResponseEntity.ok().build();
  }
}
