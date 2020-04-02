package org.burningokr.model.cycles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.structures.Company;

@Entity
@Data
public class Cycle implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDate plannedStartDate;

  @Column(nullable = false)
  private LocalDate plannedEndDate;

  private LocalDate factualStartDate;
  private LocalDate factualEndDate;
  private CycleState cycleState = CycleState.PREPARATION;

  @OneToMany(mappedBy = "cycle", cascade = CascadeType.REMOVE)
  private Collection<Company> companies = new ArrayList<>();

  @Column
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private boolean isVisible;

  public Cycle() {}

  /**
   * Initialize Cycle with name, start and end date, and factual start date.
   *
   * <p>Sets the name of the Cycle, the planned start date and factual start date to the current
   * Date, and the planned end date to 3 month in the future.
   *
   * @param name a string value
   */
  public Cycle(String name) {
    this.setName(name);
    this.setPlannedStartDate(LocalDate.now());
    this.setPlannedEndDate(LocalDate.now().plusMonths(3));
    this.setFactualStartDate(LocalDate.now());
  }

  public boolean isVisible() {
    return isVisible;
  }

  public void setVisible(boolean visible) {
    isVisible = visible;
  }

  /**
   * Creates a copy of the Cycle without relations.
   *
   * <p>The values that are copied are:
   *
   * <ul>
   *   <li>Name
   *   <li>PlannedStartDate
   *   <li>PlannedEndDate
   *   <li>FactualStartDate
   *   <li>FactualEndDate
   *   <li>CycleState
   *   <li>Visible
   * </ul>
   *
   * @return a copy of the Cycle without relations
   */
  public Cycle getCopyWithoutRelations() {
    Cycle copy = new Cycle();
    copy.setName(getName());
    copy.setPlannedStartDate(getPlannedStartDate());
    copy.setPlannedEndDate(getPlannedEndDate());
    copy.setFactualStartDate(getFactualStartDate());
    copy.setFactualEndDate(getFactualEndDate());
    copy.setCycleState(getCycleState());
    copy.setVisible(isVisible());
    return copy;
  }
  // endregion
}
