import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { Router } from '@angular/router';
import { Consts } from '../../../../shared/consts';
import { AuthTypeHandlerBase } from './auth-type-handler-base';
import { AuthenticationService } from '../authentication.service';

@Injectable()
export class LocalAuthTypeHandlerService implements AuthTypeHandlerBase {

  constructor(protected oAuthService: OAuthService,
              private router: Router) { }

  async startLoginProcedure(authenticationService: AuthenticationService): Promise<boolean> {
    return authenticationService.getRefreshToken()
      .then(() => {
        return true;
      })
      .catch(() => {
        this.router.navigate(['auth', 'login']);

        return false;
      });
  }

  setupSilentRefresh(authenticationService: AuthenticationService): void {
    const expirationTime: number = this.oAuthService.getAccessTokenExpiration();
    const now: number = Date.now();
    const duration: number = (expirationTime - now) * Consts.SILENT_REFRESH_MULTIPLIER;
    if (duration < 0) {
      this.safeRefreshToken(authenticationService);
    } else {
      setTimeout(() => {
        this.safeRefreshToken(authenticationService)
          .then(() => {
            this.setupSilentRefresh(authenticationService);
          });
      }, duration);
    }
  }

  private async safeRefreshToken(authenticationService: AuthenticationService): Promise<boolean> {
    return authenticationService.getRefreshToken()
      .then(() => {
        return true;
      })
      .catch(() => {
        this.router.navigate(['auth', 'login']);

        return false;
      });
  }
}
