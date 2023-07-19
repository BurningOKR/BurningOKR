package org.burningokr.service.security.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
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
    log.debug("authenticate called");
    var jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
    var applicationAuthentication = jwtAuthenticationProvider.authenticate(authentication);
    if (applicationAuthentication.isAuthenticated()) {
      log.debug("update user from jwt called");
      var userToken = (Jwt) applicationAuthentication.getCredentials();
      authenticationUserContextService.updateUserFromToken(userToken);
    }

    return applicationAuthentication;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
