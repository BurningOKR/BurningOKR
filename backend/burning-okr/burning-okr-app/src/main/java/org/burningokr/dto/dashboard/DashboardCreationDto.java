package org.burningokr.dto.dashboard;

import lombok.Data;

import java.util.Collection;

@Data
public class DashboardCreationDto {
  private Long dashboardCreationId;
  private String title;
  private Collection<ChartCreationOptionsDto[]> chartCreationOptions;
}
