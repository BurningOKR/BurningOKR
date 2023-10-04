package org.burningokr.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.burningokr.service.security.websocket.WebSocketAuthentication;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@RequiredArgsConstructor
public class StompHeaderInterceptor implements ChannelInterceptor {

  private final WebSocketAuthentication socketAuth;

  @Override
  public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
    final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if (accessor == null)
      throw new NullPointerException("StompHeaderAccessor is null");

    if (this.socketAuth.isConnectionAttempt(accessor)) {
      this.socketAuth.tryToAuthenticate(accessor);
    }

    return message;
  }
}
