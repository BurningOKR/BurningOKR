package org.burningokr.model.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Department extends CompanyStructure implements ChildStructure {
  @ManyToOne @EqualsAndHashCode.Exclude private CompanyStructure parentStructure;

  @OneToMany(mappedBy = "parentStructure", cascade = CascadeType.REMOVE)
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @EqualsAndHashCode.Exclude
  private Collection<Department> subDepartments = new ArrayList<>();

  private UUID okrMasterId;

  @Column
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private boolean isActive;

  // Themenverantwortlicher/Themenepate
  private UUID okrTopicSponsorId;

  @ElementCollection
  @CollectionTable(name = "okr_member")
  @Column(name = "okr_member_id")
  private Collection<UUID> okrMemberIds = new ArrayList<>();

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

  /**
   * Creates a copy of the Department without relations.
   *
   * <p>The values that are copied are:
   *
   * <ul>
   *   <li>Name
   *   <li>Label
   *   <li>OkrMasterId
   *   <li>OkrTopicSponsorId
   *   <li>Active
   *   <li>OkrMemberIds
   * </ul>
   *
   * @return a copy of the Department without relations
   */
  public Department getCopyWithoutRelations() {
    Department copy = new Department();
    copy.setName(this.getName());
    copy.setLabel(this.getLabel());
    copy.setOkrMasterId(this.getOkrMasterId());
    copy.setOkrTopicSponsorId(this.getOkrTopicSponsorId());
    copy.setActive(this.isActive);
    List<UUID> okrMembersIds = new ArrayList<>(this.getOkrMemberIds());
    copy.setOkrMemberIds(okrMembersIds);
    return copy;
  }
  // endregion
}
