package org.burningokr.model.okrUnits;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrCompanyHistory;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value = "OKR_COMPANY")
public class OkrCompany extends OkrUnit implements OkrParentUnit {

  @ToString.Exclude
  @ManyToOne
  @EqualsAndHashCode.Exclude
  private Cycle cycle;

  @OneToMany(
    mappedBy = "parentOkrUnit",
    cascade = CascadeType.REMOVE,
    targetEntity = OkrChildUnit.class
  )
  @EqualsAndHashCode.Exclude
  private Collection<OkrChildUnit> okrChildUnits = new ArrayList<>();
  @ManyToOne
  @JoinColumn(name = "company_history_id")
  private OkrCompanyHistory companyHistory;

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
   * Creates a copy of the OkrCompany without relations.
   *
   * <p>The values that are copied are:
   *
   * <ul>
   *   <li>Name
   *   <li>Label
   *   <li>History
   * </ul>
   *
   * @return a copy of the OkrCompany without relations
   */
  public OkrCompany getCopyWithoutRelations() {
    OkrCompany copy = new OkrCompany();
    copy.setCompanyHistory(this.getCompanyHistory());
    copy.setName(this.getName());
    copy.setLabel(this.getLabel());
    return copy;
  }
}
