package org.burningokr.dto.okrUnit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OkrCompanyDto extends OkrUnitDto {

  private Long cycleId;

  private Collection<Long> okrChildUnitIds = new ArrayList<>();

  private Long historyId;

  public OkrCompanyDto(OkrUnitDto parentUnit) {
    super(parentUnit);
  }
}
