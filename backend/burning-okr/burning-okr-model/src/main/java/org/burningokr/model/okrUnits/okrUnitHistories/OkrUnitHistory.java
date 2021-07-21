package org.burningokr.model.okrUnits.okrUnitHistories;

import java.util.Collection;
import javax.persistence.*;
import lombok.Data;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.*;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class OkrUnitHistory<T extends OkrUnit> implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Override
  public String getName() {
    return "History " + id;
  }

  public abstract void addUnit(T unit);

  public abstract Collection<T> getUnits();

  public abstract void clearUnits();

  public abstract void removeUnit(T unit);
}
