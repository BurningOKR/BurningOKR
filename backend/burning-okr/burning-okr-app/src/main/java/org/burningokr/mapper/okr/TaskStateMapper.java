package org.burningokr.mapper.okr;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.okr.TaskStateDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaskStateMapper implements DataMapper<TaskState, TaskStateDto> {
  private final Logger logger = LoggerFactory.getLogger(ObjectiveMapper.class);

  @Override
  public TaskState mapDtoToEntity(TaskStateDto stateDto) {
    TaskState stateEntity = new TaskState();
    stateEntity.setId(stateDto.getId());
    stateEntity.setTitle(stateDto.getTitle());

    TaskBoard taskboard = new TaskBoard();
    taskboard.setId(stateDto.getId());
    stateEntity.setParentTaskBoard(taskboard);
    return stateEntity;
  }

  @Override
  public TaskStateDto mapEntityToDto(TaskState input) {
    TaskStateDto stateDto = new TaskStateDto();
    stateDto.setId(input.getId());
    stateDto.setTitle(input.getTitle());

    return stateDto;
  }

  @Override
  public Collection<TaskState> mapDtosToEntities(Collection<TaskStateDto> input) {
    Collection<TaskState> stateList = new ArrayList<>();
    for (TaskStateDto dto : input) {
      stateList.add(mapDtoToEntity(dto));
    }
    return stateList;
  }

  @Override
  public Collection<TaskStateDto> mapEntitiesToDtos(Collection<TaskState> input) {
    Collection<TaskStateDto> dtoList = new ArrayList<>();
    for (TaskState stateEntity : input) {
      dtoList.add(mapEntityToDto(stateEntity));
    }
    return dtoList;
  }
}
