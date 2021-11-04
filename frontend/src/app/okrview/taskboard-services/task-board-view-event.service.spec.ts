import { TestBed } from '@angular/core/testing';

import { TaskBoardViewEventService } from './task-board-view-event.service';

describe('TaskBoardViewEventService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TaskBoardViewEventService = TestBed.inject(TaskBoardViewEventService);
    expect(service)
      .toBeTruthy();
  });
});
