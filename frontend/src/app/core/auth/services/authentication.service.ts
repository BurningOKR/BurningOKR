import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { AuthTypeHandlerBase } from './auth-type-handler/auth-type-handler-base';
import { authCodeFlowConfig } from '../auth-flow-code-config';

@Injectable()
export class AuthenticationService {

  authTypeHandler: Promise<AuthTypeHandlerBase>;
  path: string;

  constructor(
    private oAuthService: OAuthService,
  ) {
  }

  async configure(): Promise<void> {
    return new Promise(resolve => {
      this.oAuthService.configure(authCodeFlowConfig);
      this.oAuthService.setStorage(localStorage);
      this.oAuthService.loadDiscoveryDocumentAndLogin().then(t => {
        console.log(`AuthenticationService - loadDiscoveryDocument: ${t}`);
        resolve();
      });
      console.log('Test');
    });
  }

  getAccessToken(): string {
    return this.oAuthService.getAccessToken();
  }

  getPath(): string {
    return this.path;
  }

  /**
   * Checks wether the user is logged in.
   * The user will be redirected to the login page of the current AuthenticationHandler, when they are not logged in.
   *
   * @returns true when the user is logged. False otherwise.
   */
  async redirectToLoginProvider(): Promise<boolean> {
    const authTypeHandler: AuthTypeHandlerBase = await this.authTypeHandler;

    return authTypeHandler.startLoginProcedure();
  }

  login() {
    this.oAuthService.initCodeFlow();
  }

  hasValidAccessToken(): boolean {
    return true; // TODO fix Auth
  }

  /**
   * Logs the user out.
   */
  logout(): void {
    this.oAuthService.logOut();
  }
}
