package org.burningokr.model.okr;

import jakarta.persistence.*;
import lombok.*;
import org.burningokr.model.activity.Trackable;
import org.hibernate.envers.Audited;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TaskState implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  private Long id;

  @Column(length = 255)
  @Audited
  private String title;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_task_board_id")
  private TaskBoard parentTaskBoard;

  @Override
  public String getName() {
    return title;
  }

  @Override
  public Long getId() {
    return id;
  }

  public TaskState copy() {
    TaskState copy = new TaskState();
    copy.setParentTaskBoard(this.getParentTaskBoard());
    copy.setTitle(this.getTitle());

    return copy;
  }
}
