package org.burningokr.dto.dashboard;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LineChartOptionsDto extends BaseChartOptionsDto{
  String[] xAxisCategories;
  LineChartLineKeyValues[] series;

}

@Data
class LineChartLineKeyValues {
  String name;
  double[] number;
}
