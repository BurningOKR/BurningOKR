package org.burningokr.config.authorizationserver;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Qualifier("LocalUserAccessTokenConverter")
public class LocalUserAccessTokenConverter extends DefaultAccessTokenConverter {
  @Override
  public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
    OAuth2Authentication authentication = super.extractAuthentication(map);
    authentication.setDetails(map);
    return authentication;
  }
}
