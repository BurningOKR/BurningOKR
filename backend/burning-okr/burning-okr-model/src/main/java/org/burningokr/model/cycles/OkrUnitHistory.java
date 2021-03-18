package org.burningokr.model.cycles;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

import lombok.Data;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.Historical;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;

@Entity
@Data
public class OkrUnitHistory<T extends OkrUnit> implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  // TODO: (R.J. 18.03.2021) OkrCompany.class is not correct here. It should be changed, so that it also covers departments and branches.
  @OneToMany(mappedBy = "history", cascade = CascadeType.REMOVE, targetEntity = OkrCompany.class)
  private Collection<T> units = new ArrayList<>();

  @Override
  public String getName() {
    return "History " + id;
  }
}
