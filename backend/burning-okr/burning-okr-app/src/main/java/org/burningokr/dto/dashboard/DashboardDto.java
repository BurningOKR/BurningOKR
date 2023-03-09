package org.burningokr.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import org.burningokr.model.users.User;

import java.util.Collection;

@Data
@Builder
public class DashboardDto {
  private Long id;
  private String title;
  private Long companyId;
  private User creator;
  private Collection<BaseChartOptionsDto> chartDtos;
}
