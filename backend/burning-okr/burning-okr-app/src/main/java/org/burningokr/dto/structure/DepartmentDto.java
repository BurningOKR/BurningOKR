package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentDto extends SubStructureDto {

  public DepartmentDto() {
    super(StructureType.DEPARTMENT);
  }

  private UUID okrMasterId;

  private UUID okrTopicSponsorId;

  private Collection<UUID> okrMemberIds = new ArrayList<>();
}
