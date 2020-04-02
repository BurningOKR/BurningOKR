package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class DepartmentStructureDto {

  @NotNull
  @Size(min = 1)
  private Long id;

  @NotNull
  @Size(min = 1)
  private String name;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private Boolean isActive;

  @NotNull private Collection<DepartmentStructureDto> subDepartments = new ArrayList<>();

  @NotNull private DepartmentDtoRole userRole;

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean active) {
    isActive = active;
  }
}
