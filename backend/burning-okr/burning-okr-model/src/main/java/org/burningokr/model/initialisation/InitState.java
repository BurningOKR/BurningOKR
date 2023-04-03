package org.burningokr.model.initialisation;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class InitState {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  private Long id;

  private InitStateName initState;

  @Transient
  private String runtimeId;
}
