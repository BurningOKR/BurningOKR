package org.burningokr.dto.okrUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class OkrBranchDto extends OkrChildUnitDto {

  private Collection<Long> okrChildUnitIds = new ArrayList<>();

  public OkrBranchDto() {
    super(UnitType.OKR_BRANCH);
  }
}
