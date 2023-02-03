package org.burningokr.repositories.okr;

import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OkrTopicDescriptionRepository
  extends ExtendedRepository<OkrTopicDescription, Long> {
}
