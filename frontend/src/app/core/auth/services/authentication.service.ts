import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { filter, take } from 'rxjs/operators';
import { FetchingService } from '../../services/fetching.service';
import { ReplaySubject } from 'rxjs';
import { OidcConfigurationService } from '../../../config/oidc/oidc-configuration.service';
import { AuthFlowConfig } from '../../../config/oidc/auth-flow-config';
import { OidcConfiguration } from '../../../config/oidc/oidc-configuration';

// TODO move magic strings to config file
@Injectable()
export class AuthenticationService {
  path: string;
  finishedInitialisation: Promise<boolean>;
  private isUserLoggedIn$: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);
  private initialized: boolean = false;

  constructor(
    private oAuthService: OAuthService,
    private fetchingService: FetchingService,
    private oidcConfigurationService: OidcConfigurationService,
  ) {
    this.isUserLoggedIn$.next(false); // user is not logged in on application start by default
    this.finishedInitialisation = new Promise<boolean>(resolve => {
      this.configure().then(() => {
        this.initialized = true;
        resolve(true);
      });
    });
  }

  async configure(): Promise<void> {
    const oidcConfiguration: OidcConfiguration = await this.oidcConfigurationService.getOidcConfiguration$();

    this.oAuthService.configure(this.getAuthFlowConfig(oidcConfiguration));
    this.oAuthService.setupAutomaticSilentRefresh();
    this.onFirstTokenExecuteRefetch();
    this.onTokenFailureSetLoggedInFalse();
  }

  getAuthFlowConfig(oidcConfig: OidcConfiguration): AuthFlowConfig {
    return {
      issuer: oidcConfig.issuerUri,
      redirectUri: `${window.location.origin}`,
      clientId: oidcConfig.clientId,
      responseType: 'code',
      scope: oidcConfig.scopes.join(' '),
      showDebugInformation: true, // TODO remove after azure ad is working
      strictDiscoveryDocumentValidation: oidcConfig.strictDiscoveryDocumentValidation,
    } as AuthFlowConfig;
  }

  getAccessToken(): string {
    return this.oAuthService.getAccessToken();
  }

  logout(): void {
    this.oAuthService.logOut();
  }

  isUserLoggedIn(): boolean {
    return this.oAuthService.hasValidAccessToken() && this.oAuthService.hasValidIdToken();
  }

  async waitForInitializationToFinish(): Promise<boolean> {
    return this.finishedInitialisation;
  }

  isInitialized(): boolean {
    return this.initialized;
  }

  async login(url: string): Promise<string | boolean> {
    await this.oAuthService.loadDiscoveryDocumentAndTryLogin();

    if (!this.isUserLoggedIn()) {
      this.writeRedirectUrlToLocalStorage(url);
      this.oAuthService.initCodeFlow();

      return false;
    }

    // retrieve target path
    return this.getRedirectUrlFromLocalStorage();
  }

  private writeRedirectUrlToLocalStorage(redirectUrl: string): void {
    localStorage.setItem('login_redirect', redirectUrl);
  }

  private getRedirectUrlFromLocalStorage(): string {
    const redirect: string = localStorage.getItem('login_redirect');
    localStorage.removeItem('login_redirect');

    return redirect;
  }

  private onFirstTokenExecuteRefetch(): void {
    this.oAuthService.events.pipe(
      filter(event => event.type === 'token_received'),
      take(1),
    ).subscribe(() => {
      this.fetchingService.refetchAll(this.isUserLoggedIn());
    });
  }

  private onTokenFailureSetLoggedInFalse(): void {
    this.oAuthService.events.pipe(
      filter(event => this.matchesAnyString(event.type, [
          'user_profile_load_error',
          'discovery_document_load_error',
          'discovery_document_validation_error',
          'token_error',
          'session_error',
          'logout',
        ]),
      ),
    ).subscribe(() => {
      this.isUserLoggedIn$.next(false);
    });
  }

  // TODO setLoggedIn after successful re-login

  private matchesAnyString(string: string, matchers: string[]): boolean {
    matchers.forEach(matcher => {
      if (string === matcher) {
        return true;
      }
    });

    return false;
  }
}
