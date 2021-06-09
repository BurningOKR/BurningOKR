package org.burningokr.repositories.cycle;

import org.burningokr.model.okrUnits.okrUnitHistories.OkrCompanyHistory;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyHistoryRepository extends ExtendedRepository<OkrCompanyHistory, Long> {}
