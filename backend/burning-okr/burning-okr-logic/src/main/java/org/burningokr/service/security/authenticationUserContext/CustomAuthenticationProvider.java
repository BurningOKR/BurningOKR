package org.burningokr.service.security.authenticationUserContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.users.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final JwtDecoder jwtDecoder;
  private final AuthenticationUserContextService authenticationUserContextService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    log.debug("custom authenticate called");

    var jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
    var applicationAuthentication = jwtAuthenticationProvider.authenticate(authentication);
    User userFromToken = authenticationUserContextService.getUserFromToken((Jwt) applicationAuthentication.getCredentials());

    BurningOkrAuthentication burningOkrAuthentication = createCustomAuthentication(
        (Jwt) applicationAuthentication.getCredentials(), userFromToken, applicationAuthentication
    );

    if (applicationAuthentication.isAuthenticated()) {
      log.debug("updating user from jwt because token is valid, calling update user on AuthenticationUserContextService");
      authenticationUserContextService.updateCachedAndDatabaseUser(userFromToken);
    }

    return burningOkrAuthentication;
  }

  private BurningOkrAuthentication createCustomAuthentication(Jwt jwt, User userFromToken, Authentication authentication) {
    BurningOkrAuthentication burningOkrAuthentication = new BurningOkrAuthentication(jwt, userFromToken);
    burningOkrAuthentication.setAuthenticated(authentication.isAuthenticated());

    return burningOkrAuthentication;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
