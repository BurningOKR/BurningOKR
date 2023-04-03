package org.burningokr.model.okr.histories;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okr.KeyResult;

import java.time.LocalDate;

@Entity
@Data
public class KeyResultHistory implements Trackable<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
  private Long id;
  @PositiveOrZero
  private long startValue;
  @PositiveOrZero
  private long currentValue;
  @PositiveOrZero
  private long targetValue;
  private LocalDate dateChanged;
  @ManyToOne
  @NotNull
  private KeyResult keyResult;

  @Override
  public String getName() {return "KeyResultHistory: " + id;}
}
