package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.structures.Structure;

@Data
@EqualsAndHashCode(callSuper = true)
public class CorporateObjectiveStructureDto extends SubStructureDto {

  public CorporateObjectiveStructureDto() {
    super(StructureType.CORPORATE_OBJECTIVE_STRUCTURE);
  }

  private Collection<Long> corporateObjectiveStructureIds = new ArrayList<>();
}
