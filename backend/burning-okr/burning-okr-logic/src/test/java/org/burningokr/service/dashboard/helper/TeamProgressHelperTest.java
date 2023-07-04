package org.burningokr.service.dashboard.helper;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.service.shared.KeyResultBuilder;
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
    KeyResult keyResult = KeyResultBuilder.Create().Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).Build();
  
    LocalDate startDate = LocalDate.of(2023, 7, 4);
  
    long numberOfDays = 0;
  
    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);
  
    assertEquals(0, result.size());
  }

  @Test()
  public void getProgressForTeam_shouldCalculate0PercentPerDay() {
    KeyResult keyResult = KeyResultBuilder.Create().Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 10;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(10, result.size());

    for(int i = 0; i < result.size(); i++)
    {
      assertNull(result.get(i));
    }
  }

  @Test()
  public void getProgressForTeam_shouldCalculate100Percent() {
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 10, LocalDate.of(2023, 7, 4))
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
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 5))
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
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 5))
            .Build();
    KeyResult keyResult2 = KeyResultBuilder.Create(20, 20)
            .AddHistory(20, 10, LocalDate.of(2023, 7, 4))
            .AddHistory(20, 20, LocalDate.of(2023, 7, 5))
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
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 5))
            .Build();
    KeyResult keyResult2 = KeyResultBuilder.Create(20, 20)
            .AddHistory(20, 10, LocalDate.of(2023, 7, 6))
            .AddHistory(20, 20, LocalDate.of(2023, 7, 7))
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
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
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

    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 7))
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
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 5))
            .Build();
    KeyResult keyResult2 = KeyResultBuilder.Create(20, 20)
            .AddHistory(20, 10, LocalDate.of(2023, 7, 6))
            .AddHistory(20, 20, LocalDate.of(2023, 7, 7))
            .Build();
    KeyResult keyResult3 = KeyResultBuilder.Create(20, 20)
            .AddHistory(40, 20, LocalDate.of(2023, 7, 4))
            .AddHistory(40, 40, LocalDate.of(2023, 7, 8))
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).AddKeyResult(keyResult2).Build();
    Objective objective2 = ObjectiveBuilder.Create().AddKeyResult(keyResult3).Build();
    OkrChildUnit team = TeamBuilder.CreateDepartment().AddObjective(objective).AddObjective(objective2).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 5;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(5, result.size());
    assertEquals(50, result.get(0));
    assertEquals(100, result.get(1));
    assertEquals(75, result.get(2));
    assertEquals(100, result.get(3));
    assertEquals(100, result.get(4));
  }

  @Test()
  public void getProgressForTeam_shouldCalculateSameAsBeforeButWithBranch() {
    KeyResult keyResult = KeyResultBuilder.Create(10, 10)
            .AddHistory(10, 5, LocalDate.of(2023, 7, 4))
            .AddHistory(10, 10, LocalDate.of(2023, 7, 5))
            .Build();
    KeyResult keyResult2 = KeyResultBuilder.Create(20, 20)
            .AddHistory(20, 10, LocalDate.of(2023, 7, 6))
            .AddHistory(20, 20, LocalDate.of(2023, 7, 7))
            .Build();
    KeyResult keyResult3 = KeyResultBuilder.Create(20, 20)
            .AddHistory(40, 20, LocalDate.of(2023, 7, 4))
            .AddHistory(40, 40, LocalDate.of(2023, 7, 8))
            .Build();
    Objective objective = ObjectiveBuilder.Create().AddKeyResult(keyResult).AddKeyResult(keyResult2).Build();
    Objective objective2 = ObjectiveBuilder.Create().AddKeyResult(keyResult3).Build();
    OkrChildUnit team = TeamBuilder.CreateBranch().AddObjective(objective).AddObjective(objective2).Build();
    LocalDate startDate = LocalDate.of(2023, 7, 4);
    long numberOfDays = 5;

    ArrayList<Double> result = ProgressHelper.getProgressForTeam(team, startDate, numberOfDays);

    assertEquals(5, result.size());
    assertEquals(50, result.get(0));
    assertEquals(100, result.get(1));
    assertEquals(75, result.get(2));
    assertEquals(100, result.get(3));
    assertEquals(100, result.get(4));
  }
}
