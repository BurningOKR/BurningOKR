package org.burningokr.dto.dashboard;

import lombok.Data;
import java.util.UUID;

@Data
public class DashboardDto {
  private Long id;
  private String title;
  private UUID creatorId;
  private BaseChartOptionsDto[] chartDtos;
}
