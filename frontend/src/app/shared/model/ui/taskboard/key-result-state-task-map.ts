import { StateTaskMap } from "./state-task-map";
import { ViewKeyResult } from "../view-key-result";

export interface KeyResultStateTaskMap {
    keyResult: ViewKeyResult;
    statesWithTasks: StateTaskMap[];
}