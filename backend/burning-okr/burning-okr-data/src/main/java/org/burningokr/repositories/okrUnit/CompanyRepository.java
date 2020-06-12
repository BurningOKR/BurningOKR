package org.burningokr.repositories.okrUnit;

import java.util.List;
import org.burningokr.model.cycles.OkrCompanyHistory;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends ExtendedRepository<OkrCompany, Long> {
  List<OkrCompany> findByHistory(OkrCompanyHistory okrCompanyHistory);
}
