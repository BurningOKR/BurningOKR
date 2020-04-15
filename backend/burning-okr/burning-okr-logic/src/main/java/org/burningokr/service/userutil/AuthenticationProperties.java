package org.burningokr.service.userutil;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.burningokr.service.condition.AadCondition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Conditional(AadCondition.class)
@ConfigurationProperties("security.oauth2.client")
@Component
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationProperties {

  @NotEmpty private String userAuthorizationUri;

  @NotEmpty private String accessTokenUri;

  @NotEmpty private String clientId;

  @NotEmpty private String clientSecret;

  @NotEmpty private String scope;

  @NotEmpty private String tokenName;

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
