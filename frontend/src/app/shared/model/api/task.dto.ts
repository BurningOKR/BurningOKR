import { DepartmentId, KeyResultId, TaskId, UserId, TaskStateId } from "../id-types";
import { TaskStateDto } from "./task-state.dto";

export class TaskDto {
    id?: TaskId;
    title: string;
    description: string;
    assignedUserIds: UserId[];
    assignedKeyResultId: KeyResultId;
    parentOkrUnitId: DepartmentId;
    stateId: TaskStateId;
}
