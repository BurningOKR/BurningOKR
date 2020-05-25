import { inject, TestBed } from '@angular/core/testing';

import { LocalGuard } from './local.guard';
import { OAuthFrontendDetailsService } from '../services/o-auth-frontend-details.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { Consts } from '../../../shared/consts';

const oAuthDetailsMock: any = {
  getAuthType$: jest.fn()
};
const routerMock: any = {
  navigate: jest.fn(),
};

describe('LocalAuthGuard', () => {
  oAuthDetailsMock.getAuthType$.mockReset();
  routerMock.navigate.mockReset();

  oAuthDetailsMock.getAuthType$
    .mockReturnValueOnce(of(Consts.AUTHTYPE_AZURE));
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        LocalGuard,
        {provide: OAuthFrontendDetailsService, useValue: oAuthDetailsMock},
        {provide: Router, useValue: routerMock}
      ]
    })
      .compileComponents();
  });

  it('should create', inject([LocalGuard], (guard: LocalGuard) => {
    expect(guard)
      .toBeTruthy();
  }));

  it('should route for aad-auth ', inject([LocalGuard], (guard: LocalGuard, done) => {
    const returnValue: any = guard.canActivate(null, null);

    returnValue.subscribe(() => {
      expect(routerMock.navigate)
        .toHaveBeenCalled();
      done();
    });
  }));
});
