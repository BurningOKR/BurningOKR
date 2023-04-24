package org.burningokr.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.repositories.dashboard.ChartCreationOptionsRepository;
import org.burningokr.repositories.dashboard.DashboardCreationRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.security.AuthorizationUserContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DashboardService {
  private final DashboardCreationRepository dashboardCreationRepository;
  private final ChartCreationOptionsRepository chartCreationOptionsRepository;
  private final ActivityService activityService;
  private final Logger logger = LoggerFactory.getLogger(DashboardService.class);
  private final AuthorizationUserContextService authorizationUserContextService;

  public DashboardCreation findDashboardCreationById(long dashboardId) {
    return dashboardCreationRepository.findByIdOrThrow(dashboardId);
  }

  public DashboardCreation createDashboard(DashboardCreation dashboardCreation) {
    dashboardCreation.setCreatorId(authorizationUserContextService.getAuthenticatedUser().getId());

    dashboardCreation = dashboardCreationRepository.save(dashboardCreation);
    logger.info("Created Dashboard: " + dashboardCreation.getTitle());
    activityService.createActivity(dashboardCreation, Action.CREATED);
    createChartOptions(dashboardCreation);

    return dashboardCreation;
  }

  public Collection<DashboardCreation> findDashboardsOfCompany(long companyId) {
    return dashboardCreationRepository.findDashboardCreationsByCompanyId(companyId);
  }

  private void createChartOptions(DashboardCreation dashboardCreation) {
    for (ChartCreationOptions chartCreationOption : dashboardCreation.getChartCreationOptions()) {
      chartCreationOption.setDashboardCreation(dashboardCreation);
      chartCreationOptionsRepository.save(chartCreationOption);
      logger.info("Created ChartCreationOption: " + chartCreationOption.getTitle());
    }
  }

  public void deleteDashboard(long dashboardId) {
    DashboardCreation dashboardCreationToDelete = dashboardCreationRepository.findByIdOrThrow(dashboardId);

    dashboardCreationRepository.deleteById(dashboardCreationToDelete.getId());
    activityService.createActivity(dashboardCreationToDelete, Action.DELETED);
  }
}
