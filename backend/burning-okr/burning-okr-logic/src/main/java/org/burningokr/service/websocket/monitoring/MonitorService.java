package org.burningokr.service.websocket.monitoring;

import lombok.NonNull;
import org.burningokr.model.users.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MonitorService {
  private static final Pattern URL_TO_ID_PATTERN = Pattern.compile("^/topic/unit/(\\d+)/tasks/users$");
  private static final MonitoredObjectType MONITORED_OBJECT_TYPE = MonitoredObjectType.TASKBOARD;

  private final Map<MonitoredObject, List<UUID>> monitoredObjectSubscribersMap = Collections.synchronizedMap(new HashMap<>());

  public void addUserAsSubscriberForMonitoredObject(@NonNull MonitoredObject monitoredObject, User user) {
    if (monitoredObjectSubscribersMap.containsKey(monitoredObject)) return;
    monitoredObjectSubscribersMap.put(monitoredObject, Collections.synchronizedList(new LinkedList<>()));
    monitoredObjectSubscribersMap.get(monitoredObject).add(user.getId());
  }

  public void removeUserAsSubscriberFromMonitoredObject(@NonNull MonitoredObject monitoredObject, @NonNull User user) {
    if (!monitoredObjectSubscribersMap.containsKey(monitoredObject)) return;
    monitoredObjectSubscribersMap.get(monitoredObject).remove(user.getId());
  }

  /**
   * returns a list of user ids which are actively monitoring an object
   */
  public List<UUID> getUserIdsWhichAreSubscribedToObject(MonitoredObject monitoredObject) {
    return monitoredObjectSubscribersMap.getOrDefault(monitoredObject, new LinkedList<>());
  }

  public boolean isUserSubscriberForAnyObject(@NonNull User user) {
    return monitoredObjectSubscribersMap.values().stream().anyMatch(list -> list.contains(user.getId()));
  }

  public MonitoredObject getMonitoredObjectBySubscribeUrl(String subscribeUrl) {
    Matcher matcher = URL_TO_ID_PATTERN.matcher(subscribeUrl);
    if (matcher.find()) {
      Long unitId = Long.valueOf(matcher.group(1));
      return new MonitoredObject(MONITORED_OBJECT_TYPE, unitId);
    }
    throw new IllegalArgumentException("Invalid unit id in passed subscribe url %s".formatted(subscribeUrl));
  }

  public void removeUserFromAllActiveSubscriptions(User user) {
    monitoredObjectSubscribersMap.forEach((monitoredObject, uuids) -> {
      if (uuids.contains(user.getId())) removeUserAsSubscriberFromMonitoredObject(monitoredObject, user);
    });
  }
}
