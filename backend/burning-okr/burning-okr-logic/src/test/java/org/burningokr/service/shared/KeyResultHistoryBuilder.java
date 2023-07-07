package org.burningokr.service.shared;

import org.burningokr.model.okr.histories.KeyResultHistory;

import java.time.LocalDate;

public class KeyResultHistoryBuilder {
  KeyResultHistory currentKeyResultHistory;

  public KeyResultHistoryBuilder()
  {
    this.currentKeyResultHistory = new KeyResultHistory();
  }

  public KeyResultHistoryBuilder SetBaseInformation(long currentValue, long targetValue, Long startValue, LocalDate date) {
    currentKeyResultHistory.setCurrentValue(currentValue);
    currentKeyResultHistory.setTargetValue(targetValue);
    currentKeyResultHistory.setDateChanged(date);

    if(startValue != null)
    {
      currentKeyResultHistory.setStartValue(startValue);
    }

    return this;
  }

  public KeyResultHistory Build() {
    return currentKeyResultHistory;
  }
}
