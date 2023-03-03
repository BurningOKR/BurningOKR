package org.burningokr.model.okrUnits;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrBranchHistory;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class OkrBranch extends OkrChildUnit implements OkrParentUnit {

  @OneToMany(
    mappedBy = "parentOkrUnit",
    cascade = CascadeType.REMOVE,
    targetEntity = OkrChildUnit.class
  )
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @EqualsAndHashCode.Exclude
  protected Collection<OkrChildUnit> okrChildUnits = new ArrayList<>();
  @ManyToOne
  private OkrBranchHistory history;

  public boolean hasDepartments() {
    return !okrChildUnits.isEmpty();
  }

  @Override
  public Collection<OkrChildUnit> getOkrChildUnits() {
    return okrChildUnits;
  }

  @Override
  public void setOkrChildUnits(Collection<OkrChildUnit> subDepartments) {
    this.okrChildUnits = subDepartments;
  }

  /**
   * Creates a copy of the OkrBranch without relations.
   *
   * <p>The values that are copied are:
   *
   * <ul>
   *   <li>Name
   * </ul>
   *
   * @return a copy of the OkrBranch without relations
   */
  public OkrBranch getCopyWithoutRelations() {
    OkrBranch copy = new OkrBranch();
    copy.setName(this.getName());
    copy.setLabel(this.getLabel());
    copy.setActive(this.isActive);
    copy.setHistory(this.getHistory());
    return copy;
  }
}
