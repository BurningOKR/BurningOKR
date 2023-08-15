export interface OidcConfiguration {
  clientId: string;
  issuerUri: string;
  scopes: string[];
  strictDiscoveryDocumentValidation: boolean;
}
