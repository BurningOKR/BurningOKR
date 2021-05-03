
import { TaskId, TaskStateId, UserId, KeyResultId, DepartmentId, TaskBoardId } from "../../id-types";
import { TaskBoardObject } from "./task-board-object";

export class ViewTask implements TaskBoardObject {
    id?: TaskId;
    title: string;
    description: string;
    previousTaskId: number;
    taskStateId: TaskStateId;
    assignedUserIds: UserId[];
    assignedKeyResultId: KeyResultId;
    parentTaskBoardId: DepartmentId;

    version: number;

    constructor(id: number, title: string, description: string, assignedUserIds: UserId[],
        assignedKeyResultId: KeyResultId, parentTaskBoardId: TaskBoardId,
        state: TaskStateId, previousTaskId: number, version: number) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignedUserIds = assignedUserIds;
        this.assignedKeyResultId = assignedKeyResultId;
        this.parentTaskBoardId = parentTaskBoardId;
        this.taskStateId = state;
        this.previousTaskId = previousTaskId;
        this.version = version;
    }

    copy(): ViewTask {
        const assignedUserIds: UserId[] = [];
        if (this.assignedUserIds) {
            for (const id of this.assignedUserIds) {
                assignedUserIds.push(id);
            }
        }

        return new ViewTask(this.id, this.title, this.description, assignedUserIds,
            this.assignedKeyResultId, this.parentTaskBoardId, this.taskStateId, this.previousTaskId, this.version);
    }
}
