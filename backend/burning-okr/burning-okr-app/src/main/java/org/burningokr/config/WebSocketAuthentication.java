package org.burningokr.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.burningokr.exceptions.AuthorizationHeaderException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.List;

@RequiredArgsConstructor
public class WebSocketAuthentication {

  private Authentication authentication;
  private final JwtDecoder jwtDecoder;
  public static final String USER_SESSION_ATTRIBUTE_KEY = "userId";
  private static final String WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY = "Authorization";

  private Authentication decodeToken(@NonNull String token) {
    if (token.isEmpty()) throw new NullPointerException("Token is either null or is empty");
    Jwt jwt = jwtDecoder.decode(token);
    return new JwtAuthenticationConverter().convert(jwt);
  }

  private String getBearerToken(@NonNull StompHeaderAccessor header) {
    if (!header.containsNativeHeader(WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY)) return "";
    final List<String> nativeHeader = header.getNativeHeader(WEBSOCKET_STOMP_HEADER_AUTHORIZATION_KEY);
    return nativeHeader != null && !nativeHeader.isEmpty() ? nativeHeader.get(0).split(":")[1] : "";
  }

  protected boolean isConnectionAttempt(@NonNull StompHeaderAccessor accessor) {
    return StompCommand.CONNECT.equals(accessor.getCommand());
  }

  private boolean isAuthenticated(@NonNull final Authentication current) {
    return this.authentication.isAuthenticated() && current.isAuthenticated()
        && this.authentication.getName().equals(current.getName());
  }

  protected void tryToAuthenticate(@NonNull StompHeaderAccessor header) throws AuthorizationHeaderException {
    final String bearerToken = getBearerToken(header);
    final Authentication currentAuth = decodeToken(bearerToken);

    if (isAuthenticated(currentAuth)) return;

//    if (this.authentication.isAuthenticated()) {
//      logger.warn("User has been successful authenticated");
//      return;
//    }

    header.setUser(this.authentication);
    this.authentication = currentAuth;
    SecurityContextHolder.getContext().setAuthentication(this.authentication);
    if (header.getSessionAttributes() == null) throw new RuntimeException("Session Attributes are null");
    header.getSessionAttributes().put(USER_SESSION_ATTRIBUTE_KEY, this.authentication.getName());
  }
}
