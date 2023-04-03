package org.burningokr.model.okrUnits.okrUnitHistories;

import jakarta.persistence.*;
import lombok.Data;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.OkrUnit;

import java.util.Collection;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class OkrUnitHistory<T extends OkrUnit> implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
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
