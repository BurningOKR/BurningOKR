package org.burningokr.service.websocket.taskboard;

import org.burningokr.service.websocket.WebsocketUserService;
import org.burningokr.service.websocket.monitoring.MonitorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
class TaskboardSubscribeServiceTest {
  @Mock
  private SimpMessagingTemplate simpMessagingTemplateMock;

  @Mock
  private SimpUserRegistry simpUserRegistryMock;

  @Mock
  private WebsocketUserService websocketUserServiceMock;

  @Mock
  private MonitorService monitorServiceMock;

  @InjectMocks
  private TaskboardSubscribeService taskboardSubscribeService;

  @Test
  void handleSubscribeEvent_shouldReturnPrematurely_whenHeaderDestinationIsNull() {
    // DESCRIBE
    SessionSubscribeEvent inputSessionSubscribeEventMock = mock(SessionSubscribeEvent.class);
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);

    doReturn(stompHeaderAccessorMock).when(this.taskboardSubscribeService)
      .createStompHeaderAccessorFromMessage(any());
    doReturn(null).when(stompHeaderAccessorMock).getDestination();
    // ACT
    taskboardSubscribeService.handleSubscribeEvent(inputSessionSubscribeEventMock);

    // ASSERT
    verify(taskboardSubscribeService, times(0)).getMonitoredObjectBySubscribeUrl(any());
    verify(taskboardSubscribeService, times(0)).addUserAsWatcherForMonitoredObject(any(), any());
    verify(taskboardSubscribeService, times(0)).sendListOfUsersWhichAreMonitoringObject(any());
    verifyNoMoreInteractions(
      this.monitorServiceMock,
      this.websocketUserServiceMock,
      this.simpMessagingTemplateMock,
      this.simpUserRegistryMock
    );
  }

  @Test
  void handleDisconnectEvent_shouldCallHandleRemoveMethod() {
    // DESCRIBE
    SessionDisconnectEvent sessionDisconnectEvent = mock(SessionDisconnectEvent.class);
    Set<SimpSubscription> set = new HashSet<>();

    doReturn(null).when(sessionDisconnectEvent).getMessage();
    doReturn(set).when(taskboardSubscribeService).findMatchingSubscriptionsBySessionId(any());

    // ACT
    taskboardSubscribeService.handleDisconnectEvent(sessionDisconnectEvent);

    // ASSERT
    verify(taskboardSubscribeService).handleRemove(sessionDisconnectEvent.getMessage(), set);
  }

  @Test
  void handleSubscribeEvent_shouldReturnPrematurely_whenHeaderDestinationDoesNotEndWithUsers() {
    // DESCRIBE
    SessionSubscribeEvent inputSessionSubscribeEventMock = mock(SessionSubscribeEvent.class);
    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);

    doReturn(stompHeaderAccessorMock).when(this.taskboardSubscribeService)
      .createStompHeaderAccessorFromMessage(any());
    doReturn("Something").when(stompHeaderAccessorMock).getDestination();
    // ACT
    taskboardSubscribeService.handleSubscribeEvent(inputSessionSubscribeEventMock);

    // ASSERT
    verify(taskboardSubscribeService, times(0)).getMonitoredObjectBySubscribeUrl(any());
    verify(taskboardSubscribeService, times(0)).addUserAsWatcherForMonitoredObject(any(), any());
    verify(taskboardSubscribeService, times(0)).sendListOfUsersWhichAreMonitoringObject(any());
    verifyNoMoreInteractions(
      this.monitorServiceMock,
      this.websocketUserServiceMock,
      this.simpMessagingTemplateMock,
      this.simpUserRegistryMock
    );
  }
}
