import { TestBed } from '@angular/core/testing';

import { LocalAuthTypeHandlerService } from './local-auth-type-handler.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { Router } from '@angular/router';
import { FetchingService } from '../../../services/fetching.service';

const oAuthService: any = {
  getRefreshToken: jest.fn(),
  refreshToken: jest.fn(),
  getAccessTokenExpiration: jest.fn(),
  fetchTokenUsingPasswordFlow: jest.fn()
};
const router: any = {
  navigate: jest.fn()
};
const fetchingService: any = {
  refetchAll: jest.fn()
};

describe('LocalAuthTypeHandlerService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      LocalAuthTypeHandlerService,
      { provide: OAuthService, useValue: oAuthService },
      { provide: Router, useValue: router },
      { provide: FetchingService, useValue: fetchingService }
    ]
  }));

  beforeEach(() => {
    oAuthService.getRefreshToken.mockReset();
    oAuthService.getRefreshToken.mockReturnValue(null);
    oAuthService.refreshToken.mockReset();
    oAuthService.refreshToken.mockReturnValue(null);
    oAuthService.getAccessTokenExpiration.mockReset();
    oAuthService.getAccessTokenExpiration.mockReturnValue(0);
    oAuthService.fetchTokenUsingPasswordFlow.mockReset();
    oAuthService.fetchTokenUsingPasswordFlow.mockReturnValue(new Promise<object>(resolve => resolve()));

    router.navigate.mockReset();

    fetchingService.refetchAll.mockReset();
  });

  it('should be created', () => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);

    expect(service)
      .toBeTruthy();
  });

  it('startLoginProcedure, redirects when there is no refresh token', done => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);

    oAuthService.getRefreshToken.mockReturnValue(null);

    service.startLoginProcedure()
      .then(() => {
        expect(router.navigate)
          .toHaveBeenCalledWith(['auth', 'login']);
        done();
      });
  });

  it('startLoginProcedure, returns false when there is no refresh token', done => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);

    oAuthService.getRefreshToken.mockReturnValue(null);

    service.startLoginProcedure()
      .then(value => {
        expect(value)
          .toBeFalsy();
        done();
      });
  });

  it('startLoginProcedure, does not redirect when there is a refresh token', done => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);

    oAuthService.getRefreshToken.mockReturnValue('i am a refresh token');
    oAuthService.refreshToken.mockReturnValue('i am a refresh token');

    service.startLoginProcedure()
      .then(() => {
        expect(router.navigate)
          .toHaveBeenCalledTimes(0);
        done();
      });

  });

  it('startLoginProcedure, returns true when there is a refresh token', done => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);

    oAuthService.getRefreshToken.mockReturnValue('i am a refresh token');
    oAuthService.refreshToken.mockReturnValue('i am a refresh token');

    service.startLoginProcedure()
      .then(value => {
        expect(value)
          .toBeTruthy();
        done();
      });
  });

  it('setupSilentRefresh refreshes directly when expiration duration is 0', () => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);

    oAuthService.getAccessTokenExpiration.mockReturnValue(Date.now() - 10);

    // A small trick to access private members.
    (service as any).safeRefreshToken = jest.fn();
    (service as any).safeRefreshToken
      .mockReset();

    service.setupSilentRefresh();

    expect((service as any).safeRefreshToken)
      .toHaveBeenCalledTimes(1);
  });

  it('setupSilentRefresh refreshes after expiration time', done => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);

    oAuthService.getAccessTokenExpiration.mockReturnValue(Date.now() + 1000);

    // A small trick to access private members.
    (service as any).safeRefreshToken = jest.fn();
    (service as any).safeRefreshToken
      .mockReset();
    (service as any).safeRefreshToken
      .mockReturnValue(new Promise((resolve => resolve())));

    service.setupSilentRefresh();

    setTimeout(() => {
      expect((service as any).safeRefreshToken)
        .toHaveBeenCalled();
      done();
    }, 750);
  });

  it('login fetches token using password flow', done => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);
    service.login('', '')
      .then(() => {
        expect(oAuthService.fetchTokenUsingPasswordFlow)
          .toHaveBeenCalled();
        done();
      });
  });

  it('login fetches token using email and password', done => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);

    const email: string = 'test@test.com';
    const password: string = '1234567';

    service.login(email, password)
      .then(() => {
        expect(oAuthService.fetchTokenUsingPasswordFlow)
          .toHaveBeenCalledWith(email, password);
        done();
      });
  });

  it('login refetches all fetching Services', done => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);

    service.login('', '')
      .then(() => {
        expect(fetchingService.refetchAll)
          .toHaveBeenCalled();
        done();
      });
  });

  it('login sets up silent refresh', done => {
    const service: LocalAuthTypeHandlerService = TestBed.get(LocalAuthTypeHandlerService);

    service.setupSilentRefresh = jest.fn();
    (service.setupSilentRefresh as any).mockReset();

    service.login('', '')
      .then(() => {
        expect(service.setupSilentRefresh)
          .toHaveBeenCalled();
        done();
      });
  });

});
