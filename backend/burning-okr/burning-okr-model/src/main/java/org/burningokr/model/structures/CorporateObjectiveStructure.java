package org.burningokr.model.structures;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class CorporateObjectiveStructure extends SubStructure {

  @OneToMany(mappedBy = "parentStructure", cascade = CascadeType.REMOVE)
  @EqualsAndHashCode.Exclude
  private Collection<CorporateObjectiveStructure> corporateObjectiveStructures = new ArrayList<>();

  /**
   * Creates a copy of the CorporateObjectStructure without relations.
   *
   * <p>The values that are copied are:
   *
   * <ul>
   *   <li>Name
   * </ul>
   *
   * @return a copy of the CorporateObjectiveStructure without relations
   */
  public CorporateObjectiveStructure getCopyWithOutRelations() {
    CorporateObjectiveStructure copy = new CorporateObjectiveStructure();
    copy.setName(this.getName());
    return copy;
  }
}
