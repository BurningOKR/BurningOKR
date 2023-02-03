import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TaskStateDto } from '../../model/api/task-state.dto';
import { OkrUnitId } from '../../model/id-types';
import { ViewTaskState } from '../../model/ui/taskboard/view-task-state';
import { TaskStateApiService } from '../api/task-state-api.service';

@Injectable({
  providedIn: 'root',
})
export class TaskStateMapper {

  constructor(private taskStateService: TaskStateApiService) {
  }

  private static mapToTaskStateDTO(viewState: ViewTaskState): TaskStateDto {
    return {
      id: viewState.id,
      title: viewState.name,
    };
  }

  private static mapToViewTaskState(taskState: TaskStateDto): ViewTaskState {
    return new ViewTaskState(taskState.id, taskState.title);
  }

  getTaskStates$(unitId: OkrUnitId): Observable<ViewTaskState[]> {
    return this.taskStateService.getTaskStatesForOkrUnit$(unitId)
      .pipe(
        map((stateList: TaskStateDto[]) => {
          return stateList.map(TaskStateMapper.mapToViewTaskState);
        }),
      );
  }
}
