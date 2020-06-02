package org.burningokr.dto.structure;

import javax.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class SubStructureDto extends StructureDto {

  protected SubStructureDto(StructureType structureType) {
    this.__structureType = structureType;
  }

  protected StructureType __structureType;

  @NotNull protected Long parentStructureId;

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
