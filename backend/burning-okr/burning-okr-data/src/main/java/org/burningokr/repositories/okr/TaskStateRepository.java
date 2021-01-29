package org.burningokr.repositories.okr;

import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskStateRepository extends ExtendedRepository<TaskState, Long> {
    @Query("SELECT ts FROM TaskState ts WHERE ts.parentTaskBoard = ?1")
    List<TaskState> findByTaskBoard(TaskBoard taskboard);
}
