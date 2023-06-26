package org.burningokr.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.exceptions.AuthorizationHeaderException;
import org.burningokr.service.security.AuthorizationUserContextService;
import org.burningokr.service.security.CustomAuthenticationProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketAuthenticationChannelInterceptor implements ChannelInterceptor {
  private final AuthorizationUserContextService authorizationUserContextService;
  private final CustomAuthenticationProvider customAuthenticationProvider;

  public static final String USER_SESSION_ATTRIBUTE_KEY = "userId";

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
      MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

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

    if (authByService != null) {
      accessor.setUser(authByService);
      SecurityContextHolder.getContext().setAuthentication(authByService);
      accessor
        .getSessionAttributes()
        .put(
          WebsocketAuthenticationChannelInterceptor.USER_SESSION_ATTRIBUTE_KEY,
                authorizationUserContextService.getAuthenticatedUser().getId()
        );
    } else {
      log.warn("User could not be identified");
    }
  }

  /**
   * checks if message is initial connection attempt
   * @return true if connection attempt
   */
  private boolean isConnectionAttempt(StompHeaderAccessor accessor) {
    return accessor != null && StompCommand.CONNECT.equals(accessor.getCommand());
  }

  // TODO fix auth
  private Authentication getAuthentication(String token) {
    return null; //resourceServerTokenServices.loadAuthentication(token);
  }

  /**
   * parses the bearer token from Authorization header and return the token without leading Bearer keyword.
   * @return parsed token
   */
  private String getBearerToken(StompHeaderAccessor accessor) throws AuthorizationHeaderException {
    List<String> tokenList = accessor.getNativeHeader("Authorization");
    if (tokenList == null || tokenList.size() < 1)
      throw new AuthorizationHeaderException("could not get Bearer token from Authorization header");

    String token = tokenList.get(0);
    token = token.split(" ")[1];
    return token;
  }
}
