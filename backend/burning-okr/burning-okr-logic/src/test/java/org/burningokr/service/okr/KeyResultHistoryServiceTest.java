package org.burningokr.service.okr;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.histories.KeyResultHistory;
import org.burningokr.repositories.okr.KeyResultHistoryRepository;
import org.burningokr.service.activity.ActivityService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KeyResultHistoryServiceTest {
  private static long keyResultHistoryId;
  private static long keyResultId;
  private static long originalStartValue;
  private static long originalCurrentValue;
  private static long originalTargetValue;
  private static long changedStartValue;
  private static long changedCurrentValue;
  private static long changedTargetValue;
  private static LocalDate dateChangedToday;
  private static LocalDate dateChangedYesterday;
  private static KeyResult keyResult;
  private static KeyResultHistory originalKeyResultHistory;
  private static KeyResultHistory changedKeyResultHistory;
  @Mock
  private KeyResultHistoryRepository keyResultHistoryRepository;
  @Mock
  private ActivityService activityService;
  @InjectMocks
  private KeyResultHistoryService keyResultHistoryService;

  @BeforeAll
  public static void Init() {
    keyResultHistoryId = 100L;
    keyResultId = 200L;
    originalStartValue = 0L;
    originalCurrentValue = 50L;
    originalTargetValue = 100L;
    changedStartValue = 200L;
    changedCurrentValue = 250L;
    changedTargetValue = 300L;
    dateChangedToday = LocalDate.now();
    dateChangedYesterday = LocalDate.now().minusDays(1);
  }

  @BeforeEach
  public void Refresh() {
    keyResult = createKeyResult();
    originalKeyResultHistory = createOriginalKeyResultHistory();
    changedKeyResultHistory = createChangedKeyResultHistory();
  }

  private KeyResult createKeyResult() {
    KeyResult keyResult = new KeyResult();
    keyResult.setId(keyResultId);
    keyResult.setStartValue(changedStartValue);
    keyResult.setCurrentValue(changedCurrentValue);
    keyResult.setTargetValue(changedTargetValue);
    return keyResult;
  }

  private KeyResultHistory createOriginalKeyResultHistory() {
    KeyResultHistory keyResultHistory = new KeyResultHistory();
    keyResultHistory.setId(keyResultHistoryId);
    keyResultHistory.setStartValue(originalStartValue);
    keyResultHistory.setCurrentValue(originalCurrentValue);
    keyResultHistory.setTargetValue(originalTargetValue);
    keyResultHistory.setDateChanged(dateChangedToday);
    keyResultHistory.setKeyResult(keyResult);
    return keyResultHistory;
  }

  private KeyResultHistory createChangedKeyResultHistory() {
    KeyResultHistory keyResultHistory = new KeyResultHistory();
    keyResultHistory.setId(keyResultHistoryId);
    keyResultHistory.setStartValue(changedStartValue);
    keyResultHistory.setCurrentValue(changedCurrentValue);
    keyResultHistory.setTargetValue(changedTargetValue);
    keyResultHistory.setDateChanged(dateChangedToday);
    keyResultHistory.setKeyResult(keyResult);
    return keyResultHistory;
  }

  @Test
  public void updateKeyResultHistory_shouldUpdateExistingKeyResultHistory() {
    ArrayList<KeyResultHistory> keyResultHistories = new ArrayList<>();
    keyResultHistories.add(originalKeyResultHistory);
    when(keyResultHistoryRepository.findByKeyResultOrderByDateChangedAsc(keyResult))
      .thenReturn(keyResultHistories);

    keyResultHistoryService.updateKeyResultHistory(keyResult);

    verify(keyResultHistoryRepository).save(changedKeyResultHistory);
  }

  @Test
  public void updateKeyResultHistory_expectedActivityCall() {
    ArrayList<KeyResultHistory> keyResultHistories = new ArrayList<>();
    keyResultHistories.add(originalKeyResultHistory);
    when(keyResultHistoryRepository.findByKeyResultOrderByDateChangedAsc(keyResult))
      .thenReturn(keyResultHistories);
    when(keyResultHistoryRepository.save(changedKeyResultHistory))
      .thenReturn(changedKeyResultHistory);

    keyResultHistoryService.updateKeyResultHistory(keyResult);

    verify(activityService).createActivity(changedKeyResultHistory, Action.EDITED);
  }

  @Test
  public void updateKeyResultHistory_shouldCreateKeyResultHistory() {
    originalKeyResultHistory.setDateChanged(dateChangedYesterday);
    changedKeyResultHistory.setId(null);
    ArrayList<KeyResultHistory> keyResultHistories = new ArrayList<>();
    keyResultHistories.add(originalKeyResultHistory);
    when(keyResultHistoryRepository.findByKeyResultOrderByDateChangedAsc(keyResult))
      .thenReturn(keyResultHistories);

    keyResultHistoryService.updateKeyResultHistory(keyResult);

    verify(keyResultHistoryRepository).save(changedKeyResultHistory);
  }

  @Test
  public void createKeyResultHistory_expectedActivityCall() {
    changedKeyResultHistory.setId(null);
    when(keyResultHistoryRepository.save(changedKeyResultHistory))
      .thenReturn(changedKeyResultHistory);

    keyResultHistoryService.createKeyResultHistory(keyResult);

    verify(activityService).createActivity(changedKeyResultHistory, Action.CREATED);
  }
}
