package org.burningokr.controller.dashboard;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.dashboard.DashboardDto;
import org.burningokr.dto.dashboard.creation.DashboardCreationDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.model.users.User;
import org.burningokr.service.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class DashboardController {
  private DashboardService dashboardService;
  private DataMapper<DashboardCreation, DashboardCreationDto> dashboardCreationMapper;
  private DataMapper<DashboardCreation, DashboardDto> dashboardMapper;

  @Autowired
  public DashboardController(DashboardService dashboardService,
                             DataMapper<DashboardCreation, DashboardCreationDto> dashboardCreationMapper,
                             DataMapper<DashboardCreation, DashboardDto> dashboardMapper
  ) {
    this.dashboardService = dashboardService;
    this.dashboardCreationMapper = dashboardCreationMapper;
    this.dashboardMapper = dashboardMapper;
  }

  /**
   * API Endpoint to create a DashboardCreation
   *
   * @param dashboardCreationDto a {@link DashboardCreationDto} object
   * @param user                 an {@link User} object
   * @return a {@link ResponseEntity} ok with the created DashboardCreation
   */
  @PostMapping("/dashboards")
  public ResponseEntity<DashboardCreationDto> createDashboard(
    @RequestBody DashboardCreationDto dashboardCreationDto, User user) {
    DashboardCreation dashboardCreation = dashboardCreationMapper.mapDtoToEntity(dashboardCreationDto);
    dashboardCreation = dashboardService.createDashboard(dashboardCreation, user);
    return ResponseEntity.ok(dashboardCreationMapper.mapEntityToDto(dashboardCreation));
  }

  /**
   * API Endpoint to get a DashboardDto by id.
   *
   */
  @GetMapping("/dashboards/{dashboardId}")
  public ResponseEntity<DashboardDto> getDashboardById(@PathVariable long dashboardId) {
    DashboardCreation dashboardCreation = dashboardService.findDashboardCreationById(dashboardId);
    return ResponseEntity.ok(dashboardMapper.mapEntityToDto(dashboardCreation));
  }
}

