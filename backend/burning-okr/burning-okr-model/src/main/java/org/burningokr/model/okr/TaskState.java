package org.burningokr.model.okr;

import lombok.Data;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Data
public class TaskState implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(length = 255)
  @Audited
  private String title;

  @ToString.Exclude
  @ManyToOne
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
