import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiHttpService } from 'src/app/core/services/api-http.service';
import { TaskDto } from '../../model/api/task.dto';
import { ViewTask } from '../../model/ui/view-task';
import { TaskApiService } from '../api/task-api.service';

@Injectable({
  providedIn: 'root'
})
export class TaskMapperService {

  constructor(private taskApiService: TaskApiService) { }

  private static mapToTaskDTO(viewTask: ViewTask): TaskDto {
    let userIds = [];
    if (viewTask.assignedUserIds) {
      userIds = viewTask.assignedUserIds;
    }

    return {
      id: viewTask.id,
      title: viewTask.title,
      description: viewTask.description,
      assignedKeyResultId: viewTask.assignedKeyResultId,
      assignedUserIds: userIds,
      stateId: viewTask.stateId,
      parentOkrUnitId: viewTask.parentOkrUnit
    };
  }

  private static mapToViewTask(task: TaskDto): ViewTask {
    return new ViewTask(
      task.id,
      task.title,
      task.description,
      task.assignedUserIds,
      task.assignedKeyResultId,
      task.parentOkrUnitId,
      task.stateId
    );
  }

  getTaskById$(id: number): Observable<ViewTask> {
    return this.taskApiService.getTaskById$(id)
      .pipe(map(TaskMapperService.mapToViewTask));
  }

  getTasksForOkrUnit$(unitId: number): Observable<ViewTask[]> {
    return this.taskApiService.getTasksForOkrUnit$(unitId)
      .pipe(
        map((taskList: TaskDto[]) => {
          return taskList.map(TaskMapperService.mapToViewTask);
        })
      );
  }

  getTasksForKeyResult$(keyResultId: number): Observable<ViewTask[]> {
    return this.taskApiService.getTasksForKeyResult$(keyResultId)
      .pipe(
        map((taskList: TaskDto[]) => {
          return taskList.map(TaskMapperService.mapToViewTask);
        })
      );
  }

  createTaskForOkrUnit$(unitId: number, task: ViewTask): Observable<ViewTask> {
    return this.taskApiService
      .postTask$(TaskMapperService.mapToTaskDTO(task), unitId)
      .pipe(map(TaskMapperService.mapToViewTask));
  }

  updateTask$(unitId: number, task: ViewTask): Observable<ViewTask> {
    return this.taskApiService
      .putTask$(TaskMapperService.mapToTaskDTO(task), unitId)
      .pipe(map(TaskMapperService.mapToViewTask));
  }

  deleteTask$(unitId: number, taskId: number): Observable<boolean> {
    return this.taskApiService.deleteTask$(taskId, unitId);
  }
}
