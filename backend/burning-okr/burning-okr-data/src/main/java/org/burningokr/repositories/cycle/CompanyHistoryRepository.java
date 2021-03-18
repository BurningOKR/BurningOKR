package org.burningokr.repositories.cycle;

import org.burningokr.model.cycles.OkrUnitHistory;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

// TODO: (R.J.) 18.03.2021 Change this to be a generic HistoryRepository or create HistoryRepositories for departments and branches
@Repository
public interface CompanyHistoryRepository extends ExtendedRepository<OkrUnitHistory<OkrCompany>, Long> {}
