package org.burningokr.model.okr;

import jakarta.persistence.*;
import lombok.Data;
import org.burningokr.model.activity.Trackable;

@Entity
@Data
public class DefaultTaskBoardState implements Trackable<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
  private Long id;

  @Column(length = 255)
  private String title;

  @Override
  public String getName() {
    return title;
  }

  @Override
  public Long getId() {
    return id;
  }
}
