import { TestBed } from '@angular/core/testing';

import { LocalAuthenticationService } from './local-authentication.service';
import { Router } from '@angular/router';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';
import { OAuthFrontendDetailsService } from './o-auth-frontend-details.service';
import { FetchingService } from '../../services/fetching.service';
import { of } from 'rxjs';

const router: any = {
  navigate: jest.fn()
};

const oAuthService: any = {
  configure: jest.fn(),
  setStorage: jest.fn(),
  hasValidAccessToken: jest.fn(),
  logOut: jest.fn(),
  fetchTokenUsingPasswordFlow: jest.fn(),
  getAccessTokenExpiration: jest.fn(),
  getRefreshToken: jest.fn(),
  refreshToken: jest.fn()
};

const oAuthFrontendDetailsService: any = {
  getAuthConfig$: jest.fn()
};

const fetchingService: any = {
  refetchAll: jest.fn()
};

const authConfig: AuthConfig = {
  clientId: 'test',
  dummyClientSecret: 'test123'
};

describe('LocalAuthenticationService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: OAuthService, useValue: oAuthService },
      { provide: OAuthFrontendDetailsService, useValue: oAuthFrontendDetailsService },
      { provide: FetchingService, useValue: fetchingService },
      { provide: Router, useValue: router }
    ]
  }));

  beforeEach(() => {
    oAuthService.configure.mockReset();
    oAuthService.setStorage.mockReset();
    oAuthService.logOut.mockReset();
    oAuthService.hasValidAccessToken.mockReset();
    oAuthService.hasValidAccessToken.mockReturnValue(true);
    oAuthService.fetchTokenUsingPasswordFlow.mockReset();
    oAuthService.fetchTokenUsingPasswordFlow.mockReturnValue(new Promise<object>(resolve => resolve()));
    oAuthService.getAccessTokenExpiration.mockReset();
    oAuthService.getAccessTokenExpiration.mockReturnValue(20);
    oAuthService.getRefreshToken.mockReset();
    oAuthService.getRefreshToken.mockReturnValue(null);
    oAuthService.refreshToken.mockReset();
    oAuthService.refreshToken.mockReturnValue(null);
    oAuthService.getAccessTokenExpiration.mockReset();
    oAuthService.getAccessTokenExpiration.mockReturnValue(Date.now());

    oAuthFrontendDetailsService.getAuthConfig$.mockReset();
    oAuthFrontendDetailsService.getAuthConfig$.mockReturnValue(of(authConfig));

    fetchingService.refetchAll.mockReset();

    router.navigate.mockReset();
  });

  // ----- Tests for Abstract Super Class ----- //

  it('should be created', () => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);
    expect(service)
      .toBeTruthy();
  });

  it('configure() configures oAuthService', done => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

    service.configure()
      .then(() => {
        expect(oAuthService.configure)
          .toHaveBeenCalledWith(authConfig);
        done();
      });
  });

  it('configure() configures oAuthService to use LocalStorage', done => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

    service.configure()
      .then(() => {
        expect(oAuthService.setStorage)
          .toHaveBeenCalledWith(localStorage);
        done();
      });
  });

  it('has valid access token, returns true when there is an access token', () => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);
    oAuthService.hasValidAccessToken.mockReturnValue(true);

    expect(service.hasValidAccessToken())
      .toBeTruthy();
  });

  it('has valid access token, returns false when there is no access token', () => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);
    oAuthService.hasValidAccessToken.mockReturnValue(false);

    expect(service.hasValidAccessToken())
      .toBeFalsy();
  });

  it('logout logs out', () => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

    service.logout();

    expect(oAuthService.logOut)
      .toHaveBeenCalled();
  });

  // ----- Tests for LocalAuthenticationService ----- //

  it('login fetches token using password flow', done => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);
    service.login()
      .then(() => {
        expect(oAuthService.fetchTokenUsingPasswordFlow)
          .toHaveBeenCalled();
        done();
      });
  });

  it('login fetches token using email and password', done => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

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
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

    service.login()
      .then(() => {
        expect(fetchingService.refetchAll)
          .toHaveBeenCalled();
        done();
      });
  });

  it('login sets up silent refresh', done => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

    service.setupSilentRefresh = jest.fn();
    (service.setupSilentRefresh as any).mockReset();

    service.login()
      .then(() => {
        expect(service.setupSilentRefresh)
          .toHaveBeenCalled();
        done();
      });
  });

  it('redirect to login provider, redirects when there is no refresh token', done => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

    oAuthService.getRefreshToken.mockReturnValue(null);

    service.redirectToLoginProvider()
      .then(() => {
        expect(router.navigate)
          .toHaveBeenCalledWith(['auth', 'login']);
        done();
      });

  });

  it('redirect to login provider, returns false when there is no refresh token', done => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

    oAuthService.getRefreshToken.mockReturnValue(null);

    service.redirectToLoginProvider()
      .then(value => {
        expect(value)
          .toBeFalsy();
        done();
      });
  });

  it('redirect to login provider, does not redirect when there is a refresh token', done => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

    oAuthService.getRefreshToken.mockReturnValue('i am a refresh token');
    oAuthService.refreshToken.mockReturnValue('i am a refresh token');

    service.redirectToLoginProvider()
      .then(() => {
        expect(router.navigate)
          .toHaveBeenCalledTimes(0);
        done();
      });

  });

  it('redirect to login provider, returns true when there is a refresh token', done => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

    oAuthService.getRefreshToken.mockReturnValue('i am a refresh token');
    oAuthService.refreshToken.mockReturnValue('i am a refresh token');

    service.redirectToLoginProvider()
      .then(value => {
        expect(value)
          .toBeTruthy();
        done();
      });
  });

  it('setupSilentRefresh refreshes directly when expiration duration is 0', () => {
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

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
    const service: LocalAuthenticationService = TestBed.get(LocalAuthenticationService);

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
});
