package org.burningokr.mapper.dashboard;

import lombok.extern.slf4j.Slf4j;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.model.dashboard.creation.ChartCreationOptionsDto;
import org.burningokr.model.dashboard.creation.DashboardCreationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
public class DashboardCreationMapper implements DataMapper<DashboardCreation, DashboardCreationDto> {
  private DataMapper<ChartCreationOptions, ChartCreationOptionsDto> chartCreationOptionsMapper;

  @Autowired
  public DashboardCreationMapper(
    DataMapper<ChartCreationOptions, ChartCreationOptionsDto> chartCreationOptionsMapper
  ) {
    this.chartCreationOptionsMapper = chartCreationOptionsMapper;
  }

  @Override
  public DashboardCreation mapDtoToEntity(DashboardCreationDto dto) {
    DashboardCreation entity = new DashboardCreation();
    entity.setId(dto.getId());
    entity.setTitle(dto.getTitle());
    entity.setCreatorId(dto.getCreatorId());
    entity.setCompanyId(dto.getCompanyId());
    entity.setChartCreationOptions(
      chartCreationOptionsMapper.mapDtosToEntities(dto.getChartCreationOptions()));

    log.debug("Mapped DashboardCreationDto (id: %d) successful into DashboardCreation.".formatted(dto.getId()));

    return entity;
  }

  @Override
  public DashboardCreationDto mapEntityToDto(DashboardCreation entity) {
    DashboardCreationDto dto = new DashboardCreationDto();
    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setCreatorId(entity.getCreatorId());
    dto.setCompanyId(entity.getCompanyId());
    dto.setChartCreationOptions(
      chartCreationOptionsMapper.mapEntitiesToDtos(entity.getChartCreationOptions()));

    log.debug("Mapped DashboardCreation (id: %d) successful into DashboardCreationDto.".formatted(entity.getId()));

    return dto;
  }

  @Override
  public Collection<DashboardCreation> mapDtosToEntities(Collection<DashboardCreationDto> dtos) {
    Collection<DashboardCreation> dashboardCreations = new ArrayList<>();
    dtos.forEach(dto -> dashboardCreations.add(mapDtoToEntity(dto)));
    return dashboardCreations;
  }

  @Override
  public Collection<DashboardCreationDto> mapEntitiesToDtos(
    Collection<DashboardCreation> entities
  ) {
    Collection<DashboardCreationDto> dashboardCreationDtos = new ArrayList<>();
    entities.forEach(entity -> dashboardCreationDtos.add(mapEntityToDto(entity)));
    return dashboardCreationDtos;
  }
}
