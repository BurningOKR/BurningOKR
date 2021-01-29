package org.burningokr.controller.okr;

import org.burningokr.dto.okr.TaskDto;
import org.burningokr.mapper.okr.TaskMapper;
import org.burningokr.model.okr.Task;
import org.burningokr.service.okr.TaskService;
import org.burningokr.service.security.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import springfox.documentation.spring.web.json.Json;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public void addTask(@DestinationVariable long unitId, TaskDto taskDto, Principal user) {
        logger.info(String.format("Websocket add Task dto: {id: %d, title: %s, description: %s, assignedUserIds: %s, assignedKeyResultId: %d, task board Id: %d, stateId: %d}",
                taskDto.getId(), taskDto.getTitle(), taskDto.getDescription(), String.valueOf(taskDto.getAssignedUserIds()), taskDto.getAssignedKeyResultId(), taskDto.getParentTaskBoardId(), taskDto.getTaskStateId()));

        Task newTask = taskMapper.mapDtoToEntity(taskDto);
        Collection<TaskDto> createdAndUpdatedTasks = this.taskMapper.mapEntitiesToDtos(taskService.createTask(newTask, unitId, null));

        String url = String.format("/topic/unit/%d/tasks", unitId);

        simpMessagingTemplate.convertAndSend(url, createdAndUpdatedTasks);
        logger.info("Broadcast on Topic: " + url);
    }

    @MessageMapping("unit/{unitId}/tasks/update")
    public void updateTask(@DestinationVariable long unitId, TaskDto taskDto, Principal user) throws Exception {
        logger.info("update Task on Websocket");

        Task updatedTask = taskMapper.mapDtoToEntity(taskDto);
        Collection<Task> updatedTasks = taskService.updateTask(updatedTask, null);
        Collection<TaskDto> taskDtoList = taskMapper.mapEntitiesToDtos(updatedTasks);


        String url = String.format("/topic/unit/%d/tasks", unitId);
        simpMessagingTemplate.convertAndSend(url, taskDtoList);
        logger.info("Task updated and broadcasted to: " + url);
    }

    @MessageMapping("unit/{unitId}/tasks/delete")
    public void deleteTask(@DestinationVariable long unitId, TaskDto taskDto, Principal user) {
        logger.info("delete Task on Websocket");

        Task taskToDelete = taskMapper.mapDtoToEntity(taskDto);
        Collection<Task> updatedTasks = taskService.deleteTaskById(taskToDelete.getId(), unitId, null);

        String deletionUrl = String.format("/topic/unit/%d/tasks/deleted", unitId);
        String updateUrl = String.format("/topic/unit/%d/tasks", unitId);

        simpMessagingTemplate.convertAndSend(deletionUrl, taskDto);
        simpMessagingTemplate.convertAndSend(updateUrl, taskMapper.mapEntitiesToDtos(updatedTasks));
        logger.info("Task deleted and broadcast to: " + deletionUrl);
        logger.info("Referenced Tasks updated after Deletion and broadcast to: " + updateUrl);
    }
}
