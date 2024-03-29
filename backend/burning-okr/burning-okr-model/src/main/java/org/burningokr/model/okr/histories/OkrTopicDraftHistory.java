package org.burningokr.model.okr.histories;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;

import java.util.ArrayList;
import java.util.Collection;

// @EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class OkrTopicDraftHistory implements Trackable<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  private Long id;
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "history", cascade = CascadeType.REMOVE, targetEntity = OkrTopicDraft.class)
  private Collection<OkrTopicDraft> units = new ArrayList<>();

  public String getName() {
    return "History " + id;
  }

  public void addUnit(OkrTopicDraft unit) {
    units.add(unit);
  }

}
