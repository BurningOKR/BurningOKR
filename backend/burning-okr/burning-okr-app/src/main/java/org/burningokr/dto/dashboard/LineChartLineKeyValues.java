package org.burningokr.dto.dashboard;

import lombok.Data;

import java.util.ArrayList;

@Data
public class LineChartLineKeyValues {
  String name;
  ArrayList<Double> data;
}
