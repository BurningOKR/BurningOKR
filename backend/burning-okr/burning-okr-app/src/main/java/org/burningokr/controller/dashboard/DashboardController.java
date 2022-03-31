package org.burningokr.controller.dashboard;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.dashboard.DashboardCreationDto;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.model.users.User;
import org.burningokr.service.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class DashboardController {
  private DashboardService dashboardService;

  @Autowired
  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  /**
   * API Endpoint to add a TopicDraft to an existing Okr Branch
   *
   * @param dashboardCreationDto a {@link DashboardCreationDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with the added topicdraft
   */
  @PostMapping("/dashboards/create")
  public ResponseEntity<DashboardCreationDto> createDashboard(
    @RequestBody DashboardCreationDto dashboardCreationDto, User user) {
    return null;
  }
}
