package org.burningokr.service.monitoring;

import lombok.NonNull;
import org.burningokr.model.users.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MonitorService {

  private final Map<MonitoredObject, List<UUID>> monitoringUsers = Collections.synchronizedMap(new HashMap<>());

  public void addUser(@NonNull MonitoredObject monitoredObject, User user) {
    if (monitoringUsers.containsKey(monitoredObject)) return;
    monitoringUsers.put(monitoredObject, Collections.synchronizedList(new LinkedList<>()));
    monitoringUsers.get(monitoredObject).add(user.getId());
  }

  public void removeUser(@NonNull MonitoredObject monitoredObject, @NonNull User user) {
    if (!monitoringUsers.containsKey(monitoredObject)) return;
    monitoringUsers.get(monitoredObject).remove(user.getId());
  }

  public List<UUID> getUserIdList(MonitoredObject monitoredObject) {
    return monitoringUsers.getOrDefault(monitoredObject, new LinkedList<>());
  }

  public boolean hasUser(@NonNull UUID uuid) {
    return monitoringUsers.values().stream().anyMatch(list -> list.contains(uuid));
  }
}
