import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from 'src/app/core/services/api-http.service';
import { TaskDto } from '../../model/api/task.dto';

@Injectable({
  providedIn: 'root'
})
export class TaskApiService {

  constructor(private api: ApiHttpService) { }

  getTaskById$(id: number): Observable<TaskDto> {
    return this.api.getData$(`tasks/${id}`);
  }

  getTasksForDepartment$(departmentId: number): Observable<TaskDto[]> {
    return this.api.getData$(`departments/${departmentId}/tasks`);
  }

  getTasksForKeyResult$(keyResultId: number): Observable<TaskDto[]> {
    return this.api.getData$(`keyresults/${keyResultId}/tasks`);
  }

  postTaskForDepartment$(departmentId: number, task: TaskDto): Observable<TaskDto> {
    return this.api.postData$(`departments/${departmentId}/tasks`, task);
  }

  postTaskForKeyResult$(keyResultId: number, task: TaskDto): Observable<TaskDto> {
    return this.api.postData$(`keyresults/${keyResultId}/tasks`, task);
  }

  deleteTask$(taskId: number): Observable<boolean> {
    return this.api.deleteData$(`tasks/${taskId}`);
  }
}
