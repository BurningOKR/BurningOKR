package org.burningokr.service.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.histories.KeyResultHistory;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultHistoryRepository;
import org.burningokr.service.activity.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class KeyResultHistoryService {
  private final KeyResultHistoryRepository keyResultHistoryRepository;
  private final ActivityService activityService;


  @Transactional
  public void updateKeyResultHistory(User user, KeyResult keyResult) {
    KeyResultHistory keyResultHistory = keyResultHistoryRepository.findByKeyResultOrderByDateChangedDesc(keyResult)
      .get(0);

    if (keyResultHistory.getDateChanged().equals(LocalDate.now())) {
      keyResultHistory.setStartValue(keyResult.getStartValue());
      keyResultHistory.setCurrentValue(keyResult.getCurrentValue());
      keyResultHistory.setTargetValue(keyResult.getTargetValue());
      KeyResultHistory updatedHistory = keyResultHistoryRepository.save(keyResultHistory);
      activityService.createActivity(user, updatedHistory, Action.EDITED);
    } else {
      createKeyResultHistory(user, keyResult);
    }
  }

  public void createKeyResultHistory(User user, KeyResult keyResult) {
    KeyResultHistory newKeyResultHistory = new KeyResultHistory();
    newKeyResultHistory.setKeyResult(keyResult);
    newKeyResultHistory.setDateChanged(LocalDate.now());
    newKeyResultHistory.setStartValue(keyResult.getStartValue());
    newKeyResultHistory.setCurrentValue(keyResult.getCurrentValue());
    newKeyResultHistory.setTargetValue(keyResult.getTargetValue());
    KeyResultHistory createdHistory = keyResultHistoryRepository.save(newKeyResultHistory);
    activityService.createActivity(user, createdHistory, Action.CREATED);
  }
}
