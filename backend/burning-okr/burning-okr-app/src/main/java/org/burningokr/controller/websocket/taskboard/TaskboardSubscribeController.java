package org.burningokr.controller.websocket.taskboard;

import org.burningokr.service.WebsocketUserService;
import org.burningokr.service.monitoring.MonitorService;
import org.burningokr.service.monitoring.MonitoredObject;
import org.burningokr.service.monitoring.MonitoredObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class TaskboardSubscribeController extends WebsocketSubscribeController {

  private static final Pattern URL_TO_ID_PATTERN = Pattern.compile("^/topic/unit/(\\d+)/tasks/users$");
  private static final MonitoredObjectType MONITORED_OBJECT_TYPE = MonitoredObjectType.TASKBOARD;

  @Autowired
  public TaskboardSubscribeController(SimpMessagingTemplate simpMessagingTemplate, SimpUserRegistry simpUserRegistry, WebsocketUserService websocketUserService, MonitorService monitorService) {
    super(simpMessagingTemplate, simpUserRegistry, websocketUserService, monitorService);
  }

  public MonitoredObject getMonitoredObject(String subscribeUrl) {
    Matcher matcher = URL_TO_ID_PATTERN.matcher(subscribeUrl);
    if (matcher.find()) {
      Long departmentId = Long.valueOf(matcher.group(1));
      return new MonitoredObject(MONITORED_OBJECT_TYPE, departmentId);
    } else {
      return null;
    }
  }
}
