package org.burningokr.controller.subscription;

import org.burningokr.service.WebsocketUserService;
import org.burningokr.service.monitoring.MonitorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebsocketSubscribeControllerTest {
  @Mock
  private SimpMessagingTemplate simpMessagingTemplate;

  @Mock
  private SimpUserRegistry simpUserRegistry;

  @Mock
  private WebsocketUserService websocketUserService;

  @Mock
  private MonitorService monitorService;

  // using inheritor of WebSocketSubscribeController for testing because the class itself is static
  @InjectMocks
  @Spy
  private TaskboardSubscribeController taskboardSubscribeController;

  @Test
  void handleDisconnectEvent_shouldCallHandleRemoveMethod() {
    // DESCRIBE
    SessionDisconnectEvent sessionDisconnectEvent = mock(SessionDisconnectEvent.class);
    Set<SimpSubscription> set = new HashSet<>();

    doReturn(null).when(sessionDisconnectEvent).getMessage();
    doReturn(set).when(taskboardSubscribeController).findMatchingSubscriptions(any());

    // ACT
    taskboardSubscribeController.handleDisconnectEvent(sessionDisconnectEvent);

    // ASSERT
    verify(taskboardSubscribeController).handleRemove(sessionDisconnectEvent.getMessage(), set);
  }

  @Test
  void handleSubscribeEvent_shouldReturn_whenHeaderDestinationEndsWithUsers() {
    try (
      MockedStatic<StompHeaderAccessor> stompHeaderAccessorMockedStatic = mockStatic(StompHeaderAccessor.class);
      MockedStatic<WebsocketSubscribeController> websocketSubscribeControllerMockedStatic = mockStatic(
        WebsocketSubscribeController.class)
    ) {
      // DESCRIBE
      SessionSubscribeEvent sessionSubscribeEventMock = mock(SessionSubscribeEvent.class);
      StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
      ArgumentCaptor<StompHeaderAccessor> accessorArgumentCaptor = ArgumentCaptor.forClass(StompHeaderAccessor.class);

      stompHeaderAccessorMockedStatic.when(() -> StompHeaderAccessor.wrap(any())).thenReturn(stompHeaderAccessorMock);
      websocketSubscribeControllerMockedStatic.when(() ->
          WebsocketSubscribeController.isStompHeaderAccessorDestinationEndingWithUsers(accessorArgumentCaptor.capture()))
        .thenReturn(false);

      // ACT
      taskboardSubscribeController.handleSubscribeEvent(sessionSubscribeEventMock);

      // ASSERT
      verify(taskboardSubscribeController, times(0)).getMonitoredObject(any());
      verify(taskboardSubscribeController, times(0)).addUserAsWatcherForMonitoredObject(any(), any());
      verify(taskboardSubscribeController, times(0)).sendListOfUsersWhichAreMonitoringObject(any());

      assertEquals(stompHeaderAccessorMock, accessorArgumentCaptor.getValue());
    }
  }
}