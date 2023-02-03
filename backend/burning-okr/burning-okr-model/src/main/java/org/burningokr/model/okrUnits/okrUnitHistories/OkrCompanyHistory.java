package org.burningokr.model.okrUnits.okrUnitHistories;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.okrUnits.OkrCompany;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class OkrCompanyHistory extends OkrUnitHistory<OkrCompany> {

  @OneToMany(mappedBy = "history", cascade = CascadeType.REMOVE, targetEntity = OkrCompany.class)
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
