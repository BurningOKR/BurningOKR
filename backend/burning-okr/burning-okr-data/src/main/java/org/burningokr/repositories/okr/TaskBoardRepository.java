package org.burningokr.repositories.okr;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskBoardRepository extends ExtendedRepository<TaskBoard, Long> {
    @Query("SELECT tb FROM TaskBoard tb WHERE tb.parentOkrUnit = ?1")
    List<Task> findByOkrUnit(OkrUnit unit);
}