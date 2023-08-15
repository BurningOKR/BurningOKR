package org.burningokr.model.okr;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;

@Entity
@Data
public class KeyResultMilestone implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")@SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  private Long id;

  @ToString.Exclude
  @ManyToOne
  private KeyResult parentKeyResult;

  @Column(length = 255)
  private String name;

  @PositiveOrZero
  private Long value;
}
