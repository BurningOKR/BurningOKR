import { TestBed } from '@angular/core/testing';

import { TaskStateMapper } from './task-state.mapper';
import { TaskStateDto } from '../../model/api/task-state.dto';
import { ViewTaskState } from '../../model/ui/taskboard/view-task-state';
import { OkrUnitId } from '../../model/id-types';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { TaskStateApiService } from '../api/task-state-api.service';

describe('TaskStateService', () => {
  class TaskStateApiServiceMock {
    getTaskStatesForOkrUnit$(unitId: OkrUnitId): Observable<ViewTaskState[]> {
      return of();
    }
  }

  beforeEach(() => TestBed.configureTestingModule({providers: [{provide: TaskStateApiService, useValue: new TaskStateApiServiceMock()}]}));

  it('should be created', () => {
    const service: TaskStateMapper = TestBed.get(TaskStateMapper);
    expect(service)
      .toBeTruthy();
  });
});
