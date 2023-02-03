package org.burningokr.model.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
