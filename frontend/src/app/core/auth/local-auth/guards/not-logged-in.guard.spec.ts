import { inject, TestBed } from '@angular/core/testing';

import { NotLoggedInGuard } from './not-logged-in.guard';
import { AuthenticationService } from '../../services/authentication.service';
import { Router, UrlTree } from '@angular/router';

describe('NotLoggedInGuard', () => {

  const authenticationServiceMock: any = {
    hasValidAccessToken: jest.fn()
  };
  const routerMock: any = {
    createUrlTree: jest.fn()
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

  beforeEach(() => {
    routerMock.createUrlTree.mockReset();
    routerMock.createUrlTree.mockReturnValue(new UrlTree());
  });

  it('should create', inject([NotLoggedInGuard], (guard: NotLoggedInGuard) => {
    expect(guard)
      .toBeTruthy();
  }));

  it('should return route', inject([NotLoggedInGuard], (guard: NotLoggedInGuard) => {
    authenticationServiceMock.hasValidAccessToken.mockReturnValue(true);

    const resultValue: boolean | UrlTree = guard.canActivate(null, null);

    expect(resultValue instanceof UrlTree)
      .toBeTruthy();
  }));

  it('should return true an not route', inject([NotLoggedInGuard], (guard: NotLoggedInGuard) => {
    authenticationServiceMock.hasValidAccessToken.mockReturnValue(false);

    const resultValue: boolean | UrlTree = guard.canActivate(null, null);

    expect(resultValue)
      .toBeTruthy();
  }));
});
