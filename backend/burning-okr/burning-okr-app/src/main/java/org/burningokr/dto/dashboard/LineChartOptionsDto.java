package org.burningokr.dto.dashboard;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class LineChartOptionsDto extends BaseChartOptionsDto {
  Collection<String> xAxisCategories;
  Collection<LineChartLineKeyValues> series;
}

