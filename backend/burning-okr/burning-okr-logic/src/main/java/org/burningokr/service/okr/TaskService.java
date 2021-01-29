package org.burningokr.service.okr;

import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.TaskBoardRepository;
import org.burningokr.repositories.okr.TaskRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class TaskService {
    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private TaskRepository taskRepository;
    private TaskBoardRepository taskBoardRepository;
    private UnitRepository<OkrUnit> okrUnitRepository;
    private KeyResultRepository keyResultRepository;
    private ActivityService activityService;
    private EntityCrawlerService entityCrawlerService;


    @Autowired
    public TaskService(
            TaskRepository taskRepository,
            UnitRepository okrUnitRepository,
            ActivityService activityService,
            EntityCrawlerService entityCrawlerService,
            KeyResultRepository keyResultRepository,
            TaskBoardRepository taskBoardRepository
    ) {
        this.taskRepository = taskRepository;
        this.okrUnitRepository = okrUnitRepository;
        this.activityService = activityService;
        this.entityCrawlerService = entityCrawlerService;
        this.keyResultRepository = keyResultRepository;
        this.taskBoardRepository = taskBoardRepository;
    }

    public Task getById(Long taskId) {
        return taskRepository.findByIdOrThrow(taskId);
    }

    public Collection<Task> findTaskForOkrUnit(Long okrUnitId) {
        OkrUnit okrUnit = this.okrUnitRepository.findByIdOrThrow(okrUnitId);
        TaskBoard taskboard = okrUnit.getTaskBoard();
        Collection<Task> tasks = taskRepository.findByTaskBoard(taskboard);
        return tasks;
    }

    public Collection<Task> findTasksForKeyResult(KeyResult keyResult) {
        return this.taskRepository.findByKeyResult(keyResult);
    }

    public Collection<Task> createTask(Task newTask, Long okrUnitId, User user) {
        Collection<Task> newOrUpdatedTasks = new ArrayList<>();
        OkrUnit okrUnit = this.okrUnitRepository.findByIdOrThrow(okrUnitId);
        throwIfCycleOfTaskIsClosed(okrUnit);

        Task firstTaskInList = this.taskRepository.findFirstTaskOfList(okrUnit.getTaskBoard(), newTask.getTaskState());
        newTask.setPreviousTask(null);
        newTask.setParentTaskBoard(okrUnit.getTaskBoard());
        newTask = taskRepository.save(newTask);

        newOrUpdatedTasks.add(newTask);
        logger.info("Created Task " + newTask.getName());

        logger.info("First task in list is null: " + (firstTaskInList == null ? "true" : "false"));
        if (firstTaskInList != null) {
            firstTaskInList.setPreviousTask(newTask);
            firstTaskInList = taskRepository.save(firstTaskInList);
            newOrUpdatedTasks.add(firstTaskInList);
        }

        if (user != null) {
            activityService.createActivity(user, newTask, Action.CREATED);
        }
        return newOrUpdatedTasks;
    }

    @Transactional
    public Collection<Task> updateTask(Task updatedTask, User user) throws Exception {
        logger.info("---------update Task--------------");

        Task referencedTask = taskRepository.findByIdOrThrow(updatedTask.getId());
        throwIfCycleOfTaskIsClosed(referencedTask.getParentTaskBoard().getParentOkrUnit());
        logger.info("updated Task: ");
        this.logTask(updatedTask);
        logger.info("old Task");
        this.logTask(referencedTask);

        Collection<Task> updatedTasks = new ArrayList<>();

        if (hasPositionChanged(updatedTask,referencedTask)) {
            logger.info("update Task -> wurde in eine andere Spalte verschoben: " + updatedTask.getName());
            updatedTasks.addAll(
                    updateTaskWithPositioning(updatedTask, referencedTask)
            );
        }  else {
            // es wurden nur Attributwerte geändert
            logger.info("update Task -> nur Attributwerte geändert: " + updatedTask.getName());
            updateTaskWithoutPositioning(updatedTask, referencedTask);

            updatedTasks.add(referencedTask);
        }

        logger.info("update Task -> Speichern ");
        updatedTasks = (Collection<Task>) taskRepository.saveAll(updatedTasks);


        logger.info(
                "Updated Task "
                        + referencedTask.getName()
                        + "(id:"
                        + referencedTask.getId()
                        + ")");
        if (user != null) {
            activityService.createActivity(user, updatedTask, Action.EDITED);
        }
        logger.info("End of Update Task");
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

        if (newVersion.hasAssignedKeyResult()) {
            taskToSave.setAssignedUserIds(newVersion.getAssignedUserIds());
        } else {
            taskToSave.setAssignedUserIds(null);
        }

        return taskToSave;
    }

    private Collection<Task> updateTaskWithPositioning(Task newVersion, Task oldVersion ) throws Exception {
        Collection<Task> updatedTasks = new ArrayList<>();

        Task newPreviousTask = null;
        // TODO In einen Validator Klasse auslagern
        /*
            überprüft, ob eine Aufgabe auf sich selbst referenziert oder sich der neue Vorgänger in einer anderen Spalte/Zustand befindet
         */
        logger.info("updateTaskWithPositioning - Validation");
        if (newVersion.hasPreviousTask()) {
            if (!(newVersion.getPreviousTask().getId().equals(newVersion.getId()))) {
                newPreviousTask = taskRepository.findByIdOrThrow(newVersion.getPreviousTask().getId());
                logger.info("updateTaskWithPositioning - Validation - previous and current task have different ids");
                logTask(newPreviousTask);
                if (!(newVersion.getTaskState().getId().equals(newPreviousTask.getTaskState().getId()))) {
                    throw new Exception("Aufgaben sind in unterschiedlichen Spalten/Zuständen");
                }
            }else {
                throw new Exception("Aufgabe ist sein eigener Vorgänger");
            }
        }

        //neuer Nachfolger: referenziert auf den neuen Vorgänger des verschobenen Tasks
        // gegeben sind nun der neue Vorgänger und der neue Nachfolger
        Task newNextTask;
        if(newPreviousTask != null){
            logger.info("new precessor exists");
            logTask(newPreviousTask);
            newNextTask = taskRepository.findByPreviousTask(newPreviousTask, newPreviousTask.getTaskState(), newPreviousTask.getParentTaskBoard());
        } else {
            logger.info("no new precessor");
            newNextTask= taskRepository.findFirstTaskOfList(newVersion.getParentTaskBoard(), newVersion.getTaskState());
        }

        Task nextTaskOfOldVersion = null;
        //alter nachfolger
        if(oldVersion!=null) {
            logger.info("update old successor - old version: ");
            logTask(oldVersion);

            nextTaskOfOldVersion = taskRepository.findByPreviousTask(oldVersion, oldVersion.getTaskState(), oldVersion.getParentTaskBoard());
        }


        if (newNextTask != null) {
            logger.info("update new successor");
            newNextTask.setPreviousTask(oldVersion);
            logTask(newNextTask);
            updatedTasks.add(newNextTask);
        } else {
            logger.info("no new successor");
        }

        if (nextTaskOfOldVersion != null) {
            logger.info("update old successor");
            nextTaskOfOldVersion.setPreviousTask(oldVersion.getPreviousTask());
            logTask(nextTaskOfOldVersion);
            updatedTasks.add(nextTaskOfOldVersion);
        }

        oldVersion.setPreviousTask(newPreviousTask);

        updateTaskWithoutPositioning(newVersion, oldVersion);
        oldVersion.setTaskState(newVersion.getTaskState());
        updatedTasks.add(oldVersion);

        return updatedTasks;
    }

    public Collection<Task> deleteTaskById(long taskId, long unitId, User user) {
        OkrUnit unit = okrUnitRepository.findByIdOrThrow(unitId);
        throwIfCycleOfTaskIsClosed(unit);
        Collection<Task> updatedTasks = new ArrayList<>();

        Task taskToDelete = this.taskRepository.findByIdOrThrow(taskId);
        Task previousTask = null;
        if (taskToDelete.hasPreviousTask()) {
            previousTask = taskToDelete.getPreviousTask();
            taskToDelete.setPreviousTask(null);
            taskToDelete = taskRepository.save(taskToDelete);
            updatedTasks.add(previousTask);
        }

        Task nextTask = this.taskRepository.findByPreviousTask(taskToDelete,taskToDelete.getTaskState(), taskToDelete.getParentTaskBoard());
        if (nextTask != null) {
            nextTask.setPreviousTask(previousTask);
            nextTask = taskRepository.save(nextTask);
            updatedTasks.add(nextTask);
        }

        this.taskRepository.deleteById(taskToDelete.getId());
        if (user != null) {
            activityService.createActivity(user, taskToDelete, Action.DELETED);
        }
        return updatedTasks;
    }

    private boolean hasPositionChanged(Task updatedTask, Task oldTask) {
        boolean result;

        if(!(updatedTask.getTaskState().getId().equals(oldTask.getTaskState().getId()))) {
            result=true;
        } else if(updatedTask.hasPreviousTask() != oldTask.hasPreviousTask()) {
            result=true;
        } else {
            long updatedPreviousTaskId=-1;
            long oldPreviousTaskId=-1;
            if(updatedTask.hasPreviousTask()) {
                updatedPreviousTaskId = updatedTask.getPreviousTask().getId();
            }
            if(oldTask.hasPreviousTask()) {
                oldPreviousTaskId = oldTask.getPreviousTask().getId();
            }
            result= updatedPreviousTaskId != oldPreviousTaskId;
        }
        return result;
    }

    private void throwIfCycleOfTaskIsClosed(OkrUnit unit) {
        if (entityCrawlerService.getCycleOfUnit(unit).getCycleState()
                == CycleState.CLOSED) {
            throw new ForbiddenException("Cannot modify this resource on a Task in a closed cycle.");
        }
    }

    private void logTask(Task task) {
        String keyresultId = task.hasAssignedKeyResult() ? task.getAssignedKeyResult().getId().toString(): "null";
        String previousTaskId = task.hasPreviousTask() ? task.getPreviousTask().getId().toString(): "null";
        String text = String.format("-----\nid: %s, title: %s, taskState: %s, keyresult: %s, previousTask: %s, parentTaskboard: %s, version: %s\n---------",
                task.getId(),task.getTitle(),task.getTaskState().getId(),keyresultId, previousTaskId, task.getParentTaskBoard().getId(), task.getVersion());
        this.logger.info(text);

    }
}
