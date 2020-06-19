package org.burningokr.model.configuration;

public enum OAuthConfigurationName {
  CLIENT_ID("client-id"),
  CLIENT_SECRET("client-secret"),
  SCOPE("scope"),
  REDIRECT_URI("redirect-uri"),
  SILENT_REFRESH_REDIRECT_URI("silent-refresh-redirect-uri"),
  AUTH_TYPE("auth-type"),
  ISSUER("issuer"),
  RESPONSE_TYPE("response-type"),
  TOKEN_ENDPOINT("token-endpoint"),
  OIDC("oidc"),
  SHOW_DEBUG_INFORMATION("show-debug-information"),
  REQUIRE_HTTPS("require-https"),
  STRICT_DISCOVERY_DOCUMENT_VALIDATION("strict-discovery-document-validation"),
  USE_HTTP_BASIC_AUTH("use-http-basic-auth"),
  ACCESS_TOKE_URI("access-token-uri"),
  USER_AUTHORIZATION_URI("user-authorization-uri"),
  CLIENT_AUTHENTICATION_SCHEME("client-authentication-scheme"),
  GRANT_TYPE("grant-type"),
  AUTO_APPROVE_SCOPES("auto-approve-scopes"),
  TOKEN_NAME("token-name"),
  USER_INFO_URI("user-info-uri"),
  PREFER_TOKEN_INFO("prefer-token-info");

  private String name;

  OAuthConfigurationName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
