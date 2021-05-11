package org.burningokr.dto.okr;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;

@Data
public class TaskBoardDto {
  private Long id;
  private Long parentOkrUnitId;

  private Collection<Long> taskIds = new ArrayList<>();
}
