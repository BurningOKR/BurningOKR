package org.burningokr.dto.okrUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public class OkrCompanyDto extends OkrUnitDto {

  private Long cycleId;

  private Collection<Long> okrChildUnitIds = new ArrayList<>();

  private Long historyId;
}
