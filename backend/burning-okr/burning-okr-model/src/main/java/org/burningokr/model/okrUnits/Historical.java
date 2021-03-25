package org.burningokr.model.okrUnits;

import org.burningokr.model.okrUnits.okrUnitHistories.OkrUnitHistory;

public interface Historical<T extends OkrUnit> {
  OkrUnitHistory<T> getHistory();
  void setHistory(OkrUnitHistory<T> history);
}
