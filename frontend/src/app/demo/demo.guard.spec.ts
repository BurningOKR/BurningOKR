import { TestBed, async, inject } from '@angular/core/testing';

import { DemoGuard } from './demo.guard';

describe('DemoGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DemoGuard]
    });
  });

  it('should ...', inject([DemoGuard], (guard: DemoGuard) => {
    expect(guard).toBeTruthy();
  }));
});
