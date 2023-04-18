package org.burningokr.model.okrUnits;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class OkrChildUnit extends OkrUnit implements OkrChildUnitSchema {
  @ToString.Exclude
  @ManyToOne
  @EqualsAndHashCode.Exclude
  protected OkrUnit parentOkrUnit;

  protected boolean isActive;

  public abstract OkrChildUnit getCopyWithoutRelations();
}
