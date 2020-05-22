import { OAuthFrontendDetails } from '../model/api/o-auth-frontend-details';
import { Observable, of } from 'rxjs';
import { AuthConfig } from 'angular-oauth2-oidc';

export class OAuthFrontendDetailsServiceMock {

  reloadOAuthFrontendDetails(): void {
    return;
  }

  getOAuthFrontendDetails$(): Observable<OAuthFrontendDetails> {
    return of();
  }

  getAuthType$(): Observable<string> {
    return of();
  }

  getAuthConfig$(): Observable<AuthConfig> {
    return of();
  }
}
