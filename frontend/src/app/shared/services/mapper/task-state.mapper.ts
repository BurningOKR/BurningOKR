import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { TaskStateDto } from '../../model/api/task-state.dto';
import { TaskDto } from '../../model/api/task.dto';
import { OkrUnitId } from '../../model/id-types';
import { ViewTask } from '../../model/ui/view-task';

import { ViewTaskState } from '../../model/ui/view-task-state';

@Injectable({
  providedIn: 'root'
})
export class TaskStateMapperService {

  private static mapToTaskStateDTO(viewState: ViewTaskState): TaskStateDto {
    return {
      id: viewState.id,
      name: viewState.name
    };
  }

  private static mapToViewTaskState(taskState: TaskStateDto): ViewTaskState {
    return new ViewTaskState(taskState.id, taskState.name);
  }

  getTaskStates$(unitId: OkrUnitId): Observable<ViewTaskState[]> {
    return of([
      { id: 0, name: 'Todo' },
      { id: 1, name: 'In Progress' },
      { id: 2, name: 'Blocked' },
      { id: 3, name: 'Finished' },
    ]);
  }
}
