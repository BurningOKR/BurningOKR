package org.burningokr.repositories.dashboard;

import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardCreationRepository extends ExtendedRepository<DashboardCreation, Long> {
}
