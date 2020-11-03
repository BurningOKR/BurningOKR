import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiHttpService } from 'src/app/core/services/api-http.service';
import { TaskDto, TaskState } from '../../model/api/task.dto';
import { ViewTask } from '../../model/ui/view-task';
import { TaskApiService } from '../api/task-api.service';

@Injectable({
  providedIn: 'root'
})
export class TaskMapperService {

  constructor(private taskApiService: TaskApiService) { }

  private static mapToTaskDTO(viewTask: ViewTask): TaskDto {
    return {
      id: viewTask.id,
      title: viewTask.title,
      description: viewTask.description,
      assignedKeyResultIds: viewTask.assignedKeyResultIds,
      assignedUserIds: viewTask.assignedUserIds,
      state: viewTask.state,
      parentDepartmentIds: viewTask.parentDepartmentIds
    };
  }

  private static mapToViewTask(task: TaskDto): ViewTask {
    return new ViewTask(
      task.id,
      task.title,
      task.description,
      task.assignedUserIds,
      task.assignedKeyResultIds,
      task.parentDepartmentIds,
      task.state
    );
  }

  getTaskById$(id: number): Observable<ViewTask> {
    return this.taskApiService.getTaskById$(id)
      .pipe(map(TaskMapperService.mapToViewTask));
  }

  getTasksForDepartment$(departmentId: number): Observable<ViewTask[]> {
    return of(
      [{
        id: 1, title: 'test1', description: 'Test Numero 1', assignedKeyResultIds: null,
        assignedUserIds: null, parentDepartmentIds: 79, state: TaskState.todo
      }]
    );
    return this.taskApiService.getTasksForDepartment$(departmentId)
      .pipe(
        map((taskList: TaskDto[]) => {
          return taskList.map(TaskMapperService.mapToViewTask);
        })
      );
  }

  getTasksForKeyResult$(keyResultId: number): Observable<ViewTask[]> {
    return this.taskApiService.getTasksForKeyResult$(keyResultId)
      .pipe(
        map((objectiveList: TaskDto[]) => {
          return objectiveList.map(TaskMapperService.mapToViewTask);
        })
      );
  }

  postObjectiveForDepartment$(departmentId: number, viewObjective: ViewTask): Observable<ViewTask> {
    return this.taskApiService
      .postTaskForDepartment$(departmentId, TaskMapperService.mapToTaskDTO(viewObjective))
      .pipe(map(TaskMapperService.mapToViewTask));
  }

  postObjectiveForUnit$(unitId: number, viewObjective: ViewTask): Observable<ViewTask> {
    return this.taskApiService
      .postTaskForKeyResult$(unitId, TaskMapperService.mapToTaskDTO(viewObjective))
      .pipe(map(TaskMapperService.mapToViewTask));
  }

  deleteObjective$(taskId: number): Observable<boolean> {
    return this.taskApiService.deleteTask$(taskId);
  }
}
