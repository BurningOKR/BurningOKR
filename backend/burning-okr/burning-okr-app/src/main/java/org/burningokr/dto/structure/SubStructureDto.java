package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SubStructureDto extends StructureDto {
  @NotNull protected Long parentStructureId;

  protected Collection<Long> subDepartmentIds = new ArrayList<>();

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  protected boolean isActive;

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean active) {
    isActive = active;
  }
}
