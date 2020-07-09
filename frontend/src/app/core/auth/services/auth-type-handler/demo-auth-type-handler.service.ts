import { Injectable } from '@angular/core';
import { LocalAuthTypeHandlerService } from './local-auth-type-handler.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { FetchingService } from '../../../services/fetching.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class DemoAuthTypeHandlerService extends LocalAuthTypeHandlerService {

  private EMAIL: string = 'iwant@burningokr';
  private PASSWORD: string = 'Passwort';

  constructor(oAuthService: OAuthService,
              router: Router,
              fetchingService: FetchingService) {
    super(oAuthService, router, fetchingService);
  }

  async startLoginProcedure(): Promise<boolean> {
    return this.getRefreshToken()
      .then(() => {
        return true;
      })
      .catch(() => {
        this.login(this.EMAIL, this.PASSWORD)
          .then(() => this.router.navigate(['/landingpage']));

        return false;
      });
  }
}
