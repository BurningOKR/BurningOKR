package org.burningokr.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.exceptions.AuthorizationHeaderException;
import org.burningokr.service.security.AuthorizationUserContextService;
import org.burningokr.service.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketAuthenticationChannelInterceptor implements ChannelInterceptor {
  public static final String USER_SESSION_ATTRIBUTE_KEY = "userId";
  @Autowired
  private JwtDecoder jwtDecoder;

  @Override
  public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (isConnectionAttempt(accessor)) {
      try {
        tryAuthentication(accessor);
      } catch (AuthorizationHeaderException ex) {
        log.warn(ex.getMessage());
      }
    }

    return message;
  }

  private void tryAuthentication(StompHeaderAccessor accessor) throws AuthorizationHeaderException {
    String bearerToken = getBearerToken(accessor);
    Authentication authByService = getAuthentication(bearerToken);

    if (authByService == null) {
      log.info("User could not be identified");
    }

    accessor.setUser(authByService);
    SecurityContextHolder.getContext().setAuthentication(authByService);
    accessor.getSessionAttributes().put(WebsocketAuthenticationChannelInterceptor.USER_SESSION_ATTRIBUTE_KEY, authByService.getName());
  }

  /**
   * checks if message is initial connection attempt
   *
   * @return true if connection attempt
   */
  private boolean isConnectionAttempt(StompHeaderAccessor accessor) {
    return accessor != null && StompCommand.CONNECT.equals(accessor.getCommand());
  }

  private Authentication getAuthentication(String token) {
    Jwt jwt = jwtDecoder.decode(token);
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    return converter.convert(jwt);
  }

  /**
   * parses the bearer token from Authorization header and return the token without leading Bearer keyword.
   *
   * @return parsed token
   */
  private String getBearerToken(StompHeaderAccessor accessor) {
    List<String> tokenList = accessor.getNativeHeader("Authorization");
    if (tokenList == null || tokenList.isEmpty()) return "";

    return tokenList.get(0).split(":")[1];
  }
}
