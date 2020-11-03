import { DepartmentId, KeyResultId, TaskId, UserId } from "../id-types";
import { User } from "./user";

export enum TaskState {
    todo = "todo",
    inProgress = "inProgress",
    blocked = "blocked",
    finished = "finished"
}

export class TaskDto {
    id?: TaskId;
    title: string;
    description: string;
    assignedUserIds: UserId[];
    assignedKeyResultIds: KeyResultId[];
    parentDepartmentIds: DepartmentId;
    state: TaskState;
}
