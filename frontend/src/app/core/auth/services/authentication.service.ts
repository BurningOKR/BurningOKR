import { Injectable, Injector } from '@angular/core';
import { AuthConfig, JwksValidationHandler, OAuthService } from 'angular-oauth2-oidc';
import { OAuthFrontendDetailsService } from './o-auth-frontend-details.service';
import { map, shareReplay, take } from 'rxjs/operators';
import { AuthTypeHandlerBase } from './auth-type-handler/auth-type-handler-base';
import { AzureAuthTypeHandlerService } from './auth-type-handler/azure-auth-type-handler.service';
import { LocalAuthTypeHandlerService } from './auth-type-handler/local-auth-type-handler.service';
import { FetchingService } from '../../services/fetching.service';
import { Consts } from '../../../shared/consts';

@Injectable()
export class AuthenticationService {
  silentRefreshActivated: boolean = false;
  authType: string;

  authTypeHandler: Promise<AuthTypeHandlerBase>;

  constructor(protected oAuthService: OAuthService,
              private oAuthDetails: OAuthFrontendDetailsService,
              private injector: Injector,
              private fetchingService: FetchingService) {

    this.authTypeHandler = this.oAuthDetails.getAuthType$()
      .pipe(
        take(1),
        map(authType => {
          this.authType = authType;

          if (authType === Consts.AUTHTYPE_AZURE) {
            return this.injector.get(AzureAuthTypeHandlerService);
          } else {
            const localAuthTypeHandlerService: LocalAuthTypeHandlerService = this.injector.get(LocalAuthTypeHandlerService);

            if (this.hasValidAccessToken()) {
              localAuthTypeHandlerService.setupSilentRefresh(this);
            }

            return localAuthTypeHandlerService;
          }
        }),
        shareReplay()
      )
      .toPromise();
  }

  async configure(): Promise<AuthConfig> {
    return new Promise(resolve => {
      this.oAuthDetails.getAuthConfig$()
        .subscribe((authConfig: AuthConfig) => {
          this.oAuthService.configure(authConfig);
          this.oAuthService.setStorage(localStorage);
          this.oAuthService.tokenValidationHandler = new JwksValidationHandler();
          this.oAuthService.redirectUri = `${window.location.origin}`;

          this.startLoginProcedureIfAuthTypeIsAAD()
              .then(() => resolve(authConfig));

        });
    });
  }

  async startLoginProcedure(): Promise<boolean> {
    const authTypeHandler: AuthTypeHandlerBase = await this.authTypeHandler;

    return authTypeHandler.startLoginProcedure(this);
  }

  private async startLoginProcedureIfAuthTypeIsAAD(): Promise<boolean> {
    if (this.authType !== Consts.AUTHTYPE_LOCAL) {
      return this.startLoginProcedure();
    } else {
      return true;
    }
  }

  async loginLocalUser(email: string, password: string): Promise<object> {
    const authTypeHandler: AuthTypeHandlerBase = await this.authTypeHandler;

    return this.oAuthService.fetchTokenUsingPasswordFlow(email, password)
      .then(object => {
        authTypeHandler.setupSilentRefresh(this);

        this.fetchingService.refetchAll();

        return object;
      });
  }

  async getRefreshToken(): Promise<object> {
    if (!!this.oAuthService.getRefreshToken()) {
      return this.oAuthService.refreshToken();
    } else {
      return new Promise<object>((resolve, reject) => {
        reject();
      });
    }
  }

  hasValidAccessToken(): boolean {
    return this.oAuthService.hasValidAccessToken();
  }

  logout(): void {
    this.oAuthService.logOut();
  }

  async login(): Promise<boolean> {
    return this.oAuthService.loadDiscoveryDocumentAndLogin();
  }
}
