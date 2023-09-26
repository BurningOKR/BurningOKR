package org.burningokr.controller.subscription;

import org.burningokr.service.WebsocketUserService;
import org.burningokr.service.monitoring.MonitorService;
import org.burningokr.service.monitoring.MonitoredObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebsocketSubscribeControllerTest {
  @Test
  void handleSubscribeEvent_shouldReturnPrematurely_whenHeaderDestinationIsNull() {
    // DESCRIBE
    SessionSubscribeEvent inputSessionSubscribeEventMock = mock(SessionSubscribeEvent.class);
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);

    doReturn(stompHeaderAccessorMock).when(this.websocketSubscribeController)
      .createStompHeaderAccessorFromMessage(any());
    doReturn(null).when(stompHeaderAccessorMock).getDestination();
    // ACT
    websocketSubscribeController.handleSubscribeEvent(inputSessionSubscribeEventMock);

    // ASSERT
    verify(websocketSubscribeController, times(0)).getMonitoredObjectBySubscribeUrl(any());
    verify(websocketSubscribeController, times(0)).addUserAsWatcherForMonitoredObject(any(), any());
    verify(websocketSubscribeController, times(0)).sendListOfUsersWhichAreMonitoringObject(any());
    verifyNoMoreInteractions(
      this.monitorService,
      this.websocketUserService,
      this.simpMessagingTemplate,
      this.simpUserRegistry
    );
  }

  @Mock
  private SimpMessagingTemplate simpMessagingTemplate;

  @Mock
  private SimpUserRegistry simpUserRegistry;

  @Mock
  private WebsocketUserService websocketUserService;

  @Mock
  private MonitorService monitorService;

  // using inheritor of WebSocketSubscribeController for testing because the class itself is abstract
  @InjectMocks
  @Spy
  private WebsocketSubscribeControllerTestImpl websocketSubscribeController;

  @Test
  void handleDisconnectEvent_shouldCallHandleRemoveMethod() {
    // DESCRIBE
    SessionDisconnectEvent sessionDisconnectEvent = mock(SessionDisconnectEvent.class);
    Set<SimpSubscription> set = new HashSet<>();

    doReturn(null).when(sessionDisconnectEvent).getMessage();
    doReturn(set).when(websocketSubscribeController).findMatchingSubscriptions(any());

    // ACT
    websocketSubscribeController.handleDisconnectEvent(sessionDisconnectEvent);

    // ASSERT
    verify(websocketSubscribeController).handleRemove(sessionDisconnectEvent.getMessage(), set);
  }

  @Test
  void handleSubscribeEvent_shouldReturnPrematurely_whenHeaderDestinationDoesNotEndWithUsers() {
    // DESCRIBE
    SessionSubscribeEvent inputSessionSubscribeEventMock = mock(SessionSubscribeEvent.class);
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);

    doReturn(stompHeaderAccessorMock).when(this.websocketSubscribeController)
      .createStompHeaderAccessorFromMessage(any());
    doReturn("Something").when(stompHeaderAccessorMock).getDestination();
    // ACT
    websocketSubscribeController.handleSubscribeEvent(inputSessionSubscribeEventMock);

    // ASSERT
    verify(websocketSubscribeController, times(0)).getMonitoredObjectBySubscribeUrl(any());
    verify(websocketSubscribeController, times(0)).addUserAsWatcherForMonitoredObject(any(), any());
    verify(websocketSubscribeController, times(0)).sendListOfUsersWhichAreMonitoringObject(any());
    verifyNoMoreInteractions(
      this.monitorService,
      this.websocketUserService,
      this.simpMessagingTemplate,
      this.simpUserRegistry
    );
  }

  private static class WebsocketSubscribeControllerTestImpl extends WebsocketSubscribeController {

    public WebsocketSubscribeControllerTestImpl(
      SimpMessagingTemplate simpMessagingTemplate,
      SimpUserRegistry simpUserRegistry,
      WebsocketUserService websocketUserService,
      MonitorService monitorService
    ) {
      super(simpMessagingTemplate, simpUserRegistry, websocketUserService, monitorService);
    }

    @Override
    public MonitoredObject getMonitoredObjectBySubscribeUrl(String subscribeUrl) {
      return null;
    }
  }
}
