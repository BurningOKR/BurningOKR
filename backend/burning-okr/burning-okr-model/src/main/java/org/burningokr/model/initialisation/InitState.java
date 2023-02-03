package org.burningokr.model.initialisation;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class InitState {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private InitStateName initState;

  @Transient
  private String runtimeId;
}
