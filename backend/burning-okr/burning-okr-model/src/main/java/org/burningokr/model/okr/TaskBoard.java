package org.burningokr.model.okr;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.OkrDepartment;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
public class TaskBoard implements Trackable<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  private Long id;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @EqualsAndHashCode.Exclude
  @JoinColumn(name = "parent_unit_id")
  private OkrDepartment parentOkrDepartment;

  @OneToMany(mappedBy = "parentTaskBoard", cascade = CascadeType.REMOVE)
  @EqualsAndHashCode.Exclude
  private Collection<Task> tasks = new ArrayList<>();

  @OneToMany(mappedBy = "parentTaskBoard", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Exclude
  private Collection<TaskState> availableStates = new ArrayList<>();

  @Override
  public String getName() {
    return "";
  }

  @Override
  public Long getId() {
    return id;
  }

}
