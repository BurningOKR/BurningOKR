package org.burningokr.service.dashboard.helper;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.service.shared.KeyResultBuilder;
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

    for(int i = 0; i < result.size(); i++)
    {
      assertNull(result.get(i));
    }
  }

  @Test()
  public void getKeyResultProgressOfKeyResult_shouldCalculate100Percent() {
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
                                          .AddHistory(10, 10, LocalDate.of(2023, 7, 4))
                                          .Build();

    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 1;


    ArrayList<Double> result = ProgressHelper.getKeyResultProgressOfKeyResult(keyResult, startDate, numberOfDays);

    assertEquals(1, result.size());
    assertEquals(100, result.get(0));
  }

  @Test()
  public void getKeyResultProgressOfKeyResult_shouldCalculate50Then100Percent() {

    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 5))
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

    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
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

    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 7))
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
