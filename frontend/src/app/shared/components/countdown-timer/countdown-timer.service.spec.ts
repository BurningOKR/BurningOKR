import { TestBed } from '@angular/core/testing';

import { CountdownTimerService } from './countdown-timer.service';

describe('CountdownTimerService', () => {
  let service: CountdownTimerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CountdownTimerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
