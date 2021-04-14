package org.burningokr.controller.okr;

import org.burningokr.dto.okr.TaskDto;
import org.burningokr.mapper.okr.TaskMapper;
import org.burningokr.model.okr.Task;
import org.burningokr.model.users.User;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okr.TaskService;
import org.burningokr.service.security.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

@Controller
public class WebsocketTaskController {
    private final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private AuthorizationService authorizationService;
    private TaskService taskService;
    private TaskMapper taskMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebsocketTaskController(
            AuthorizationService authorizationService,
            TaskService taskService,
            TaskMapper taskMapper,
            SimpMessagingTemplate simpMessagingTemplate
    ) {
        this.authorizationService = authorizationService;
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("unit/{unitId}/tasks/add")
    public void addTask(@DestinationVariable long unitId, TaskDto taskDto, @AuthenticationPrincipal Authentication currentUser) {
        logger.info(String.format("Websocket add Task dto: {id: %d, title: %s, description: %s, assignedUserIds: %s, assignedKeyResultId: %d, task board Id: %d, stateId: %d}",
                taskDto.getId(), taskDto.getTitle(), taskDto.getDescription(), String.valueOf(taskDto.getAssignedUserIds()), taskDto.getAssignedKeyResultId(), taskDto.getParentTaskBoardId(), taskDto.getTaskStateId()));

        Task newTask = taskMapper.mapDtoToEntity(taskDto);
        try {
            Collection<TaskDto> createdAndUpdatedTasks = this.taskMapper.mapEntitiesToDtos(taskService.createTask(newTask, unitId, null));

            sendNewOrUpdatedTasks(createdAndUpdatedTasks, unitId);
            logger.info("Broadcast for added task");
        } catch (ForbiddenException ex) {
            logger.error(ex.getMessage());
            logger.info("want to add a task in a not active cycle");
            sendDeletedTasks(taskDto, unitId);
        }
    }

    @MessageMapping("unit/{unitId}/tasks/update")
    public void updateTask(@DestinationVariable long unitId, TaskDto taskDto, @AuthenticationPrincipal Authentication currentUser) throws Exception {
        logger.info("update Task on Websocket");
        try {
            Task updatedTask = taskMapper.mapDtoToEntity(taskDto);
            Collection<Task> updatedTasks = taskService.updateTask(updatedTask, null);
            Collection<TaskDto> taskDtoList = taskMapper.mapEntitiesToDtos(updatedTasks);

            sendNewOrUpdatedTasks(taskDtoList, unitId);
            logger.info("Broadcast for updated task");
        } catch (ForbiddenException ex) {
            TaskDto oldTask = taskMapper.mapEntityToDto(taskService.getById(taskDto.getId()));
            Collection tasks = new ArrayList<TaskDto>();
            tasks.add(oldTask);
            sendNewOrUpdatedTasks(tasks,unitId);
            logger.info("want to update a task in a not active cycle");
        }
    }

    @MessageMapping("unit/{unitId}/tasks/delete")
    public void deleteTask(@DestinationVariable long unitId, TaskDto taskDto, @AuthenticationPrincipal Authentication currentUser) {
        logger.info("delete Task on Websocket");

        try {
            Task taskToDelete = taskMapper.mapDtoToEntity(taskDto);
            Collection<Task> updatedTasks = taskService.deleteTaskById(taskToDelete.getId(), unitId, null);

            String deletionUrl = String.format("/topic/unit/%d/tasks/deleted", unitId);
            String updateUrl = String.format("/topic/unit/%d/tasks", unitId);

            simpMessagingTemplate.convertAndSend(deletionUrl, taskDto);
            simpMessagingTemplate.convertAndSend(updateUrl, taskMapper.mapEntitiesToDtos(updatedTasks));
            logger.info("Task deleted and broadcast to: " + deletionUrl);
            logger.info("Referenced Tasks updated after Deletion and broadcast to: " + updateUrl);
        } catch(ForbiddenException ex) {
            TaskDto oldTask = taskMapper.mapEntityToDto(taskService.getById(taskDto.getId()));
            Collection tasks = new ArrayList<TaskDto>();
            tasks.add(oldTask);
            sendNewOrUpdatedTasks(tasks,unitId);
            logger.info("want to delete a task in a not active cycle");
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
