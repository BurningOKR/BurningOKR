package org.burningokr.model.okrUnits;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
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
