import { Injectable } from '@angular/core';
import { BaseAuthenticationService } from './base-authentication.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { OAuthFrontendDetailsService } from './o-auth-frontend-details.service';
import { Router } from '@angular/router';
import { Consts } from '../../../shared/consts';
import { FetchingService } from '../../services/fetching.service';

@Injectable({
  providedIn: 'root'
})
export class LocalAuthenticationService extends BaseAuthenticationService {

  constructor(oAuthService: OAuthService,
              oAuthDetails: OAuthFrontendDetailsService,
              private router: Router,
              private fetchingService: FetchingService) {
    super(oAuthService, oAuthDetails);
  }

  protected async afterConfigured(): Promise<any> {
    return new Promise<void>(resolve => {
      resolve();
    });
  }

  async login(email?: string, password?: string): Promise<any> {
    return this.oAuthService.fetchTokenUsingPasswordFlow(email, password)
      .then(object => {
        this.setupSilentRefresh();

        this.fetchingService.refetchAll();

        return object;
      });
  }

  async redirectToLoginProvider(): Promise<boolean> {
    return this.safeRefreshToken();
  }

  setupSilentRefresh(): void {
    const expirationTime: number = this.oAuthService.getAccessTokenExpiration();
    const now: number = Date.now();
    const duration: number = (expirationTime - now) * Consts.SILENT_REFRESH_MULTIPLIER;
    if (duration < 0) {
      this.safeRefreshToken();
    } else {
      setTimeout(() => {
        this.safeRefreshToken()
          .then(() => {
            this.setupSilentRefresh();
          });
      }, duration);
    }
  }

  private async safeRefreshToken(): Promise<boolean> {
    return this.getRefreshToken()
      .then(() => {
        return true;
      })
      .catch(() => {
        this.router.navigate(['auth', 'login']);

        return false;
      });
  }

  private async getRefreshToken(): Promise<object> {
    if (!!this.oAuthService.getRefreshToken()) {
      return this.oAuthService.refreshToken();
    } else {
      return new Promise<object>((resolve, reject) => {
        reject();
      });
    }
  }
}
