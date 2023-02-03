package org.burningokr.dto.okr;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class TaskBoardDto {
  private Long id;
  private Long parentOkrUnitId;

  private Collection<Long> taskIds = new ArrayList<>();
}
