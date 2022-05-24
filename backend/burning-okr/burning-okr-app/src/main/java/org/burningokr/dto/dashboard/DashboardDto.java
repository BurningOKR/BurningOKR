package org.burningokr.dto.dashboard;

import java.util.Collection;
import lombok.Data;
import org.burningokr.model.users.User;

@Data
public class DashboardDto {
  private Long id;
  private String title;
  private User creator;
  private Collection<BaseChartOptionsDto> chartDtos;
}
