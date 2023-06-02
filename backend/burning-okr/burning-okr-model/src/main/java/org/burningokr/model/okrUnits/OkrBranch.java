package org.burningokr.model.okrUnits;

import jakarta.persistence.*;
import lombok.*;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrBranchHistory;

import java.util.Collection;
import java.util.LinkedList;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue(value = "OKR_BRANCH")
public class OkrBranch extends OkrChildUnit implements OkrParentUnit {

  @OneToMany(
    mappedBy = "parentOkrUnit",
    cascade = CascadeType.REMOVE,
    targetEntity = OkrChildUnit.class
  )
  @EqualsAndHashCode.Exclude
  protected Collection<OkrChildUnit> okrChildUnits = new LinkedList<>();
  @ManyToOne
  @JoinColumn(name = "branch_history_id")
  private OkrBranchHistory branchHistory;

  public OkrBranch getCopyWithoutRelations() {
    OkrBranch copy = new OkrBranch();
    copy.setName(this.getName());
    copy.setLabel(this.getLabel());
    copy.setActive(this.isActive);
    copy.setBranchHistory(this.getBranchHistory());
    return copy;
  }
}
