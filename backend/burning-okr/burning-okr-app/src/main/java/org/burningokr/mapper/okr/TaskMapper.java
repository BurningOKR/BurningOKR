package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.TaskDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class TaskMapper implements DataMapper<Task, TaskDto> {
    private final Logger logger = LoggerFactory.getLogger(ObjectiveMapper.class);

    public Task mapDtoToEntity(TaskDto taskDto) {
        logger.info("mapDtoToEntity dto: {id: %s, title: %s, description: %s, assignedUserIds: %s, assignedKeyResultId: %s, parentOkrUnitId: %s, stateId: %s}",
                taskDto.getId(), taskDto.getDescription(), taskDto.getAssignedUserIds(), taskDto.getAssignedKeyResultId(), taskDto.getParentOkrUnitId(), taskDto.getStateId());
        Task taskEntity = new Task();

        taskEntity.setId(taskDto.getId());
        taskEntity.setTitle(taskDto.getTitle());
        taskEntity.setDescription(taskDto.getDescription());
        taskEntity.setOkrStateId(taskDto.getStateId());
        taskEntity.setAssignedUserIds(taskDto.getAssignedUserIds());

        TaskBoard parentTaskBoard =new TaskBoard();
        taskEntity.setParentTaskBoard(parentTaskBoard);

        if(taskDto.hasAssignedKeyResult()) {
            KeyResult keyResult = new KeyResult();
            keyResult.setId(taskDto.getAssignedKeyResultId());
            taskEntity.setAssignedKeyResult(keyResult);
        }

        logger.info("Mapped TaskDto (id:" + taskDto.getId() + ", "+ taskDto.getTitle()+") successful into Task.");
        return taskEntity;
    }

    public TaskDto mapEntityToDto(Task taskEntity) {
        TaskDto taskDto = new TaskDto();

        taskDto.setId(taskEntity.getId());
        taskDto.setTitle(taskEntity.getTitle());
        taskDto.setDescription(taskEntity.getDescription());
        taskDto.setStateId(taskEntity.getOkrStateId());
        taskDto.setAssignedUserIds(taskEntity.getAssignedUserIds());

        taskDto.setParentOkrUnitId(taskEntity.getParentTaskBoard().getParentOkrUnit().getId());

        if(taskDto.hasAssignedKeyResult()) {
            taskDto.setAssignedKeyResultId(taskEntity.getAssignedKeyResult().getId());
        }

        logger.info("Mapped Task (id:" + taskEntity.getId() + ") successful into TaskDto.");
        return taskDto;
    }

    @Override
    public Collection<Task> mapDtosToEntities(Collection<TaskDto> inputTasksDtos) {
        Collection<Task> tasks = new ArrayList<>();
        inputTasksDtos.forEach(taskDto -> tasks.add(mapDtoToEntity(taskDto)));
        return tasks;
    }

    @Override
    public Collection<TaskDto> mapEntitiesToDtos(Collection<Task> inputTasks) {
        Collection<TaskDto> taskDtos = new ArrayList<>();
        inputTasks.forEach(task -> taskDtos.add(mapEntityToDto(task)));
        return taskDtos;
    }
}
