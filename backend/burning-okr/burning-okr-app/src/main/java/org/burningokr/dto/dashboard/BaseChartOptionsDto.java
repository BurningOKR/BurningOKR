package org.burningokr.dto.dashboard;

import lombok.Data;

@Data
public abstract class BaseChartOptionsDto {
  String title;
  int chart;
  long[] teamIDs; // NEW
}
