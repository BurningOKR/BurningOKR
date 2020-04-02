package org.burningokr.model.activity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

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
