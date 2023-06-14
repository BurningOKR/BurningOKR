package org.burningokr.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.service.security.AuthorizationUserContextService;
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

  public static final String USER_SESSION_ATTRIBUTE_KEY = "userId";

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
      MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (isConnectionAttempt(accessor)) {
      tryAuthentication(accessor);
    }

    return message;
  }

  private void tryAuthentication(StompHeaderAccessor accessor) {
    String bearerToken = getBearerToken(accessor);
    if (bearerToken != null) {
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
        log.info("User could not be identified");
      }
    }
  }

  private boolean isConnectionAttempt(StompHeaderAccessor accessor) {
    return accessor != null && StompCommand.CONNECT.equals(accessor.getCommand());
  }

  private Authentication getAuthentication(String token) {
    return null; //resourceServerTokenServices.loadAuthentication(token);
  }

  private String getBearerToken(StompHeaderAccessor accessor) {
    List<String> tokenList = accessor.getNativeHeader("Authorization");
    String token = null;
    if (tokenList != null && tokenList.size() > 0) {
      token = tokenList.get(0);
      token = token.split(" ")[1];
    }
    return token;
  }
}
