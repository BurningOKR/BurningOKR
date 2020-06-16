package org.burningokr.dto.okrUnit;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public abstract class OkrUnitDto {

  protected Long OkrUnitId;

  @NotNull
  @Size(min = 1)
  protected String unitName;

  @NotNull
  @Size(min = 1)
  protected String label;

  protected Collection<Long> objectiveIds = new ArrayList<>();
}
