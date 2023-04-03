package org.burningokr.model.initialisation;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class InitState {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
  private Long id;

  private InitStateName initState;

  @Transient
  private String runtimeId;
}
