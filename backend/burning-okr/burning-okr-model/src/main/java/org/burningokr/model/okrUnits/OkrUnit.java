package org.burningokr.model.okrUnits;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okr.Objective;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class OkrUnit implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @NotNull
  @Size(min = 1)
  protected String name;

  @NotNull
  @Size(min = 1)
  protected String label;

  @OneToMany(mappedBy = "parentOkrUnit", cascade = CascadeType.REMOVE)
  @EqualsAndHashCode.Exclude
  protected Collection<Objective> objectives = new ArrayList<>();

  public boolean hasObjectives() {
    return !objectives.isEmpty();
  }
}
