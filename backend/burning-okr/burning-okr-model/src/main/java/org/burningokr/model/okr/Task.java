package org.burningokr.model.okr;

import javax.persistence.*;

import lombok.*;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.OkrUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
public class Task implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(length = 255)
  private String title;

  @Column(length = 1023)
  private String description;

  @Column(length = 255)
  private Long okrStateId;

  @ElementCollection
  @CollectionTable(name = "task_user")
  @Column(name = "user_id")
  private Collection<UUID> assignedUserIds = new ArrayList<>();

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name="parent_task_board_id")
  private TaskBoard parentTaskBoard;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name="assigned_key_result_id")
  private KeyResult assignedKeyResult;

  @Override
  public String getName() {
    return title;
  }
}