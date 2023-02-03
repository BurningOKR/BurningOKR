import { TestBed } from '@angular/core/testing';

import { TaskStateMapper } from './task-state.mapper';
import { ViewTaskState } from '../../model/ui/taskboard/view-task-state';
import { OkrUnitId } from '../../model/id-types';
import { Observable, of } from 'rxjs';
import { TaskStateApiService } from '../api/task-state-api.service';

describe('TaskStateService', () => {
  class TaskStateApiServiceMock {
    getTaskStatesForOkrUnit$(unitId: OkrUnitId): Observable<ViewTaskState[]> {
      return of();
    }
  }

  beforeEach(() => TestBed.configureTestingModule({
    providers: [{
      provide: TaskStateApiService,
      useValue: new TaskStateApiServiceMock(),
    }],
  }));

  it('should be created', () => {
    const service: TaskStateMapper = TestBed.inject(TaskStateMapper);
    expect(service)
      .toBeTruthy();
  });
});
