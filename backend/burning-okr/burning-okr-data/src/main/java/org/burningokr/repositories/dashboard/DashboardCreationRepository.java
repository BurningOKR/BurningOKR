package org.burningokr.repositories.dashboard;

import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DashboardCreationRepository extends ExtendedRepository<DashboardCreation, Long> {

  @Query("SELECT dc FROM DashboardCreation dc WHERE dc.companyId = ?1")
  public List<DashboardCreation> findDashboardCreationsByCompanyId(Long companyId);
}
