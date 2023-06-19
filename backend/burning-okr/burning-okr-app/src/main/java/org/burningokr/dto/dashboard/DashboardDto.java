package org.burningokr.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
  private Long id;
  private String title;
  private Long companyId;
  private UUID creatorId;
  private Collection<BaseChartOptionsDto> chartDtos;
}
