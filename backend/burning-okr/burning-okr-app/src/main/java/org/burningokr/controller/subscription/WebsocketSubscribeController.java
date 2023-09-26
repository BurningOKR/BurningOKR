package org.burningokr.controller.subscription;

import org.burningokr.model.users.User;
import org.burningokr.service.WebsocketUserService;
import org.burningokr.service.monitoring.MonitorService;
import org.burningokr.service.monitoring.MonitoredObject;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public abstract class WebsocketSubscribeController {

  public static final String UNIT_TASKS_USERS_UTL = "/topic/unit/%d/tasks/users";
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final SimpUserRegistry simpUserRegistry;
  private final WebsocketUserService websocketUserService;
  private final MonitorService monitorService;

  @Autowired
  public WebsocketSubscribeController(
    SimpMessagingTemplate simpMessagingTemplate,
    SimpUserRegistry simpUserRegistry,
    WebsocketUserService websocketUserService,
    MonitorService monitorService
  ) {
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.simpUserRegistry = simpUserRegistry;
    this.websocketUserService = websocketUserService;
    this.monitorService = monitorService;
  }

  @EventListener
  public void handleDisconnectEvent(SessionDisconnectEvent event) {
    handleRemove(event.getMessage(), findMatchingSubscriptions(event));
  }

  protected boolean isStompHeaderAccessorDestinationEndingWithUsers(StompHeaderAccessor stompHeaderAccessor) {
    return stompHeaderAccessor.getDestination() != null && stompHeaderAccessor.getDestination().endsWith("users");
  }

  @EventListener
  public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
    handleRemove(event.getMessage(), findMatchingSubscriptionsBySubscriptionId(event));
  }

  @EventListener
  public void handleSubscribeEvent(SessionSubscribeEvent subscribeEvent) {
    StompHeaderAccessor stompHeaderAccessor = this.wrap(subscribeEvent);

    if (!isStompHeaderAccessorDestinationEndingWithUsers(stompHeaderAccessor)) return;

    MonitoredObject monitoredObject = getMonitoredObject(stompHeaderAccessor.getDestination());
    addUserAsWatcherForMonitoredObject(stompHeaderAccessor, monitoredObject);
    sendListOfUsersWhichAreMonitoringObject(monitoredObject);
  }

  protected StompHeaderAccessor wrap(SessionSubscribeEvent subscribeEvent) {
    return StompHeaderAccessor.wrap(subscribeEvent.getMessage());
  }


  protected void addUserAsWatcherForMonitoredObject(
    StompHeaderAccessor stompHeaderAccessor,
    MonitoredObject monitoredObject
  ) {
    User user = websocketUserService.findByAccessor(stompHeaderAccessor);

    if (!monitorService.hasUser(user.getId())) monitorService.addUserAsWatcherForMonitoredObject(monitoredObject, user);
  }

  protected void handleRemove(Message<byte[]> message, Set<SimpSubscription> matchingSubscriptions) {
    StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
    User user = websocketUserService.findByAccessor(stompHeaderAccessor);
    for (SimpSubscription simpSubscription : matchingSubscriptions) {
      String destinationUrl = simpSubscription.getDestination();
      MonitoredObject monitoredObject = getMonitoredObject(destinationUrl);
      if (!monitorService.hasUser(user.getId())) {
        continue;
      }
      monitorService.removeUserAsWatcherForMonitoredObject(monitoredObject, user);
      sendListOfUsersWhichAreMonitoringObject(monitoredObject);
    }
  }

  protected Set<SimpSubscription> findMatchingSubscriptions(
    SessionDisconnectEvent sessionDisconnectEvent
  ) {
    return findMatchingSubscriptionsBySessionId(StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage()));
  }

  private Set<SimpSubscription> findMatchingSubscriptionsBySubscriptionId(
    SessionUnsubscribeEvent sessionUnsubscribeEvent
  ) {
    StompHeaderAccessor stompHeaderAccessor =
      StompHeaderAccessor.wrap(sessionUnsubscribeEvent.getMessage());
    Set<SimpSubscription> allSubscriptions = findMatchingSubscriptionsBySessionId(stompHeaderAccessor);
    return allSubscriptions.stream()
      .filter(
        possibleSubscription ->
          possibleSubscription.getId().equals(stompHeaderAccessor.getSubscriptionId()))
      .collect(Collectors.toSet());
  }

  private Set<SimpSubscription> findMatchingSubscriptionsBySessionId(StompHeaderAccessor stompHeaderAccessor) {
    return simpUserRegistry.findSubscriptions(
      possibleSubscription -> possibleSubscription
        .getSession()
        .getId()
        .equals(stompHeaderAccessor.getSessionId())
    );
  }

  public void sendListOfUsersWhichAreMonitoringObject(MonitoredObject monitoredObject) {
    String usersUrl = UNIT_TASKS_USERS_UTL.formatted(monitoredObject.getId());
    simpMessagingTemplate.convertAndSend(
      usersUrl,
      monitorService.getUserIdsWhichAreMonitoringObject(monitoredObject)
    );
  }

  public abstract MonitoredObject getMonitoredObject(String subscribeUrl);
}
