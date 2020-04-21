import { TestBed } from '@angular/core/testing';

import { CurrentCycleService } from './current-cycle.service';

describe('CurrentCycleService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CurrentCycleService = TestBed.get(CurrentCycleService);
    expect(service)
      .toBeTruthy();
  });
});
