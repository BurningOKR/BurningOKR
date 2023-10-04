import { AuthConfig } from 'angular-oauth2-oidc';

export interface AuthFlowConfig extends AuthConfig {
  issuer: string;
  redirectUri: string;
  clientId: string;
  responseType: string;
  scope: string;
  showDebugInformation: boolean;
  requireHttps: boolean;
  strictDiscoveryDocumentValidation: boolean;
}
