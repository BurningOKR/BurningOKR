package org.burningokr.service.monitoring;

import org.burningokr.model.monitoring.UserId;
import org.burningokr.model.users.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MonitorService {

  Map<MonitoredObject, List<UserId>> monitoringUsers = Collections.synchronizedMap(new HashMap<>());

  public boolean addUser(MonitoredObject monitoredObject, User user) {
    return addUser(monitoredObject, new UserId(user));
  }

  public boolean addUser(MonitoredObject monitoredObject, UserId userId) {
    if (monitoredObject == null) return false;
    if (!monitoringUsers.containsKey(monitoredObject)) {
      monitoringUsers.put(monitoredObject, Collections.synchronizedList(new LinkedList<>()));
    }
    List<UserId> currentMonitors = monitoringUsers.get(monitoredObject);
    boolean newUser = !monitoringUsers.get(monitoredObject).contains(userId);
    currentMonitors.add(userId);
    return true; // TODO: Rückgabe von newUser, aber muss aktuell immer raus, sonst erhält unter gewissen Umständen der Benutzer in einem weiteren Tab die Liste nicht. (MV)
  }

  public boolean removeUser(MonitoredObject monitoredObject, User user) {
    return removeUser(monitoredObject, new UserId(user));
  }

  public boolean removeUser(MonitoredObject monitoredObject, UserId userId) {
    if (monitoredObject == null) {
      return false;
    }
    List<UserId> userList = monitoringUsers.get(monitoredObject);
    if (userList != null) {
      monitoringUsers.get(monitoredObject).remove(userId);
      return !monitoringUsers.get(monitoredObject).contains(userId);
    }
    return false;
  }

  public List<UserId> getUserIdList(MonitoredObject monitoredObject) {
    if (!monitoringUsers.containsKey(monitoredObject)) {
      return Collections.EMPTY_LIST;
    } else {
      return monitoringUsers.get(monitoredObject);
    }
  }

  public Set<UserId> getUserSet(MonitoredObject monitoredObject) {
    return new HashSet<>(getUserIdList(monitoredObject));
  }
}
