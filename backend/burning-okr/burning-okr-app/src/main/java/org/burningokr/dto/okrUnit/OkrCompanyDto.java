package org.burningokr.dto.okrUnit;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public class OkrCompanyDto extends OkrUnitDto {
  @NotNull
  private Long cycleId;

  private Collection<Long> okrChildUnitIds = new ArrayList<>();
  @NotNull
  private Long historyId;
}
