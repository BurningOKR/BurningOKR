package org.burningokr.model.okrUnits.okrUnitHistories;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.burningokr.model.okrUnits.OkrBranch;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@DiscriminatorValue(value = "OKR_BRANCH_HISTORY")
public class OkrBranchHistory extends OkrUnitHistory<OkrBranch> {

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "history", cascade = CascadeType.REMOVE, targetEntity = OkrBranch.class)
  private Collection<OkrBranch> units = new ArrayList<>();

  @Override
  public void addUnit(OkrBranch unit) {
    units.add(unit);
  }

  @Override
  public Collection<OkrBranch> getUnits() {
    return units;
  }

  @Override
  public void clearUnits() {
    units.clear();
  }

  @Override
  public void removeUnit(OkrBranch unit) {
    units.remove(unit);
  }
}
