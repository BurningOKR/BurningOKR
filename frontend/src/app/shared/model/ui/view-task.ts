
import { TaskStateDto } from "../api/task-state.dto";
import { DepartmentId, KeyResultId, TaskBoardId, TaskId, TaskStateId, UserId } from "../id-types";
import { ViewTaskState } from "./view-task-state";

export class ViewTask {
    id?: TaskId;
    title: string;
    description: string;
    assignedUserIds: UserId[];
    assignedKeyResultId: KeyResultId;
    stateId: TaskStateId;
    parentOkrUnit: DepartmentId;

    constructor(id: number, title: string, description: string, assignedUserIds: UserId[],
                assignedKeyResultId: KeyResultId, parentOkrUnit: TaskBoardId, state: TaskStateId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignedUserIds = assignedUserIds;
        this.assignedKeyResultId = assignedKeyResultId;
        this.parentOkrUnit = parentOkrUnit;
        this.stateId = state;
    }
}
