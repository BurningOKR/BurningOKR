import { Injectable } from '@angular/core';
import { BaseAuthenticationService } from './base-authentication.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { OAuthFrontendDetailsService } from './o-auth-frontend-details.service';
import { Consts } from '../../../shared/consts';

@Injectable({
  providedIn: 'root'
})
export class AzureAuthenticationService extends BaseAuthenticationService {

  private silentRefreshActivated = false;

  constructor(oAuthService: OAuthService,
              oAuthDetails: OAuthFrontendDetailsService) {
    super(oAuthService, oAuthDetails);
  }

  protected async afterConfigured(): Promise<any> {
      return this.redirectToLoginProvider();
  }

  async login(email?: string, password?: string): Promise<any> {
    return this.oAuthService.loadDiscoveryDocumentAndLogin();
  }

  async redirectToLoginProvider(): Promise<boolean> {
    const loginResult: Promise<boolean> = this.login();

    await loginResult
      .then(_ => {
        if (this.hasValidAccessToken() && !this.silentRefreshActivated) {
          this.setupSilentRefresh();
          this.silentRefreshActivated = true;
        }
      });

    return loginResult;
  }

  setupSilentRefresh(): void {
    this.oAuthService.setupAutomaticSilentRefresh();
  }
}
