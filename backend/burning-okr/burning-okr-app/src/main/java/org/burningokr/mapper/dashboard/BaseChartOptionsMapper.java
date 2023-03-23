package org.burningokr.mapper.dashboard;

import lombok.extern.slf4j.Slf4j;
import org.burningokr.dto.dashboard.BaseChartOptionsDto;
import org.burningokr.dto.dashboard.LineChartOptionsDto;
import org.burningokr.dto.dashboard.PieChartOptionsDto;
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
//    System.out.println("Chart Type of DTO " + dto.getTitle() + ": " + dto.getChartType());
//    entity.setChartType(ChartInformationTypeEnum.values()[dto.getChartType()]);

    // Chart Type von Pie Charts ist aus unbekanntem Grund 0 statt 1
    // Im Frontend wird 1 ins Json geschrieben, aber im Backend kommt 0 an.
    if (dto instanceof LineChartOptionsDto) {
      entity.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    } else if (dto instanceof PieChartOptionsDto) {
      entity.setChartType(ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW);
    } else {
      throw new RuntimeException("Invalid BaseChartOptionsDto");
    }
//    System.out.println("Chart Type of Entity " + entity.getTitle() + ": " + entity.getChartType());
    entity.setTeamIds(dto.getSelectedTeamIds());

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
