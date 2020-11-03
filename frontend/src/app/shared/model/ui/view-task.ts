import { TaskState } from "../api/task.dto";
import { DepartmentId, KeyResultId, TaskId, UserId } from "../id-types";

export class ViewTask {
    id?: TaskId;
    title: string;
    description: string;
    assignedUserIds: UserId[];
    assignedKeyResultIds: KeyResultId[];
    state: TaskState;
    parentDepartmentIds: DepartmentId;

    constructor(id: number, title: string, description: string, assignedUserIds: UserId[],
                assignedKeyResultIds: KeyResultId[], parentDepartmentIds: DepartmentId, state: TaskState) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignedUserIds = assignedUserIds;
        this.assignedKeyResultIds = assignedKeyResultIds;
        this.parentDepartmentIds = parentDepartmentIds;
        this.state = state;
    }
}
