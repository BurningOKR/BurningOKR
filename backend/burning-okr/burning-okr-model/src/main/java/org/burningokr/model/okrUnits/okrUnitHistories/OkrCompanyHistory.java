package org.burningokr.model.okrUnits.okrUnitHistories;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.okrUnits.OkrCompany;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@DiscriminatorValue(value = "OKR_COMPANY_HISTORY")
public class OkrCompanyHistory extends OkrUnitHistory<OkrCompany> {

  @OneToMany(mappedBy = "companyHistory", cascade = CascadeType.REMOVE, targetEntity = OkrCompany.class)
  private Collection<OkrCompany> units = new ArrayList<>();

  @Override
  public void addUnit(OkrCompany unit) {
    units.add(unit);
  }

  @Override
  public Collection<OkrCompany> getUnits() {
    return units;
  }

  @Override
  public void clearUnits() {
    units.clear();
  }

  @Override
  public void removeUnit(OkrCompany unit) {
    units.remove(unit);
  }
}
