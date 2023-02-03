package org.burningokr.dto.okrUnit;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

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
