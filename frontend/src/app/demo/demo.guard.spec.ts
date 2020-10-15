import { TestBed, async, inject } from '@angular/core/testing';

import { DemoGuard } from './demo.guard';
import { RouterTestingModule } from '@angular/router/testing';

describe('DemoGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [DemoGuard]
    });
  });

  it('should ...', inject([DemoGuard], (guard: DemoGuard) => {
    expect(guard)
      .toBeTruthy();
  }));
});
