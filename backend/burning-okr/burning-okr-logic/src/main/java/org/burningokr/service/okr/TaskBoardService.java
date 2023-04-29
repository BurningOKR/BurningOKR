package org.burningokr.service.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.repositories.okr.TaskBoardRepository;
import org.burningokr.repositories.okr.TaskRepository;
import org.burningokr.repositories.okr.TaskStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class TaskBoardService {
  private final Logger logger = LoggerFactory.getLogger(TaskBoardService.class);
  private final TaskBoardRepository taskBoardRepository;
  private final DefaultTaskStateService defaultTaskStateService;
  private final TaskStateRepository taskStateRepository;
  private final TaskService taskService;
  private final TaskRepository taskRepository;

  public TaskBoard createNewTaskBoardWithDefaultStates(OkrDepartment parentOkrDepartment) {
    TaskBoard taskBoard = new TaskBoard();
    Collection<TaskState> states = defaultTaskStateService.getDefaultTaskStatesForNewTaskBoard(taskBoard);
    taskBoard.setAvailableStates(states);
    taskBoard.setParentOkrDepartment(parentOkrDepartment);

    return taskBoard;
  }

  public TaskBoard copyTaskBoardWithParentOkrUnitOnly(
    TaskBoard taskBoardToCopy, OkrDepartment okrDepartment
  ) {
    TaskBoard copiedTaskboard = new TaskBoard();
    copiedTaskboard.setParentOkrDepartment(okrDepartment);
    return copiedTaskboard;
  }

  @Transactional
  public TaskBoard saveTaskBoard(TaskBoard taskBoard) {
    Collection<TaskState> states = new ArrayList<>();

    for (TaskState state : taskBoard.getAvailableStates()) {
      state.setParentTaskBoard(taskBoard);
      states.add(state);
    }

    taskBoard = taskBoardRepository.save(taskBoard);

    for (TaskState state : taskStateRepository.saveAll(states)) {
      states.add(state);
    }

    taskBoard.setAvailableStates(states);
    return taskBoardRepository.save(taskBoard);
  }

  @Transactional
  public TaskBoard cloneTaskBoard(OkrDepartment copy, TaskBoard taskBoardToCopy) {

    TaskBoard copiedTaskBoard = this.copyTaskBoardWithParentOkrUnitOnly(taskBoardToCopy, copy);
    this.taskBoardRepository.save(copiedTaskBoard);

    Collection<TaskState> copiedStates =
      this.copyTaskStateListAndSetTaskBoard(
        taskBoardToCopy.getAvailableStates(), copiedTaskBoard);
    taskStateRepository.saveAll(copiedStates);
    copiedTaskBoard.setAvailableStates(copiedStates);
    this.taskBoardRepository.save(copiedTaskBoard);

    Collection<Task> notFinishedTasks = this.findUnfinishedTasks(taskBoardToCopy);
    copiedTaskBoard.setTasks(
      taskService.copyTasksAndBindToNewCopyOfTaskStatesListAndTaskBoard(
        notFinishedTasks, copiedStates, copiedTaskBoard));
    taskRepository.saveAll(copiedTaskBoard.getTasks());
    updatePreviousTaskOfSavedCopiedTasks(copiedTaskBoard.getTasks());
    taskRepository.saveAll(copiedTaskBoard.getTasks());

    this.taskBoardRepository.save(copiedTaskBoard);

    return copiedTaskBoard;
  }

  private Collection<Task> updatePreviousTaskOfSavedCopiedTasks(Collection<Task> copiedTasks) {
    for (Task copiedTask : copiedTasks) {
      for (Task forPreviousTask : copiedTasks) {
        if (copiedTask.hasPreviousTask()
          && copiedTask.getPreviousTask().getTitle().equals(forPreviousTask.getTitle())) {
          copiedTask.setPreviousTask(forPreviousTask);
        }
      }
    }

    return copiedTasks;
  }

  public Collection<TaskState> copyTaskStateListAndSetTaskBoard(
    Collection<TaskState> statesToCopy, TaskBoard parentTaskBoard
  ) {
    Collection<TaskState> copiedAvailableStates = new ArrayList<>();
    for (TaskState state : statesToCopy) {
      TaskState copiedState = state.copy();
      copiedState.setParentTaskBoard(parentTaskBoard);
      copiedAvailableStates.add(copiedState);
    }

    return copiedAvailableStates;
  }

  public TaskState findFinishedState(Collection<TaskState> statesToCopy) {
    TaskState result = null;

    for (TaskState state : statesToCopy) {
      if (state.getTitle().equals("Finished")) {
        result = state;
        break;
      }
    }
    return result;
  }

  public Collection<Task> findUnfinishedTasks(TaskBoard taskBoard) {
    TaskState finishedState = findFinishedState(taskBoard.getAvailableStates());
    Collection<Task> notFinishedTasks =
      taskRepository.findNotFinishedTasksByTaskBoard(taskBoard, finishedState);
    return notFinishedTasks;
  }

  public void logTaskBoard(TaskBoard taskBoard) {
    this.logger.info(
      "id: "
        + taskBoard.getId()
        + "parentDepartment: "
        + taskBoard.getParentOkrDepartment().getId());
  }
}
