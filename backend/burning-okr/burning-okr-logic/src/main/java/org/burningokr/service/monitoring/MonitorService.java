package org.burningokr.service.monitoring;

import jakarta.validation.constraints.Null;
import lombok.NonNull;
import org.burningokr.model.users.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MonitorService {

  private final Map<MonitoredObject, List<UUID>> monitoringUsers = Collections.synchronizedMap(new HashMap<>());

  public void addUser(MonitoredObject monitoredObject, @NonNull User user) {
    if (monitoredObject == null) return;
    if (!monitoringUsers.containsKey(monitoredObject)) {
      monitoringUsers.put(monitoredObject, Collections.synchronizedList(new LinkedList<UUID>()));
    }

    monitoringUsers.get(monitoredObject).add(user.getId());
  }

  public void removeUser(MonitoredObject monitoredObject, @NonNull User user) {
    if (monitoredObject == null) return;
    if (!monitoringUsers.containsKey(monitoredObject)) return;

    monitoringUsers.get(monitoredObject).remove(user.getId());
  }

  public List<UUID> getUserIdList(MonitoredObject monitoredObject) {
    return monitoringUsers.getOrDefault(monitoredObject, new LinkedList<UUID>());
  }

  public boolean hasUser(@NonNull UUID uuid) {
    return monitoringUsers.values().stream().toList().stream().anyMatch(uuid::equals);
  }
}
