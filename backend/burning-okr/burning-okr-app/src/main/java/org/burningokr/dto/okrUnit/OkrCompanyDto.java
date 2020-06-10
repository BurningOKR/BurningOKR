package org.burningokr.dto.okrUnit;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OkrCompanyDto extends OkrUnitDto {

  private Long cycleId;

  private Collection<Long> OkrChildUnitIds = new ArrayList<>();

  private Long historyId;
}
