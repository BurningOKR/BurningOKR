import { TestBed, async, inject } from '@angular/core/testing';

import { NotInitiliazedGuard } from './not-initiliazed.guard';

describe('NotInitiliazedGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NotInitiliazedGuard]
    });
  });

  it('should ...', inject([NotInitiliazedGuard], (guard: NotInitiliazedGuard) => {
    expect(guard)
      .toBeTruthy();
  }));
});
