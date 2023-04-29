import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authCodeFlowConfig } from '../auth-flow-code-config';
import { filter, take } from 'rxjs/operators';
import { FetchingService } from '../../services/fetching.service';
import { ReplaySubject } from 'rxjs';

@Injectable()
export class AuthenticationService {
  path: string;
  private $isUserLoggedIn: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);

  constructor(
    private oAuthService: OAuthService,
    private fetchingService: FetchingService,
    ) {
    this.$isUserLoggedIn.next(false); // user is not logged in on application start by default
    this.configure();
  }

  configure() {
    this.oAuthService.configure(authCodeFlowConfig);
    this.oAuthService.setupAutomaticSilentRefresh();
    this.onFirstTokenExecuteRefetch();
  }

  getAccessToken(): string {
    return this.oAuthService.getAccessToken();
  }

  login() {
    this.oAuthService.initLoginFlow();
  }

  hasValidAccessToken(): boolean {
    return this.oAuthService.hasValidAccessToken(); // TODO fix Auth
  }

  logout(): void {
    // this.oAuthService.logOut(); TODO fix auth
  }

  isUserLoggedIn(): boolean {
    return this.oAuthService.hasValidAccessToken() && this.oAuthService.hasValidIdToken();
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
        ])
      )
    ).subscribe(() => {
      this.$isUserLoggedIn.next(false);
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
