package org.burningokr.service.shared;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.histories.KeyResultHistory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class KeyResultBuilder {
  KeyResult currentKeyResult;

  private KeyResultBuilder()
  {
    this.currentKeyResult = new KeyResult();
  }

  public static KeyResultBuilder Create()
  {
    return new KeyResultBuilder();
  }
  public static KeyResultBuilder Create(long target, long current)
  {
    return new KeyResultBuilder().SetTarget(target).SetCurrent(current);
  }
  public static KeyResultBuilder Create(long target, long current, long start)
  {
    return new KeyResultBuilder().SetTarget(target).SetCurrent(current).SetStart(start);
  }


  public KeyResultBuilder AddHistory(long targetValue, long currentValue, LocalDate date)
  {
    return AddHistory(targetValue, currentValue, null, date);
  }

  public KeyResultBuilder AddHistory(long targetValue, long currentValue, Long startValue, LocalDate date) {
    KeyResultHistory keyResultHistory = new KeyResultHistory();
    keyResultHistory.setDateChanged(date);
    keyResultHistory.setTargetValue(targetValue);
    keyResultHistory.setCurrentValue(currentValue);

    if(startValue != null) {
      keyResultHistory.setStartValue(startValue);
    }

    Collection<KeyResultHistory> keyResultHistoryForKeyResult = currentKeyResult.getKeyResultHistory();

    if(keyResultHistoryForKeyResult == null) {
      currentKeyResult.setKeyResultHistory(List.of(keyResultHistory));
    } else {
      keyResultHistoryForKeyResult.add(keyResultHistory);
    }

    return this;
  }

  public KeyResultBuilder SetTarget(long targetValue) {
    currentKeyResult.setTargetValue(targetValue);
    return this;
  }

  public KeyResultBuilder SetCurrent(long currentValue) {
    currentKeyResult.setCurrentValue(currentValue);
    return this;
  }

  public KeyResultBuilder SetStart(long startValue) {
    currentKeyResult.setStartValue(startValue);
    return this;
  }

  public KeyResult Build() {
    return currentKeyResult;
  }
}
