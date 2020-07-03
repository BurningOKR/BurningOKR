import { AuthConfig, JwksValidationHandler, OAuthService } from 'angular-oauth2-oidc';
import { OAuthFrontendDetailsService } from './o-auth-frontend-details.service';

export abstract class BaseAuthenticationService {

  protected constructor(protected oAuthService: OAuthService,
                        protected oAuthDetails: OAuthFrontendDetailsService) {
  }

  protected abstract afterConfigured(): Promise<any>;

  abstract redirectToLoginProvider(): Promise<boolean>;
  abstract login(email?: string, password?: string): Promise<any>;
  abstract setupSilentRefresh(): void;

  async configure(): Promise<AuthConfig> {
    return new Promise(resolve => {
      this.oAuthDetails.getAuthConfig$()
        .subscribe((authConfig: AuthConfig) => {
          this.oAuthService.configure(authConfig);
          this.oAuthService.setStorage(localStorage);
          this.oAuthService.tokenValidationHandler = new JwksValidationHandler();
          this.oAuthService.redirectUri = `${window.location.origin}`;

          this.afterConfigured()
            .then(() => resolve(authConfig));

        });
    });
  }

  hasValidAccessToken(): boolean {
    return this.oAuthService.hasValidAccessToken();
  }

  logout(): void {
    this.oAuthService.logOut();
  }
}
