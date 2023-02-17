package org.burningokr.model.configuration;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "oauth_client_details")
public class OAuthClientDetails implements AuthenticationProperties {
  @Id
  private String clientId;
  private String clientSecret;
  private String resourceIds;
  private String scope;
  private String authorizedGrantTypes;
  private String webServerRedirectUri;
  private String authorities;
  private String additionalInformation;
  private String autoapprove;

  private Integer accessTokenValidity;
  private Integer refreshTokenValidity;
}
