package org.burningokr.repositories.okr;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends ExtendedRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.parentTaskBoard = ?1")
    List<Task> findByTaskBoard(TaskBoard taskBoard);

    @Query("SELECT t FROM Task t WHERE t.assignedKeyResult = ?1")
    List<Task> findByKeyResult(KeyResult keyResult);
}
