package org.burningokr.config;

import org.burningokr.service.security.UserFromContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebsocketAuthenticationChannelInterceptor implements ChannelInterceptor {

  public static final String USER_SESSION_ATTRIBUTE_KEY = "userId";

  private final Logger logger =
      LoggerFactory.getLogger(WebsocketAuthenticationChannelInterceptor.class);

  private final TokenStore tokenStore;

  @Autowired
  public WebsocketAuthenticationChannelInterceptor(TokenStore tokenStore) {
    this.tokenStore = tokenStore;
  }

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
                UserFromContextService.extractUserIdFromSecurityContext());
      } else {
        logger.info("User could not be identified");
      }
    }
  }

  private boolean isConnectionAttempt(StompHeaderAccessor accessor) {
    return accessor != null && StompCommand.CONNECT.equals(accessor.getCommand());
  }

  private Authentication getAuthentication(String token) {
    Authentication authByService = tokenStore.readAuthentication(token);
    // MV: Hier stand mal weiterer nicht funktionierender Code zur Authentifizierung, falls es
    // hiermit Probleme geben sollte, schaut in der GIT-Historie.
    return authByService;
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
