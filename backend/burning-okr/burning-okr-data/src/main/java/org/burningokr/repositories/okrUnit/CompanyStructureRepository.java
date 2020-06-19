package org.burningokr.repositories.okrUnit;

import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyStructureRepository extends ExtendedRepository<OkrUnit, Long> {}
// Todo 10.06.2020 dturnschek; Rename?
