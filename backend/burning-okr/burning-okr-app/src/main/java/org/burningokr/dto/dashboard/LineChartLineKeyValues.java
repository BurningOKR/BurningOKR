package org.burningokr.dto.dashboard;

import lombok.Data;

import java.util.Collection;

@Data
public class LineChartLineKeyValues {
  String name;
  Collection<Double> data;
}
