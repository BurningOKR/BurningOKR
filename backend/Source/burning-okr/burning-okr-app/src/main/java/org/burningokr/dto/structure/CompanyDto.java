package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyDto extends CompanyStructureDto {

  private Long cycleId;

  private Collection<Long> departmentIds = new ArrayList<>();

  private Long historyId;
}
