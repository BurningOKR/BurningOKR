package org.burningokr.model.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.OkrDepartment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
public class TaskBoard implements Trackable<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  @EqualsAndHashCode.Exclude
  @JoinColumn(name = "parent_unit_id")
  private OkrDepartment parentOkrDepartment;

  @OneToMany(mappedBy = "parentTaskBoard", cascade = CascadeType.REMOVE)
  @EqualsAndHashCode.Exclude
  private Collection<Task> tasks = new ArrayList<>();

  @OneToMany(mappedBy = "parentTaskBoard", cascade = CascadeType.REMOVE)
  private Collection<TaskState> availableStates = new ArrayList<>();

  @Override
  public String getName() {
    return "";
  }

  @Override
  public Long getId() {
    return id;
  }

  public TaskBoard getCopyWithCopiedTasksWhichNotFinished() {
    TaskBoard copiedTaskboard = new TaskBoard();
    Collection<TaskState> copiedAvailableStates = new ArrayList<>();
    for (TaskState state : this.getAvailableStates()) {
      TaskState copiedState = state.copy();
      copiedState.setParentTaskBoard(copiedTaskboard);
      copiedAvailableStates.add(copiedState);
    }

    copiedTaskboard.setAvailableStates(copiedAvailableStates);

    return copiedTaskboard;
  }
}
