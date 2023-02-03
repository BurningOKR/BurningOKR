package org.burningokr.repositories.okr;

import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NoteTopicDraftRepository extends ExtendedRepository<NoteTopicDraft, Long> {
  Collection<NoteTopicDraft> findNoteTopicDraftsByParentTopicDraft_Id(long topicDraftId);
}
