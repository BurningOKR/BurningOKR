package org.burningokr.service.okr;

import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.repositories.okr.TaskStateRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TaskStateService {
    private  TaskStateRepository taskStateRepository;
    private  UnitRepository<OkrUnit> okrUnitRepository;

    @Autowired
    public TaskStateService(
            TaskStateRepository taskStateRepository,
            UnitRepository<OkrUnit> okrUnitRepository
    ) {
        this.taskStateRepository = taskStateRepository;
        this.okrUnitRepository = okrUnitRepository;
    }

    public Collection<TaskState> findTaskStatesForTaskBoard(TaskBoard taskBoard) {
        return this.taskStateRepository.findByTaskBoard(taskBoard);
    }

    public Collection<TaskState> findTaskStatesForUnitId(long unitId) {
        TaskBoard taskboard = okrUnitRepository.findById(unitId).get().getTaskBoard();
        return this.taskStateRepository.findByTaskBoard(taskboard);
    }
}
