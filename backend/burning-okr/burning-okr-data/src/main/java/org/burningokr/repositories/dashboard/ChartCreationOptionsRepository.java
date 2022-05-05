package org.burningokr.repositories.dashboard;

import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartCreationOptionsRepository extends ExtendedRepository<ChartCreationOptions, Long> {
}
