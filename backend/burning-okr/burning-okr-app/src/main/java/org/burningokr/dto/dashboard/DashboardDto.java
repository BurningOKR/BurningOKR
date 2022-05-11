package org.burningokr.dto.dashboard;

import lombok.Data;
import org.burningokr.model.users.User;

@Data
public class DashboardDto {
  private Long id;
  private String title;
  private User creator;
  private BaseChartOptionsDto[] chartDtos;
}
