package org.burningokr.dto.okrUnit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class OkrCompanyDto extends OkrUnitDto {
  @Positive
  private Long cycleId;

  private Collection<Long> okrChildUnitIds = new ArrayList<>();
  @Positive
  private Long historyId;
}
