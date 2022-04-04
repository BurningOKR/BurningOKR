package org.burningokr.controller.dashboard;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.dashboard.DashboardCreationDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.model.users.User;
import org.burningokr.service.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class DashboardController {
  private DashboardService dashboardService;
  private DataMapper<DashboardCreation, DashboardCreationDto> dashboardCreationMapper;

  @Autowired
  public DashboardController(DashboardService dashboardService,
                             DataMapper<DashboardCreation, DashboardCreationDto> dashboardCreationMapper
                             ) {
    this.dashboardService = dashboardService;
    this.dashboardCreationMapper = dashboardCreationMapper;
  }

  /**
   * API Endpoint to add a TopicDraft to an existing Okr Branch
   *
   * @param dashboardCreationDto a {@link DashboardCreationDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with the added topicdraft
   */
  @PostMapping("/dashboards")
  public ResponseEntity<DashboardCreationDto> createDashboard(
    @RequestBody DashboardCreationDto dashboardCreationDto, User user) {
    DashboardCreation dashboardCreation = dashboardCreationMapper.mapDtoToEntity(dashboardCreationDto);
    dashboardCreation = dashboardService.createDashboard(dashboardCreation, user);
    return ResponseEntity.ok(dashboardCreationMapper.mapEntityToDto(dashboardCreation));
  }
}
