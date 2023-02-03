package org.burningokr.repositories.okr;

import org.burningokr.model.okr.TaskBoard;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardRepository extends ExtendedRepository<TaskBoard, Long> {
}
