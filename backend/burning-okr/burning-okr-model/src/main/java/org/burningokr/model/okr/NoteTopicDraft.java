package org.burningokr.model.okr;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class NoteTopicDraft extends Note {

  @ManyToOne
  private OkrTopicDraft parentTopicDraft;

  public NoteTopicDraft() {
    super();
  }

  public NoteTopicDraft(Note note) {
    super(note);
  }
}
