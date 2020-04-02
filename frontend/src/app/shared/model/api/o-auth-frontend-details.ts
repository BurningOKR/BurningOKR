export interface OAuthFrontendDetails {
  clientId: string;
  dummyClientSecret: string;
  scope: string;
  redirectUri: string;
  silentRefreshRedirectUri: string;
  authType: string;
  issuer: string;
  responseType: string;
  tokenEndpoint: string;
  oidc: boolean;
  showDebugInformation: boolean;
  requireHttps: boolean;
  strictDiscoveryDocumentValidation: boolean;
  useHttpBasicAuth: boolean;
}
