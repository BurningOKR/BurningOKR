package org.burningokr.service.okr;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.repositories.okr.TaskRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class TaskService {

  private final TaskRepository taskRepository;

  private final OkrDepartmentRepository okrDepartmentRepository;

  private final ActivityService activityService;
  private final EntityCrawlerService entityCrawlerService;

  @Autowired
  public TaskService(
    TaskRepository taskRepository,
    OkrDepartmentRepository okrDepartmentRepository,
    ActivityService activityService,
    EntityCrawlerService entityCrawlerService
  ) {
    this.taskRepository = taskRepository;
    this.okrDepartmentRepository = okrDepartmentRepository;
    this.activityService = activityService;
    this.entityCrawlerService = entityCrawlerService;
  }

  public Task getById(Long taskId) {
    return taskRepository.findByIdOrThrow(taskId);
  }

  public Collection<Task> findTaskForOkrUnit(Long okrUnitId) {
    OkrDepartment okrUnit = this.okrDepartmentRepository.findByIdOrThrow(okrUnitId);
    TaskBoard taskboard = okrUnit.getTaskBoard();
    return taskRepository.findByTaskBoard(taskboard);
  }

  public Collection<Task> findTasksForKeyResult(KeyResult keyResult) {
    return this.taskRepository.findByKeyResult(keyResult);
  }

  public Collection<Task> createTask(Task newTask, Long okrUnitId) {
    Collection<Task> newOrUpdatedTasks = new ArrayList<>();
    OkrDepartment okrUnit = this.okrDepartmentRepository.findByIdOrThrow(okrUnitId);
    throwIfCycleOfTaskIsNotActive(okrUnit);

    Task firstTaskInList =
      this.taskRepository.findFirstTaskOfList(okrUnit.getTaskBoard(), newTask.getTaskState());
    newTask.setPreviousTask(null);
    newTask.setParentTaskBoard(okrUnit.getTaskBoard());
    newTask = taskRepository.save(newTask);

    newOrUpdatedTasks.add(newTask);
    log.debug("Created task " + newTask.getName());

    log.debug("First task in list is null: " + (firstTaskInList == null ? "true" : "false"));
    if (firstTaskInList != null) {
      firstTaskInList.setPreviousTask(newTask);
      firstTaskInList = taskRepository.save(firstTaskInList);
      newOrUpdatedTasks.add(firstTaskInList);
    }

    activityService.createActivity(newTask, Action.CREATED);

    return newOrUpdatedTasks;
  }

  @Transactional
  public Collection<Task> updateTask(Task updatedTask) throws Exception {
    log.debug("Updating task in transaction");
    Task referencedTask = taskRepository.findByIdOrThrow(updatedTask.getId());
    throwIfCycleOfTaskIsNotActive(referencedTask.getParentTaskBoard().getParentOkrDepartment());

    log.debug("Updated task: ");
    this.logTask(updatedTask);
    log.debug("Old task");
    this.logTask(referencedTask);

    Collection<Task> updatedTasks = new ArrayList<>();

    if (hasPositionChanged(updatedTask, referencedTask)) {
      log.debug("Update task -> has been moved to column: " + updatedTask.getName());
      updatedTasks.addAll(updateTaskWithPositioning(updatedTask, referencedTask));
    } else {
      log.debug("Update task -> field values changed: " + updatedTask.getName());
      updateTaskWithoutPositioning(updatedTask, referencedTask);

      updatedTasks.add(referencedTask);
    }

    log.debug("Update task -> persist changes");
    updatedTasks = (Collection<Task>) taskRepository.saveAll(updatedTasks);

    log.debug("Updated task %s (id: %d)".formatted(referencedTask.getName(), referencedTask.getId()));
    activityService.createActivity(updatedTask, Action.EDITED);

    log.debug("Finished updating");
    return updatedTasks;
  }

  private Task updateTaskWithoutPositioning(Task newVersion, Task taskToSave) {
    taskToSave.setTitle(newVersion.getTitle());
    taskToSave.setDescription(newVersion.getDescription());
    taskToSave.setVersion(newVersion.getVersion());

    if (newVersion.hasAssignedKeyResult()) {
      taskToSave.setAssignedKeyResult(newVersion.getAssignedKeyResult());
    } else {
      taskToSave.setAssignedKeyResult(null);
    }

    if (newVersion.hasAssignedUserIds()) {
      taskToSave.setAssignedUserIds(newVersion.getAssignedUserIds());
    } else {
      taskToSave.setAssignedUserIds(null);
    }

    return taskToSave;
  }

  private Collection<Task> updateTaskWithPositioning(Task newVersion, Task oldVersion)
    throws Exception {
    Collection<Task> updatedTasks = new ArrayList<>();

    Task newPreviousTask;

    log.debug("updateTaskWithPositioning - Validation");
    TaskValidator taskValidator = new TaskValidator();
    newPreviousTask = taskValidator.validateTask(newVersion, taskRepository);

    // neuer Nachfolger: referenziert auf den neuen Vorgänger des verschobenen Tasks
    // gegeben sind nun der neue Vorgänger und der neue Nachfolger
    Task newNextTask;
    if (newPreviousTask != null) {
      log.debug("New predecessor exists");
      logTask(newPreviousTask);
      newNextTask =
        taskRepository.findByPreviousTask(
          newPreviousTask,
          newPreviousTask.getTaskState(),
          newPreviousTask.getParentTaskBoard()
        );
    } else {
      log.debug("No new predecessor");
      newNextTask =
          taskRepository.findFirstTaskOfList(
              newVersion.getParentTaskBoard(), newVersion.getTaskState());
    }

    Task nextTaskOfOldVersion = null;
    // alter nachfolger
    if (oldVersion != null) {
      log.debug("Update old successor - old version: ");
      logTask(oldVersion);

      nextTaskOfOldVersion =
        taskRepository.findByPreviousTask(
          oldVersion, oldVersion.getTaskState(), oldVersion.getParentTaskBoard());
    }

    if (newNextTask != null) {
      log.debug("Update new successor");
      newNextTask.setPreviousTask(oldVersion);
      logTask(newNextTask);
      updatedTasks.add(newNextTask);
    } else {
      log.debug("No new successor");
    }

    if (nextTaskOfOldVersion != null) {
      log.debug("Update old successor");
      nextTaskOfOldVersion.setPreviousTask(oldVersion.getPreviousTask());
      logTask(nextTaskOfOldVersion);
      updatedTasks.add(nextTaskOfOldVersion);
    }

    oldVersion.setPreviousTask(newPreviousTask);

    updateTaskWithoutPositioning(newVersion, oldVersion);
    oldVersion.setTaskState(newVersion.getTaskState());
    oldVersion.setVersion(newVersion.getVersion());
    updatedTasks.add(oldVersion);

    return updatedTasks;
  }

  public Collection<Task> deleteTaskById(long taskId, long unitId) {
    OkrUnit unit = okrDepartmentRepository.findByIdOrThrow(unitId);
    this.throwIfCycleOfTaskIsNotActive(unit);
    Collection<Task> updatedTasks = new ArrayList<>();

    Task taskToDelete = this.taskRepository.findByIdOrThrow(taskId);
    Task previousTask = null;
    if (taskToDelete.hasPreviousTask()) {
      previousTask = taskToDelete.getPreviousTask();
      taskToDelete.setPreviousTask(null);
      taskToDelete = taskRepository.save(taskToDelete);
      updatedTasks.add(previousTask);
    }

    Task nextTask =
      this.taskRepository.findByPreviousTask(
        taskToDelete, taskToDelete.getTaskState(), taskToDelete.getParentTaskBoard());
    if (nextTask != null) {
      nextTask.setPreviousTask(previousTask);
      nextTask = taskRepository.save(nextTask);
      updatedTasks.add(nextTask);
    }

    this.taskRepository.deleteById(taskToDelete.getId());
    this.activityService.createActivity(taskToDelete, Action.DELETED);

    return updatedTasks;
  }

  public void throwIfCycleOfTaskIsNotActive(OkrUnit unit) {
    if (entityCrawlerService.getCycleOfUnit(unit).getCycleState() == CycleState.CLOSED) {
      throw new ForbiddenException("Cannot modify task because it belongs to a closed cycle.");
    }
  }

  private boolean hasPositionChanged(Task updatedTask, Task oldTask) {
    boolean result;

    if (!(updatedTask.getTaskState().getId().equals(oldTask.getTaskState().getId()))) {
      result = true;
    } else if (updatedTask.hasPreviousTask() != oldTask.hasPreviousTask()) {
      result = true;
    } else {
      long updatedPreviousTaskId = -1;
      long oldPreviousTaskId = -1;
      if (updatedTask.hasPreviousTask()) {
        updatedPreviousTaskId = updatedTask.getPreviousTask().getId();
      }
      if (oldTask.hasPreviousTask()) {
        oldPreviousTaskId = oldTask.getPreviousTask().getId();
      }
      result = updatedPreviousTaskId != oldPreviousTaskId;
    }
    return result;
  }

  public Collection<Task> copyTasksAndSetNewStates(
    Collection<Task> notFinishedTasks,
    Collection<TaskState> copiedStates,
    TaskBoard copiedTaskBoard
  ) {
    Collection<Task> copiedTasks = new ArrayList<>();
    for (Task oldTask : notFinishedTasks) {
      copiedTasks.add(this.copyTask(oldTask, copiedStates, copiedTaskBoard));
    }
    return copiedTasks;
  }

  public void setStateFromStatesCollection(@NotNull Task task, Collection<TaskState> states) {
    Optional<TaskState> result = states.stream()
      .filter(state -> task.getTaskState().getTitle().equals(state.getTitle()))
      .findFirst();

    result.ifPresent(task::setTaskState);
  }

  public Task copyTask(
    Task oldTask,
    Collection<TaskState> copiedStates,
    TaskBoard copiedTaskBoard
  ) {
    Task copiedTask = oldTask.copyWithNoRelations();
    copiedTask.setParentTaskBoard(copiedTaskBoard);
    this.setStateFromStatesCollection(copiedTask, copiedStates);
    return copiedTask;
  }


  private void logTask(Task task) {
    String keyresultId =
      task.hasAssignedKeyResult() ? task.getAssignedKeyResult().getId().toString() : "null";
    String previousTaskId =
      task.hasPreviousTask() ? task.getPreviousTask().getId().toString() : "null";
    log.debug(
      String.format(
        "-----\nid: %s, title: %s, taskState: %s, keyresult: %s, previousTask: %s, parentTaskboard: %s, version: %s\n---------",
        task.getId(),
        task.getTitle(),
        task.getTaskState().getId(),
        keyresultId,
        previousTaskId,
        task.getParentTaskBoard().getId(),
        task.getVersion()
      ));
  }
}
