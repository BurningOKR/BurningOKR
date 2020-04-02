package org.burningokr.model.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamMemberRow {
  @ColumnIndex(1)
  private String teamName;

  @ColumnIndex(2)
  private String role;

  @ColumnIndex(3)
  private String fullName;

  @ColumnIndex(4)
  private String emailAddress;
}
