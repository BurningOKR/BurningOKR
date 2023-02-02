import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TaskDto } from '../../model/api/task.dto';
import { ViewTask } from '../../model/ui/taskboard/view-task';
import { TaskApiService } from '../api/task-api.service';
import { UserId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class TaskMapperService {

  constructor(private taskApiService: TaskApiService) { }

  mapToTaskDTO(viewTask: ViewTask): TaskDto {
    let userIds: UserId[] = [];
    if (viewTask.assignedUserIds) {
      userIds = viewTask.assignedUserIds;
    }

    return {
      id: viewTask.id,
      title: viewTask.title,
      description: viewTask.description,
      assignedKeyResultId: viewTask.assignedKeyResultId,
      assignedUserIds: userIds,
      taskStateId: viewTask.taskStateId,
      parentTaskBoardId: viewTask.parentTaskBoardId,
      previousTaskId: viewTask.previousTaskId,
      version: viewTask.version
    };
  }

  mapToViewTask(task: TaskDto): ViewTask {
    return new ViewTask(
      task.id,
      task.title,
      task.description,
      task.assignedUserIds,
      task.assignedKeyResultId,
      task.parentTaskBoardId,
      task.taskStateId,
      task.previousTaskId,
      task.version
    );
  }

  getTaskById$(id: number): Observable<ViewTask> {
    return this.taskApiService.getTaskById$(id)
      .pipe(map(this.mapToViewTask));
  }

  getTasksForOkrUnit$(unitId: number): Observable<ViewTask[]> {
    return this.taskApiService.getTasksForOkrUnit$(unitId)
      .pipe(
        map((taskList: TaskDto[]) => {
          return taskList.map(this.mapToViewTask);
        })
      );
  }

  getTasksForKeyResult$(keyResultId: number): Observable<ViewTask[]> {
    return this.taskApiService.getTasksForKeyResult$(keyResultId)
      .pipe(
        map((taskList: TaskDto[]) => {
          return taskList.map(this.mapToViewTask);
        })
      );
  }

  createTaskForOkrUnit$(unitId: number, task: ViewTask): Observable<ViewTask> {
    return this.taskApiService
      .postTask$(this.mapToTaskDTO(task), unitId)
      .pipe(map(this.mapToViewTask));
  }

  updateTask$(unitId: number, task: ViewTask): Observable<ViewTask> {
    return this.taskApiService
      .putTask$(this.mapToTaskDTO(task), unitId)
      .pipe(map(this.mapToViewTask));
  }

  deleteTask$(unitId: number, taskId: number): Observable<boolean> {
    return this.taskApiService.deleteTask$(taskId, unitId);
  }
}
