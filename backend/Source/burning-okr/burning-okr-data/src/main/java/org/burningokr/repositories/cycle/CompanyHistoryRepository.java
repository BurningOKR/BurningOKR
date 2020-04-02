package org.burningokr.repositories.cycle;

import org.burningokr.model.cycles.CompanyHistory;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyHistoryRepository extends ExtendedRepository<CompanyHistory, Long> {}
