import { ViewTask } from './view-task';
import { ViewTaskState } from './view-task-state';

export interface StateTaskMap {
  state: ViewTaskState;
  tasks: ViewTask[];
}
