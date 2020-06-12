package org.burningokr.model.okrUnits;

import java.util.Collection;

public interface OkrParentUnit {
  Collection<OkrChildUnit> getOkrChildUnits();

  void setOkrChildUnits(Collection<OkrChildUnit> departments);
}
