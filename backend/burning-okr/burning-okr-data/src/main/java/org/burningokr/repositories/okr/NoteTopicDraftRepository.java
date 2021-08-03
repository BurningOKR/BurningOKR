package org.burningokr.repositories.okr;

import java.util.Collection;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteTopicDraftRepository extends ExtendedRepository<NoteTopicDraft, Long> {
  Collection<NoteTopicDraft> findNoteTopicDraftsByParentTopicDraft_Id(long topicDraftId);
}
