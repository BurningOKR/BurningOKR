package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyDto extends StructureDto {

  private Long cycleId;

  private Collection<Long> subStructureIds = new ArrayList<>();

  private Long historyId;
}
