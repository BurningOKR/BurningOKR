package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CorporateObjectiveStructureDto extends SubStructureDto {
  private Collection<Long> corporateObjectiveStructureIds = new ArrayList<>();
}
