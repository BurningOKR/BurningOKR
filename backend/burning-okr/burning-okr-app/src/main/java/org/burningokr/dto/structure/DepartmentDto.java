package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentDto extends SubStructureDto {

  public DepartmentDto() {
    super(StructureType.DEPARTMENT);
  }

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private boolean isParentStructureADepartment;

  public boolean getIsParentStructureADepartment() {
    return isParentStructureADepartment;
  }

  public void setIsParentStructureADepartment(boolean parentStructureADepartment) {
    isParentStructureADepartment = parentStructureADepartment;
  }

  private UUID okrMasterId;

  private UUID okrTopicSponsorId;

  private Collection<UUID> okrMemberIds = new ArrayList<>();
}
