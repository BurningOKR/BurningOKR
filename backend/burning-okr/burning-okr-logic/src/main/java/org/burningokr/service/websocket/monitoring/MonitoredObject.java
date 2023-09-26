package org.burningokr.service.websocket.monitoring;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MonitoredObject {
  private MonitoredObjectType monitoredObjectType;
  private Long id;
}
