package org.burningokr.model.okr;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.burningokr.model.activity.Trackable;

@Entity
@Data
public class KeyResultMilestone implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne private KeyResult parentKeyResult;

  @Column(length = 255)
  private String name;

  @PositiveOrZero private Long value;
}
