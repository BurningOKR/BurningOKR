package org.burningokr.dto.dashboard;

import lombok.Data;

@Data
public class LineChartOptionsDto extends BaseChartOptionsDto{
  String[] xAxisCategories;
  LineChartLineKeyValues[] series;

}

@Data
class LineChartLineKeyValues {
  String name;
  double[] number;
}
