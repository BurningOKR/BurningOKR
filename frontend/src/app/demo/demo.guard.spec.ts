import { TestBed, inject } from '@angular/core/testing';

import { DemoGuard } from './demo.guard';
import { RouterTestingModule } from '@angular/router/testing';
import {environment} from '../../environments/environment';

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

  it('should deactivate DemoGuard in Prod-Mode', inject([DemoGuard], (guard: DemoGuard) => {

    environment.playground = true;
    expect(guard.canActivate(null, null)).toBeDefined();

    environment.playground = false;
    expect(guard.canActivate(null, null)).toBeTruthy();
  }));
});
