package org.burningokr.dto.dashboard;

import lombok.Data;

import java.util.Collection;

@Data
public class ChartCreationOptionsDto {
  private Long id;
  private String title;
  private int chartType;
  private int informationType;
  private Collection<Long> teamIds;
}
