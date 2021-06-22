package org.burningokr.repositories.cycle;

import org.burningokr.model.okr.histories.OkrTopicDraftHistory;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicDraftHistoryRepository
    extends ExtendedRepository<OkrTopicDraftHistory, Long> {}
