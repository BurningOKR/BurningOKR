package org.burningokr.properties;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("security.oauth2.client")
@Component
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthentificationProperties {

  @NotEmpty private String userAuthorizationUri;

  @NotEmpty private String accessTokenUri;

  @NotEmpty private String clientId;

  @NotEmpty private String clientSecret;

  @NotEmpty private String scope;

  @NotEmpty private String tokenName;

  @NotEmpty
  public String getUserAuthorizationUri() {
    return userAuthorizationUri;
  }

  public void setUserAuthorizationUri(String userAuthorizationUri) {
    this.userAuthorizationUri = userAuthorizationUri;
  }

  public String getAccessTokenUri() {
    return accessTokenUri;
  }

  public void setAccessTokenUri(String accessTokenUri) {
    this.accessTokenUri = accessTokenUri;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getTokenName() {
    return tokenName;
  }

  public void setTokenName(String tokenName) {
    this.tokenName = tokenName;
  }
}
