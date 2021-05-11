import { ViewTask } from '../ui/taskboard/view-task';
import { ViewTaskState } from '../ui/taskboard/view-task-state';
import { ViewKeyResult } from '../ui/view-key-result';

export class ViewTaskBoardEvent {
    keyResults: ViewKeyResult[] = [];
    tasks: ViewTask[] = [];
    taskStates: ViewTaskState[] = [];
}
