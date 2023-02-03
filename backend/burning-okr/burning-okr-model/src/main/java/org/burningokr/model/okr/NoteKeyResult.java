package org.burningokr.model.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class NoteKeyResult extends Note {

  @ManyToOne
  private KeyResult parentKeyResult;

  public NoteKeyResult() {
    super();
  }

  public NoteKeyResult(Note note) {
    super(note);
  }
}
