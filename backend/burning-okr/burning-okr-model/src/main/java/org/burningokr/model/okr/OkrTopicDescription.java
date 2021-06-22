package org.burningokr.model.okr;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;
import org.burningokr.model.activity.Trackable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class OkrTopicDescription implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(length = 255)
  private String name;

  private UUID initiatorId;

  @ElementCollection
  @CollectionTable(name = "okr_description_member")
  @Column(name = "okr_member_id")
  private Collection<UUID> startTeam;

  @ElementCollection
  @CollectionTable(name = "okr_description_stakeholder")
  @Column(name = "okr_stakeholder_id")
  private Collection<UUID> stakeholders;

  @Column(length = 1023)
  private String acceptanceCriteria;

  @Column(length = 1023)
  private String contributesTo;

  @Column(length = 1023)
  private String delimitation;

  private LocalDate beginning;

  @Column(length = 1023)
  private String dependencies;

  @Column(length = 1023)
  private String resources;

  @Column(length = 1023)
  private String handoverPlan;

  public OkrTopicDescription() {}

  public OkrTopicDescription(String name) {
    this.name = name;
  }
}
