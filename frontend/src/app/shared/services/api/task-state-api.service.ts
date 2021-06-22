import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { TaskStateDto } from '../../model/api/task-state.dto';

@Injectable({
  providedIn: 'root'
})
export class TaskStateApiService {

  constructor(private api: ApiHttpService) { }

  getTaskStatesForOkrUnit$(unitId: number): Observable<TaskStateDto[]> {
    return this.api.getData$(`unit/${unitId}/states`);
  }

}
