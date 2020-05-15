import { TestBed, inject } from '@angular/core/testing';

import { NotLoggedInGuard } from './not-logged-in.guard';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthenticationService } from '../../services/authentication.service';
import { AuthenticationServiceMock } from '../../../../shared/mocks/authentication-service-mock';

describe('NotLoggedInGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        NotLoggedInGuard,
        {provide: AuthenticationService, useValue: AuthenticationServiceMock},
        ],
    });
  });

  it('should ...', inject([NotLoggedInGuard], (guard: NotLoggedInGuard) => {
    expect(guard)
      .toBeTruthy();
  }));
});
