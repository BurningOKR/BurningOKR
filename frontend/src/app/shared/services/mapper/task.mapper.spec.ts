import { TestBed } from '@angular/core/testing';

import { TaskMapperService } from './task.mapper';
import { OkrUnitId } from '../../model/id-types';
import { Observable, of } from 'rxjs';
import { ViewTaskState } from '../../model/ui/taskboard/view-task-state';
import { TaskApiService } from '../api/task-api.service';

describe('Task.MapperService', () => {
  class TaskApiServiceMock {
    getTaskStatesForOkrUnit$(unitId: OkrUnitId): Observable<ViewTaskState[]> {
      return of();
    }
  }

  beforeEach(() => TestBed.configureTestingModule({
      providers: [{
        provide: TaskApiService,
        useValue: new TaskApiServiceMock()
      }]
    })
  );

  it('should be created', () => {
    const service: TaskMapperService = TestBed.inject(TaskMapperService);
    expect(service)
      .toBeTruthy();
  });
});
