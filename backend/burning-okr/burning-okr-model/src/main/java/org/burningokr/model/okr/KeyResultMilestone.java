package org.burningokr.model.okr;

import lombok.Data;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Data
public class KeyResultMilestone implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ToString.Exclude
  @ManyToOne
  private KeyResult parentKeyResult;

  @Column(length = 255)
  private String name;

  @PositiveOrZero
  private Long value;
}
