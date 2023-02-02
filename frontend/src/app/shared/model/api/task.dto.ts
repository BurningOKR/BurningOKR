import { DepartmentId, KeyResultId, TaskId, TaskStateId, UserId } from '../id-types';

export class TaskDto {
  id?: TaskId;
  title: string;
  description: string;
  taskStateId: TaskStateId;
  assignedUserIds: UserId[];
  assignedKeyResultId: KeyResultId;
  parentTaskBoardId: DepartmentId;
  previousTaskId: number;
  version: number;
}
