package org.burningokr.dto.okrUnit;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OkrUnitSchemaDto {

  @NotNull
  @Size(min = 1)
  private Long id;

  @NotNull
  @Size(min = 1)
  private String name;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private Boolean isActive;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private Boolean isTeam;

  @NotNull private Collection<OkrUnitSchemaDto> subDepartments = new ArrayList<>();

  @NotNull private OkrDepartmentDtoRole userRole;

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean active) {
    isActive = active;
  }

  public Boolean getIsTeam() {
    return isTeam;
  }

  public void setIsTeam(Boolean team) {
    isTeam = team;
  }
}
