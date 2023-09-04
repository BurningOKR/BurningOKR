package org.burningokr.dto.cycle;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.burningokr.model.cycles.CycleState;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class CycleDto {

  private Long id;

  @NotNull
  @Size(min = 1, message = "The name of the Cycle may not be longer than 255 characters. ")
  private String name;

  @NotNull(message = "Planned Start Date must not overlap or be older with the default starting date.")
  private String plannedStartDate;

  @NotNull(message = "Planned End Date must not overlap with the default ending date.")
  private String plannedEndDate;

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
