package org.burningokr.service.websocket.util;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.users.User;
import org.burningokr.service.websocket.WebsocketUserService;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StompHeaderUtilService {
  private final WebsocketUserService websocketUserService;

  public StompHeaderAccessor createStompHeaderAccessorFromMessage(Message<byte[]> message) {
    return StompHeaderAccessor.wrap(message);
  }

  public boolean isStompHeaderAccessorDestinationEndingWithUsers(StompHeaderAccessor stompHeaderAccessor) {
    return stompHeaderAccessor.getDestination() != null && stompHeaderAccessor.getDestination().endsWith("users");
  }

  public User getUserFromMessage(Message<byte[]> message) {
    return websocketUserService.findByAccessor(createStompHeaderAccessorFromMessage(message));
  }
}
