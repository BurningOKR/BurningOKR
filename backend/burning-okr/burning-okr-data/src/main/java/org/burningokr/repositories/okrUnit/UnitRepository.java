package org.burningokr.repositories.okrUnit;

import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository<T extends OkrUnit> extends ExtendedRepository<T, Long> {
}
