package org.burningokr.mapper.dashboard;

import org.burningokr.dto.dashboard.ChartCreationOptionsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ChartCreationOptionsMapper implements DataMapper<ChartCreationOptions, ChartCreationOptionsDto> {

  private final Logger logger = LoggerFactory.getLogger(ChartCreationOptionsMapper.class);

  @Override
  public ChartCreationOptions mapDtoToEntity(ChartCreationOptionsDto dto) {
    ChartCreationOptions entity = new ChartCreationOptions();

    entity.setId(dto.getId());
    entity.setTitle(dto.getTitle());
    entity.setChartType(dto.getChartType());
    entity.setTeamIds(dto.getTeamIds());

    logger.info("Mapped ChartCreationOptionsDto (id:" + dto.getId() + ") successful into ChartCreationOption.");

    return entity;
  }

  @Override
  public ChartCreationOptionsDto mapEntityToDto(ChartCreationOptions entity) {
    ChartCreationOptionsDto dto = new ChartCreationOptionsDto();

    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setChartType(entity.getChartType());
    dto.setTeamIds(entity.getTeamIds());

    logger.info("Mapped ChartCreationOptions (id:" + entity.getId() + ") successful into ChartCreationOptionsDto.");

    return dto;
  }

  @Override
  public Collection<ChartCreationOptions> mapDtosToEntities(Collection<ChartCreationOptionsDto> dtos) {
    Collection<ChartCreationOptions> entities = new ArrayList<>();
    dtos.forEach(dto -> entities.add(mapDtoToEntity(dto)));
    return entities;
  }

  @Override
  public Collection<ChartCreationOptionsDto> mapEntitiesToDtos(Collection<ChartCreationOptions> entities) {
    Collection<ChartCreationOptionsDto> dtos = new ArrayList<>();
    entities.forEach(entity -> dtos.add(mapEntityToDto(entity)));
    return dtos;
  }
}
