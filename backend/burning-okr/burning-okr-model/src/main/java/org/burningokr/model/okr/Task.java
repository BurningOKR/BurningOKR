package org.burningokr.model.okr;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;
import org.hibernate.envers.Audited;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Task implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  private Long id;

  @Column(length = 255)
  @NotNull
  @Audited
  private String title;

  @Column(length = 1023)
  @Audited
  private String description;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @NotNull
  @JoinColumn(name = "task_state_id")
  @Audited
  private TaskState taskState;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "task_user")
  @Column(name = "user_id")
  @Audited
  private Collection<UUID> assignedUserIds = new ArrayList<>();

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_task_board_id")
  private TaskBoard parentTaskBoard;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @Nullable
  @JoinColumn(name = "assigned_key_result_id")
  @Audited
  private KeyResult assignedKeyResult;

  @OneToOne(fetch = FetchType.LAZY)
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
