package org.burningokr.mapper.dashboard;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.dashboard.BaseChartOptionsDto;
import org.burningokr.dto.dashboard.DashboardDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.service.ChartBuilderService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardMapper implements DataMapper<DashboardCreation, DashboardDto> {
  private final ChartBuilderService chartBuilderService;

  @Override
  public DashboardCreation mapDtoToEntity(DashboardDto input) {
    return null;
  }

  @Override
  public DashboardDto mapEntityToDto(DashboardCreation entity) {
    DashboardDto dto = new DashboardDto();
    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setCreatorId(entity.getCreatorId());

    BaseChartOptionsDto[] chartOptionsDtos = entity.getChartCreationOptions().stream().map(chartBuilderService::buildChart).toArray(BaseChartOptionsDto[]::new);
    dto.setChartDtos(chartOptionsDtos);

    return dto;
  }

  @Override
  public Collection<DashboardCreation> mapDtosToEntities(Collection<DashboardDto> input) {
    return null;
  }

  @Override
  public Collection<DashboardDto> mapEntitiesToDtos(Collection<DashboardCreation> input) {
    return null;
  }
}
