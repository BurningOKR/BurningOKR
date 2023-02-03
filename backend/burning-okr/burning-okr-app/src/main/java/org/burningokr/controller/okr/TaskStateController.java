package org.burningokr.controller.okr;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.TaskStateDto;
import org.burningokr.mapper.okr.TaskStateMapper;
import org.burningokr.model.okr.TaskState;
import org.burningokr.service.okr.TaskStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

@RestApiController
public class TaskStateController {
  private final Logger logger = LoggerFactory.getLogger(TaskStateController.class);
  private final TaskStateService taskStateService;
  private final TaskStateMapper taskStateMapper;

  @Autowired
  public TaskStateController(TaskStateService taskStateService, TaskStateMapper taskStateMapper) {
    this.taskStateService = taskStateService;
    this.taskStateMapper = taskStateMapper;
  }

  @GetMapping("/unit/{unitId}/states")
  public Collection<TaskStateDto> getStatesForTaskBoard(@PathVariable long unitId) {
    Collection<TaskState> stateList = taskStateService.findTaskStatesForUnitId(unitId);
    return taskStateMapper.mapEntitiesToDtos(stateList);
  }
}
