package org.burningokr.model.okrUnits;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.burningokr.model.okr.OkrTeamDescription;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class OkrDepartment extends OkrChildUnit {

  private UUID okrMasterId;

  // Themenverantwortlicher/Themenepate
  private UUID okrTopicSponsorId;

  @ElementCollection
  @CollectionTable(name = "okr_member")
  @Column(name = "okr_member_id")
  private Collection<UUID> okrMemberIds = new ArrayList<>();

  @ToString.Exclude
  @ManyToOne
  @EqualsAndHashCode.Exclude
  private OkrTeamDescription okrTeamDescription;

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
    return copy;
  }
  // endregion
}
