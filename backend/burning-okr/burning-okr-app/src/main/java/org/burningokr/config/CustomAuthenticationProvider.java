package org.burningokr.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final JwtDecoder jwtDecoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//    log.info(authentication.toString());
    log.info(authentication.getDetails().toString());
    var test = new JwtAuthenticationProvider(jwtDecoder);
    return test.authenticate(authentication);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
