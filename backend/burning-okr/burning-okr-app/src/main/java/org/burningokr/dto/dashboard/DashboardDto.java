package org.burningokr.dto.dashboard;

import lombok.Data;
import org.burningokr.model.users.IUser;

import java.util.Collection;

@Data
public class DashboardDto {
  private Long id;
  private String title;
  private IUser creator;
  private Collection<BaseChartOptionsDto> chartDtos;
}
