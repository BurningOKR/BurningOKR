package org.burningokr.dto.okrUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class OkrDepartmentDto extends OkrChildUnitDto {

  public OkrDepartmentDto() {
    super(UnitType.DEPARTMENT);
  }

  private UUID okrMasterId;

  private UUID okrTopicSponsorId;

  private Collection<UUID> okrMemberIds = new ArrayList<>();
}
