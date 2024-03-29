package org.burningokr.model.okrUnits.okrUnitHistories;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.okrUnits.OkrDepartment;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@DiscriminatorValue(value = "OKR_DEPARTMENT_HISTORY")
public class OkrDepartmentHistory extends OkrUnitHistory<OkrDepartment> {

  @OneToMany(mappedBy = "departmentHistory", cascade = CascadeType.REMOVE, targetEntity = OkrDepartment.class)
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
