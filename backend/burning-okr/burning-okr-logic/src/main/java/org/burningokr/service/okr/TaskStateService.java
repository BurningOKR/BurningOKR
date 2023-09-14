package org.burningokr.service.okr;

import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.repositories.okr.TaskStateRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class TaskStateService {
  private final TaskStateRepository taskStateRepository;
  private final OkrDepartmentRepository okrDepartmentRepository;

  @Autowired
  public TaskStateService(
    TaskStateRepository taskStateRepository, OkrDepartmentRepository okrDepartmentRepository
  ) {
    this.taskStateRepository = taskStateRepository;
    this.okrDepartmentRepository = okrDepartmentRepository;
  }

  public Collection<TaskState> findTaskStatesForTaskBoard(TaskBoard taskBoard) {
    return this.taskStateRepository.findByTaskBoard(taskBoard);
  }

  public Collection<TaskState> findTaskStatesForUnitId(long unitId) {
    TaskBoard taskboard = okrDepartmentRepository.findById(unitId).get().getTaskBoard();
    return this.taskStateRepository.findByTaskBoard(taskboard);
  }

  public Collection<TaskState> copyTaskStates(TaskBoard taskBoardToCopy) {
    return taskBoardToCopy.getAvailableStates().stream().map(TaskState::copy).collect(Collectors.toList());
  }
}
