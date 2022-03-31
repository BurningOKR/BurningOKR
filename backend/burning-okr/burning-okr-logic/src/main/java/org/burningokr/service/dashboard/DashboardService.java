package org.burningokr.service.dashboard;

import org.burningokr.model.activity.Action;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.model.users.User;
import org.burningokr.repositories.dashboard.DashboardCreationRepository;
import org.burningokr.service.activity.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
  private final DashboardCreationRepository dashboardCreationRepository;
  private final ActivityService activityService;
  private final Logger logger = LoggerFactory.getLogger(DashboardService.class);

  @Autowired
  public DashboardService(DashboardCreationRepository dashboardCreationRepository,
                          ActivityService activityService) {
    this.dashboardCreationRepository = dashboardCreationRepository;
    this.activityService = activityService;
  }

  public DashboardCreation createDashboard(DashboardCreation dashboardCreation, User user) {
    dashboardCreation.setCreatorId(user.getId());

    dashboardCreation = this.dashboardCreationRepository.save(dashboardCreation);

    logger.info("Created Dashboard: " + dashboardCreation.getTitle());
    activityService.createActivity(user, dashboardCreation, Action.CREATED);

    return dashboardCreation;
  }
}
