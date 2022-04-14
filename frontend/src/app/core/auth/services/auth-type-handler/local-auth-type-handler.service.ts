import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { Router } from '@angular/router';
import { Consts } from '../../../../shared/consts';
import { AuthTypeHandlerBase } from './auth-type-handler-base';
import { FetchingService } from '../../../services/fetching.service';

@Injectable()
export class LocalAuthTypeHandlerService implements AuthTypeHandlerBase {
  private silentRefreshActivated: boolean = false;

  constructor(protected oAuthService: OAuthService) {
  }

  async startLoginProcedure(): Promise<boolean> {
    const loginResult: Promise<boolean> = this.login();

    await loginResult
      .then(_ => {
        if (this.oAuthService.hasValidAccessToken() && !this.silentRefreshActivated) {
          this.setupSilentRefresh();
          this.silentRefreshActivated = true;
        }
      });

    return loginResult;
  }

  setupSilentRefresh(): void {
    this.oAuthService.setupAutomaticSilentRefresh();
  }

  async afterConfigured(): Promise<any> {
    return this.startLoginProcedure();
  }

  async login(): Promise<boolean> {
    return this.oAuthService.loadDiscoveryDocumentAndLogin();
  }
}
