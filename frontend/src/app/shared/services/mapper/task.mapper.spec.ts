import { TestBed } from '@angular/core/testing';

import { TaskMapperService } from './task.mapper';

describe('Task.MapperService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TaskMapperService = TestBed.get(TaskMapperService);
    expect(service).toBeTruthy();
  });
});
