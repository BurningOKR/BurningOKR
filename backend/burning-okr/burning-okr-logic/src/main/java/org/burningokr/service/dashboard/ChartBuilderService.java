package org.burningokr.service.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.dashboard.BaseChartOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChartBuilderService {
  private final PieChartService pieChartService;
  private final LineChartService lineChartService;

  public BaseChartOptionsDto buildChart(ChartCreationOptions chartCreationOptions, Long companyId) {
    BaseChartOptionsDto chartOptionsDto;
    switch (chartCreationOptions.getChartType()) {
      case PIE_TOPICDRAFTOVERVIEW ->
              chartOptionsDto = pieChartService.buildTopicDraftOverviewChart(chartCreationOptions);
      case LINE_PROGRESS -> {
        chartOptionsDto = lineChartService.buildProgressChart(chartCreationOptions, companyId);
        chartOptionsDto.setSelectedTeamIds(chartCreationOptions.getTeamIds());
      }
      default -> {
        log.warn("Unknown enum value encountered while mapping ChartInformationType!");
        throw new IllegalStateException("Unknown enum value encountered while mapping ChartInformationType!");
      }
    }

    return chartOptionsDto;
  }
}
