package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public abstract class StructureDto {

  protected Long structureId;

  @NotNull
  @Size(min = 1)
  protected String structureName;

  @NotNull
  @Size(min = 1)
  protected String label;

  protected Collection<Long> objectiveIds = new ArrayList<>();
}
