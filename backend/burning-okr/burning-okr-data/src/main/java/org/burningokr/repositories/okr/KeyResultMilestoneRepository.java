package org.burningokr.repositories.okr;

import org.burningokr.model.okr.KeyResultMilestone;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyResultMilestoneRepository
    extends ExtendedRepository<KeyResultMilestone, Long> {}
