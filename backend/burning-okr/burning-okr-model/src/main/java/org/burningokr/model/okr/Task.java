package org.burningokr.model.okr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;

@Entity
@Data
public class Task implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
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

  public boolean hasAssignedUserIds() {
    return assignedUserIds.size() > 0;
  }

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "parent_task_board_id")
  private TaskBoard parentTaskBoard;

  @ToString.Exclude
  @ManyToOne
  @Nullable
  @JoinColumn(name = "assigned_key_result_id")
  private KeyResult assignedKeyResult;

  public boolean hasAssignedKeyResult() {
    return assignedKeyResult != null;
  }

  @OneToOne
  @Nullable
  @JoinColumn(name = "previous_task_id")
  private Task previousTask;

  public boolean hasPreviousTask() {
    return previousTask != null;
  }

  @Column @Version private Long version;

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
