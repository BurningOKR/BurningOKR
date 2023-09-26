package org.burningokr.service.websocket.taskboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.users.User;
import org.burningokr.service.websocket.WebsocketUserService;
import org.burningokr.service.websocket.monitoring.MonitorService;
import org.burningokr.service.websocket.monitoring.MonitoredObject;
import org.burningokr.service.websocket.monitoring.MonitoredObjectType;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskboardSubscribeService {
  public static final String UNIT_TASKS_USERS_URL = "/topic/unit/%d/tasks/users";
  // used for extracting the unit id from the destination attribute of a message
  private static final Pattern URL_TO_ID_PATTERN = Pattern.compile("^/topic/unit/(\\d+)/tasks/users$");
  private static final MonitoredObjectType MONITORED_OBJECT_TYPE = MonitoredObjectType.TASKBOARD;
  private final WebsocketUserService websocketUserService;
  private final MonitorService monitorService;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final SimpUserRegistry simpUserRegistry;

  /**
   * Removes the unsubscribing user from the list of subscriptions.
   */
  public void handleUnsubscribe(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
    Set<SimpSubscription> matchingSubscriptions = findMatchingSubscriptionsBySubscriptionId(sessionUnsubscribeEvent);
    removeSubscribedUserFromMatchingSubscriptions(sessionUnsubscribeEvent.getMessage(), matchingSubscriptions);
  }

  /**
   * Removes the user from the list of subscriptions.
   */
  public void handleDisconnect(SessionDisconnectEvent sessionDisconnectEvent) {
    Set<SimpSubscription> matchingSubscriptions = findMatchingSubscriptionsBySessionId(sessionDisconnectEvent.getSessionId());
    removeSubscribedUserFromMatchingSubscriptions(sessionDisconnectEvent.getMessage(), matchingSubscriptions);
  }

  /**
   * Adds a user as subscriber for the object that is referenced in the destination attribute of the given message.
   */
  public void handleSubscribe(Message<byte[]> message) {
    StompHeaderAccessor stompHeaderAccessor = createStompHeaderAccessorFromMessage(message);

    if (!isStompHeaderAccessorDestinationEndingWithUsers(stompHeaderAccessor)) return;

    MonitoredObject monitoredObject = getMonitoredObjectBySubscribeUrl(stompHeaderAccessor.getDestination());
    addUserAsSubscriberForMonitoredObject(stompHeaderAccessor, monitoredObject);
    sendListOfUsersWhichAreMonitoringObject(monitoredObject);
  }

  protected void removeSubscribedUserFromMatchingSubscriptions(
    Message<byte[]> message,
    Set<SimpSubscription> matchingSubscriptions
  ) {
    StompHeaderAccessor stompHeaderAccessor = createStompHeaderAccessorFromMessage(message);
    User user = websocketUserService.findByAccessor(stompHeaderAccessor);

    for (SimpSubscription simpSubscription : matchingSubscriptions) {
      MonitoredObject monitoredObject = getMonitoredObjectBySubscribeUrl(simpSubscription.getDestination());

      if (monitorService.isUserSubscriberForAnyObject(user)) {
        monitorService.removeUserAsSubscriberForMonitoredObject(monitoredObject, user);
        sendListOfUsersWhichAreMonitoringObject(monitoredObject);
      }
    }
  }

  protected StompHeaderAccessor createStompHeaderAccessorFromMessage(Message<byte[]> message) {
    return StompHeaderAccessor.wrap(message);
  }

  protected MonitoredObject getMonitoredObjectBySubscribeUrl(String subscribeUrl) {
    Matcher matcher = URL_TO_ID_PATTERN.matcher(subscribeUrl);
    if (matcher.find()) {
      Long unitId = Long.valueOf(matcher.group(1));
      return new MonitoredObject(MONITORED_OBJECT_TYPE, unitId);
    }
    throw new IllegalArgumentException("Invalid unit id in passed subscribe url %s".formatted(subscribeUrl));
  }

  protected void sendListOfUsersWhichAreMonitoringObject(MonitoredObject monitoredObject) {
    String usersUrl = UNIT_TASKS_USERS_URL.formatted(monitoredObject.getId());
    simpMessagingTemplate.convertAndSend(
      usersUrl,
      monitorService.getUserIdsWhichAreSubscribedToObject(monitoredObject)
    );
  }

  protected boolean isStompHeaderAccessorDestinationEndingWithUsers(StompHeaderAccessor stompHeaderAccessor) {
    return stompHeaderAccessor.getDestination() != null && stompHeaderAccessor.getDestination().endsWith("users");
  }

  protected void addUserAsSubscriberForMonitoredObject(
    StompHeaderAccessor stompHeaderAccessor,
    MonitoredObject monitoredObject
  ) {
    User user = websocketUserService.findByAccessor(stompHeaderAccessor);

    if (!monitorService.isUserSubscriberForAnyObject(user)) {
      monitorService.addUserAsSubscriberForMonitoredObject(monitoredObject, user);
    }
  }

  protected Set<SimpSubscription> findMatchingSubscriptionsBySubscriptionId(
    SessionUnsubscribeEvent sessionUnsubscribeEvent
  ) {
    StompHeaderAccessor stompHeaderAccessor = createStompHeaderAccessorFromMessage(sessionUnsubscribeEvent.getMessage());
    Set<SimpSubscription> allSubscriptionsForSessionId = findMatchingSubscriptionsBySessionId(stompHeaderAccessor.getSessionId());
    return allSubscriptionsForSessionId.stream()
      .filter(
        subscription -> subscription.getId().equals(stompHeaderAccessor.getSubscriptionId())
      )
      .collect(Collectors.toSet());
  }

  protected Set<SimpSubscription> findMatchingSubscriptionsBySessionId(String sessionId) {
    return simpUserRegistry.findSubscriptions(
      possibleSubscription -> possibleSubscription
        .getSession()
        .getId()
        .equals(sessionId)
    );
  }
}
