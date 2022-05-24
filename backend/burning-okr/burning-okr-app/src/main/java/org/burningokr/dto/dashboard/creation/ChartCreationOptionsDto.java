package org.burningokr.dto.dashboard.creation;

import java.util.Collection;
import lombok.Data;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;

@Data
public class ChartCreationOptionsDto {
  private Long id;
  private String title;
  private ChartInformationTypeEnum chartType;
  private Collection<Long> teamIds;
}
