package org.burningokr.dto.dashboard;

import lombok.Data;

import java.util.Collection;

@Data
public abstract class BaseChartOptionsDto {
  String title;
  int chartType;
  Collection<Long> selectedTeamIds;
}
