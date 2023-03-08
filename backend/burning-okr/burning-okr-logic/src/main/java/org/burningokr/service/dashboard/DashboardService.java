package org.burningokr.service.dashboard;

import org.burningokr.model.activity.Action;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.dashboard.ChartCreationOptionsRepository;
import org.burningokr.repositories.dashboard.DashboardCreationRepository;
import org.burningokr.service.activity.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DashboardService {
  private final DashboardCreationRepository dashboardCreationRepository;
  private final ChartCreationOptionsRepository chartCreationOptionsRepository;
  private final ActivityService activityService;
  private final Logger logger = LoggerFactory.getLogger(DashboardService.class);

  @Autowired
  public DashboardService(
    DashboardCreationRepository dashboardCreationRepository,
    ChartCreationOptionsRepository chartCreationOptionsRepository,
    ActivityService activityService
  ) {
    this.dashboardCreationRepository = dashboardCreationRepository;
    this.chartCreationOptionsRepository = chartCreationOptionsRepository;
    this.activityService = activityService;
  }

  public DashboardCreation findDashboardCreationById(long dashboardId) {
    return dashboardCreationRepository.findByIdOrThrow(dashboardId);
  }

  public DashboardCreation createDashboard(DashboardCreation dashboardCreation, IUser IUser) {
    dashboardCreation.setCreatorId(IUser.getId());

    dashboardCreation = dashboardCreationRepository.save(dashboardCreation);
    logger.info("Created Dashboard: " + dashboardCreation.getTitle());
    activityService.createActivity(IUser, dashboardCreation, Action.CREATED);
    createChartOptions(dashboardCreation, IUser);

    return dashboardCreation;
  }

  public Collection<DashboardCreation> findDashboardsOfCompany(long companyId) {
    return dashboardCreationRepository.findDashboardCreationsByCompanyId(companyId);
  }

  private void createChartOptions(DashboardCreation dashboardCreation, IUser IUser) {
    for (ChartCreationOptions chartCreationOption : dashboardCreation.getChartCreationOptions()) {
      chartCreationOption.setDashboardCreation(dashboardCreation);
      chartCreationOptionsRepository.save(chartCreationOption);
      logger.info("Created ChartCreationOption: " + chartCreationOption.getTitle());
    }
  }

  public void deleteDashboard(long dashboardId, IUser IUser) {
    DashboardCreation dashboardCreationToDelete = dashboardCreationRepository.findByIdOrThrow(dashboardId);

    dashboardCreationRepository.deleteById(dashboardCreationToDelete.getId());
    activityService.createActivity(IUser, dashboardCreationToDelete, Action.DELETED);
  }
}
