package org.burningokr.service.websocket.taskboard;

import org.burningokr.service.websocket.WebsocketUserService;
import org.burningokr.service.websocket.monitoring.MonitorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;

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
  void handleUnsubscribe_shouldRemoveSubscribedUserFromMatchingSubscriptions() {
    // DESCRIBE


    // ACT

    // ASSERT
  }
}
