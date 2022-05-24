package org.burningokr.model.okr.histories;

import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okr.KeyResult;

@Entity
@Data
public class KeyResultHistory implements Trackable<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Override
  public String getName() {
    return "KeyResultHistory: " + id;
  }

  @PositiveOrZero private long startValue;
  @PositiveOrZero private long currentValue;
  @PositiveOrZero private long targetValue;

  private LocalDate dateChanged;

  @ManyToOne @NotNull private KeyResult keyResult;
}
