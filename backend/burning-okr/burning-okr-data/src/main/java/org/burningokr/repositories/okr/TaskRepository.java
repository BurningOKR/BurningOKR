package org.burningokr.repositories.okr;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
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

  @Query(
      "Select t from Task t where t.parentTaskBoard=?1 and t.taskState=?2 and t.previousTask is null")
  Task findFirstTaskOfList(TaskBoard taskboard, TaskState state);

  @Query("Select t from Task t where t.previousTask=?1 and t.taskState=?2 and t.parentTaskBoard=?3")
  Task findByPreviousTask(Task task, TaskState state, TaskBoard taskBoard);

  @Query("SELECT t FROM Task t WHERE t.parentTaskBoard = ?1 and not t.taskState=?2")
  List<Task> findNotFinishedTasksByTaskBoard(TaskBoard taskBoard, TaskState state);
}
