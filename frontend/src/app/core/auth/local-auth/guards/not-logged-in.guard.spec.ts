import { inject, TestBed } from '@angular/core/testing';

import { NotLoggedInGuard } from './not-logged-in.guard';
import { AuthenticationService } from '../../services/authentication.service';
import { Router } from '@angular/router';

describe('NotLoggedInGuard', () => {

  const authenticationServiceMock: any = {
    hasValidAccessToken: jest.fn()
  };
  const routerMock: any = {
    navigate: jest.fn()
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
      providers: [
        NotLoggedInGuard,
        {provide: AuthenticationService, useValue: authenticationServiceMock},
        {provide: Router, useValue: routerMock}
      ],
    });
  });

  it('should create', inject([NotLoggedInGuard], (guard: NotLoggedInGuard) => {
    expect(guard)
      .toBeTruthy();
  }));

  it('should return false and route', inject([NotLoggedInGuard], (guard: NotLoggedInGuard) => {
    authenticationServiceMock.hasValidAccessToken.mockReturnValue(true);

    const resultValue: boolean = guard.canActivate(null, null);

    expect(resultValue)
      .toBeFalsy();
    expect(routerMock.navigate)
      .toHaveBeenCalled();
  }));

  it('should return true an not route', inject([NotLoggedInGuard], (guard: NotLoggedInGuard) => {
    authenticationServiceMock.hasValidAccessToken.mockReturnValue(false);

    const resultValue: boolean = guard.canActivate(null, null);

    expect(resultValue)
      .toBeTruthy();
  }));
});
