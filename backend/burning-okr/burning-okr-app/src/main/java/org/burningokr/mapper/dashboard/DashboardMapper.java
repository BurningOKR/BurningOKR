package org.burningokr.mapper.dashboard;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.dashboard.BaseChartOptionsDto;
import org.burningokr.dto.dashboard.DashboardDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.service.dashboard.ChartBuilderService;
import org.burningokr.service.userhandling.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardMapper implements DataMapper<DashboardCreation, DashboardDto> {
  private final ChartBuilderService chartBuilderService;
  private final UserService userService;

  @Override
  public DashboardCreation mapDtoToEntity(DashboardDto dtos) {
    return null;
  }

  @Override
  public DashboardDto mapEntityToDto(DashboardCreation entity) {
    Collection<BaseChartOptionsDto> chartOptionsDtos = entity.getChartCreationOptions().stream()
      .map(chartBuilderService::buildChart)
      .collect(Collectors.toList());

    return DashboardDto.builder()
      .id(entity.getId())
      .title(entity.getTitle())
      .companyId(entity.getCompanyId())
      .creator(userService.findById(entity.getCreatorId()))
      .chartDtos(chartOptionsDtos)
      .build();
  }

  @Override
  public Collection<DashboardCreation> mapDtosToEntities(Collection<DashboardDto> dtos) {
    return null;
  }

  @Override
  public Collection<DashboardDto> mapEntitiesToDtos(Collection<DashboardCreation> entities) {
    Collection<DashboardDto> dtos = new ArrayList<>();
    for (DashboardCreation entity : entities) {
      dtos.add(mapEntityToDto(entity));
    }
    return dtos;
  }
}
