package org.burningokr.model.activity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Activity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
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
