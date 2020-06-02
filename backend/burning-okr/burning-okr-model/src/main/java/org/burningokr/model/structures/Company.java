package org.burningokr.model.structures;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.cycles.CompanyHistory;
import org.burningokr.model.cycles.Cycle;

@Entity
@Table(name = "company")
@Data
@EqualsAndHashCode(callSuper = true)
public class Company extends Structure implements ParentStructure {

  @ManyToOne @EqualsAndHashCode.Exclude private Cycle cycle;

  @ManyToOne @EqualsAndHashCode.Exclude private CompanyHistory history;

  @OneToMany(
      mappedBy = "parentStructure",
      cascade = CascadeType.REMOVE,
      targetEntity = SubStructure.class)
  @EqualsAndHashCode.Exclude
  private Collection<SubStructure> subStructures = new ArrayList<>();

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
   * Creates a copy of the Company without relations.
   *
   * <p>The values that are copied are:
   *
   * <ul>
   *   <li>Name
   *   <li>Label
   *   <li>History
   * </ul>
   *
   * @return a copy of the Company without relations
   */
  public Company getCopyWithoutRelations() {
    Company copy = new Company();
    copy.setHistory(this.getHistory());
    copy.setName(this.getName());
    copy.setLabel(this.getLabel());
    return copy;
  }
}
