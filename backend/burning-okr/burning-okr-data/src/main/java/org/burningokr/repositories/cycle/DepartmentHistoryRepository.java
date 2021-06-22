package org.burningokr.repositories.cycle;

import org.burningokr.model.okrUnits.okrUnitHistories.OkrDepartmentHistory;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentHistoryRepository
    extends ExtendedRepository<OkrDepartmentHistory, Long> {}
