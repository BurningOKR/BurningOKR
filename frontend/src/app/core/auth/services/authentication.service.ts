import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authCodeFlowConfig } from '../auth-flow-code-config';

@Injectable()
export class AuthenticationService {
  path: string;

  constructor(
    private oAuthService: OAuthService,
  ) {
    this.configure();
    this.oAuthService.setupAutomaticSilentRefresh();
  }

  configure() {
    console.log('AuthenticationService - running configure');
    this.oAuthService.configure(authCodeFlowConfig);
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
    // this.oAuthService.logOut();
  }
}
