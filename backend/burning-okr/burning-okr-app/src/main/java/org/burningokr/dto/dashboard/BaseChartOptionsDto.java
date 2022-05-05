package org.burningokr.dto.dashboard;

import lombok.Data;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;

@Data
public abstract class BaseChartOptionsDto {
  String title;
  ChartInformationTypeEnum chart;
}
