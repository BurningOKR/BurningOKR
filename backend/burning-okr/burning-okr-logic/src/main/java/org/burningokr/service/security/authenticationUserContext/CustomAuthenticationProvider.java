<<<<<<<< HEAD:backend/burning-okr/burning-okr-logic/src/main/java/org/burningokr/service/security/authorization/CustomAuthenticationProvider.java
package org.burningokr.service.security.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
========
package org.burningokr.service.security.authenticationUserContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.users.User;
>>>>>>>> refactor/spring-auth:backend/burning-okr/burning-okr-logic/src/main/java/org/burningokr/service/security/authenticationUserContext/CustomAuthenticationProvider.java
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
<<<<<<<< HEAD:backend/burning-okr/burning-okr-logic/src/main/java/org/burningokr/service/security/authorization/CustomAuthenticationProvider.java

========
>>>>>>>> refactor/spring-auth:backend/burning-okr/burning-okr-logic/src/main/java/org/burningokr/service/security/authenticationUserContext/CustomAuthenticationProvider.java

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
<<<<<<<< HEAD:backend/burning-okr/burning-okr-logic/src/main/java/org/burningokr/service/security/authorization/CustomAuthenticationProvider.java
      log.debug("update user from jwt called");
      var userToken = (Jwt) applicationAuthentication.getCredentials();
      authenticationUserContextService.updateUserFromToken(userToken);
========
      log.debug("updating user from jwt because token is valid, calling update user on AuthenticationUserContextService");
      authenticationUserContextService.updateCachedAndDatabaseUser(userFromToken);
>>>>>>>> refactor/spring-auth:backend/burning-okr/burning-okr-logic/src/main/java/org/burningokr/service/security/authenticationUserContext/CustomAuthenticationProvider.java
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
