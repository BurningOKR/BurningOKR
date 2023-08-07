package org.burningokr.controller.okr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.dto.okr.TaskDto;
import org.burningokr.mapper.okr.TaskMapper;
import org.burningokr.model.okr.Task;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okr.TaskService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebsocketTaskController {
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @MessageMapping("unit/{unitId}/tasks/add")
  public void addTask(
      @DestinationVariable long unitId,
      TaskDto taskDto,
      Authentication authentication
  ) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
    log.debug(
        String.format(
            "Websocket add Task dto: {id: %d, title: %s, description: %s, assignedUserIds: %s, assignedKeyResultId: %d, task board Id: %d, stateId: %d}",
            taskDto.getId(),
            taskDto.getTitle(),
            taskDto.getDescription(),
            taskDto.getAssignedUserIds(),
            taskDto.getAssignedKeyResultId(),
            taskDto.getParentTaskBoardId(),
            taskDto.getTaskStateId()
        ));

    Task newTask = taskMapper.mapDtoToEntity(taskDto);
    try {
      Collection<TaskDto> createdAndUpdatedTasks =
          this.taskMapper.mapEntitiesToDtos(taskService.createTask(newTask, unitId));

      sendNewOrUpdatedTasks(createdAndUpdatedTasks, unitId);
      log.debug("Broadcast for added task");
    } catch (ForbiddenException ex) {
      log.error(ex.getMessage());
      log.warn("want to add a task in a not active cycle");
      sendDeletedTasks(taskDto, unitId);
    }
  }

  @MessageMapping("unit/{unitId}/tasks/update")
  public void updateTask(@DestinationVariable long unitId, TaskDto taskDto, Authentication authentication) throws Exception {
    log.debug("update Task on Websocket");
    SecurityContextHolder.getContext().setAuthentication(authentication);
    try {
      Task updatedTask = taskMapper.mapDtoToEntity(taskDto);
      Collection<Task> updatedTasks = taskService.updateTask(updatedTask);
      Collection<TaskDto> taskDtoList = taskMapper.mapEntitiesToDtos(updatedTasks);

      sendNewOrUpdatedTasks(taskDtoList, unitId);
      log.debug("Broadcast for updated task");
    } catch (ForbiddenException ex) {
      TaskDto oldTask = taskMapper.mapEntityToDto(taskService.getById(taskDto.getId()));
      Collection<TaskDto> tasks = new ArrayList<>();
      tasks.add(oldTask);
      sendNewOrUpdatedTasks(tasks, unitId);
      log.warn("want to update a task in a not active cycle");
    }
  }

  @MessageMapping("unit/{unitId}/tasks/delete")
  public void deleteTask(@DestinationVariable long unitId, TaskDto taskDto, Authentication authentication) {
    log.debug("delete Task on Websocket");
    SecurityContextHolder.getContext().setAuthentication(authentication);
    try {
      Task taskToDelete = taskMapper.mapDtoToEntity(taskDto);
      Collection<Task> updatedTasks =
          taskService.deleteTaskById(taskToDelete.getId(), unitId);

      String deletionUrl = String.format("/topic/unit/%d/tasks/deleted", unitId);
      String updateUrl = String.format("/topic/unit/%d/tasks", unitId);

      simpMessagingTemplate.convertAndSend(deletionUrl, taskDto);
      simpMessagingTemplate.convertAndSend(updateUrl, taskMapper.mapEntitiesToDtos(updatedTasks));
      log.debug("Task deleted and broadcast to: " + deletionUrl);
      log.debug("Referenced Tasks updated after Deletion and broadcast to: " + updateUrl);
    } catch (ForbiddenException ex) {
      TaskDto oldTask = taskMapper.mapEntityToDto(taskService.getById(taskDto.getId()));
      Collection<TaskDto> tasks = new ArrayList<>();
      tasks.add(oldTask);
      sendNewOrUpdatedTasks(tasks, unitId);
      log.warn("want to delete a task in a not active cycle");
    }
  }

  private void sendNewOrUpdatedTasks(Collection<TaskDto> taskDtoList, long unitId) {
    String url = String.format("/topic/unit/%d/tasks", unitId);
    simpMessagingTemplate.convertAndSend(url, taskDtoList);
  }

  private void sendDeletedTasks(TaskDto deletedTask, long unitId) {
    String deletionUrl = String.format("/topic/unit/%d/tasks/deleted", unitId);
    simpMessagingTemplate.convertAndSend(deletionUrl, deletedTask);
  }
}
