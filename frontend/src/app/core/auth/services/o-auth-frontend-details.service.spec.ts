import { TestBed } from '@angular/core/testing';
import { OAuthFrontendDetailsService } from './o-auth-frontend-details.service';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { OAuthFrontendDetails } from '../../../shared/model/api/o-auth-frontend-details';
import { AuthConfig } from 'angular-oauth2-oidc';
import { Consts } from '../../../shared/consts';

const httpClient: any = {
  get: jest.fn()
};

let testOAuthFrontendDetails: OAuthFrontendDetails;

describe('OAuthFrontendDetailsService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      OAuthFrontendDetailsService,
      { provide: HttpClient, useValue: httpClient }
    ]
  }));

  beforeEach(() => {
    testOAuthFrontendDetails = {
      authType: 'local',
      clientId: 'testClientId',
      dummyClientSecret: 'clientSecret',
      issuer: 'issuer',
      oidc: true,
      redirectUri: 'burningokr.com',
      requireHttps: false,
      responseType: 'a response type',
      scope: 'USER',
      showDebugInformation: false,
      silentRefreshRedirectUri: 'burningokr.com',
      strictDiscoveryDocumentValidation: false,
      tokenEndpoint: 'a token endpoint',
      useHttpBasicAuth: true
    };

    httpClient.get.mockReset();
    httpClient.get.mockReturnValue(of(testOAuthFrontendDetails));
  });

  it('should be created', () => {
    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    expect(service)
      .toBeTruthy();
  });

  it('should reloadOAuthFrontendDetails on init', () => {
    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    expect(httpClient.get)
      .toHaveBeenCalled();
  });

  it('getAuthConfig maps OAuthFrontendDetails to AuthConfig', done => {
    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getAuthConfig$()
      .subscribe((authConfig: AuthConfig) => {
        expect(authConfig.clientId)
          .toBe(testOAuthFrontendDetails.clientId);
        expect(authConfig.dummyClientSecret)
          .toBe(testOAuthFrontendDetails.dummyClientSecret);
        expect(authConfig.issuer)
          .toBe(testOAuthFrontendDetails.issuer);
        expect(authConfig.oidc)
          .toBe(testOAuthFrontendDetails.oidc);
        expect(authConfig.redirectUri)
          .toBe(testOAuthFrontendDetails.redirectUri);
        expect(authConfig.responseType)
          .toBe(testOAuthFrontendDetails.responseType);
        expect(authConfig.scope)
          .toBe(testOAuthFrontendDetails.scope);
        expect(authConfig.showDebugInformation)
          .toBe(testOAuthFrontendDetails.showDebugInformation);
        expect(authConfig.silentRefreshRedirectUri)
          .toBe(testOAuthFrontendDetails.silentRefreshRedirectUri);
        expect(authConfig.tokenEndpoint)
          .toBe(testOAuthFrontendDetails.tokenEndpoint);
        expect(authConfig.useHttpBasicAuth)
          .toBe(testOAuthFrontendDetails.useHttpBasicAuth);
        expect(authConfig.skipIssuerCheck)
          .toBeTruthy();
        expect(authConfig.requireHttps)
          .toBe(testOAuthFrontendDetails.requireHttps);
        expect(authConfig.strictDiscoveryDocumentValidation)
          .toBe(testOAuthFrontendDetails.strictDiscoveryDocumentValidation);
        done();
      });
  });

  it('getAuthConfig does not emit on falsy (null) value', done => {
    httpClient.get.mockReturnValue(of(null));

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getAuthConfig$()
      .subscribe((authConfig: AuthConfig) => {
        fail('getAuthConfig$ emitted a value');
        done();
      });

    // when getAuthConfig does not emit after 1 seconds, we can assume that it will never emit...
    setTimeout(() => {
      done();
    }, 1000);
  });

  it('getAuthConfig does not emit on falsy (undefined) value', done => {
    httpClient.get.mockReturnValue(of(undefined));

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getAuthConfig$()
      .subscribe((authConfig: AuthConfig) => {
        fail('getAuthConfig$ emitted a value');
        done();
      });

    // when getAuthConfig does not emit after 1 seconds, we can assume that it will never emit...
    setTimeout(() => {
      done();
    }, 1000);
  });

  it('getOAuthFrontendDetails returns oAuthFrontendDetails', done => {
    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getOAuthFrontendDetails$()
      .subscribe((details: OAuthFrontendDetails) => {
        expect(details)
          .toEqual(testOAuthFrontendDetails);
        done();
      });
  });

  it('getOAuthFrontendDetails does not emit on falsy (null) value', done => {
    httpClient.get.mockReturnValue(of(null));

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getOAuthFrontendDetails$()
      .subscribe((details: OAuthFrontendDetails) => {
        fail('getOAuthFrontendDetails emitted a value');
        done();
      });

    // when getOAuthFrontendDetails does not emit after 1 seconds, we can assume that it will never emit...
    setTimeout(() => {
      done();
    }, 1000);
  });

  it('getOAuthFrontendDetails does not emit on falsy (undefined) value', done => {
    httpClient.get.mockReturnValue(of(undefined));

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getOAuthFrontendDetails$()
      .subscribe((details: OAuthFrontendDetails) => {
        fail('getOAuthFrontendDetails emitted a value');
        done();
      });

    // when getOAuthFrontendDetails does not emit after 3 seconds, we can assume that it will never emit...
    setTimeout(() => {
      done();
    }, 3000);
  });

  it('getAuthType returns azure for azure authType', done => {
    testOAuthFrontendDetails.authType = Consts.AUTHTYPE_AZURE;

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getAuthType$()
      .subscribe((authType: string) => {
        expect(authType)
          .toBe(Consts.AUTHTYPE_AZURE);
        done();
      });
  });

  it('getAuthType returns local for local authType', done => {
    testOAuthFrontendDetails.authType = Consts.AUTHTYPE_LOCAL;

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getAuthType$()
      .subscribe((authType: string) => {
        expect(authType)
          .toBe(Consts.AUTHTYPE_LOCAL);
        done();
      });
  });

  it('getAuthType returns somethingElse for somethingElse authType', done => {
    testOAuthFrontendDetails.authType = 'somethingElse';

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getAuthType$()
      .subscribe((authType: string) => {
        expect(authType)
          .toBe('somethingElse');
        done();
      });
  });

  it('getAuthType does not emit on falsy (null) value', done => {
    httpClient.get.mockReturnValue(of(null));

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getAuthType$()
      .subscribe(() => {
        fail('getAuthType emitted a value');
        done();
      });

    // when getAuthType does not emit after 1 seconds, we can assume that it will never emit...
    setTimeout(() => {
      done();
    }, 1000);
  });

  it('getAuthType does not emit on falsy (undefined) value', done => {
    httpClient.get.mockReturnValue(of(undefined));

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.getAuthType$()
      .subscribe(() => {
        fail('getAuthType emitted a value');
        done();
      });

    // when getAuthType does not emit after 1 seconds, we can assume that it will never emit...
    setTimeout(() => {
      done();
    }, 1000);
  });

  it('isLocalAuthType returns true for local auth type', done => {
    testOAuthFrontendDetails.authType = Consts.AUTHTYPE_LOCAL;

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.isLocalAuthType$()
      .subscribe((isLocal: boolean) => {
        expect(isLocal)
          .toBeTruthy();
        done();
      });
  });

  it('isLocalAuthType returns false for azure auth type', done => {
    testOAuthFrontendDetails.authType = Consts.AUTHTYPE_AZURE;

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.isLocalAuthType$()
      .subscribe((isLocal: boolean) => {
        expect(isLocal)
          .toBeFalsy();
        done();
      });
  });

  it('isLocalAuthType returns false for somethingElse', done => {
    testOAuthFrontendDetails.authType = 'somethingElse';

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.isLocalAuthType$()
      .subscribe((isLocal: boolean) => {
        expect(isLocal)
          .toBeFalsy();
        done();
      });
  });

  it('isLocalAuthType does not emit on falsy (undefined) value', done => {
    httpClient.get.mockReturnValue(of(undefined));

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.isLocalAuthType$()
      .subscribe(() => {
        fail('getAuthType emitted a value');
        done();
      });

    // when isLocalAuthType$ does not emit after 1 seconds, we can assume that it will never emit...
    setTimeout(() => {
      done();
    }, 1000);
  });

  it('isLocalAuthType does not emit on falsy (null) value', done => {
    httpClient.get.mockReturnValue(of(null));

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.isLocalAuthType$()
      .subscribe(() => {
        fail('getAuthType emitted a value');
        done();
      });

    // when isLocalAuthType$ does not emit after 1 seconds, we can assume that it will never emit...
    setTimeout(() => {
      done();
    }, 1000);
  });

  it('isAzureAuthType returns false for local auth type', done => {
    testOAuthFrontendDetails.authType = Consts.AUTHTYPE_LOCAL;

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.isAzureAuthType$()
      .subscribe((isLocal: boolean) => {
        expect(isLocal)
          .toBeFalsy();
        done();
      });
  });

  it('isAzureAuthType returns true for azure auth type', done => {
    testOAuthFrontendDetails.authType = Consts.AUTHTYPE_AZURE;

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.isAzureAuthType$()
      .subscribe((isLocal: boolean) => {
        expect(isLocal)
          .toBeTruthy();
        done();
      });
  });

  it('isAzureAuthType returns false for somethingElse', done => {
    testOAuthFrontendDetails.authType = 'somethingElse';

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.isAzureAuthType$()
      .subscribe((isLocal: boolean) => {
        expect(isLocal)
          .toBeFalsy();
        done();
      });
  });

  it('isAzureAuthType does not emit on falsy (undefined) value', done => {
    httpClient.get.mockReturnValue(of(undefined));

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.isAzureAuthType$()
      .subscribe(() => {
        fail('getAuthType emitted a value');
        done();
      });

    // when isAzureAuthType$ does not emit after 1 seconds, we can assume that it will never emit...
    setTimeout(() => {
      done();
    }, 1000);
  });

  it('isAzureAuthType does not emit on falsy (null) value', done => {
    httpClient.get.mockReturnValue(of(null));

    const service: OAuthFrontendDetailsService = TestBed.inject(OAuthFrontendDetailsService);

    service.isAzureAuthType$()
      .subscribe(() => {
        fail('getAuthType emitted a value');
        done();
      });

    // when isLocalAuthType$ does not emit after 1 seconds, we can assume that it will never emit...
    setTimeout(() => {
      done();
    }, 1000);
  });
});
