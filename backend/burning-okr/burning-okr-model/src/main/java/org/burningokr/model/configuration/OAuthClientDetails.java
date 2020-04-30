package org.burningokr.model.configuration;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Table;

@Entity
@Data
@Table(appliesTo = "oauth_client_details")
public class OAuthClientDetails implements AuthenticationProperties {
  @Id private String clientId;
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
