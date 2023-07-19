package org.burningokr.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.burningokr.exceptions.AuthorizationHeaderException;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebSocketAuthentication {

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
    return nativeHeader != null && !nativeHeader.isEmpty() ? nativeHeader.get(0).split("\\s")[1] : "";
  }

  protected boolean isConnectionAttempt(@NonNull StompHeaderAccessor accessor) {
    return StompCommand.CONNECT.equals(accessor.getCommand());
  }

  protected void tryToAuthenticate(@NonNull StompHeaderAccessor header) throws AuthorizationHeaderException {
    final String bearerToken = getBearerToken(header);
    final Authentication authentication = decodeToken(bearerToken);

    if (!authentication.isAuthenticated()) return;

    header.setUser(authentication);
    System.out.println("Current Auth Attribute has uuid as => " + authentication.getName());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    if (header.getSessionAttributes() == null) throw new RuntimeException("Session Attributes are null");
    header.getSessionAttributes().put(USER_SESSION_ATTRIBUTE_KEY, authentication.getName());
  }
}
