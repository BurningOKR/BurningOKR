package org.burningokr.dto.okrUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OkrDepartmentDto extends ChildUnitDto {

  public OkrDepartmentDto() {
    super(UnitType.DEPARTMENT);
  }

  private UUID okrMasterId;

  private UUID okrTopicSponsorId;

  private Collection<UUID> okrMemberIds = new ArrayList<>();
}
