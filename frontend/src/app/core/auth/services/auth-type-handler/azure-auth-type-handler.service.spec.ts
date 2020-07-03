import { TestBed } from '@angular/core/testing';
import { AzureAuthTypeHandlerService } from './azure-auth-type-handler.service';
import { OAuthService } from 'angular-oauth2-oidc';

const oAuthServiceMock: any = {
  loadDiscoveryDocumentAndLogin: jest.fn(),
  hasValidAccessToken: jest.fn(),
  setupAutomaticSilentRefresh: jest.fn()
};

let service: AzureAuthTypeHandlerService;

describe('AzureAuthTypeHandlerService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      AzureAuthTypeHandlerService,
      {provide: OAuthService, useValue: oAuthServiceMock}
    ]
  }));

  beforeEach(() => {
    service = TestBed.get(AzureAuthTypeHandlerService);

    oAuthServiceMock.loadDiscoveryDocumentAndLogin.mockReset();
    oAuthServiceMock.hasValidAccessToken.mockReset();
    oAuthServiceMock.setupAutomaticSilentRefresh.mockReset();

    // oAuthServiceMock.setupAutomaticSilentRefresh.mockReturnValue();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('should start login procedure', done => {
    oAuthServiceMock.loadDiscoveryDocumentAndLogin.mockReturnValue(new Promise(resolve => resolve(true)));

    service.startLoginProcedure()
      .then(loginResult => {
        expect(loginResult)
          .toBeTruthy();
        done();
      });
  });

  it('should not start login procedure', done => {
    oAuthServiceMock.loadDiscoveryDocumentAndLogin.mockReturnValue(new Promise(resolve => resolve(false)));

    service.startLoginProcedure()
      .then(loginResult => {
        expect(loginResult)
          .toBeFalsy();
        done();
      });
  });

  it('should start login procedure with valid access token and silentRefreshActivated false', done => {
    oAuthServiceMock.hasValidAccessToken.mockReturnValue(true);

    service.startLoginProcedure()
      .then(() => {
        expect((service as any).silentRefreshActivated)
          .toBeTruthy();
        expect(oAuthServiceMock.setupAutomaticSilentRefresh)
          .toHaveBeenCalled();
        done();
      });

  });

  it('should start login procedure with invalid access token and silentRefreshActivated false', done => {
    oAuthServiceMock.hasValidAccessToken.mockReturnValue(false);

    service.startLoginProcedure()
      .then(() => {
        expect((service as any).silentRefreshActivated)
          .toBeFalsy();
        expect(oAuthServiceMock.setupAutomaticSilentRefresh)
          .toHaveBeenCalledTimes(0);
        done();
      });

  });

  it('should start login procedure with valid access token and silentRefreshActivated true', done => {
    oAuthServiceMock.hasValidAccessToken.mockReturnValue(true);
    (service as any).silentRefreshActivated = true;

    service.startLoginProcedure()
      .then(() => {
        expect((service as any).silentRefreshActivated)
          .toBeTruthy();
        expect(oAuthServiceMock.setupAutomaticSilentRefresh)
          .toHaveBeenCalledTimes(0);
        done();
      });

  });

  it('should start login procedure with invalid access token and silentRefreshActivated true', done => {
    oAuthServiceMock.hasValidAccessToken.mockReturnValue(false);
    (service as any).silentRefreshActivated = true;

    service.startLoginProcedure()
      .then(() => {
        expect((service as any).silentRefreshActivated)
          .toBeTruthy();
        expect(oAuthServiceMock.setupAutomaticSilentRefresh)
          .toHaveBeenCalledTimes(0);
        done();
      });

  });

  it('should setup silent refresh', () => {
    service.setupSilentRefresh();

    expect(oAuthServiceMock.setupAutomaticSilentRefresh)
      .toHaveBeenCalled();
  });

  it('afterConfigured should start login procedure', done => {
    oAuthServiceMock.loadDiscoveryDocumentAndLogin.mockReturnValue(new Promise(resolve => resolve(true)));

    service.afterConfigured()
      .then(loginResult => {
        expect(loginResult)
          .toBeTruthy();
        done();
      });
  });

  it('afterConfigured should not start login procedure', done => {
    oAuthServiceMock.loadDiscoveryDocumentAndLogin.mockReturnValue(new Promise(resolve => resolve(false)));

    service.afterConfigured()
      .then(loginResult => {
        expect(loginResult)
          .toBeFalsy();
        done();
      });
  });

  it('afterConfigured should start login procedure with valid access token and silentRefreshActivated false', done => {
    oAuthServiceMock.hasValidAccessToken.mockReturnValue(true);

    service.startLoginProcedure()
      .then(() => {
        expect((service as any).silentRefreshActivated)
          .toBeTruthy();
        expect(oAuthServiceMock.setupAutomaticSilentRefresh)
          .toHaveBeenCalled();
        done();
      });
  });

  it('afterConfigured should start login procedure with invalid access token and silentRefreshActivated false', done => {
    oAuthServiceMock.hasValidAccessToken.mockReturnValue(false);

    service.afterConfigured();

    service.startLoginProcedure()
      .then(() => {
        expect((service as any).silentRefreshActivated)
          .toBeFalsy();
        expect(oAuthServiceMock.setupAutomaticSilentRefresh)
          .toHaveBeenCalledTimes(0);
        done();
      });
  });

  it('afterConfigured should start login procedure with valid access token and silentRefreshActivated true', done => {
    oAuthServiceMock.hasValidAccessToken.mockReturnValue(true);
    (service as any).silentRefreshActivated = true;

    service.startLoginProcedure()
      .then(() => {
        expect((service as any).silentRefreshActivated)
          .toBeTruthy();
        expect(oAuthServiceMock.setupAutomaticSilentRefresh)
          .toHaveBeenCalledTimes(0);
        done();
      });
  });

  it('afterConfigured should start login procedure with invalid access token and silentRefreshActivated true', done => {
    oAuthServiceMock.hasValidAccessToken.mockReturnValue(false);
    (service as any).silentRefreshActivated = true;

    service.startLoginProcedure()
      .then(() => {
        expect((service as any).silentRefreshActivated)
          .toBeTruthy();
        expect(oAuthServiceMock.setupAutomaticSilentRefresh)
          .toHaveBeenCalledTimes(0);
        done();
      });
  });

  it('should login', () => {
    service.login();

    expect(oAuthServiceMock.loadDiscoveryDocumentAndLogin)
      .toHaveBeenCalled();
  });
});
