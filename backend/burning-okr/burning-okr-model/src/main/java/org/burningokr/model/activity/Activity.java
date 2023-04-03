package org.burningokr.model.activity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Activity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  private Long id;

  private LocalDateTime date;
  private Action action;
  private String userId;
  private String object;

  @Override
  public String toString() {
    return date.toString() + " " + userId + " " + action.toString().toLowerCase() + " " + object;
  }
}
