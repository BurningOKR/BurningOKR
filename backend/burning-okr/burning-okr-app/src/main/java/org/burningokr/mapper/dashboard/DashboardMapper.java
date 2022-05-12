package org.burningokr.mapper.dashboard;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.dashboard.BaseChartOptionsDto;
import org.burningokr.dto.dashboard.DashboardDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.service.dashboard.ChartBuilderService;
import org.burningokr.service.userhandling.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

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
    DashboardDto dto = new DashboardDto();
    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setCreator(userService.findById(entity.getCreatorId()));

    BaseChartOptionsDto[] chartOptionsDtos = entity.getChartCreationOptions().stream().map(chartBuilderService::buildChart).toArray(BaseChartOptionsDto[]::new);
    dto.setChartDtos(chartOptionsDtos);

    return dto;
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
