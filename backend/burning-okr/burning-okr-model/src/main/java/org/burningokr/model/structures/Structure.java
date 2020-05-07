package org.burningokr.model.structures;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okr.Objective;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class Structure implements Trackable<Long>, ParentStructure {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  @NotNull
  @Size(min = 1)
  protected String name;

  @NotNull
  @Size(min = 1)
  protected String label;

  @OneToMany(mappedBy = "parentStructure", cascade = CascadeType.REMOVE)
  @EqualsAndHashCode.Exclude
  protected Collection<Objective> objectives = new ArrayList<>();

  public boolean hasObjectives() {
    return !objectives.isEmpty();
  }
}
