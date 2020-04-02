import { Injectable } from '@angular/core';
import { AuthenticationService } from '../authentication.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { AuthTypeHandlerBase } from './auth-type-handler-base';

@Injectable()
export class AzureAuthTypeHandlerService implements AuthTypeHandlerBase {

  constructor(protected oAuthService: OAuthService) {
  }

  async startLoginProcedure(authenticationService: AuthenticationService): Promise<boolean> {
    const loginResult: Promise<boolean> = authenticationService.login();

    await loginResult
      .then(_ => {
        if (authenticationService.hasValidAccessToken() && !authenticationService.silentRefreshActivated) {
          this.setupSilentRefresh();
          authenticationService.silentRefreshActivated = true;
        }
      });

    return loginResult;
  }

  setupSilentRefresh(): void {
    this.oAuthService.setupAutomaticSilentRefresh();
  }
}
