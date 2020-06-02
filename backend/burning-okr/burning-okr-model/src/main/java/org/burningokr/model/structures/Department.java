package org.burningokr.model.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Department extends SubStructure {

  private UUID okrMasterId;

  // Themenverantwortlicher/Themenepate
  private UUID okrTopicSponsorId;

  @ElementCollection
  @CollectionTable(name = "okr_member")
  @Column(name = "okr_member_id")
  private Collection<UUID> okrMemberIds = new ArrayList<>();

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
  @Override
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
