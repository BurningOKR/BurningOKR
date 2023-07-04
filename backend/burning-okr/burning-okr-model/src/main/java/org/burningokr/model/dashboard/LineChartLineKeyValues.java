package org.burningokr.model.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class LineChartLineKeyValues {
  String name;
  List<Double> data;
}
