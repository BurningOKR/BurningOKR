package org.burningokr.model.structures;

import javax.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SubStructure extends Structure implements ChildStructure {
  @ManyToOne @EqualsAndHashCode.Exclude protected Structure parentStructure;

  @Column
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  protected boolean isActive;

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public abstract SubStructure getCopyWithoutRelations();
}
