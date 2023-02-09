package org.burningokr.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.dashboard.BaseChartOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChartBuilderService {
  private final PieChartService pieChartService;
  private final LineChartService lineChartService;

  public BaseChartOptionsDto buildChart(ChartCreationOptions chartCreationOptions) {
    BaseChartOptionsDto chartOptionsDto;
    switch (chartCreationOptions.getChartType()) {
      case PIE_TOPICDRAFTOVERVIEW:
        chartOptionsDto = pieChartService.buildTopicDraftOverviewChart(chartCreationOptions);
        break;
      default:
        chartOptionsDto = lineChartService.buildProgressChart(chartCreationOptions);
        break;
    }

    chartOptionsDto.setTitle(chartCreationOptions.getTitle());
    return chartOptionsDto;
  }
}
