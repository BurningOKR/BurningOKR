package org.burningokr.mapper.dashboard;

import org.burningokr.dto.dashboard.BaseChartOptionsDto;
import org.burningokr.dto.dashboard.LineChartOptionsDto;
import org.burningokr.dto.dashboard.PieChartOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class BaseChartOptionsMapperTest {
  private BaseChartOptionsMapper baseChartOptionsMapper;
  private ChartCreationOptions chartCreationOptionsLine;
  private ChartCreationOptions chartCreationOptionsPie;
  private BaseChartOptionsDto baseChartOptionsDtoLine;
  private BaseChartOptionsDto baseChartOptionsDtoPie;

  @BeforeEach
  public void init() {
    chartCreationOptionsLine = new ChartCreationOptions();
    chartCreationOptionsPie = new ChartCreationOptions();
    baseChartOptionsDtoLine = new LineChartOptionsDto();
    baseChartOptionsDtoPie = new PieChartOptionsDto();
    baseChartOptionsMapper = new BaseChartOptionsMapper();

    baseChartOptionsDtoLine.setId(5L);
    baseChartOptionsDtoLine.setTitle("expectedTitle");
    baseChartOptionsDtoLine.setChartType(0);
    baseChartOptionsDtoLine.setSelectedTeamIds(new ArrayList<>());

    baseChartOptionsDtoPie.setId(5L);
    baseChartOptionsDtoPie.setTitle("expectedTitle");
    baseChartOptionsDtoPie.setChartType(1);
    baseChartOptionsDtoPie.setSelectedTeamIds(new ArrayList<>());

    chartCreationOptionsLine.setId(5L);
    chartCreationOptionsLine.setTitle("expectedTitle");
    chartCreationOptionsLine.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    chartCreationOptionsLine.setTeamIds(new ArrayList<>());

    chartCreationOptionsPie.setId(5L);
    chartCreationOptionsPie.setTitle("expectedTitle");
    chartCreationOptionsPie.setChartType(ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW);
    chartCreationOptionsPie.setTeamIds(new ArrayList<>());
  }

  @Test
  public void test_mapDtoToEntity_line() {
    ChartCreationOptions mapped_entity = baseChartOptionsMapper.mapDtoToEntity(baseChartOptionsDtoLine);
    assertEquals(chartCreationOptionsLine, mapped_entity);
  }

  @Test
  public void test_mapDtoToEntity_pie() {
    ChartCreationOptions mapped_entity = baseChartOptionsMapper.mapDtoToEntity(baseChartOptionsDtoPie);
    assertEquals(chartCreationOptionsPie, mapped_entity);
  }

}
