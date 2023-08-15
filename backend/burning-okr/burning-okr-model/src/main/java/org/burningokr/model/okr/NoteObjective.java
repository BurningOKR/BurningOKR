package org.burningokr.model.okr;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class NoteObjective extends Note {

  @ManyToOne
  private Objective parentObjective;
}
