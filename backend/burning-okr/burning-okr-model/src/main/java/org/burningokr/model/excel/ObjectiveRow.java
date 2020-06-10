package org.burningokr.model.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ObjectiveRow {
  @ColumnIndex(1)
  private String team;

  @ColumnIndex(2)
  private String objective;

  @ColumnIndex(3)
  private PercentageCellValue progress;

  @ColumnIndex(4)
  private String parentUnitGoal;

  @ColumnIndex(5)
  private String keyResult;

  @ColumnIndex(6)
  private String description;

  @ColumnIndex(7)
  private long start;

  @ColumnIndex(8)
  private long end;

  @ColumnIndex(9)
  private long actual;

  @ColumnIndex(10)
  private String unit;
}
