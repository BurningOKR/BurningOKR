package org.burningokr.controller.okr;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.TaskDto;
import org.burningokr.mapper.okr.TaskMapper;
import org.burningokr.model.okr.Task;
import org.burningokr.model.users.User;
import org.burningokr.service.okr.TaskService;
import org.burningokr.service.security.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestApiController
public class TaskController {
    private AuthorizationService authorizationService;
    private TaskService taskService;
    private TaskMapper taskMapper;

    public TaskController(
            AuthorizationService authorizationService,
            TaskService taskService,
            TaskMapper taskMapper
    ){
    this.authorizationService = authorizationService;
    this.taskService =taskService;
    this.taskMapper = taskMapper;
    }

    @GetMapping("/unit/{unitId}/tasks")
    public ResponseEntity<Collection<TaskDto>> getTasksForUnit(@PathVariable long unitId) {
        Collection<Task> tasks =taskService.findTaskForOkrUnit(unitId);
        return ResponseEntity.ok(taskMapper.mapEntitiesToDtos(tasks));
    }


    @PostMapping("/unit/{unitId}/tasks")
    public ResponseEntity<TaskDto> addTask(@PathVariable long unitId, @Valid @RequestBody TaskDto taskDto, User user) {
        Task newTask = taskMapper.mapDtoToEntity(taskDto);
        newTask= taskService.createTask(newTask, taskDto.getAssignedKeyResultId(), unitId,user);
        return ResponseEntity.ok(taskMapper.mapEntityToDto(newTask));
    }

    @PutMapping("/unit/{unitId}/tasks/{taskId}")
    public ResponseEntity<TaskDto>  updateTask(@PathVariable long unitId, @PathVariable Long taskId, @Valid @RequestBody TaskDto taskDto, User user) {
        Task updatedTask = taskMapper.mapDtoToEntity(taskDto);
        updatedTask = taskService.updateTask(updatedTask, user);
        return ResponseEntity.ok(taskMapper.mapEntityToDto(updatedTask));

    }

    @DeleteMapping("/unit/{unitId}/tasks/{taskId}")
    public ResponseEntity updateTask(@PathVariable long unitId, @PathVariable Long taskId, User user) {
        this.taskService.deleteTaskById(taskId,unitId,user);
        return ResponseEntity.ok().build();
    }
}
