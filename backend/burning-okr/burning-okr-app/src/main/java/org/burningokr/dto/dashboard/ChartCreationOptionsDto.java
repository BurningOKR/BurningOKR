package org.burningokr.dto.dashboard;

import lombok.Data;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;

@Data
public class ChartCreationOptionsDto {
  private Long chartCreationOptionsId;
  private String title;
  private int chartType;
  private OkrDepartmentDto[] teams;
}
