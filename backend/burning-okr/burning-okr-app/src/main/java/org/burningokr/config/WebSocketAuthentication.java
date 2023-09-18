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
  public static final String USER_SESSION_ATTRIBUTE_KEY = "userId";
  private static final String WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY = "Authorization";

  private final AuthenticationUserContextService authenticationUserContextService;

  private Authentication decodeToken(@NonNull String token) {
    if (token.isEmpty()) throw new NullPointerException("Token is either null or is empty");
    Jwt jwt = jwtDecoder.decode(token); // TODO CK refactor when working
    AbstractAuthenticationToken abstractAuthenticationToken = new JwtAuthenticationConverter().convert(jwt);

    // new
    User userFromToken = authenticationUserContextService.getUserFromToken(jwt);

    BurningOkrAuthentication burningOkrAuthentication = createCustomAuthentication(
        jwt, userFromToken, abstractAuthenticationToken
    );

    if (burningOkrAuthentication.isAuthenticated()) {
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

  private String getBearerToken(@NonNull StompHeaderAccessor header) {
    if (!header.containsNativeHeader(WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY)) return "";
    final List<String> nativeHeader = header.getNativeHeader(WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY);
    return nativeHeader != null && !nativeHeader.isEmpty() ? nativeHeader.get(0).split("\\s")[1] : "";
  }

  public boolean isConnectionAttempt(@NonNull StompHeaderAccessor accessor) {
    return StompCommand.CONNECT.equals(accessor.getCommand());
  }

  public void tryToAuthenticate(@NonNull StompHeaderAccessor header) throws AuthorizationHeaderException {
    final String bearerToken = getBearerToken(header);
    final Authentication authentication = decodeToken(bearerToken);

    if (!authentication.isAuthenticated()) throw new AuthorizationHeaderException("user is not authenticated");

    header.setUser(authentication);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    log.debug("set auth after receiving jwt via websocket connection");
    if (header.getSessionAttributes() == null) throw new RuntimeException("Session Attributes are null");
    header.getSessionAttributes().put(USER_SESSION_ATTRIBUTE_KEY,
        authenticationUserContextService.getAuthenticatedUser().getId());
  }
}
