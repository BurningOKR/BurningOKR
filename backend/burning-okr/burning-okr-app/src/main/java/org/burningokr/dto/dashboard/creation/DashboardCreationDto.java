package org.burningokr.dto.dashboard.creation;

import java.util.Collection;
import java.util.UUID;
import lombok.Data;

@Data
public class DashboardCreationDto {
  private Long id;
  private String title;
  private UUID creatorId;
  private Long companyId;
  private Collection<ChartCreationOptionsDto> chartCreationOptions;
}
