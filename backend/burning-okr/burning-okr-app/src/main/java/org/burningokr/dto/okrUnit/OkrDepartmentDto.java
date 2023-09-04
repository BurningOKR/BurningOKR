package org.burningokr.dto.okrUnit;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class OkrDepartmentDto extends OkrChildUnitDto {

  private UUID okrMasterId;
  private UUID okrTopicSponsorId;
  private Collection<UUID> okrMemberIds = new ArrayList<>();

  public OkrDepartmentDto() {
    super(UnitType.DEPARTMENT);
  }
}
