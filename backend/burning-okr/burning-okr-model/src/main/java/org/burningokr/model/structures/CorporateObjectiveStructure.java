package org.burningokr.model.structures;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class CorporateObjectiveStructure extends SubStructure implements ParentStructure {

  @OneToMany(
      mappedBy = "parentStructure",
      cascade = CascadeType.REMOVE,
      targetEntity = SubStructure.class)
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @EqualsAndHashCode.Exclude
  protected Collection<SubStructure> subStructures = new ArrayList<>();

  public boolean hasDepartments() {
    return !subStructures.isEmpty();
  }

  @Override
  public Collection<SubStructure> getSubStructures() {
    return subStructures;
  }

  @Override
  public void setSubStructures(Collection<SubStructure> subDepartments) {
    this.subStructures = subDepartments;
  }

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
  public CorporateObjectiveStructure getCopyWithoutRelations() {
    CorporateObjectiveStructure copy = new CorporateObjectiveStructure();
    copy.setName(this.getName());
    return copy;
  }
}
