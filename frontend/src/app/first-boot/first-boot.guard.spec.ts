import { inject, TestBed } from '@angular/core/testing';

import { FirstBootGuard } from './first-boot.guard';

describe('FirstBootGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FirstBootGuard]
    });
  });

  it('should ...', inject([FirstBootGuard], (guard: FirstBootGuard) => {
    expect(guard)
      .toBeTruthy();
  }));
});
