package org.burningokr.model.okr;

import lombok.Data;
import org.burningokr.model.activity.Trackable;

import javax.persistence.*;

@Entity
@Data
public class TaskState implements Trackable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 255)
    private String title;

    @ManyToOne
    @JoinColumn(name="parent_task_board_id")
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
