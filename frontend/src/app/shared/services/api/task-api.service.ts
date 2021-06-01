import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { TaskDto } from '../../model/api/task.dto';
import { KeyResultId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class TaskApiService {

  constructor(private api: ApiHttpService) { }

  getTaskById$(id: number): Observable<TaskDto> {
    return this.api.getData$(`tasks/${id}`);
  }
  getTasksForKeyResult$(keyResultId: KeyResultId): Observable<TaskDto[]> {
    return this.api.getData$(`tasks/keyresults/${keyResultId}`);
  }

  getTasksForOkrUnit$(unitId: number): Observable<TaskDto[]> {
    return this.api.getData$(`unit/${unitId}/tasks`);
  }

  postTask$(task: TaskDto, unitId: number): Observable<TaskDto> {
    return this.api.postData$(`unit/${unitId}/tasks`, task);
  }

  putTask$(task: TaskDto, unitId: number): Observable<TaskDto> {
    return this.api.putData$(`unit/${unitId}/tasks/${task.id}`, task);
  }

  deleteTask$(taskId: number, unitId: number): Observable<boolean> {
    return this.api.deleteData$(`unit/${unitId}/tasks/${taskId}`);
  }
}
