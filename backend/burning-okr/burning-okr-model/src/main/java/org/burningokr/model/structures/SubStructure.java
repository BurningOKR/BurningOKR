package org.burningokr.model.structures;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SubStructure extends Structure implements ChildStructure {
  @ManyToOne @EqualsAndHashCode.Exclude protected Structure parentStructure;

  @OneToMany(mappedBy = "parentStructure", cascade = CascadeType.REMOVE, targetEntity = SubStructure.class)
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @EqualsAndHashCode.Exclude
  protected Collection<Department> subDepartments = new ArrayList<>();

  @Column
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  protected boolean isActive;

  public boolean hasDepartments() {
    return !subDepartments.isEmpty();
  }

  @Override
  public Collection<Department> getDepartments() {
    return subDepartments;
  }

  @Override
  public void setDepartments(Collection<Department> subDepartments) {
    this.subDepartments = subDepartments;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }
}
