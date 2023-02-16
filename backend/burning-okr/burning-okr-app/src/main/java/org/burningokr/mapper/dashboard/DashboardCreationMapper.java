package org.burningokr.mapper.dashboard;

import org.burningokr.dto.dashboard.creation.ChartCreationOptionsDto;
import org.burningokr.dto.dashboard.creation.DashboardCreationDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.DashboardCreation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class DashboardCreationMapper
  implements DataMapper<DashboardCreation, DashboardCreationDto> {
  private final Logger logger = LoggerFactory.getLogger(DashboardCreationMapper.class);
  private DataMapper<ChartCreationOptions, ChartCreationOptionsDto> chartCreationMapper;

  @Autowired
  public DashboardCreationMapper(
    DataMapper<ChartCreationOptions, ChartCreationOptionsDto> chartCreationMapper
  ) {
    this.chartCreationMapper = chartCreationMapper;
  }

  @Override
  public DashboardCreation mapDtoToEntity(DashboardCreationDto dto) {
    DashboardCreation entity = new DashboardCreation();
    entity.setId(dto.getId());
    entity.setTitle(dto.getTitle());
    entity.setCreatorId(dto.getCreatorId());
    entity.setCompanyId(dto.getCompanyId());
    entity.setChartCreationOptions(
      chartCreationMapper.mapDtosToEntities(dto.getChartCreationOptions()));

    logger.info(
      "Mapped DashboardCreationDto (id:" + dto.getId() + ") successful into DashboardCreation.");

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
      chartCreationMapper.mapEntitiesToDtos(entity.getChartCreationOptions()));

    logger.info(
      "Mapped DashboardCreation (id:"
        + entity.getId()
        + ") successful into DashboardCreationDto.");

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
