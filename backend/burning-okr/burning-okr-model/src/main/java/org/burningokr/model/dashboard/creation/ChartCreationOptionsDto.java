package org.burningokr.model.dashboard.creation;

import lombok.Data;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;

import java.util.Collection;

@Data
public class ChartCreationOptionsDto {
  private Long id;
  private String title;
  private ChartInformationTypeEnum chartType;
  private Collection<Long> selectedTeamIds;
}
