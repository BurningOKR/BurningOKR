package org.burningokr.repositories.cycle;

import org.burningokr.model.cycles.OkrCompanyHistory;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyHistoryRepository extends ExtendedRepository<OkrCompanyHistory, Long> {}
