package org.burningokr.repositories.structre;

import java.util.List;
import org.burningokr.model.cycles.CompanyHistory;
import org.burningokr.model.structures.Company;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends ExtendedRepository<Company, Long> {
  List<Company> findByHistory(CompanyHistory companyHistory);
}
