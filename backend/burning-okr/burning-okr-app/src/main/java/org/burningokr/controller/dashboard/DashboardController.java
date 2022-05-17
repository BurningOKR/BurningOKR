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
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestApiController
public class DashboardController {
  private DashboardService dashboardService;
  private DataMapper<DashboardCreation, DashboardCreationDto> dashboardCreationMapper;
  private DataMapper<DashboardCreation, DashboardDto> dashboardMapper;

  @Autowired
  public DashboardController(
    DashboardService dashboardService,
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
   * @param user                 a {@link User} object
   * @return a {@link ResponseEntity} ok with the created DashboardCreation
   */
  @PostMapping("/dashboards")
  public ResponseEntity<DashboardCreationDto> createDashboard(
    @RequestBody DashboardCreationDto dashboardCreationDto, User user
  ) {
    DashboardCreation dashboardCreation = dashboardCreationMapper.mapDtoToEntity(dashboardCreationDto);
    dashboardCreation = dashboardService.createDashboard(dashboardCreation, user);
    return ResponseEntity.ok(dashboardCreationMapper.mapEntityToDto(dashboardCreation));
  }

  /**
   * API Endpoint to get a DashboardDto by id.
   *
   * @param dashboardId a {@link Long} object
   * @return a {@link ResponseEntity} ok with a {@Link DashboardDto}
   */
  @GetMapping("/dashboards/{dashboardId}")
  public ResponseEntity<DashboardDto> getDashboardById(
    @PathVariable long dashboardId
  ) {
    DashboardCreation dashboardCreation = dashboardService.findDashboardCreationById(dashboardId);
    DashboardDto dashboardDto = dashboardMapper.mapEntityToDto(dashboardCreation);
    return ResponseEntity.ok(dashboardDto);
  }

  /**
   * API Endpoint to get all dashboards of a company
   *
   * @param companyId a {@link Long} object
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Dashboards
   */
  @GetMapping("/dashboards/company/{companyId}")
  public ResponseEntity<Collection<DashboardDto>> getDashboardsOfCompany(
    @PathVariable long companyId
  ) {
    Collection<DashboardCreation> dashboardCreations = dashboardService.findDashboardsOfCompany(companyId);
    return ResponseEntity.ok(dashboardMapper.mapEntitiesToDtos(dashboardCreations));
  }

  /**
   * API Endpoint to delete a DashboardCreation
   *
   * @param dashboardId a {@link Long} object
   * @param user        a {@link User} object
   * @return a {@link ResponseEntity} ok
   */
  @DeleteMapping("dashboards/{dashboardId}")
  public ResponseEntity deleteDashboard(
    @PathVariable long dashboardId, User user
  ) {
    dashboardService.deleteDashboard(dashboardId, user);
    return ResponseEntity.ok().build();
  }
}

