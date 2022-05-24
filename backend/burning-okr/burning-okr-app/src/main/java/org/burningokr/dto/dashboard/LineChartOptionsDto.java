package org.burningokr.dto.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LineChartOptionsDto extends BaseChartOptionsDto {
  Collection<String> xAxisCategories = new ArrayList<>();
  Collection<LineChartLineKeyValues> series = new ArrayList<>();
}
