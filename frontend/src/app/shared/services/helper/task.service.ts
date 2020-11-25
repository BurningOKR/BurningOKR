import { Injectable } from '@angular/core';
import { StateTaskMap } from '../../model/ui/state-task-map';
import { ViewTask } from '../../model/ui/view-task';
import { ViewTaskState } from '../../model/ui/view-task-state';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  generateStateTaskMapList(states: ViewTaskState[], tasks: ViewTask[]): StateTaskMap[] {
    const map: StateTaskMap[] = [];
    for (const state of states) {
      map.push({
        state,
        tasks: tasks.filter(viewTask => viewTask.stateId === state.id)
      });
    }

    return map;
  }

  extractStates(stateTaskMap: StateTaskMap[]): ViewTaskState[] {
    const states: ViewTaskState[] = [];
    for (const taskMap of stateTaskMap) {
      states.push(taskMap.state);
    }

    return states;
  }

  isTaskInList(task: ViewTask, taskList: ViewTask[]): boolean {
    return !!taskList.find(listTask => task.id === listTask.id);
  }

  getListIndex(stateId: number, stateTaskMap: StateTaskMap[]): number {
    return stateTaskMap.findIndex(map => stateId === map.state.id);
  }
}
