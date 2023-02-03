package org.burningokr.dto.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthFrontendDetailsDto {
  @Id
  private String clientId;
  private String dummyClientSecret;
  private String scope;
  private String redirectUri;
  private String silentRefreshRedirectUri;
  private String authType;
  private String issuer;
  private String responseType;
  private String tokenEndpoint;
  private Boolean oidc;
  private Boolean showDebugInformation;
  private Boolean requireHttps;
  private Boolean strictDiscoveryDocumentValidation;
  private Boolean useHttpBasicAuth;
}
