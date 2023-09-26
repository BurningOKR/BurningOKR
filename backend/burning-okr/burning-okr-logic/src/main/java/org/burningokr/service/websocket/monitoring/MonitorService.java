package org.burningokr.service.websocket.monitoring;

import lombok.NonNull;
import org.burningokr.model.users.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MonitorService {

  private final Map<MonitoredObject, List<UUID>> monitoringUsers = Collections.synchronizedMap(new HashMap<>());

  public void addUserAsSubscriberForMonitoredObject(@NonNull MonitoredObject monitoredObject, User user) {
    if (monitoringUsers.containsKey(monitoredObject)) return;
    monitoringUsers.put(monitoredObject, Collections.synchronizedList(new LinkedList<>()));
    monitoringUsers.get(monitoredObject).add(user.getId());
  }

  public void removeUserAsSubscriberForMonitoredObject(@NonNull MonitoredObject monitoredObject, @NonNull User user) {
    if (!monitoringUsers.containsKey(monitoredObject)) return;
    monitoringUsers.get(monitoredObject).remove(user.getId());
  }

  /**
   * returns a list of user ids which are actively monitoring an object
   */
  public List<UUID> getUserIdsWhichAreSubscribedToObject(MonitoredObject monitoredObject) {
    return monitoringUsers.getOrDefault(monitoredObject, new LinkedList<>());
  }

  public boolean isUserSubscriberForAnyObject(@NonNull User user) {
    return monitoringUsers.values().stream().anyMatch(list -> list.contains(user.getId()));
  }
}
