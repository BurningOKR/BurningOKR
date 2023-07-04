package org.burningokr.model.dashboard;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PieChartOptionsDto extends BaseChartOptionsDto {
  Double[] series;
  String[] valueLabels;
}
