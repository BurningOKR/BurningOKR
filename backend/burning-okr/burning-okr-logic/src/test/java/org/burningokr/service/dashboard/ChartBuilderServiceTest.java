package org.burningokr.service.dashboard;

import org.burningokr.model.dashboard.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChartBuilderServiceTest {
  @Mock
  private PieChartService pieChartService;
  @Mock
  private LineChartService lineChartService;
  @InjectMocks
  private ChartBuilderService chartBuilderService;

  @Test()
  public void buildChart_shouldBuildNonEmptyPieChart() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW);
    PieChartOptionsDto expected = getPieChartOptions();
    when(pieChartService.buildTopicDraftOverviewChart(chartCreationOptions)).thenReturn(expected);
    
    BaseChartOptionsDto result = chartBuilderService.buildChart(chartCreationOptions, 10L);

    assertNotNull(result);
    assertSame(expected, result);
  }

  @Test()
  public void buildChart_shouldBuildNonEmptyLineChart() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    Collection<Long> teamIds = List.of(10L, 20L);
    chartCreationOptions.setTeamIds(teamIds);
    LineChartOptionsDto expected = getLineChartOptions();
    when(lineChartService.buildProgressChart(chartCreationOptions, 10L)).thenReturn(expected);

    BaseChartOptionsDto result = chartBuilderService.buildChart(chartCreationOptions, 10L);

    assertNotNull(result);
    assertSame(expected, result);
    assertSame(teamIds, result.getSelectedTeamIds());
  }

  private PieChartOptionsDto getPieChartOptions()
  {
    PieChartOptionsDto pieChartOptionsDto = new PieChartOptionsDto();
    pieChartOptionsDto.setId(1L);
    pieChartOptionsDto.setTitle("Titel");
    pieChartOptionsDto.setValueLabels(List.of("asdf").toArray(new String[0]));
    pieChartOptionsDto.setSeries(List.of(10D, 20D, 30D, 40D).toArray(new Double[0]));
    pieChartOptionsDto.setChartType(ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW.ordinal());

    return pieChartOptionsDto;
  }
  private LineChartOptionsDto getLineChartOptions()
  {
    LineChartOptionsDto lineChartOptionsDto = new LineChartOptionsDto();
    lineChartOptionsDto.setId(1L);
    lineChartOptionsDto.setTitle("Titel");
    lineChartOptionsDto.setSeries(List.of(new LineChartLineKeyValues()));
    lineChartOptionsDto.setChartType(ChartInformationTypeEnum.LINE_PROGRESS.ordinal());

    return lineChartOptionsDto;
  }
}
