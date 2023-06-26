package org.burningokr.service.monitoring;

import org.burningokr.model.users.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MonitorService {

  Map<MonitoredObject, List<UUID>> monitoringUsers = Collections.synchronizedMap(new HashMap<>());

  public boolean addUser(MonitoredObject monitoredObject, User user) {
    if (monitoredObject == null) return false;

    if (!monitoringUsers.containsKey(monitoredObject)) {
      monitoringUsers.put(monitoredObject, Collections.synchronizedList(new LinkedList<UUID>()));
    }
    List<UUID> currentMonitoredUsers = monitoringUsers.get(monitoredObject);
    boolean newUser = !monitoringUsers.get(monitoredObject).contains(user.getId()); // TODO (C.K.): check why
    currentMonitoredUsers.add(user.getId());

    // TODO: Rückgabe von newUser, aber muss aktuell immer raus, sonst erhält unter gewissen Umständen
    //  der Benutzer in einem weiteren Tab die Liste nicht. (MV)
    return true;
  }

  public boolean removeUser(MonitoredObject monitoredObject, User user) {
    if (monitoredObject == null) return false;

    List<UUID> userList = monitoringUsers.get(monitoredObject);
    if (userList == null) return false;

    monitoringUsers.get(monitoredObject).remove(user.getId());
    return !monitoringUsers.get(monitoredObject).contains(user.getId());
  }

  public List<UUID> getUserIdList(MonitoredObject monitoredObject) {
    return monitoringUsers.getOrDefault(monitoredObject, new LinkedList<UUID>());
  }

}
