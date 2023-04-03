import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authCodeFlowConfig } from '../auth-flow-code-config';
import { filter } from 'rxjs/operators';

@Injectable()
export class AuthenticationService {
  path: string;

  constructor(
    private oAuthService: OAuthService,
  ) {
  }

  configure() {
    console.log('AuthenticationService - running configure');
    this.oAuthService.configure(authCodeFlowConfig);
    this.oAuthService.setupAutomaticSilentRefresh();
    this.oAuthService.loadDiscoveryDocumentAndTryLogin().then(t => console.log(`AuthenticationService - load discovery-document: ${t}}`));
    this.oAuthService.events.pipe(
      filter((e: any) => {
        return e.type === 'token_received';
      }),
    ).subscribe(() => {
      console.log('New Token received');
    });
  }

  getAccessToken(): string {
    return this.oAuthService.getAccessToken();
  }

  // getPath(): string {
  //   return this.path;
  // }

  /**
   * Checks wether the user is logged in.
   * The user will be redirected to the login page of the current AuthenticationHandler, when they are not logged in.
   *
   * @returns true when the user is logged. False otherwise.
   */
  // async redirectToLoginProvider(): Promise<boolean> {
  //   const authTypeHandler: AuthTypeHandlerBase = await this.authTypeHandler;
  //
  //   return authTypeHandler.startLoginProcedure();
  // }

  login() {
    this.oAuthService.initLoginFlow();
  }

  hasValidAccessToken(): boolean {
    return this.oAuthService.hasValidAccessToken(); // TODO fix Auth
  }

  /**
   * Logs the user out.
   */
  logout(): void {
    // this.oAuthService.logOut();
  }
}
