package org.burningokr.repositories.log;

import org.burningokr.model.log.FrontendLog;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontendLoggerRepository extends ExtendedRepository<FrontendLog, Long> {}
