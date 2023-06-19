import { Injectable } from '@angular/core';
import { OidcConfiguration } from './oidc-configuration';

@Injectable({
  providedIn: 'root'
})
export class OidcConfigurationService {

  async getOidcConfiguration$(): Promise<OidcConfiguration> {
    // Usage of fetch is needed to avoid a circular dependency:
    // AuthService -> OidcConfigService -> HTTP-Client -> OAuthInterceptor --
    //      ^                                                                |
    //      |                                                                |
    //      -----------------------------------------------------------------
    const response: Response = await fetch(`http://${window.location.origin}/applicationSettings/oidcConfiguration`);

    return response.json();
  }
}
