package org.burningokr.dto.okrUnit;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "__okrUnitType",
  visible = true
)
@JsonSubTypes(
  {
    @JsonSubTypes.Type(value = OkrDepartmentDto.class, name = "DEPARTMENT"),
    @JsonSubTypes.Type(value = OkrBranchDto.class, name = "OKR_BRANCH")
  }
)
public abstract class OkrChildUnitDto extends OkrUnitDto {

  protected UnitType __okrUnitType;
  @NotNull
  protected Long parentUnitId;
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  protected boolean isActive;
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private boolean isParentUnitABranch;

  protected OkrChildUnitDto(UnitType unitType) {
    this.__okrUnitType = unitType;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean active) {
    isActive = active;
  }

  public boolean getIsParentUnitABranch() {
    return isParentUnitABranch;
  }

  public void setIsParentUnitABranch(boolean parentUnitADepartment) {
    isParentUnitABranch = parentUnitADepartment;
  }
}
