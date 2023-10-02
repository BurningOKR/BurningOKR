package org.burningokr.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.exceptions.AuthorizationHeaderException;
import org.burningokr.model.users.User;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.burningokr.service.security.authenticationUserContext.BurningOkrAuthentication;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class WebSocketAuthentication {

  private final JwtDecoder jwtDecoder;

  private final JwtAuthenticationConverter jwtAuthenticationConverter;
  public static final String USER_SESSION_ATTRIBUTE_KEY = "userId";
  public static final String WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY = "Authorization";

  private final AuthenticationUserContextService authenticationUserContextService;

  protected boolean isConnectionAttempt(@NonNull StompHeaderAccessor accessor) {
    return StompCommand.CONNECT.equals(accessor.getCommand());
  }

  protected void tryToAuthenticate(@NonNull StompHeaderAccessor header) throws AuthorizationHeaderException {
    final String bearerToken = getBearerToken(header);
    final Jwt jwt = jwtDecoder.decode(bearerToken);
    BurningOkrAuthentication authentication = this.createAuthenticationFromJwt(jwt);

    if (!authentication.isAuthenticated()) throw new AuthorizationHeaderException("User is not authenticated");

    log.debug("Updating user from jwt because token is valid, calling update user on AuthenticationUserContextService");
    authenticationUserContextService.updateCachedAndDatabaseUser(authentication.getPrincipal());
    header.setUser(authentication);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    log.debug("Set auth after receiving jwt via websocket connection");
    if (header.getSessionAttributes() == null) throw new RuntimeException("Session attributes are null");
    header.getSessionAttributes().put(USER_SESSION_ATTRIBUTE_KEY,
      authenticationUserContextService.getAuthenticatedUser().getId());
  }

  private BurningOkrAuthentication createAuthenticationFromJwt(Jwt jwt) {
    AbstractAuthenticationToken abstractAuthenticationToken = this.jwtAuthenticationConverter.convert(jwt);

    User userFromToken = authenticationUserContextService.getUserFromToken(jwt);
    return createCustomAuthentication(jwt, userFromToken, abstractAuthenticationToken);
  }

  private String getBearerToken(@NonNull StompHeaderAccessor header) {
    if (!header.containsNativeHeader(WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY)) throw new IllegalArgumentException("Key '%s' is missing in stomp header".formatted(WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY));
    final List<String> nativeHeader = header.getNativeHeader(WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY);

    if(nativeHeader == null || nativeHeader.isEmpty()) throw new IllegalArgumentException("Value of key '%s' in stomp header is null or empty".formatted(WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY));
    return extractTokenFromHeaderValue(nativeHeader.get(0));
  }

  private String extractTokenFromHeaderValue(String value) {
    String[] words = value.split("\\s");
    if(words.length != 2) throw new IllegalArgumentException("Authorization token in header is malformed. Expected structure: Bearer {token}");
    return words[1];
  }

  private BurningOkrAuthentication createCustomAuthentication(Jwt jwt, User userFromToken, Authentication authentication) {
    BurningOkrAuthentication burningOkrAuthentication = new BurningOkrAuthentication(jwt, userFromToken);
    burningOkrAuthentication.setAuthenticated(authentication.isAuthenticated());

    return burningOkrAuthentication;
  }
}
