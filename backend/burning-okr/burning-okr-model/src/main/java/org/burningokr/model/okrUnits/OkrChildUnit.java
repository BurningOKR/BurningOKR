package org.burningokr.model.okrUnits;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class OkrChildUnit extends OkrUnit implements OkrChildUnitSchema {
  @ToString.Exclude
  @ManyToOne
  @EqualsAndHashCode.Exclude
  protected OkrUnit parentOkrUnit;

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

  public abstract OkrChildUnit getCopyWithoutRelations();
}
