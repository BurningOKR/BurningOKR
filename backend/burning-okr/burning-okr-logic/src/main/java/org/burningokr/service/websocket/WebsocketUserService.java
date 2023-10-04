package org.burningokr.service.websocket;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.monitoring.UserId;
import org.burningokr.model.users.User;
import org.burningokr.service.security.websocket.WebSocketAuthentication;
import org.burningokr.service.userhandling.UserService;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebsocketUserService {
  private final UserService userService;

  public User findByAccessor(StompHeaderAccessor stompHeaderAccessor) {
    Map<String, Object> sessionAttributes = stompHeaderAccessor.getSessionAttributes();
    if (sessionAttributes == null || !sessionAttributes.containsKey(WebSocketAuthentication.USER_SESSION_ATTRIBUTE_KEY)) {
      throw new RuntimeException("StompHeaderAccessor does not contain (valid) userId.");
    }
    UserId userId = new UserId(sessionAttributes.get(WebSocketAuthentication.USER_SESSION_ATTRIBUTE_KEY).toString());
    return userService.findById(userId.getUserId()).orElseThrow();
  }
}
