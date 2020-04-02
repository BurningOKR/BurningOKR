package org.burningokr.model.configuration;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Table;

@Entity
@Data
@Table(appliesTo = "oauth_frontend_details")
public class OAuthFrontendDetails {
  @Id private String clientId;
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
