package org.burningokr.dto.dashboard;

import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class DashboardCreationDto {
  private Long dashboardCreationId;
  private String title;
  private UUID creatorId;
  private Collection<ChartCreationOptionsDto[]> chartCreationOptions;
}
