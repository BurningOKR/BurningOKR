package org.burningokr.controller.subscription;

import lombok.RequiredArgsConstructor;
import org.burningokr.service.websocket.taskboard.TaskboardSubscribeService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Controller
@RequiredArgsConstructor
public class TaskboardSubscribeController {
  private final TaskboardSubscribeService taskboardSubscribeService;

  @EventListener
  public void handleDisconnectEvent(SessionDisconnectEvent sessionDisconnectEvent) {
    taskboardSubscribeService.handleDisconnect(sessionDisconnectEvent);
  }

  @EventListener
  public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
    taskboardSubscribeService.handleUnsubscribe(event);
  }

  @EventListener
  public void handleSubscribeEvent(SessionSubscribeEvent subscribeEvent) {
    taskboardSubscribeService.handleSubscribe(subscribeEvent.getMessage());
  }
}
