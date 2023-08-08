package org.burningokr.service.security.authenticationUserContext;

import org.burningokr.model.users.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.List;
import java.util.Map;

public class BurningOkrAuthentication extends AbstractOAuth2TokenAuthenticationToken<Jwt> {
  public BurningOkrAuthentication(Jwt token, User principal) {
    super(
        token,
        principal,
        token,
        List.of()
    );
  }

  @Override
  public User getPrincipal() {
    return (User) super.getPrincipal();
  }

  @Override
  public Map<String, Object> getTokenAttributes() {
    return null;
  }
}
