package org.burningokr.controller.subscription;

import org.burningokr.mapper.users.UserMapper;
import org.burningokr.model.users.User;
import org.burningokr.service.WebsocketUserService;
import org.burningokr.service.monitoring.MonitorService;
import org.burningokr.service.monitoring.MonitoredObject;
import org.burningokr.service.userhandling.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public abstract class WebsocketSubscribeController {

  private final SimpMessagingTemplate simpMessagingTemplate;
  private final SimpUserRegistry simpUserRegistry;
  private final WebsocketUserService websocketUserService;
  private final UserMapper userMapper;
  private final MonitorService monitorService;
  private final UserService userService;
  private final String sendUrl = "/topic/unit/%d/tasks/users";

  public WebsocketSubscribeController(
          SimpMessagingTemplate simpMessagingTemplate,
          SimpUserRegistry simpUserRegistry,
          UserService userService,
          WebsocketUserService websocketUserService,
          UserMapper userMapper,
          MonitorService monitorService
  ) {
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.simpUserRegistry = simpUserRegistry;
    this.websocketUserService = websocketUserService;
    this.userMapper = userMapper;
    this.monitorService = monitorService;
    this.userService = userService;
  }

  @EventListener
  public void handleDisconnectEvent(SessionDisconnectEvent event) {
    handleRemove(event.getMessage(), findMatchingSubscriptions(event));
  }

  @EventListener
  public void handleSubscribeEvent(SessionSubscribeEvent subscribeEvent) {
    StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(subscribeEvent.getMessage());
    MonitoredObject monitoredObject = getMonitoredObject(stompHeaderAccessor.getDestination());
    User user = websocketUserService.findByAccessor(stompHeaderAccessor);
    if (monitorService.addUser(monitoredObject, user)) {
      sendUserList(monitoredObject);
    }
  }

  @EventListener
  public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
    handleRemove(event.getMessage(), findMatchingSubscriptions(event));
  }

  private void handleRemove(Message<byte[]> message, Set<SimpSubscription> matchingSubscriptions) {
    StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
    User user = websocketUserService.findByAccessor(stompHeaderAccessor);
    for (SimpSubscription simpSubscription : matchingSubscriptions) {
      String destinationUrl = simpSubscription.getDestination();
      MonitoredObject monitoredObject = getMonitoredObject(destinationUrl);
      if (monitorService.removeUser(monitoredObject, user)) {
        sendUserList(monitoredObject);
      }
    }
  }

  private Set<SimpSubscription> findMatchingSubscriptions(
      SessionDisconnectEvent sessionDisconnectEvent) {
    return findMatchingSubscriptions(StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage()));
  }

  private Set<SimpSubscription> findMatchingSubscriptions(
      SessionUnsubscribeEvent sessionUnsubscribeEvent) {
    StompHeaderAccessor stompHeaderAccessor =
        StompHeaderAccessor.wrap(sessionUnsubscribeEvent.getMessage());
    Set<SimpSubscription> allSubscriptions = findMatchingSubscriptions(stompHeaderAccessor);
    return allSubscriptions.stream()
        .filter(
            possibleSubscription ->
                possibleSubscription.getId().equals(stompHeaderAccessor.getSubscriptionId()))
        .collect(Collectors.toSet());
  }

  private Set<SimpSubscription> findMatchingSubscriptions(StompHeaderAccessor stompHeaderAccessor) {
    return simpUserRegistry.findSubscriptions(
        possibleSubscription ->
            possibleSubscription.getSession().getId().equals(stompHeaderAccessor.getSessionId()));
  }

  public abstract MonitoredObject getMonitoredObject(String subscribeUrl);

  public void sendUserList(MonitoredObject watchedObject) {
    String usersUrl = String.format(sendUrl, watchedObject.getId());
    List<UUID> userList =
        monitorService.getUserSet(watchedObject).stream()
            .map(u -> u.getUserId())
            .collect(Collectors.toList());
    simpMessagingTemplate.convertAndSend(usersUrl, userList);
  }
}
