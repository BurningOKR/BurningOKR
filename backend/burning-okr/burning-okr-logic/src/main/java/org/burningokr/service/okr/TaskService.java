package org.burningokr.service.okr;

import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.Unit;
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
    private final Logger logger = LoggerFactory.getLogger(ObjectiveService.class);

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
    ){
        this.taskRepository =taskRepository;
        this.okrUnitRepository = okrUnitRepository;
        this.activityService=activityService;
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
        return taskRepository.findByTaskBoard(taskboard);
    }

    public Collection<Task> findTasksForKeyResult(KeyResult keyResult) {
        return this.taskRepository.findByKeyResult(keyResult);
    }

    public Task createTask(Task newTask, Long assignedKeyResultId, Long okrUnitId, User user) {
        OkrUnit okrUnit =  this.okrUnitRepository.findByIdOrThrow(okrUnitId);
        this.throwIfCycleOfTaskIsClosed(okrUnit);
        newTask.setParentTaskBoard(okrUnit.getTaskBoard());
        newTask= taskRepository.save(newTask);
        logger.info("Created Task " + newTask.getName());
        activityService.createActivity(user, newTask, Action.CREATED);
        return newTask;
    }

    @Transactional
    public Task updateTask(Task updatedTask, User user) {
        Task referencedTask = taskRepository.findByIdOrThrow(updatedTask.getId());
        throwIfCycleOfTaskIsClosed(referencedTask.getParentTaskBoard().getParentOkrUnit());

        referencedTask.setTitle(updatedTask.getTitle());
        referencedTask.setDescription(updatedTask.getDescription());
        referencedTask.setOkrStateId(updatedTask.getOkrStateId());
        referencedTask.setAssignedKeyResult(updatedTask.getAssignedKeyResult());
        referencedTask.setAssignedUserIds(updatedTask.getAssignedUserIds());

        referencedTask = taskRepository.save(referencedTask);

        logger.info(
                "Updated Task "
                        + referencedTask.getName()
                        + "(id:"
                        + referencedTask.getId()
                        + ")");
        activityService.createActivity(user, referencedTask, Action.EDITED);
        return referencedTask;
    }

    public void deleteTaskById(long taskId, long unitId , User user) {
        OkrUnit unit = okrUnitRepository.findByIdOrThrow(unitId);
        throwIfCycleOfTaskIsClosed(unit);
        Task referencedTask= this.taskRepository.findByIdOrThrow(taskId);
        this.taskRepository.deleteById(taskId);
        activityService.createActivity(user, referencedTask, Action.DELETED);
    }

    private void throwIfCycleOfTaskIsClosed(OkrUnit unit) {
        if (entityCrawlerService.getCycleOfUnit(unit).getCycleState()
                == CycleState.CLOSED) {
            throw new ForbiddenException("Cannot modify this resource on a Task in a closed cycle.");
        }
    }
}
