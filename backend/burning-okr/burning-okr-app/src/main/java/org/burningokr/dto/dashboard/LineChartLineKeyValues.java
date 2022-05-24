package org.burningokr.dto.dashboard;

import java.util.ArrayList;
import lombok.Data;

@Data
public class LineChartLineKeyValues {
  String name;
  ArrayList<Double> data;
}
