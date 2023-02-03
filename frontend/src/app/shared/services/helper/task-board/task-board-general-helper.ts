import { ViewTask } from '../../../model/ui/taskboard/view-task';
import { TaskService } from './task.service';
import { Injectable } from '@angular/core';

@Injectable()
export class TaskBoardGeneralHelper extends TaskService {

  /**
   * update the current list of tasks for the department, removed the moved task and change the position
   * informations of the successor task.
   * returns all updated tasks.
   *
   * @param currentCompleteTaskList sorted complete list of tasks for the current department
   * @param movedTask updated item with new position informations (previousTaskId and/or taskStateId)
   */

  updateOldPositionOfMovedTaskInCompleteTaskList(
    currentSortedCompleteTaskList: ViewTask[],
    movedTask: ViewTask,
  ): ViewTask {
    let result: ViewTask = null;

    const currentTaskIndex: number = this.getListIndexForTask(currentSortedCompleteTaskList, movedTask);
    if (currentSortedCompleteTaskList[currentTaskIndex].taskStateId !== movedTask.taskStateId ||
      currentSortedCompleteTaskList[currentTaskIndex].previousTaskId !== movedTask.previousTaskId) {
      const oldTaskInformations: ViewTask = currentSortedCompleteTaskList[currentTaskIndex];
      const successor: ViewTask = currentSortedCompleteTaskList[currentTaskIndex + 1];

      if (successor.previousTaskId === movedTask.id) {
        successor.previousTaskId = oldTaskInformations.previousTaskId;
        result = successor;
      }
      this.removeTaskFromTaskList(currentSortedCompleteTaskList, movedTask);
    }

    return result;
  }

  removeTaskAndUpdateTaskList(currentList: ViewTask[], task: ViewTask): ViewTask[] {
    let result: ViewTask[] = this.copyTaskList(currentList);
    if (result && task) {
      const currentListIndex: number = this.getListIndexForTaskId(result, task.id);
      if (currentListIndex >= 0) {
        const oldTask: ViewTask = result[currentListIndex];
        let precessor: ViewTask = result[currentListIndex - 1];
        let successor: ViewTask = result[currentListIndex + 1];

        if (successor && (successor.taskStateId !== oldTask.taskStateId || successor.previousTaskId !== oldTask.id)) {
          successor = null;
        }

        if (precessor && (precessor.taskStateId !== oldTask.taskStateId || oldTask.previousTaskId !== precessor.id)) {
          precessor = null;
        }

        if (successor && precessor) {
          successor.previousTaskId = precessor.id;
        } else if (successor) {
          successor.previousTaskId = null;
        }
        result = this.removeTaskFromTaskList(result, oldTask);
      }
    }

    return result;
  }

  addTaskAndUpdateTaskList(currentList: ViewTask[], movedAndUpdatedTask: ViewTask): ViewTask[] {
    const listIndexOfNewPrecessor: number = this.getListIndexForTaskId(currentList, movedAndUpdatedTask.previousTaskId);
    let listIndexOfNewSuccessor: number = -1;
    let resultList: ViewTask[] = this.copyTaskList(currentList);

    if (listIndexOfNewPrecessor >= 0 && movedAndUpdatedTask.taskStateId === resultList[listIndexOfNewPrecessor].taskStateId) {
      if (resultList[listIndexOfNewPrecessor + 1] &&
        resultList[listIndexOfNewPrecessor + 1].previousTaskId === resultList[listIndexOfNewPrecessor].id &&
        resultList[listIndexOfNewPrecessor + 1].taskStateId === resultList[listIndexOfNewPrecessor].taskStateId) {
        listIndexOfNewSuccessor = listIndexOfNewPrecessor + 1;
      }
    } else {
      const successorIndex: number = this.getListIndexForFirstTaskOfStateId(
        resultList,
        movedAndUpdatedTask.taskStateId,
      );
      if (successorIndex >= 0) {
        listIndexOfNewSuccessor = successorIndex;
      }
    }

    if (listIndexOfNewSuccessor >= 0) {
      resultList[listIndexOfNewSuccessor].previousTaskId = movedAndUpdatedTask.id;
      resultList = this.addTaskOnPosition(resultList, movedAndUpdatedTask, listIndexOfNewSuccessor);

    } else if (listIndexOfNewPrecessor >= 0) {
      resultList = this.addTaskOnPosition(resultList, movedAndUpdatedTask, listIndexOfNewPrecessor + 1);

    } else {
      resultList.unshift(movedAndUpdatedTask);
    }

    return resultList;
  }

  orderTaskList(taskList: ViewTask[]): ViewTask[] {
    let result: ViewTask[] = this.copyTaskList(taskList);

    if (result) {
      result = this.orderManually(result);
    }

    return result;
  }

  getAllFirstTasksSortedOnTaskState(taskList: ViewTask[]): ViewTask[] {
    let result: ViewTask[] = taskList.filter(task => task.previousTaskId === null);
    result = result.sort((firstTask, secondTask) => {
      let sortResult: number;
      if (firstTask.taskStateId < secondTask.taskStateId) {
        sortResult = -1;
      } else {
        sortResult = 1;
      }

      return sortResult;
    });

    return result;
  }

  private orderManually(taskList: ViewTask[]): ViewTask[] {
    let unsortedTasks: ViewTask[] = this.copyTaskList(taskList);

    let sortedTasks: ViewTask[] = this.getAllFirstTasksSortedOnTaskState(unsortedTasks);
    unsortedTasks = this.removeTasksFromTaskList(unsortedTasks, sortedTasks);

    for (let index: number = 0; index < sortedTasks.length; index++) {
      const nextTask: ViewTask = unsortedTasks.find(task => task.previousTaskId === sortedTasks[index].id);
      if (nextTask) {
        sortedTasks = this.addTaskOnPosition(sortedTasks, nextTask, index + 1);
        unsortedTasks = this.removeTaskFromTaskList(unsortedTasks, nextTask);
      }

    }

    return sortedTasks;
  }

  private isOrderValid(taskList: ViewTask[]): boolean {
    let result: boolean = true;

    for (let index: number = 0; index < taskList.length - 2; index++) {

      if (
        (taskList[index].taskStateId === taskList[index + 1].taskStateId) &&
        (taskList[index].id !== taskList[index + 1].previousTaskId)
      ) {
        result = false;
        break;
      } else if (taskList[index].taskStateId !== taskList[index + 1].taskStateId) {
        result = (taskList[index].taskStateId < taskList[index + 1].taskStateId);
      }

    }

    return result;
  }
}
