package org.burningokr.dto.structure;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "__structureType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DepartmentDto.class, name = "DEPARTMENT"),
    @JsonSubTypes.Type(value = CorporateObjectiveStructureDto.class, name = "CORPORATE_OBJECTIVE_STRUCTURE")
})
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

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private boolean isParentStructureACorporateObjectiveStructure;

  public boolean getIsParentStructureACorporateObjectiveStructure() {
    return isParentStructureACorporateObjectiveStructure;
  }

  public void setIsParentStructureACorporateObjectiveStructure(boolean parentStructureADepartment) {
    isParentStructureACorporateObjectiveStructure = parentStructureADepartment;
  }
}
