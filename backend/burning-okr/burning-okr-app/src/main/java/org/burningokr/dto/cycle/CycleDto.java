package org.burningokr.dto.cycle;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.burningokr.model.cycles.CycleState;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class CycleDto {

  private Long id;

  @NotNull
  @Size(min = 1)
  private String name;

  @NotNull
  private LocalDate plannedStartDate;

  @NotNull
  private LocalDate plannedEndDate;

  private CycleState cycleState = CycleState.PREPARATION;
  private Collection<Long> companyIds = new ArrayList<>();

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private boolean isVisible;

  public boolean getIsVisible() {
    return isVisible;
  }

  public void setIsVisible(boolean visible) {
    isVisible = visible;
  }

  /**
   * Gets the last companyId in the Cycle.
   *
   * @return the last company ID
   */
  public Long getCompanyId() {
    Long companyId = null;
    for (Long currentId : companyIds) {
      companyId = currentId;
    }
    return companyId;
  }
}
