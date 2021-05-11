package org.burningokr.mapper.okr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.dto.okr.TaskDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaskMapper implements DataMapper<Task, TaskDto> {
  private final Logger logger = LoggerFactory.getLogger(TaskMapper.class);

  public Task mapDtoToEntity(TaskDto taskDto) {
    logger.info(
        String.format(
            "mapDtoToEntity dto: {id: %d, title: %s, description: %s, assignedUserIds: %s, assignedKeyResultId: %d, task board Id: %d, stateId: %d, previousTaskId: %d}",
            taskDto.getId(),
            taskDto.getTitle(),
            taskDto.getDescription(),
            String.valueOf(taskDto.getAssignedUserIds()),
            taskDto.getAssignedKeyResultId(),
            taskDto.getParentTaskBoardId(),
            taskDto.getTaskStateId(),
            taskDto.getPreviousTaskId()));
    Task taskEntity = new Task();

    taskEntity.setId(taskDto.getId());
    taskEntity.setTitle(taskDto.getTitle());
    taskEntity.setDescription(taskDto.getDescription());

    if (taskDto.getAssignedUserIds() == null) {
      taskEntity.setAssignedUserIds(new ArrayList<>());
    } else {
      Collection<UUID> copiedUserIds = copyUUIDList(taskDto.getAssignedUserIds());
      taskEntity.setAssignedUserIds(copiedUserIds);
    }

    TaskState taskState = new TaskState();
    taskState.setId(taskDto.getTaskStateId());
    taskEntity.setTaskState(taskState);

    TaskBoard taskboard = new TaskBoard();
    taskboard.setId(taskDto.getParentTaskBoardId());
    taskEntity.setParentTaskBoard(taskboard);

    if (taskDto.getPreviousTaskId() != null) {
      Task previousTask = new Task();
      previousTask.setId(taskDto.getPreviousTaskId());
      taskEntity.setPreviousTask(previousTask);
    }

    if (taskDto.hasAssignedKeyResult()) {
      KeyResult keyResult = new KeyResult();
      keyResult.setId(taskDto.getAssignedKeyResultId());
      taskEntity.setAssignedKeyResult(keyResult);
    }
    taskEntity.setVersion(taskDto.getVersion());
    return taskEntity;
  }

  public TaskDto mapEntityToDto(Task taskEntity) {
    TaskDto taskDto = new TaskDto();

    taskDto.setId(taskEntity.getId());
    taskDto.setTitle(taskEntity.getTitle());
    taskDto.setDescription(taskEntity.getDescription());
    taskDto.setTaskStateId(taskEntity.getTaskState().getId());
    taskDto.setVersion(taskEntity.getVersion());
    taskDto.setParentTaskBoardId(taskEntity.getParentTaskBoard().getId());

    if (taskEntity.hasPreviousTask()) {
      taskDto.setPreviousTaskId(taskEntity.getPreviousTask().getId());
    }

    if (taskEntity.getAssignedUserIds() == null) {
      taskDto.setAssignedUserIds(new ArrayList<>());
    } else {
      Collection<UUID> copiedUserIds = copyUUIDList(taskEntity.getAssignedUserIds());
      taskDto.setAssignedUserIds(copiedUserIds);
    }

    if (taskEntity.hasAssignedKeyResult()) {
      taskDto.setAssignedKeyResultId(taskEntity.getAssignedKeyResult().getId());
    }

    logger.info(
        "mapEntityToDto (id:"
            + taskDto.getId()
            + " assigned Key Result id: "
            + taskDto.getAssignedKeyResultId()
            + ") successful into TaskDto.");
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
    logDTOList(taskDtos);
    return taskDtos;
  }

  private Collection<UUID> copyUUIDList(Collection<UUID> list) {
    ArrayList copy = new ArrayList<UUID>();
    for (UUID userId : list) {
      UUID copyID = new UUID(userId.getMostSignificantBits(), userId.getLeastSignificantBits());
      copy.add(copyID);
    }
    return copy;
  }

  private void logDTOList(Collection<TaskDto> taskList) {

    StringBuilder result = new StringBuilder("Log DTO List\n");
    for (TaskDto task : taskList) {
      result.append("--------------\n");
      result.append("Id: ").append(task.getId()).append("\n");
      result.append("title: ").append(task.getTitle()).append("\n");
      result.append("description: ").append(task.getDescription()).append("\n");
      result.append("stateId: ").append(task.getTaskStateId()).append("\n");
      result.append("Assigned Key Result Id: ").append(task.getAssignedKeyResultId()).append("\n");
      result.append("previous Task Id: ").append(task.getPreviousTaskId()).append("\n");
      result.append("parent TaskBoard Id: ").append(task.getParentTaskBoardId()).append("\n");
      result
          .append("My Assigned User Ids: ")
          .append(String.valueOf(task.getAssignedUserIds()))
          .append("\n");
      result.append("Version: ").append(task.getVersion()).append("\n");
    }
    logger.info(result.toString());
  }
}
