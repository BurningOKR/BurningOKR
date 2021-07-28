package org.burningokr.model.okr;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.*;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.OkrUnit;

@Entity
@Data
public class Objective implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ToString.Exclude @ManyToOne private OkrUnit parentOkrUnit;

  @ToString.Exclude @ManyToOne private Objective parentObjective;

  @Column(length = 255)
  private String name;

  @Column(length = 1023)
  private String description;

  @Column(length = 1023)
  private String remark;

  @Column(length = 2047)
  private String review;

  @Column
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private boolean isActive;

  @Column private String contactPersonId;

  @Column private int sequence;

  @OneToMany(mappedBy = "parentObjective", cascade = CascadeType.DETACH)
  private Collection<Objective> subObjectives = new ArrayList<>();

  @OneToMany(mappedBy = "parentObjective", cascade = CascadeType.REMOVE)
  private Collection<KeyResult> keyResults = new ArrayList<>();

  @ToString.Exclude
  @OneToMany(mappedBy = "parentObjective", cascade = CascadeType.REMOVE)
  private Collection<NoteObjective> notes = new ArrayList<>();

  public boolean hasParentObjective() {
    return parentObjective != null;
  }

  public boolean hasContactPerson() {
    return this.contactPersonId != null;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Creates a copy of the Objective without relations.
   *
   * <p>The values that are copied are:
   *
   * <ul>
   *   <li>Name
   *   <li>Description
   *   <li>ContactPersonId
   *   <li>Remark
   *   <li>Active
   *   <li>Sequence
   * </ul>
   *
   * @return a copy of the Objective without relations
   */
  public Objective getCopyWithoutRelations() {
    Objective copy = new Objective();
    copy.setContactPersonId(this.getContactPersonId());
    copy.setName(this.getName());
    copy.setDescription(this.getDescription());
    copy.setRemark(this.getRemark());
    copy.setActive(this.isActive());
    copy.setSequence(this.getSequence());
    return copy;
  }
}
