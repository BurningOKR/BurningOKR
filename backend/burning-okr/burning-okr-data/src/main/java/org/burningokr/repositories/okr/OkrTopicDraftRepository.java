package org.burningokr.repositories.okr;

import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OkrTopicDraftRepository extends ExtendedRepository<OkrTopicDraft, Long> {
}
