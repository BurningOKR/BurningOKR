package org.burningokr.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.burningokr.dto.dashboard.BaseChartOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

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

    //chartOptionsDto.setUserIDs(chartCreationOptions.getTeamIds().toArray(new Long[0])); //NEW
    //TODO
    chartOptionsDto.setTeamIDs(ArrayUtils.toPrimitive(chartCreationOptions.getTeamIds().toArray(new Long[0])));
    System.out.println("Team IDs as long[]: " + chartOptionsDto.getTeamIDs());
    return chartOptionsDto;
  }
}
