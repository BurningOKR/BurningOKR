package org.burningokr.model.okr.okrTopicDraft;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.histories.OkrTopicDraftHistory;
import org.burningokr.model.okrUnits.OkrUnit;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class OkrTopicDraft extends OkrTopicDescription {

  @Nullable
  @ManyToOne
  private OkrUnit parentUnit;

  @ManyToOne
  private OkrTopicDraftHistory history;

  @Enumerated(EnumType.STRING)
  private OkrTopicDraftStatusEnum currentStatus;

  @ToString.Exclude
  @OneToMany(mappedBy = "parentTopicDraft", cascade = CascadeType.REMOVE)
  private Collection<NoteTopicDraft> notes = new ArrayList<>();
}
