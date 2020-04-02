package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentDto extends CompanyStructureDto {

  @NotNull private Long parentStructureId;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private boolean isParentStructureADepartment;

  public boolean getIsParentStructureADepartment() {
    return isParentStructureADepartment;
  }

  public void setIsParentStructureADepartment(boolean parentStructureADepartment) {
    isParentStructureADepartment = parentStructureADepartment;
  }

  private Collection<Long> subDepartmentIds = new ArrayList<>();
  private Collection<Long> groupIds = new ArrayList<>();

  private UUID okrMasterId;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private boolean isActive;

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean active) {
    isActive = active;
  }

  private UUID okrTopicSponsorId;

  private Collection<UUID> okrMemberIds = new ArrayList<>();
}
