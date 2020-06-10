package org.burningokr.dto.okrUnit;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OkrBranchDTO extends ChildUnitDto {

  public OkrBranchDTO() {
    super(UnitType.OKR_BRANCH);
  }

  private Collection<Long> childUnits = new ArrayList<>();
}
