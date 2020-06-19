import { Injectable } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { OAuthFrontendDetails } from '../../../shared/model/api/o-auth-frontend-details';
import { HttpClient } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';
import { AuthConfig } from 'angular-oauth2-oidc';
import { Consts } from '../../../shared/consts';

@Injectable()
export class OAuthFrontendDetailsService {
  private oAuthFrontendDetails$ = new ReplaySubject<OAuthFrontendDetails>(null);

  constructor(private httpClient: HttpClient) {
    this.reloadOAuthFrontendDetails();
  }

  reloadOAuthFrontendDetails(): void {
    this.oAuthFrontendDetails$.next(null);

    this.httpClient
      .get<OAuthFrontendDetails>(`${Consts.API_URL}oAuthFrontendDetails`)
      .subscribe(frontendDetails => {
        this.oAuthFrontendDetails$.next(frontendDetails);
      });
  }

  getOAuthFrontendDetails$(): Observable<OAuthFrontendDetails> {
    return this.oAuthFrontendDetails$.pipe(
      filter(v => !!v)
    );
  }

  getAuthType$(): Observable<string> {
    return this.getOAuthFrontendDetails$()
      .pipe(
        map(details => details.authType)
      );
  }

  getAuthConfig$(): Observable<AuthConfig> {
    return this.getOAuthFrontendDetails$()
      .pipe(
        map(details => {
          return {
            clientId: details.clientId,
            dummyClientSecret: details.dummyClientSecret,
            issuer: details.authType === 'local' ? `${location.origin}${details.issuer}` : details.issuer,
            oidc: details.oidc,
            redirectUri: details.redirectUri,
            responseType: details.responseType,
            scope: details.scope,
            showDebugInformation: details.showDebugInformation,
            silentRefreshRedirectUri: details.silentRefreshRedirectUri,
            tokenEndpoint: details.tokenEndpoint,
            useHttpBasicAuth: details.useHttpBasicAuth,
            skipIssuerCheck: true,
            requireHttps: details.requireHttps,
            strictDiscoveryDocumentValidation: details.strictDiscoveryDocumentValidation
          };
        })
      );
  }
}
