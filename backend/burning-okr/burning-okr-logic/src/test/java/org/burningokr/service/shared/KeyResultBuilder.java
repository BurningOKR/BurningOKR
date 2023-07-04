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


  public KeyResultBuilder AddHistory(long targetValue, long currentValue, LocalDate date)
  {
    KeyResultHistory keyResultHistory = new KeyResultHistory();
    keyResultHistory.setDateChanged(date);
    keyResultHistory.setTargetValue(targetValue);
    keyResultHistory.setCurrentValue(currentValue);

    Collection<KeyResultHistory> keyResultHistoryForKeyResult = currentKeyResult.getKeyResultHistory();

    if(keyResultHistoryForKeyResult == null)
    {
      currentKeyResult.setKeyResultHistory(List.of(keyResultHistory));
    }
    else
    {
      keyResultHistoryForKeyResult.add(keyResultHistory);
    }

    return this;
  }

  public KeyResultBuilder SetTarget(long targetValue)
  {
    currentKeyResult.setTargetValue(targetValue);
    return this;
  }

  public KeyResultBuilder SetCurrent(long currentValue)
  {
    currentKeyResult.setCurrentValue(currentValue);
    return this;
  }

  public KeyResult Build()
  {
    return currentKeyResult;
  }
}
