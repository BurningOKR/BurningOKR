import { TestBed } from '@angular/core/testing';

import { TaskStateMapper } from './task-state.mapper';

describe('TaskStateService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TaskStateMapper = TestBed.get(TaskStateMapper);
    expect(service).toBeTruthy();
  });
});
