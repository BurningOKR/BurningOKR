import { Injectable } from '@angular/core';
import { AuthConfig, JwksValidationHandler, OAuthService } from 'angular-oauth2-oidc';
import { OAuthFrontendDetailsService } from './o-auth-frontend-details.service';
import { AuthTypeHandlerBase } from './auth-type-handler/auth-type-handler-base';
import { AuthTypeHandlerFactoryService } from './auth-type-handler/auth-type-handler-factory.service';

@Injectable()
export class AuthenticationService {

  authTypeHandler: Promise<AuthTypeHandlerBase>;

  constructor(protected oAuthService: OAuthService,
              private oAuthDetails: OAuthFrontendDetailsService,
              private authTypeHandlerFactoryService: AuthTypeHandlerFactoryService) {

    this.authTypeHandler = this.authTypeHandlerFactoryService.getAuthTypeHandler();

  }

  async configure(): Promise<AuthConfig> {
    const authTypeHandlerBase: AuthTypeHandlerBase = await this.authTypeHandler;

    return new Promise(resolve => {
      this.oAuthDetails.getAuthConfig$()
        .subscribe((authConfig: AuthConfig) => {
          this.oAuthService.configure(authConfig);
          this.oAuthService.setStorage(localStorage);
          this.oAuthService.tokenValidationHandler = new JwksValidationHandler();
          this.oAuthService.redirectUri = `${window.location.origin}`;

          authTypeHandlerBase.afterConfigured()
            .then(() => resolve(authConfig));

        });
    });
  }

  async redirectToLoginProvider(): Promise<boolean> {
    const authTypeHandler: AuthTypeHandlerBase = await this.authTypeHandler;

    return authTypeHandler.startLoginProcedure();
  }

  async login(email: string, password: string): Promise<any> {
    const authTypeHandler: AuthTypeHandlerBase = await this.authTypeHandler;

    return authTypeHandler.login(email, password);
  }

  hasValidAccessToken(): boolean {
    return this.oAuthService.hasValidAccessToken();
  }

  logout(): void {
    this.oAuthService.logOut();
  }
}
