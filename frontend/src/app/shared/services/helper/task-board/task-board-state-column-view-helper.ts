import { TaskService } from './task.service';
import { ViewTaskState } from '../../../model/ui/taskboard/view-task-state';
import { ViewTask } from '../../../model/ui/taskboard/view-task';
import { StateTaskMap } from '../../../model/ui/taskboard/state-task-map';
import { Injectable } from '@angular/core';

@Injectable()
export class TaskBoardStateColumnViewHelper extends TaskService {
    /**
     * Create a List of Tasks for all given states and returned the result list.
     * When parameter defaultStateTaskMap is given and not all tasks are mapped to one state. These tasks are add to
     * this parameter and these map is also added to the result map list.
     * When parameter defaultStateTaskMap is null, then all tasks were can not mapped to one the given states are not
     * part of the result map list.
     * @param states: available task states
     * @param tasks: list of tasks
     * @param defaultStateTaskMap default container for the tasks, which can not map to one of the given states. Recommended
     *                            attribute is title. Id and tasks are not required.
     */
    createStateTaskMapList(states: ViewTaskState[], tasks: ViewTask[], defaultStateTaskMap?: StateTaskMap): StateTaskMap[] {
        let copiedTasks: ViewTask[] = this.copyTaskList(tasks);

        const map: StateTaskMap[] = [];
        for (const state of states) {
            const tasksForStates: ViewTask[] = copiedTasks.filter(viewTask => viewTask.taskStateId === state.id);
            copiedTasks = this.removeTasksFromTaskList(copiedTasks, tasksForStates);
            map.push({
                state,
                tasks: tasksForStates
            });
        }

        if (defaultStateTaskMap && (copiedTasks.length > 0 || map.length === 0)) {
            defaultStateTaskMap.tasks = copiedTasks;
            map.unshift(defaultStateTaskMap);
        }

        return map;
    }

    getFirstTaskIdForState(stateTaskMap: StateTaskMap[], stateId: number): number {
        const index: number = this.getListIndexOfState(stateId, stateTaskMap);
        let result: number;
        if (stateTaskMap && stateTaskMap[index].tasks[0]) {
            result = this.getFirstTaskId(stateTaskMap[index].tasks);
        }

        return result;
    }

    getMovedTaskWithNewPositionData(previousIndex: number, previousContainer: ViewTask[],
                                    currentIndex: number, currentContainer: ViewTask[], newTaskState: ViewTaskState): ViewTask {
        let result: ViewTask = null;
        if (previousIndex >= 0 && previousContainer) {
            result = previousContainer[previousIndex].copy();
            if (currentIndex >= 0 && currentContainer) {
                // Set Task to the first position
                if (currentIndex === 0) {
                    if (currentContainer[currentIndex]) {
                        result.previousTaskId = currentContainer[currentIndex].previousTaskId;
                    } else {
                        result.previousTaskId = null;
                    }

                } else
                    // append task to the end the list, when
                    if (currentContainer.length > 0 &&
                        (
                            (currentIndex === currentContainer.length) ||
                            (currentIndex > previousIndex)
                        )
                    ) {
                        if (
                            currentContainer[currentIndex] &&
                            currentContainer[currentIndex] !== result &&
                            currentContainer[currentIndex].taskStateId === result.taskStateId
                        ) {
                            result.previousTaskId = currentContainer[currentIndex].id;
                        } else {
                            result.previousTaskId = currentContainer[currentIndex - 1].id;
                        }

                    } else {
                        //
                        result.previousTaskId = currentContainer[currentIndex - 1].id;
                    }

            }
        }
        if (result && result.taskStateId !== newTaskState.id) {
            result.taskStateId = newTaskState.id;
        }

        return result;
    }

    overwriteOneTaskListOfStateTaskMap(statetaskMap: StateTaskMap[], taskList: ViewTask[], state: ViewTaskState): StateTaskMap[] {
        const index: number = this.getListIndexOfState(state.id, statetaskMap);
        this.updateTaskList(statetaskMap[index].tasks, taskList);
        this.orderTaskList(statetaskMap[index].tasks);

        return statetaskMap;
    }

    clearAndUpdateTaskListsOfStateTaskMap(statetaskMap: StateTaskMap[], taskList: ViewTask[]): void {
        this.clearTaskListsOfStateTaskMap(statetaskMap);

        for (const map of statetaskMap) {
            map.tasks.push(...taskList.filter(viewTask => viewTask.taskStateId === map.state.id));
            this.orderTaskList(map.tasks);
        }
    }

    clearTaskListsOfStateTaskMap(statetaskMap: StateTaskMap[]): void {
        for (const map of statetaskMap) {
            this.clearTaskList(map.tasks);
        }
    }

    removeTaskFromStateTaskMap(stateTaskMap: StateTaskMap[], task: ViewTask, oldStateId: number): void {
        const index: number = this.getListIndexOfState(oldStateId, stateTaskMap);
        if (index >= 0) {
            this.removeTaskFromTaskList(stateTaskMap[index].tasks, task);
        }
    }

    extractStates(stateTaskMap: StateTaskMap[]): ViewTaskState[] {
        const states: ViewTaskState[] = [];
        for (const taskMap of stateTaskMap) {
            states.push(taskMap.state);
        }

        return states;
    }

    getListIndexOfState(stateId: number, stateTaskMap: StateTaskMap[]): number {
        return stateTaskMap.findIndex(map => stateId === map.state.id);
    }

    orderTaskList(taskList: ViewTask[]): ViewTask[] {
        return taskList.sort((firstTask, secondTask) => {
            let result: number = 0;
            if ((!firstTask.previousTaskId) || (firstTask.id === secondTask.previousTaskId)) {
                result = -1;
            } else if ((!secondTask.previousTaskId) || (firstTask.previousTaskId === secondTask.id)) {
                result = 1;
            } else {
                result = -1;
            }

            return result;
        });
    }
}
