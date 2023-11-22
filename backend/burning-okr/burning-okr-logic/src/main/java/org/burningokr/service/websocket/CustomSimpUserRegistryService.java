package org.burningokr.service.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.service.websocket.util.StompHeaderUtilService;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomSimpUserRegistryService {
  private final SimpUserRegistry simpUserRegistry;
  private final StompHeaderUtilService stompHeaderUtilService;

  public Set<SimpSubscription> findMatchingSubscriptionsBySubscriptionId(
    SessionUnsubscribeEvent sessionUnsubscribeEvent
  ) {
    StompHeaderAccessor stompHeaderAccessor = stompHeaderUtilService.createStompHeaderAccessorFromMessage(
      sessionUnsubscribeEvent.getMessage());
    Set<SimpSubscription> allSubscriptionsForSessionId = findMatchingSubscriptionsBySessionId(stompHeaderAccessor.getSessionId());
    return allSubscriptionsForSessionId.stream()
      .filter(
        subscription -> subscription.getId().equals(stompHeaderAccessor.getSubscriptionId())
      )
      .collect(Collectors.toSet());
  }

  public Set<SimpSubscription> findMatchingSubscriptionsBySessionId(String sessionId) {
    return simpUserRegistry.findSubscriptions(
      possibleSubscription -> possibleSubscription
        .getSession()
        .getId()
        .equals(sessionId)
    );
  }
}
