package org.burningokr.service.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.activity.Action;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.repositories.dashboard.ChartCreationOptionsRepository;
import org.burningokr.repositories.dashboard.DashboardCreationRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
  private final DashboardCreationRepository dashboardCreationRepository;
  private final ChartCreationOptionsRepository chartCreationOptionsRepository;
  private final ActivityService activityService;
  private final AuthenticationUserContextService authenticationUserContextService;

  public DashboardCreation findDashboardCreationById(long dashboardId) {
    return dashboardCreationRepository.findByIdOrThrow(dashboardId);
  }

  public DashboardCreation createDashboard(DashboardCreation dashboardCreation) {
    dashboardCreation.setCreatorId(authenticationUserContextService.getAuthenticatedUser().getId());

    dashboardCreation = dashboardCreationRepository.save(dashboardCreation);
    log.debug("Created Dashboard: %s.".formatted(dashboardCreation.getTitle()));
    activityService.createActivity(dashboardCreation, Action.CREATED);
    createChartOptions(dashboardCreation);

    return dashboardCreation;
  }

  public DashboardCreation updateDashboard(DashboardCreation dashboardCreation) {
    dashboardCreation = dashboardCreationRepository.save(dashboardCreation);

    log.debug("Updated Dashboard: %s.".formatted(dashboardCreation.getTitle()));
    activityService.createActivity(dashboardCreation, Action.EDITED);
    return dashboardCreation;
  }

  public Collection<DashboardCreation> findDashboardsOfCompany(long companyId) {
    return dashboardCreationRepository.findDashboardCreationsByCompanyId(companyId);
  }

  private void createChartOptions(DashboardCreation dashboardCreation) {
    for (ChartCreationOptions chartCreationOption : dashboardCreation.getChartCreationOptions()) {
      chartCreationOptionsRepository.save(chartCreationOption);
      log.debug("Created ChartCreationOption: %s.".formatted(chartCreationOption.getTitle()));
    }
  }

  public void deleteDashboard(long dashboardId) {
    DashboardCreation dashboardCreationToDelete = dashboardCreationRepository.findByIdOrThrow(dashboardId);

    dashboardCreationRepository.deleteById(dashboardCreationToDelete.getId());
    activityService.createActivity(dashboardCreationToDelete, Action.DELETED);
  }
}
