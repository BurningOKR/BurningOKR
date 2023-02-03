package org.burningokr.service.okr;

import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.repositories.okr.TaskStateRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TaskStateService {
  private TaskStateRepository taskStateRepository;
  private OkrDepartmentRepository okrDepartmentRepository;

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
}
