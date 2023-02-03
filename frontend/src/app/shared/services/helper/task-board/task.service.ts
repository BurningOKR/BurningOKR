import { Injectable } from '@angular/core';
import { ViewTask } from '../../../model/ui/taskboard/view-task';
import { TaskId, TaskStateId } from '../../../model/id-types';

@Injectable({
  providedIn: 'root',
})
export abstract class TaskService {
  getFirstTaskId(taskList: ViewTask[]): number {
    let result: number = -1;
    if (taskList && taskList[0]) {
      result = taskList[0].id;
    }

    return result;
  }

  getListIndexForTask(currentTaskList: ViewTask[], taskToFind: ViewTask): number {
    let result: number = -1;
    if (taskToFind && currentTaskList) {
      result = this.getListIndexForTaskId(currentTaskList, taskToFind.id);
    }

    return result;
  }

  getListIndexForTaskId(currentTaskList: ViewTask[], taskIdToFind: TaskId): number {
    let result: number = -1;
    if (taskIdToFind >= 0 && currentTaskList) {
      result = currentTaskList.findIndex(listTask => listTask.id === taskIdToFind);
    }

    return result;
  }

  getListIndexForFirstTaskOfStateId(currentTaskList: ViewTask[], stateId: TaskStateId): number {
    let result: number = -1;
    if (currentTaskList && stateId >= 0) {
      result = currentTaskList.findIndex(task => task.taskStateId === stateId && !task.previousTaskId);
    }

    return result;
  }

  isTaskInList(task: ViewTask, taskList: ViewTask[]): boolean {
    return !!taskList.find(listTask => task.id === listTask.id);
  }

  addTaskOnPosition(taskList: ViewTask[], task: ViewTask, index: number): ViewTask[] {
    const result: ViewTask[] = this.copyTaskList(taskList);
    if (taskList && task && index >= 0) {
      result.splice(index, 0, task);
    }

    return result;
  }

  updateTaskList(taskListToUpdate: ViewTask[], newTaskList: ViewTask[]): ViewTask[] {
    let result: ViewTask[] = null;
    if (taskListToUpdate !== newTaskList) {
      result = this.copyTaskList(newTaskList);
    }

    return result;
  }

  mergeTaskListWithNewOrUpdatedElements(sortedCurrentTaskList: ViewTask[], newOrUpdatedTasks: ViewTask[]): void {
    for (const newOrUpdatedTask of newOrUpdatedTasks) {
      const index: number = sortedCurrentTaskList.findIndex(task => task.id === newOrUpdatedTask.id);
      if (index >= 0) {
        sortedCurrentTaskList.splice(index, 1, newOrUpdatedTask);
      } else {
        sortedCurrentTaskList.unshift(newOrUpdatedTask);
      }
    }
  }

  copyTaskList(taskList: ViewTask[]): ViewTask[] {
    let copiedTasks: ViewTask[] = null;
    if (taskList) {
      copiedTasks = [];
      for (const task of taskList) {
        copiedTasks.push(task.copy());
      }
    }

    return copiedTasks;
  }

  clearTaskList(taskList: ViewTask[]): void {
    if (taskList) {
      taskList.splice(0);
    }
  }

  removeTaskFromTaskList(currentTaskList: ViewTask[], taskToRemove: ViewTask): ViewTask[] {
    const copiedTasks: ViewTask[] = this.copyTaskList(currentTaskList);

    const taskIndex: number = this.getListIndexForTask(copiedTasks, taskToRemove);
    copiedTasks.splice(taskIndex, 1);

    return copiedTasks;
  }

  removeTasksFromTaskList(currentTaskList: ViewTask[], tasksToRemove: ViewTask[]): ViewTask[] {
    let result: ViewTask[] = this.copyTaskList(currentTaskList);
    for (const task of tasksToRemove) {
      result = this.removeTaskFromTaskList(result, task);
    }

    return result;
  }

  abstract orderTaskList(taskList: ViewTask[]): ViewTask[];
}
