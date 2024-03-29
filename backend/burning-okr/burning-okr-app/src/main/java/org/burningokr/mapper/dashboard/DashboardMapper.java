package org.burningokr.mapper.dashboard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.BaseChartOptionsDto;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.model.dashboard.DashboardDto;
import org.burningokr.service.dashboard.ChartBuilderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardMapper implements DataMapper<DashboardCreation, DashboardDto> {
  private final ChartBuilderService chartBuilderService;
  private final BaseChartOptionsMapper baseChartOptionsMapper;

  @Override
  public DashboardCreation mapDtoToEntity(DashboardDto dto) {
    DashboardCreation entity = new DashboardCreation();
    entity.setId(dto.getId());
    entity.setTitle(dto.getTitle());
    entity.setCreatorId(dto.getCreatorId());
    entity.setCompanyId(dto.getCompanyId());
    entity.setChartCreationOptions(
      baseChartOptionsMapper.mapDtosToEntities(dto.getChartDtos())
    );

    log.debug("Mapped DashboardCreationDto (id: %d) successful into DashboardCreation.".formatted(dto.getId()));

    return entity;
  }

  @Override
  public DashboardDto mapEntityToDto(DashboardCreation entity) {
    Collection<BaseChartOptionsDto> chartOptionsDtos = entity.getChartCreationOptions().stream()
      .map(chart -> chartBuilderService.buildChart(chart, entity.getCompanyId()))
      .collect(Collectors.toList());

    return DashboardDto.builder()
      .id(entity.getId())
      .title(entity.getTitle())
      .companyId(entity.getCompanyId())
      .creatorId(entity.getCreatorId())
      .chartDtos(chartOptionsDtos)
      .build();
  }

  @Override
  public Collection<DashboardCreation> mapDtosToEntities(Collection<DashboardDto> dtos) {
    Collection<DashboardCreation> entities = new ArrayList<>();
    for (DashboardDto dto : dtos) {
      entities.add(mapDtoToEntity(dto));
    }
    return entities;
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
