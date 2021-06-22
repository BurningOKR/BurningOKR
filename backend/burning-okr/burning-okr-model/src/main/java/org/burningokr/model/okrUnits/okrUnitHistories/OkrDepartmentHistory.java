package org.burningokr.model.okrUnits.okrUnitHistories;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.okrUnits.OkrDepartment;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class OkrDepartmentHistory extends OkrUnitHistory<OkrDepartment> {

  @OneToMany(mappedBy = "history", cascade = CascadeType.REMOVE, targetEntity = OkrDepartment.class)
  private Collection<OkrDepartment> units = new ArrayList<>();

  @Override
  public void addUnit(OkrDepartment unit) {
    units.add(unit);
  }

  @Override
  public Collection<OkrDepartment> getUnits() {
    return units;
  }

  @Override
  public void clearUnits() {
    units.clear();
  }

  @Override
  public void removeUnit(OkrDepartment unit) {
    units.remove(unit);
  }
}
