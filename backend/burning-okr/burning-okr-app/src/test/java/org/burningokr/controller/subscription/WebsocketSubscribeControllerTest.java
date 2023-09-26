//package org.burningokr.controller.subscription;
//
//import org.burningokr.service.websocket.WebsocketUserService;
//import org.burningokr.service.websocket.monitoring.MonitorService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.simp.user.SimpSubscription;
//import org.springframework.messaging.simp.user.SimpUserRegistry;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//import org.springframework.web.socket.messaging.SessionSubscribeEvent;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class WebsocketSubscribeControllerTest {
//  @Mock
//  private SimpMessagingTemplate simpMessagingTemplate;
//
//  @Mock
//  private SimpUserRegistry simpUserRegistry;
//
//  @Mock
//  private WebsocketUserService websocketUserService;
//
//  @Mock
//  private MonitorService monitorService;
//
//  // using inheritor of WebSocketSubscribeController for testing because the class itself is abstract
//  @InjectMocks
//  @Spy
//  private TaskboardSubscribeController taskboardSubscribeController;
//
//  @Test
//  void handleSubscribeEvent_shouldReturnPrematurely_whenHeaderDestinationIsNull() {
//    // DESCRIBE
//    SessionSubscribeEvent inputSessionSubscribeEventMock = mock(SessionSubscribeEvent.class);
//    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
//
//    doReturn(stompHeaderAccessorMock).when(this.taskboardSubscribeController)
//      .createStompHeaderAccessorFromMessage(any());
//    doReturn(null).when(stompHeaderAccessorMock).getDestination();
//    // ACT
//    taskboardSubscribeController.handleSubscribeEvent(inputSessionSubscribeEventMock);
//
//    // ASSERT
//    verify(taskboardSubscribeController, times(0)).getMonitoredObjectBySubscribeUrl(any());
//    verify(taskboardSubscribeController, times(0)).addUserAsWatcherForMonitoredObject(any(), any());
//    verify(taskboardSubscribeController, times(0)).sendListOfUsersWhichAreMonitoringObject(any());
//    verifyNoMoreInteractions(
//      this.monitorService,
//      this.websocketUserService,
//      this.simpMessagingTemplate,
//      this.simpUserRegistry
//    );
//  }
//
//  @Test
//  void handleDisconnectEvent_shouldCallHandleRemoveMethod() {
//    // DESCRIBE
//    SessionDisconnectEvent sessionDisconnectEvent = mock(SessionDisconnectEvent.class);
//    Set<SimpSubscription> set = new HashSet<>();
//
//    doReturn(null).when(sessionDisconnectEvent).getMessage();
//    doReturn(set).when(taskboardSubscribeController).findMatchingSubscriptionsBySessionId(any());
//
//    // ACT
//    taskboardSubscribeController.handleDisconnectEvent(sessionDisconnectEvent);
//
//    // ASSERT
//    verify(taskboardSubscribeController).handleRemove(sessionDisconnectEvent.getMessage(), set);
//  }
//
//  @Test
//  void handleSubscribeEvent_shouldReturnPrematurely_whenHeaderDestinationDoesNotEndWithUsers() {
//    // DESCRIBE
//    SessionSubscribeEvent inputSessionSubscribeEventMock = mock(SessionSubscribeEvent.class);
//    StompHeaderAccessor stompHeaderAccessorMock = mock(StompHeaderAccessor.class);
//
//    doReturn(stompHeaderAccessorMock).when(this.taskboardSubscribeController)
//      .createStompHeaderAccessorFromMessage(any());
//    doReturn("Something").when(stompHeaderAccessorMock).getDestination();
//    // ACT
//    taskboardSubscribeController.handleSubscribeEvent(inputSessionSubscribeEventMock);
//
//    // ASSERT
//    verify(taskboardSubscribeController, times(0)).getMonitoredObjectBySubscribeUrl(any());
//    verify(taskboardSubscribeController, times(0)).addUserAsWatcherForMonitoredObject(any(), any());
//    verify(taskboardSubscribeController, times(0)).sendListOfUsersWhichAreMonitoringObject(any());
//    verifyNoMoreInteractions(
//      this.monitorService,
//      this.websocketUserService,
//      this.simpMessagingTemplate,
//      this.simpUserRegistry
//    );
//  }
//}
