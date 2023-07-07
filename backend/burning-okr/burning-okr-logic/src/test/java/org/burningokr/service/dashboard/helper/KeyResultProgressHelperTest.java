package org.burningokr.service.dashboard.helper;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.histories.KeyResultHistory;
import org.burningokr.service.shared.KeyResultBuilder;
import org.burningokr.service.shared.KeyResultHistoryBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class KeyResultProgressHelperTest {
  @Test()
  public void getKeyResultProgressOfKeyResult_shouldCalculateForNoDays() {
    KeyResult keyResult = new KeyResult();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 0;

    ArrayList<Double> result = ProgressHelper.getKeyResultProgressOfKeyResult(keyResult, startDate, numberOfDays);

    assertEquals(0, result.size());
  }

  @Test()
  public void getKeyResultProgressOfKeyResult_shouldCalculate0PercentPerDay() {
    KeyResult keyResult = new KeyResult();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 10;

    ArrayList<Double> result = ProgressHelper.getKeyResultProgressOfKeyResult(keyResult, startDate, numberOfDays);

    assertEquals(10, result.size());

    for (Double aDouble : result) {
      assertNull(aDouble);
    }
  }

  @Test()
  public void getKeyResultProgressOfKeyResult_shouldCalculate100Percent() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.of(2023, 7, 4)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
                                          .AddHistory(keyResultHistory1)
                                          .Build();

    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 1;


    ArrayList<Double> result = ProgressHelper.getKeyResultProgressOfKeyResult(keyResult, startDate, numberOfDays);

    assertEquals(1, result.size());
    assertEquals(100, result.get(0));
  }

  @Test()
  public void getKeyResultProgressOfKeyResult_shouldNotThrowDivisionByZeroException() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, 10L, LocalDate.of(2023, 7, 4)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetStart(10).SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .Build();

    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 1;


    ArrayList<Double> result = ProgressHelper.getKeyResultProgressOfKeyResult(keyResult, startDate, numberOfDays);

    assertEquals(1, result.size());
    assertEquals(0, result.get(0));
  }

  @Test()
  public void getKeyResultProgressOfKeyResult_shouldCalculate50Then100Percent() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.of(2023, 7, 5)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 2;

    ArrayList<Double> result = ProgressHelper.getKeyResultProgressOfKeyResult(keyResult, startDate, numberOfDays);

    assertEquals(2, result.size());
    assertEquals(50, result.get(0));
    assertEquals(100, result.get(1));
  }

  @Test()
  public void getKeyResultProgressOfKeyResult_shouldOutputAllBeforeStartDate00() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.of(2023, 7, 4)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .Build();
    LocalDate startDate = LocalDate.of(2023, 7, 2);
    long numberOfDays = 3;

    ArrayList<Double> result = ProgressHelper.getKeyResultProgressOfKeyResult(keyResult, startDate, numberOfDays);

    assertEquals(3, result.size());
    assertNull(result.get(0));
    assertNull(result.get(1));
    assertEquals(50, result.get(2));
  }

  @Test()
  public void getKeyResultProgressOfKeyResult_shouldCalculateSpaceInBetweenHistories() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.of(2023, 7, 7)).Build();
    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 4;

    ArrayList<Double> result = ProgressHelper.getKeyResultProgressOfKeyResult(keyResult, startDate, numberOfDays);

    assertEquals(4, result.size());
    assertEquals(50, result.get(0));
    assertEquals(50, result.get(1));
    assertEquals(50, result.get(2));
    assertEquals(100, result.get(3));
  }
}
