package org.burningokr.dto.dashboard;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = false)
public class LineChartOptionsDto extends BaseChartOptionsDto {
  Collection<String> xAxisCategories = new ArrayList<>();
  Collection<LineChartLineKeyValues> series = new ArrayList<>();
}

