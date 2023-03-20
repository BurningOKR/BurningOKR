package org.burningokr.mapper.dashboard;

import lombok.extern.slf4j.Slf4j;
import org.burningokr.dto.dashboard.BaseChartOptionsDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
public class BaseChartOptionsMapper
  implements DataMapper<ChartCreationOptions, BaseChartOptionsDto> {

  @Override
  public ChartCreationOptions mapDtoToEntity(BaseChartOptionsDto dto) {
    ChartCreationOptions entity = new ChartCreationOptions();

    entity.setId(dto.getId());
    entity.setTitle(dto.getTitle());
    entity.setChartType(ChartInformationTypeEnum.values()[dto.getChartType()]);
    entity.setTeamIds(dto.getSelectedTeamIds());
//    entity.setDashboardCreation(dto.get);

    log.info(
      "Mapped BaseChartOptionsDto (id:"
        + dto.getId()
        + ") successful into ChartCreationOption.");

    return entity;
  }

  @Override
  public BaseChartOptionsDto mapEntityToDto(ChartCreationOptions input) {
    return null;
  }

  @Override
  public Collection<ChartCreationOptions> mapDtosToEntities(
    Collection<BaseChartOptionsDto> dtos
  ) {
    Collection<ChartCreationOptions> entities = new ArrayList<>();
    dtos.forEach(dto -> entities.add(mapDtoToEntity(dto)));
    return entities;
  }

  @Override
  public Collection<BaseChartOptionsDto> mapEntitiesToDtos(Collection<ChartCreationOptions> input) {
    return null;
  }
}
