package org.burningokr.model.initialisation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class InitState {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private InitStateName initState;

  @Transient private String runtimeId;
}
