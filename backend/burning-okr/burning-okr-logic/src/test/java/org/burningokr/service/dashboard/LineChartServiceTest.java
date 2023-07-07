package org.burningokr.service.dashboard;

import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;
import org.burningokr.model.dashboard.LineChartLineKeyValues;
import org.burningokr.model.dashboard.LineChartOptionsDto;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.histories.KeyResultHistory;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okr.KeyResultHistoryService;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.shared.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineChartServiceTest {
  @Mock
  private CompanyService companyService;
  @Mock
  private KeyResultHistoryService keyResultHistoryService;

  @InjectMocks
  private LineChartService lineChartService;

  @Test()
  public void buildProgressChart_shouldBuildNonEmptyPieChart() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    Collection<Long> teamIds = List.of(10L, 20L);
    chartCreationOptions.setTeamIds(teamIds);
    chartCreationOptions.setTitle("TestTitle");
    long companyId = 11L;
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.now().minusDays(5)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.now().minusDays(4)).Build();
    KeyResultHistory keyResultHistory3 = new KeyResultHistoryBuilder().SetBaseInformation(10, 20, null, LocalDate.now().minusDays(3)).Build();
    KeyResultHistory keyResultHistory4 = new KeyResultHistoryBuilder().SetBaseInformation(20, 20, null, LocalDate.now().minusDays(2)).Build();
    KeyResultHistory keyResultHistory5 = new KeyResultHistoryBuilder().SetBaseInformation(10, 20, null, LocalDate.now().minusDays(2)).Build();
    KeyResultHistory keyResultHistory6 = new KeyResultHistoryBuilder().SetBaseInformation(20, 20, null, LocalDate.now().minusDays(1)).Build();
    KeyResult keyResult = new KeyResultBuilder()
            .SetTarget(10).SetCurrent(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    KeyResult keyResult2 = new KeyResultBuilder()
            .SetTarget(20).SetCurrent(20)
            .AddHistory(keyResultHistory3)
            .AddHistory(keyResultHistory4)
            .Build();
    KeyResult keyResult3 = new KeyResultBuilder()
            .SetTarget(20).SetCurrent(20)
            .AddHistory(keyResultHistory5)
            .AddHistory(keyResultHistory6)
            .Build();
    Objective objective1 = ObjectiveBuilder.Create().AddKeyResult(keyResult).AddKeyResult(keyResult2).Build();
    Objective objective2 = ObjectiveBuilder.Create().AddKeyResult(keyResult3).Build();
    OkrChildUnit team1 = TeamBuilder.CreateDepartment(10L).AddObjective(objective1).Build();
    OkrChildUnit team2 = TeamBuilder.CreateDepartment(20L).AddObjective(objective2).Build();
    OkrCompany company = new CompanyBuilder().AddChildUnits(team1).AddChildUnits(team2).Build();
    when(keyResultHistoryService.findOldestKeyResultHistoryForKeyResultList(any())).thenReturn(keyResult.getKeyResultHistory().stream().toList().get(0));
    when(companyService.findById(companyId)).thenReturn(company);

    LineChartOptionsDto result = lineChartService.buildProgressChart(chartCreationOptions, companyId);

    assertNotNull(result);
    assertEquals(2, result.getSeries().size());
    List<Double> firstLineData = result.getSeries().toArray(new LineChartLineKeyValues[0])[0].getData();
    List<Double> secondLineData = result.getSeries().toArray(new LineChartLineKeyValues[0])[1].getData();
    assertEquals(6, firstLineData.size());
    assertEquals(6, secondLineData.size());
    assertEquals(50, firstLineData.get(0));
    assertEquals(100, firstLineData.get(1));
    assertEquals(75, firstLineData.get(2));
    assertEquals(100, firstLineData.get(3));
    assertEquals(100, firstLineData.get(4));
    assertEquals(100, firstLineData.get(5));
    assertNull(secondLineData.get(0));
    assertNull(secondLineData.get(1));
    assertNull(secondLineData.get(2));
    assertEquals(50, secondLineData.get(3));
    assertEquals(100, secondLineData.get(4));
    assertEquals(100, secondLineData.get(5));
    assertEquals("TestTitle", result.getTitle());
  }

  @Test()
  public void buildProgressChart_shouldBuildNonEmptyPieChartWithAllTeams() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    Collection<Long> teamIds = new ArrayList<>();
    chartCreationOptions.setTeamIds(teamIds);
    chartCreationOptions.setTitle("TestTitle");
    long companyId = 11L;
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(10, 20, null, LocalDate.now().minusDays(3)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(20, 20, null, LocalDate.now().minusDays(2)).Build();
    KeyResultHistory keyResultHistory3 = new KeyResultHistoryBuilder().SetBaseInformation(10, 20, null, LocalDate.now().minusDays(2)).Build();
    KeyResultHistory keyResultHistory4 = new KeyResultHistoryBuilder().SetBaseInformation(20, 20, null, LocalDate.now().minusDays(1)).Build();
    KeyResult keyResult2 = new KeyResultBuilder()
            .SetTarget(20).SetCurrent(20)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    KeyResult keyResult3 = new KeyResultBuilder()
            .SetTarget(20).SetCurrent(20)
            .AddHistory(keyResultHistory3)
            .AddHistory(keyResultHistory4)
            .Build();
    Objective objective1 = ObjectiveBuilder.Create().AddKeyResult(keyResult2).Build();
    Objective objective2 = ObjectiveBuilder.Create().AddKeyResult(keyResult3).Build();
    OkrChildUnit team1 = TeamBuilder.CreateDepartment(10L).AddObjective(objective1).Build();
    OkrChildUnit team2 = TeamBuilder.CreateDepartment(20L).AddObjective(objective2).Build();
    OkrCompany company = new CompanyBuilder().AddChildUnits(team1).AddChildUnits(team2).Build();
    when(keyResultHistoryService.findOldestKeyResultHistoryForKeyResultList(any())).thenReturn(keyResult2.getKeyResultHistory().stream().toList().get(0));
    when(companyService.findById(companyId)).thenReturn(company);

    LineChartOptionsDto result = lineChartService.buildProgressChart(chartCreationOptions, companyId);

    assertNotNull(result);
    assertEquals(1, result.getSeries().size());
    List<Double> firstLineData = result.getSeries().toArray(new LineChartLineKeyValues[0])[0].getData();
    assertEquals(4, firstLineData.size());
    assertEquals(50, firstLineData.get(0));
    assertEquals(75, firstLineData.get(1));
    assertEquals(100, firstLineData.get(2));
    assertEquals(100, firstLineData.get(3));
    assertEquals("TestTitle", result.getTitle());
  }

  @Test()
  public void buildProgressChart_shouldBuildNonEmptyPieChartButOnlyOneSeries() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    Collection<Long> teamIds = List.of(10L);
    chartCreationOptions.setTeamIds(teamIds);
    chartCreationOptions.setTitle("TestTitle");
    long companyId = 11L;
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.now().minusDays(5)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.now().minusDays(4)).Build();
    KeyResultHistory keyResultHistory3 = new KeyResultHistoryBuilder().SetBaseInformation(10, 20, null, LocalDate.now().minusDays(3)).Build();
    KeyResultHistory keyResultHistory4 = new KeyResultHistoryBuilder().SetBaseInformation(20, 20, null, LocalDate.now().minusDays(2)).Build();
    KeyResultHistory keyResultHistory5 = new KeyResultHistoryBuilder().SetBaseInformation(10, 20, null, LocalDate.now().minusDays(2)).Build();
    KeyResultHistory keyResultHistory6 = new KeyResultHistoryBuilder().SetBaseInformation(20, 20, null, LocalDate.now().minusDays(1)).Build();
    KeyResult keyResult = new KeyResultBuilder()
            .SetTarget(10).SetCurrent(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    KeyResult keyResult2 = new KeyResultBuilder()
            .SetTarget(20).SetCurrent(20)
            .AddHistory(keyResultHistory3)
            .AddHistory(keyResultHistory4)
            .Build();
    KeyResult keyResult3 = new KeyResultBuilder()
            .SetTarget(20).SetCurrent(20)
            .AddHistory(keyResultHistory5)
            .AddHistory(keyResultHistory6)
            .Build();
    Objective objective1 = ObjectiveBuilder.Create().AddKeyResult(keyResult).AddKeyResult(keyResult2).Build();
    Objective objective2 = ObjectiveBuilder.Create().AddKeyResult(keyResult3).Build();
    OkrChildUnit team1 = TeamBuilder.CreateDepartment(10L).AddObjective(objective1).Build();
    OkrChildUnit team2 = TeamBuilder.CreateDepartment(20L).AddObjective(objective2).Build();
    OkrCompany company = new CompanyBuilder().AddChildUnits(team1).AddChildUnits(team2).Build();
    when(keyResultHistoryService.findOldestKeyResultHistoryForKeyResultList(any())).thenReturn(keyResult.getKeyResultHistory().stream().toList().get(0));
    when(companyService.findById(companyId)).thenReturn(company);

    LineChartOptionsDto result = lineChartService.buildProgressChart(chartCreationOptions, companyId);

    assertNotNull(result);
    assertEquals(1, result.getSeries().size());
    List<Double> firstLineData = result.getSeries().toArray(new LineChartLineKeyValues[0])[0].getData();
    assertEquals(6, firstLineData.size());
    assertEquals(50, firstLineData.get(0));
    assertEquals(100, firstLineData.get(1));
    assertEquals(75, firstLineData.get(2));
    assertEquals(100, firstLineData.get(3));
    assertEquals(100, firstLineData.get(4));
    assertEquals(100, firstLineData.get(5));
    assertEquals("TestTitle", result.getTitle());
  }

  @Test()
  public void buildProgressChart_shouldBuildCorrectXAxisLabels() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    Collection<Long> teamIds = List.of(10L);
    chartCreationOptions.setTeamIds(teamIds);
    chartCreationOptions.setTitle("TestTitle");
    long companyId = 11L;
    KeyResultHistory keyResultHistory1 = new KeyResultHistoryBuilder().SetBaseInformation(5, 10, null, LocalDate.now().minusDays(5)).Build();
    KeyResultHistory keyResultHistory2 = new KeyResultHistoryBuilder().SetBaseInformation(10, 10, null, LocalDate.now().minusDays(4)).Build();
    KeyResult keyResult = new KeyResultBuilder()
            .SetTarget(10).SetCurrent(10)
            .AddHistory(keyResultHistory1)
            .AddHistory(keyResultHistory2)
            .Build();
    Objective objective1 = ObjectiveBuilder.Create().AddKeyResult(keyResult).Build();
    OkrChildUnit team1 = TeamBuilder.CreateDepartment(10L).AddObjective(objective1).Build();
    OkrCompany company = new CompanyBuilder().AddChildUnits(team1).Build();
    when(keyResultHistoryService.findOldestKeyResultHistoryForKeyResultList(any())).thenReturn(keyResult.getKeyResultHistory().stream().toList().get(0));
    when(companyService.findById(companyId)).thenReturn(company);

    LineChartOptionsDto result = lineChartService.buildProgressChart(chartCreationOptions, companyId);

    assertNotNull(result);
    assertEquals(6, result.getXAxisCategories().size());
    String[] xAxisLabels = result.getXAxisCategories().toArray(new String[0]);
    assertEquals(LocalDate.now().minusDays(5).toString(), xAxisLabels[0]);
    assertEquals(LocalDate.now().minusDays(4).toString(), xAxisLabels[1]);
    assertEquals(LocalDate.now().minusDays(3).toString(), xAxisLabels[2]);
    assertEquals(LocalDate.now().minusDays(2).toString(), xAxisLabels[3]);
    assertEquals(LocalDate.now().minusDays(1).toString(), xAxisLabels[4]);
    assertEquals(LocalDate.now().toString(), xAxisLabels[5]);
    assertEquals("TestTitle", result.getTitle());
  }

  @Test()
  public void buildProgressChart_shouldReturnEmptyStatistic() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    Collection<Long> teamIds = new ArrayList<>();
    chartCreationOptions.setTeamIds(teamIds);
    chartCreationOptions.setTitle("TestTitle");
    long companyId = 11L;
    Objective objective1 = ObjectiveBuilder.Create().Build();
    Objective objective2 = ObjectiveBuilder.Create().Build();
    OkrChildUnit team1 = TeamBuilder.CreateDepartment(10L).AddObjective(objective1).Build();
    OkrChildUnit team2 = TeamBuilder.CreateDepartment(20L).AddObjective(objective2).Build();
    OkrCompany company = new CompanyBuilder().AddChildUnits(team1).AddChildUnits(team2).Build();
    when(companyService.findById(companyId)).thenReturn(company);

    LineChartOptionsDto result = lineChartService.buildProgressChart(chartCreationOptions, companyId);

    assertNotNull(result);
    assertEquals(1, result.getSeries().size());
    List<Double> firstLineData = result.getSeries().toArray(new LineChartLineKeyValues[0])[0].getData();
    assertEquals(1, firstLineData.size());
    assertEquals(50, firstLineData.get(0));
    assertEquals("TestTitle", result.getTitle());
  }
}
