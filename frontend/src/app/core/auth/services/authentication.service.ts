import { Injectable } from '@angular/core';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';
import { AuthTypeHandlerBase } from './auth-type-handler/auth-type-handler-base';
import { authCodeFlowConfig } from '../auth-flow-code-config';
import { JwksValidationHandler } from 'angular-oauth2-oidc-jwks';

@Injectable()
export class AuthenticationService {

  authTypeHandler: Promise<AuthTypeHandlerBase>;
  path: string;

  constructor(
    protected oAuthService: OAuthService,
    // private oAuthDetails: OAuthFrontendDetailsService,
    // private authTypeHandlerFactoryService: AuthTypeHandlerFactoryService,
  ) {

    // this.authTypeHandler = this.authTypeHandlerFactoryService.getAuthTypeHandler();

  }

  /**
   * Configures the AuthenticationService with the OAuthDetails from the backend.
   * Should be called at application startup.
   *
   * @returns the AuthConfig
   */
  async configure(): Promise<AuthConfig> {
    return new Promise(() => null);
    // const authTypeHandlerBase: AuthTypeHandlerBase = await this.authTypeHandler;
    //
    // return new Promise(resolve => {
    //   this.oAuthDetails.getAuthConfig$()
    //     .subscribe((authConfig: AuthConfig) => {
    //       this.oAuthService.configure(authConfig);
    //       this.oAuthService.setStorage(localStorage);
    //       this.oAuthService.tokenValidationHandler = new JwksValidationHandler();
    //       this.oAuthService.redirectUri = `${window.location.origin}`;
    //       this.path = location.pathname;
    //       authTypeHandlerBase.afterConfigured()
    //         .then(() => resolve(authConfig));
    //
    //     });
    // });
  }

  initCodeFlow(): void {
    console.log('init code flow');
    this.oAuthService.configure(authCodeFlowConfig);
    this.oAuthService.setStorage(localStorage);
    this.oAuthService.tokenValidationHandler = new JwksValidationHandler();
    this.oAuthService.loadDiscoveryDocumentAndLogin().then(bool => console.log(`Login: ${bool}`));
    console.log(this.oAuthService.getAccessToken());
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

  /**
   * Logs the user in with the current AuthenticationService.
   *
   * @param email The email of the user
   * @param password The password of the user
   */
  async login(email?: string, password?: string): Promise<any> {
    const authTypeHandler: AuthTypeHandlerBase = await this.authTypeHandler;

    return authTypeHandler.login(email, password);
  }

  /**
   * Checks wether the user has a valid access token
   *
   * @returns true when the user has a valid access token. False otherwise.
   */
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
