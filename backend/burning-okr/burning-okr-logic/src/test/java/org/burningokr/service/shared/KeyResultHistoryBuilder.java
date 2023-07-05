package org.burningokr.service.shared;

import org.burningokr.model.okr.histories.KeyResultHistory;

import java.time.LocalDate;

public class KeyResultHistoryBuilder {
  KeyResultHistory currentKeyResultHistory;

  private KeyResultHistoryBuilder()
  {
    this.currentKeyResultHistory = new KeyResultHistory();
  }

  public static KeyResultHistoryBuilder Create()
  {
    return new KeyResultHistoryBuilder();
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

  public KeyResultHistoryBuilder SetBaseInformation(long currentValue, long targetValue, Long startValue, int year, int month, int day) {
    return SetBaseInformation(currentValue, targetValue, startValue, LocalDate.of(year, month, day));
  }

  public KeyResultHistory Build() {
    return currentKeyResultHistory;
  }
}
