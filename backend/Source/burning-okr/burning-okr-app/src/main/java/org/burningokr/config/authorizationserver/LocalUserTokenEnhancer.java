package org.burningokr.config.authorizationserver;

import java.util.HashMap;
import java.util.Map;
import org.burningokr.model.users.LocalUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class LocalUserTokenEnhancer implements TokenEnhancer {
  @Override
  public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    Map<String, Object> additionalInfo = new HashMap<>();
    LocalUser user =
        ((LocalUserDetails) authentication.getUserAuthentication().getPrincipal()).getUser();
    additionalInfo.put("id", user.getId());
    additionalInfo.put("givenName", user.getGivenName());
    additionalInfo.put("surname", user.getSurname());
    additionalInfo.put("mail", user.getMail());
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    return accessToken;
  }
}
