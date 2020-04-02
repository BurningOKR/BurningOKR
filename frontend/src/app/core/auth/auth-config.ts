import { AuthConfig } from 'angular-oauth2-oidc';

// Not used
// TODO: Include example configuration in docs
// TODO: Include default local configuration in init service
export const authConfig: AuthConfig = {
  clientId: 'clientapp',                    // as used with postman
  dummyClientSecret: '123abc',              // as used with postman
  issuer: 'http://localhost:4200/oauth/token',
  oidc: false,                              // must be disabled, while using password flow
  redirectUri: `${window.location.origin}/companies`,
  responseType: 'password,refresh_token',   // as used with postman, don't know about the comma tho
  scope: 'USER',                            // as used with postman
  showDebugInformation: true,
  tokenEndpoint: '/oauth/token',
  useHttpBasicAuth: true,                   // using basic auth fot http
  skipIssuerCheck: true,
  requireHttps: false
};

// /* azure */
// {
//   clientId: environment.auth.clientId,
//   issuer: `https://login.microsoftonline.com/${environment.auth.tenant}/v2.0`,
//   oidc: true,
//   redirectUri: `${window.location.origin}${environment.production ? '/app' : ''}/redirect`,
//   responseType: 'id_token token',
//   scope: 'openid profile email',
//   showDebugInformation: false,
//   silentRefreshRedirectUri: `${window.location.origin}${environment.production ? '/app' : ''}/silent-refresh.html`,
//   skipIssuerCheck: true,
//   strictDiscoveryDocumentValidation: false,
// }
