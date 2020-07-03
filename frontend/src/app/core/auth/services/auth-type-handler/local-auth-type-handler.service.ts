import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { Router } from '@angular/router';
import { Consts } from '../../../../shared/consts';
import { AuthTypeHandlerBase } from './auth-type-handler-base';
import { FetchingService } from '../../../services/fetching.service';

@Injectable()
export class LocalAuthTypeHandlerService implements AuthTypeHandlerBase {

  constructor(protected oAuthService: OAuthService,
              protected router: Router,
              private fetchingService: FetchingService) {
  }

  async startLoginProcedure(): Promise<boolean> {
    return this.getRefreshToken()
      .then(() => {
        return true;
      })
      .catch(() => {
        this.router.navigate(['auth', 'login']);

        return false;
      });
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

  async afterConfigured(): Promise<any> {
    return new Promise(resolve => {
      resolve();
    });
  }

  async login(email: string, password: string): Promise<object> {

    return this.oAuthService.fetchTokenUsingPasswordFlow(email, password)
      .then(object => {
        this.setupSilentRefresh();

        this.fetchingService.refetchAll();

        return object;
      });
  }

  protected async getRefreshToken(): Promise<object> {
    if (!!this.oAuthService.getRefreshToken()) {
      return this.oAuthService.refreshToken();
    } else {
      return new Promise<object>((resolve, reject) => {
        reject();
      });
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
}
