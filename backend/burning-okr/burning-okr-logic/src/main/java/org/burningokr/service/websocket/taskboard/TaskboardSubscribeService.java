package org.burningokr.service.websocket.taskboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.users.User;
import org.burningokr.service.websocket.CustomSimpUserRegistryService;
import org.burningokr.service.websocket.WebsocketUserService;
import org.burningokr.service.websocket.monitoring.MonitorService;
import org.burningokr.service.websocket.monitoring.MonitoredObject;
import org.burningokr.service.websocket.util.StompHeaderUtilService;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskboardSubscribeService {
  public static final String UNIT_TASKS_USERS_URL = "/topic/unit/%d/tasks/users";

  private final WebsocketUserService websocketUserService;
  private final MonitorService monitorService;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final CustomSimpUserRegistryService customSimpUserRegistryService;
  private final StompHeaderUtilService stompHeaderUtilService;

  /**
   * Removes the unsubscribing user from the list of subscriptions.
   */
  public void handleUnsubscribe(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
    log.debug("Received unsubscribe");
    Set<SimpSubscription> matchingSubscriptions = customSimpUserRegistryService.findMatchingSubscriptionsBySubscriptionId(
      sessionUnsubscribeEvent);
    removeSubscribedUserFromMatchingSubscriptions(sessionUnsubscribeEvent.getMessage(), matchingSubscriptions);
  }

  /**
   * Removes the user from the list of subscriptions.
   */
  public void handleDisconnect(SessionDisconnectEvent sessionDisconnectEvent) {
    User user = stompHeaderUtilService.getUserFromMessage(sessionDisconnectEvent.getMessage());
    monitorService.removeUserFromAllActiveSubscriptions(user);
  }

  /**
   * Adds a user as subscriber for the object that is referenced in the destination attribute of the given message.
   */
  public void handleSubscribe(Message<byte[]> message) {
    StompHeaderAccessor stompHeaderAccessor = stompHeaderUtilService.createStompHeaderAccessorFromMessage(message);

    if (!stompHeaderUtilService.isStompHeaderAccessorDestinationEndingWithUsers(stompHeaderAccessor)) return;

    MonitoredObject monitoredObject = monitorService.getMonitoredObjectBySubscribeUrl(stompHeaderAccessor.getDestination());
    addUserAsSubscriberForMonitoredObject(stompHeaderAccessor, monitoredObject);
    sendListOfUsersWhichAreMonitoringObject(monitoredObject);
  }

  protected void removeSubscribedUserFromMatchingSubscriptions(
    Message<byte[]> message,
    Set<SimpSubscription> matchingSubscriptions
  ) {
    User user = stompHeaderUtilService.getUserFromMessage(message);

    for (SimpSubscription simpSubscription : matchingSubscriptions) {
      MonitoredObject monitoredObject = monitorService.getMonitoredObjectBySubscribeUrl(simpSubscription.getDestination());

      if (monitorService.isUserSubscriberForAnyObject(user)) {
        monitorService.removeUserAsSubscriberFromMonitoredObject(monitoredObject, user);
        sendListOfUsersWhichAreMonitoringObject(monitoredObject);
      }
    }
  }

  protected void sendListOfUsersWhichAreMonitoringObject(MonitoredObject monitoredObject) {
    String usersUrl = UNIT_TASKS_USERS_URL.formatted(monitoredObject.getId());
    simpMessagingTemplate.convertAndSend(
      usersUrl,
      monitorService.getUserIdsWhichAreSubscribedToObject(monitoredObject)
    );
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
}
