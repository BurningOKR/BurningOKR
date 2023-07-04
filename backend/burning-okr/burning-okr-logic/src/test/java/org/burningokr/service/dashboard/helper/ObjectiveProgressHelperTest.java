package org.burningokr.service.dashboard.helper;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.service.shared.KeyResultBuilder;
import org.burningokr.service.shared.ObjectiveBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ObjectiveProgressHelperTest {
  @Test()
  public void getObjectiveProgressOfObjective_shouldCalculateForNoDays() {
    KeyResult keyResult = KeyResultBuilder.Create().Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();

    LocalDate startDate = LocalDate.of(2023, 7, 4);

    long numberOfDays = 0;

    ArrayList<Double> result = ProgressHelper.getProgressForObjective(objective, startDate, numberOfDays);

    assertEquals(0, result.size());
  }

  @Test()
  public void getObjectiveProgressOfObjective_shouldCalculate0PercentPerDay() {
    KeyResult keyResult = KeyResultBuilder.Create().Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 10;

    ArrayList<Double> result = ProgressHelper.getProgressForObjective(objective, startDate, numberOfDays);

    assertEquals(10, result.size());

    for (Double aDouble : result) {
      assertNull(aDouble);
    }
  }

  @Test()
  public void getObjectiveProgressOfObjective_shouldCalculate100Percent() {
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 10, LocalDate.of(2023, 7, 4))
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();

    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 1;


    ArrayList<Double> result = ProgressHelper.getProgressForObjective(objective, startDate, numberOfDays);

    assertEquals(1, result.size());
    assertEquals(100, result.get(0));
  }

  @Test()
  public void getObjectiveProgressOfObjective_shouldCalculate50Then100Percent() {
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 5))
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 2;

    ArrayList<Double> result = ProgressHelper.getProgressForObjective(objective, startDate, numberOfDays);

    assertEquals(2, result.size());
    assertEquals(50, result.get(0));
    assertEquals(100, result.get(1));
  }

  @Test()
  public void getObjectiveProgressOfObjective_shouldCalculate50Then100PercentBecauseOfTwoKeyResults() {
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 5))
            .Build();
    KeyResult keyResult2 = KeyResultBuilder.Create(20, 20)
            .AddHistory(20, 10, LocalDate.of(2023, 7, 4))
            .AddHistory(20, 20, LocalDate.of(2023, 7, 5))
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).AddKeyResult(keyResult2).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 2;

    ArrayList<Double> result = ProgressHelper.getProgressForObjective(objective, startDate, numberOfDays);

    assertEquals(2, result.size());
    assertEquals(50, result.get(0));
    assertEquals(100, result.get(1));
  }

  @Test()
  public void getObjectiveProgressOfObjective_shouldCalculateCorrectPercentOfTwoKeyResultsDifferentDays() {
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 5))
            .Build();
    KeyResult keyResult2 = KeyResultBuilder.Create(20, 20)
            .AddHistory(20, 10, LocalDate.of(2023, 7, 6))
            .AddHistory(20, 20, LocalDate.of(2023, 7, 7))
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).AddKeyResult(keyResult2).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 4;

    ArrayList<Double> result = ProgressHelper.getProgressForObjective(objective, startDate, numberOfDays);

    assertEquals(4, result.size());
    assertEquals(50, result.get(0));
    assertEquals(100, result.get(1));
    assertEquals(75, result.get(2));
    assertEquals(100, result.get(3));
  }

  @Test()
  public void getObjectiveProgressOfObjective_shouldOutputAllBeforeStartDate00() {
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 2);
    long numberOfDays = 3;

    ArrayList<Double> result = ProgressHelper.getProgressForObjective(objective, startDate, numberOfDays);

    assertEquals(3, result.size());
    assertNull(result.get(0));
    assertNull(result.get(1));
    assertEquals(50, result.get(2));
  }

  @Test()
  public void getObjectiveProgressOfObjective_shouldCalculateSpaceInBetweenHistories() {

    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 7))
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 4;

    ArrayList<Double> result = ProgressHelper.getProgressForObjective(objective, startDate, numberOfDays);

    assertEquals(4, result.size());
    assertEquals(50, result.get(0));
    assertEquals(50, result.get(1));
    assertEquals(50, result.get(2));
    assertEquals(100, result.get(3));
  }
}
