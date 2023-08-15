package org.burningokr.service;

import org.burningokr.config.WebSocketAuthentication;
import org.burningokr.model.monitoring.UserId;
import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WebsocketUserService {
  private final UserService userService;

  @Autowired
  public WebsocketUserService(UserService userService) {
    this.userService = userService;
  }

  public User findByAccessor(StompHeaderAccessor stompHeaderAccessor) {
    Map<String, Object> sessionAttributes = stompHeaderAccessor.getSessionAttributes();
    if (sessionAttributes == null || !sessionAttributes.containsKey(WebSocketAuthentication.USER_SESSION_ATTRIBUTE_KEY)) {
      throw new RuntimeException("StompHeaderAccessor does not contain (valid) userId.");
    }
    UserId userId = new UserId(sessionAttributes.get(WebSocketAuthentication.USER_SESSION_ATTRIBUTE_KEY).toString());
    return userService.findById(userId.getUserId()).orElseThrow();
  }
}
