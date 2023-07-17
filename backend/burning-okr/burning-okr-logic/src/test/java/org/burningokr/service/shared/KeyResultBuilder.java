package org.burningokr.service.shared;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.histories.KeyResultHistory;

import java.util.Collection;
import java.util.List;

public class KeyResultBuilder {
  KeyResult currentKeyResult;

  public KeyResultBuilder()
  {
    this.currentKeyResult = new KeyResult();
  }

  public KeyResultBuilder AddHistory(KeyResultHistory toAdd) {
    Collection<KeyResultHistory> keyResultHistoryForKeyResult = currentKeyResult.getKeyResultHistory();

    if(keyResultHistoryForKeyResult == null) {
      currentKeyResult.setKeyResultHistory(List.of(toAdd));
    } else {
      keyResultHistoryForKeyResult.add(toAdd);
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
