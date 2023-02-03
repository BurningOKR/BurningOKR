package org.burningokr.repositories.okrUnit;

import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrUnitHistory;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends ExtendedRepository<OkrCompany, Long> {
  List<OkrCompany> findByHistory(OkrUnitHistory<OkrCompany> okrUnitHistory);
}
