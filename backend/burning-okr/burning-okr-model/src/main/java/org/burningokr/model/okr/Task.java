package org.burningokr.model.okr;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
public class Task implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
  private Long id;

  @Column(length = 255)
  @NotNull
  private String title;

  @Column(length = 1023)
  private String description;

  @OneToOne
  @NotNull
  @JoinColumn(name = "task_state_id")
  private TaskState taskState;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "task_user")
  @Column(name = "user_id")
  private Collection<UUID> assignedUserIds = new ArrayList<>();
  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "parent_task_board_id")
  private TaskBoard parentTaskBoard;
  @ToString.Exclude
  @ManyToOne
  @Nullable
  @JoinColumn(name = "assigned_key_result_id")
  private KeyResult assignedKeyResult;
  @OneToOne
  @Nullable
  @JoinColumn(name = "previous_task_id")
  private Task previousTask;
  @Column
  @Version
  private Long version;

  public boolean hasAssignedUserIds() {
    return assignedUserIds.size() > 0;
  }

  public boolean hasAssignedKeyResult() {
    return assignedKeyResult != null;
  }

  public boolean hasPreviousTask() {
    return previousTask != null;
  }

  @Override
  public String getName() {
    return title;
  }

  public Task copyWithNoRelations() {
    Task copiedTask = new Task();
    copiedTask.setTitle(this.getTitle());
    copiedTask.setDescription(this.getDescription());

    Collection<UUID> userIds = new ArrayList<>(this.getAssignedUserIds());

    copiedTask.setAssignedUserIds(userIds);
    copiedTask.setTaskState(this.getTaskState());
    copiedTask.setPreviousTask(this.getPreviousTask());
    return copiedTask;
  }
}
