package org.burningokr.model.okrUnits;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrBranchHistory;

import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OkrBranch extends OkrChildUnit implements OkrParentUnit {

  @OneToMany(
    mappedBy = "parentOkrUnit",
    cascade = CascadeType.REMOVE,
    targetEntity = OkrChildUnit.class
  )
  @EqualsAndHashCode.Exclude
  protected Collection<OkrChildUnit> okrChildUnits;
  @ManyToOne
  private OkrBranchHistory history;

  public OkrBranch getCopyWithoutRelations() {
    OkrBranch copy = new OkrBranch();
    copy.setName(this.getName());
    copy.setLabel(this.getLabel());
    copy.setActive(this.isActive);
    copy.setHistory(this.getHistory());
    return copy;
  }
}
