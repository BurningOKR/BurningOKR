package org.burningokr.controller.subscription;

import org.burningokr.mapper.users.UserMapper;
import org.burningokr.service.WebsocketUserService;
import org.burningokr.service.monitoring.MonitorService;
import org.burningokr.service.monitoring.MonitoredObject;
import org.burningokr.service.monitoring.MonitoredObjectType;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class TaskboardSubscribeController extends WebsocketSubscribeController {

  private static final Pattern URL_TO_ID_PATTERN =
      Pattern.compile("^/topic/unit/(\\d+)/tasks/users$");
  private static final String SEND_URL = "/topic/unit/%d/tasks/users";
  private static final MonitoredObjectType MONITORED_OBJECT_TYPE = MonitoredObjectType.TASKBOARD;

  @Autowired
  public TaskboardSubscribeController(
      SimpMessagingTemplate simpMessagingTemplate,
      SimpUserRegistry simpUserRegistry,
      UserService userService,
      WebsocketUserService websocketUserService,
      UserMapper userMapper,
      MonitorService monitorService) {
    super(
        simpMessagingTemplate,
        simpUserRegistry,
        userService,
        websocketUserService,
        userMapper,
        monitorService,
        SEND_URL);
  }

  public MonitoredObject getMonitoredObject(String subscribeUrl) {
    Matcher m = URL_TO_ID_PATTERN.matcher(subscribeUrl);
    if (m.find()) {
      Long departmentId = Long.valueOf(m.group(1));
      return new MonitoredObject(MONITORED_OBJECT_TYPE, departmentId);
    } else {
      return null;
    }
  }
}
