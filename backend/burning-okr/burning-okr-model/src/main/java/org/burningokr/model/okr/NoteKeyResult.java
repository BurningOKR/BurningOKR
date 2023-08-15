package org.burningokr.model.okr;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
