import { TestBed } from '@angular/core/testing';

import { TaskStateMapperService } from './task-state.mapper';

describe('TaskStateService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TaskStateMapperService = TestBed.get(TaskStateMapperService);
    expect(service).toBeTruthy();
  });
});
