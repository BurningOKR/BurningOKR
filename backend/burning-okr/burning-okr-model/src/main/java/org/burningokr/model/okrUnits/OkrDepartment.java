package org.burningokr.model.okrUnits;

import jakarta.persistence.*;
import lombok.*;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrDepartmentHistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue(value = "OKR_DEPARTMENT")
public class OkrDepartment extends OkrChildUnit {

  private UUID okrMasterId;

  // Themenverantwortlicher/Themenepate
  private UUID okrTopicSponsorId;

  @ElementCollection
  @CollectionTable(name = "okr_member")
  @Column(name = "okr_member_id")
  private Collection<UUID> okrMemberIds = new ArrayList<>();

  @ToString.Exclude
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @EqualsAndHashCode.Exclude
  private OkrTopicDescription okrTopicDescription;

  @OneToOne(mappedBy = "parentOkrDepartment", cascade = CascadeType.ALL)
  private TaskBoard taskBoard;

  @ManyToOne
  @JoinColumn(name = "history_department_id")
  private OkrDepartmentHistory history_department;

//  @ManyToOne(cascade = CascadeType.REMOVE)
//  @JoinColumn(name = "okr_department_history_id")
//  private OkrDepartmentHistory okrDepartmentHistory;

  /**
   * Creates a copy of the OkrDepartment without relations.
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
   * @return a copy of the OkrDepartment without relations
   */
  @Override
  public OkrDepartment getCopyWithoutRelations() {
    OkrDepartment copy = new OkrDepartment();
    copy.setName(this.getName());
    copy.setLabel(this.getLabel());
    copy.setOkrMasterId(this.getOkrMasterId());
    copy.setOkrTopicSponsorId(this.getOkrTopicSponsorId());
    copy.setActive(this.isActive);
    List<UUID> okrMembersIds = new ArrayList<>(this.getOkrMemberIds());
    copy.setOkrMemberIds(okrMembersIds);
    copy.setOkrTopicDescription(this.getOkrTopicDescription());
    copy.setHistory_department(this.getHistory_department());
    return copy;
  }
}
