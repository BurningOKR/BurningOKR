package org.burningokr.controller.okr;

import org.burningokr.dto.okr.TaskDto;
import org.burningokr.mapper.okr.TaskMapper;
import org.burningokr.model.okr.Task;
import org.burningokr.service.WebsocketUserService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okr.TaskService;
import org.burningokr.service.userhandling.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;

@Controller
public class WebsocketTaskController {
  private final Logger logger = LoggerFactory.getLogger(TaskService.class);
  private final AuthorizationService authorizationService;
  private final TaskService taskService;
  private final TaskMapper taskMapper;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final WebsocketUserService websocketUserService;
  private final SimpUserRegistry simpUserRegistry;

  @Autowired
  public WebsocketTaskController(
    AuthorizationService authorizationService,
    TaskService taskService,
    TaskMapper taskMapper,
    SimpMessagingTemplate simpMessagingTemplate,
    UserService userService,
    WebsocketUserService websocketUserService,
    SimpUserRegistry simpUserRegistry
  ) {
    this.authorizationService = authorizationService;
    this.taskService = taskService;
    this.taskMapper = taskMapper;
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.websocketUserService = websocketUserService;
    this.simpUserRegistry = simpUserRegistry;
  }

  @MessageMapping("unit/{unitId}/tasks/add")
  public void addTask(
    @DestinationVariable long unitId,
    TaskDto taskDto,
    StompHeaderAccessor stompHeaderAccessor
  ) {
    logger.info(
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
        this.taskMapper.mapEntitiesToDtos(
              taskService.createTask(
                  newTask, unitId, websocketUserService.findByAccessor(stompHeaderAccessor)));

      sendNewOrUpdatedTasks(createdAndUpdatedTasks, unitId);
      logger.info("Broadcast for added task");
    } catch (ForbiddenException ex) {
      logger.error(ex.getMessage());
      logger.info("want to add a task in a closed cycle");
      sendDeletedTasks(taskDto, unitId);
    }
  }

  @MessageMapping("unit/{unitId}/tasks/update")
  public void updateTask(
      @DestinationVariable long unitId,
      TaskDto taskDto, StompHeaderAccessor stompHeaderAccessor)
      throws Exception {
    logger.info("update Task on Websocket");
    try {
      Task updatedTask = taskMapper.mapDtoToEntity(taskDto);
      Collection<Task> updatedTasks =
          taskService.updateTask(
              updatedTask, websocketUserService.findByAccessor(stompHeaderAccessor));
      Collection<TaskDto> taskDtoList = taskMapper.mapEntitiesToDtos(updatedTasks);

      sendNewOrUpdatedTasks(taskDtoList, unitId);
      logger.info("Broadcast for updated task");
    } catch (ForbiddenException ex) {
      TaskDto oldTask = taskMapper.mapEntityToDto(taskService.getById(taskDto.getId()));
      Collection<TaskDto> tasks = new ArrayList<>();
      tasks.add(oldTask);
      sendNewOrUpdatedTasks(tasks, unitId);
      logger.info("want to update a task in a closed cycle");
    }
  }

  @MessageMapping("unit/{unitId}/tasks/delete")
  public void deleteTask(
    @DestinationVariable long unitId,
    TaskDto taskDto,
    StompHeaderAccessor stompHeaderAccessor
  ) {
    logger.info("delete Task on Websocket");

    try {
      Task taskToDelete = taskMapper.mapDtoToEntity(taskDto);
      Collection<Task> updatedTasks =
        taskService.deleteTaskById(
              taskToDelete.getId(),
              unitId,
              websocketUserService.findByAccessor(stompHeaderAccessor));

      String deletionUrl = String.format("/topic/unit/%d/tasks/deleted", unitId);
      String updateUrl = String.format("/topic/unit/%d/tasks", unitId);

      simpMessagingTemplate.convertAndSend(deletionUrl, taskDto);
      simpMessagingTemplate.convertAndSend(updateUrl, taskMapper.mapEntitiesToDtos(updatedTasks));
      logger.info("Task deleted and broadcast to: " + deletionUrl);
      logger.info("Referenced Tasks updated after Deletion and broadcast to: " + updateUrl);
    } catch (ForbiddenException ex) {
      TaskDto oldTask = taskMapper.mapEntityToDto(taskService.getById(taskDto.getId()));
      Collection<TaskDto> tasks = new ArrayList<>();
      tasks.add(oldTask);
      sendNewOrUpdatedTasks(tasks, unitId);
      logger.info("want to delete a task in a closed cycle");
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
