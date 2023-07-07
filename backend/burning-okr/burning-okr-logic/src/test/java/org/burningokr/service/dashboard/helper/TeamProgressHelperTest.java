package org.burningokr.service.dashboard.helper;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.histories.KeyResultHistory;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.service.shared.KeyResultBuilder;
import org.burningokr.service.shared.KeyResultHistoryBuilder;
import org.burningokr.service.shared.ObjectiveBuilder;
import org.burningokr.service.shared.TeamBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TeamProgressHelperTest {
  @Test()
  public void getProgressForTeam_shouldCalculateForNoDays() {
    KeyResult keyResult = new KeyResultBuilder().Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).Build();
  
    LocalDate startDate = LocalDate.of(2023, 7, 4);
  
    long numberOfDays = 0;
  
    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);
  
    assertEquals(0, result.size());
  }

  @Test()
  public void getProgressForTeam_shouldCalculate0PercentPerDay() {
    KeyResult keyResult = new KeyResultBuilder().Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 10;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(10, result.size());

    for (Double aDouble : result) {
      assertNull(aDouble);
    }
  }

  @Test()
  public void getProgressForTeam_shouldCalculate100Percent() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.of(2023, 7, 4)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).Build();

    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 1;


    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(1, result.size());
    assertEquals(100, result.get(0));
  }

  @Test()
  public void getProgressForTeam_shouldCalculate50Then100Percent() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.of(2023, 7, 5)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 2;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(2, result.size());
    assertEquals(50, result.get(0));
    assertEquals(100, result.get(1));
  }

  @Test()
  public void getProgressForTeam_shouldCalculate50Then100PercentBecauseOfTwoKeyResults() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.of(2023, 7, 5)).Build();
    KeyResultHistory keyResultHistory3 = new KeyResultHistoryBuilder().SetBaseInformation(10, 20, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory4 = new KeyResultHistoryBuilder().SetBaseInformation(20, 20, null, LocalDate.of(2023, 7, 5)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    KeyResult keyResult2 = new KeyResultBuilder().SetCurrent(20).SetTarget(20)
            .AddHistory(keyResultHistory3)
            .AddHistory(keyResultHistory4)
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).AddKeyResult(keyResult2).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 2;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(2, result.size());
    assertEquals(50, result.get(0));
    assertEquals(100, result.get(1));
  }

  @Test()
  public void getProgressForTeam_shouldCalculateCorrectPercentOfTwoKeyResultsDifferentDays() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.of(2023, 7, 5)).Build();
    KeyResultHistory keyResultHistory3 = new KeyResultHistoryBuilder().SetBaseInformation(10, 20, null, LocalDate.of(2023, 7, 6)).Build();
    KeyResultHistory keyResultHistory4 = new KeyResultHistoryBuilder().SetBaseInformation(20, 20, null, LocalDate.of(2023, 7, 7)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    KeyResult keyResult2 = new KeyResultBuilder().SetCurrent(20).SetTarget(20)
            .AddHistory(keyResultHistory3)
            .AddHistory(keyResultHistory4)
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).AddKeyResult(keyResult2).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 4;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(4, result.size());
    assertEquals(50, result.get(0));
    assertEquals(100, result.get(1));
    assertEquals(75, result.get(2));
    assertEquals(100, result.get(3));
  }

  @Test()
  public void getProgressForTeam_shouldOutputAllBeforeStartDate00() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.of(2023, 7, 4)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 2);
    long numberOfDays = 3;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(3, result.size());
    assertNull(result.get(0));
    assertNull(result.get(1));
    assertEquals(50, result.get(2));
  }

  @Test()
  public void getProgressForTeam_shouldCalculateSpaceInBetweenHistories() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.of(2023, 7, 7)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 4;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(4, result.size());
    assertEquals(50, result.get(0));
    assertEquals(50, result.get(1));
    assertEquals(50, result.get(2));
    assertEquals(100, result.get(3));
  }

  @Test()
  public void getProgressForTeam_shouldCalculateCorrectPercentOfThreeKeyResultsInTwoObjectivesDifferentDays() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.of(2023, 7, 5)).Build();
    KeyResultHistory keyResultHistory3 = new KeyResultHistoryBuilder().SetBaseInformation(10, 20, null, LocalDate.of(2023, 7, 6)).Build();
    KeyResultHistory keyResultHistory4 = new KeyResultHistoryBuilder().SetBaseInformation(20, 20, null, LocalDate.of(2023, 7, 7)).Build();
    KeyResultHistory keyResultHistory5 = new KeyResultHistoryBuilder().SetBaseInformation(20, 40, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory6 = new KeyResultHistoryBuilder().SetBaseInformation(40, 40, null, LocalDate.of(2023, 7, 8)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    KeyResult keyResult2 = new KeyResultBuilder().SetCurrent(20).SetTarget(20)
            .AddHistory(keyResultHistory3)
            .AddHistory(keyResultHistory4)
            .Build();
    KeyResult keyResult3 = new KeyResultBuilder().SetCurrent(20).SetTarget(20)
            .AddHistory(keyResultHistory5)
            .AddHistory(keyResultHistory6)
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).AddKeyResult(keyResult2).Build();
    Objective objective2 = ObjectiveBuilder.Create().AddKeyResult(keyResult3).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).AddObjective(objective2).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 5;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(5, result.size());
    assertEquals(50, result.get(0));
    assertEquals(75, result.get(1));
    assertEquals(62.5, result.get(2));
    assertEquals(75, result.get(3));
    assertEquals(100, result.get(4));
  }

  @Test()
  public void getProgressForTeam_shouldCalculateSameAsBeforeButWithBranch() {
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.of(2023, 7, 5)).Build();
    KeyResultHistory keyResultHistory3 = new KeyResultHistoryBuilder().SetBaseInformation(10, 20, null, LocalDate.of(2023, 7, 6)).Build();
    KeyResultHistory keyResultHistory4 = new KeyResultHistoryBuilder().SetBaseInformation(20, 20, null, LocalDate.of(2023, 7, 7)).Build();
    KeyResultHistory keyResultHistory5 = new KeyResultHistoryBuilder().SetBaseInformation(20, 40, null, LocalDate.of(2023, 7, 4)).Build();
    KeyResultHistory keyResultHistory6 = new KeyResultHistoryBuilder().SetBaseInformation(40, 40, null, LocalDate.of(2023, 7, 8)).Build();

    KeyResult keyResult = new KeyResultBuilder().SetCurrent(10).SetTarget(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    KeyResult keyResult2 = new KeyResultBuilder().SetCurrent(20).SetTarget(20)
            .AddHistory(keyResultHistory3)
            .AddHistory(keyResultHistory4)
            .Build();
    KeyResult keyResult3 = new KeyResultBuilder().SetCurrent(20).SetTarget(20)
            .AddHistory(keyResultHistory5)
            .AddHistory(keyResultHistory6)
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).AddKeyResult(keyResult2).Build();
    Objective objective2 = ObjectiveBuilder.Create().AddKeyResult(keyResult3).Build();
    OkrChildUnit team = TeamBuilder.CreateBranch().AddObjective(objective).AddObjective(objective2).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 5;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(5, result.size());
    assertEquals(50, result.get(0));
    assertEquals(75, result.get(1));
    assertEquals(62.5, result.get(2));
    assertEquals(75, result.get(3));
    assertEquals(100, result.get(4));
  }
}
