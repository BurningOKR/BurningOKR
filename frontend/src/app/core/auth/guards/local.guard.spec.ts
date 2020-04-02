import { inject, TestBed } from '@angular/core/testing';

import { LocalGuard } from './local.guard';
import { RouterTestingModule } from '@angular/router/testing';

describe('LocalAuthGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [LocalGuard]
    });
  });

  it('should ...', inject([LocalGuard], (guard: LocalGuard) => {
    expect(guard)
      .toBeTruthy();
  }));
});
