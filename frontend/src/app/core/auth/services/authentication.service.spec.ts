import { TestBed } from '@angular/core/testing';
import { AuthenticationService } from './authentication.service';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';
import { OAuthFrontendDetailsService } from './o-auth-frontend-details.service';
import { AuthTypeHandlerFactoryService } from './auth-type-handler/auth-type-handler-factory.service';
import { of } from 'rxjs';

const oAuthService: any = {
  setStorage: jest.fn(),
  configure: jest.fn(),
  hasValidAccessToken: jest.fn(),
  logOut: jest.fn()
};

const oAuthFrontendDetailsService: any = {
  getAuthConfig$: jest.fn()
};

const authTypeHandlerFactoryService: any = {
  getAuthTypeHandler: jest.fn()
};

const authTypeHandler: any = {
  afterConfigured: jest.fn(),
  startLoginProcedure: jest.fn(),
  login: jest.fn()
};

const authConfig: AuthConfig = {
  clientId: 'testing',
  dummyClientSecret: 'testingSecret'
};

describe('AuthenticationService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      AuthenticationService,
      { provide: OAuthService, useValue: oAuthService },
      { provide: OAuthFrontendDetailsService, useValue: oAuthFrontendDetailsService },
      { provide: AuthTypeHandlerFactoryService, useValue: authTypeHandlerFactoryService }
    ]
  }));

  beforeEach(() => {
    authTypeHandlerFactoryService.getAuthTypeHandler.mockReset();
    authTypeHandlerFactoryService.getAuthTypeHandler.mockReturnValue(new Promise(resolve => resolve(authTypeHandler)));

    oAuthFrontendDetailsService.getAuthConfig$.mockReset();
    oAuthFrontendDetailsService.getAuthConfig$.mockReturnValue(of(authConfig));

    oAuthService.setStorage.mockReset();
    oAuthService.configure.mockReset();
    oAuthService.logOut.mockReset();
    oAuthService.hasValidAccessToken.mockReset();
    oAuthService.hasValidAccessToken.mockReturnValue(null);

    authTypeHandler.afterConfigured.mockReset();
    authTypeHandler.afterConfigured.mockReturnValue(new Promise(resolve => resolve()));
    authTypeHandler.startLoginProcedure.mockReset();
    authTypeHandler.login.mockReset();
  });

  it('should be created', () => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);

    expect(service)
      .toBeTruthy();
  });

  it('configure should set localStorage', done => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);

    service.configure()
      .then(() => {
        expect(oAuthService.setStorage)
          .toHaveBeenCalledWith(localStorage);
        done();
      });
  });

  it('configure configures oAuthService', done => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);

    service.configure()
      .then(() => {
        expect(oAuthService.configure)
          .toHaveBeenCalledWith(authConfig);
        done();
      });
  });

  it('configure calls afterConfigured() on AuthTypeHandler', done => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);

    service.configure()
      .then(() => {
        expect(authTypeHandler.afterConfigured)
          .toHaveBeenCalled();
        done();
      });
  });

  it('redirectToLoginProvider calls startLoginProcedure on AuthTypeHandler', done => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);

    service.redirectToLoginProvider()
      .then(() => {
        expect(authTypeHandler.startLoginProcedure)
          .toHaveBeenCalled();
        done();
      });
  });

  it('login calls login on AuthTypeHandler', done => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);

    const username: string = 'test@test.com';
    const password: string = '1234567';

    service.login(username, password)
      .then(() => {
        expect(authTypeHandler.login)
          .toHaveBeenCalledWith(username, password);
        done();
      });
  });

  it('hasValidAccessToken, returns true when there is an access token', () => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);
    oAuthService.hasValidAccessToken.mockReturnValue(true);

    expect(service.hasValidAccessToken())
      .toBeTruthy();
  });

  it('logout logs out', () => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);

    service.logout();

    expect(oAuthService.logOut)
      .toHaveBeenCalled();
  });
});
