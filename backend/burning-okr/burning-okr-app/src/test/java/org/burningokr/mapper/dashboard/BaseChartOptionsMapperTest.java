package org.burningokr.mapper.dashboard;

import org.burningokr.dto.dashboard.BaseChartOptionsDto;
import org.burningokr.dto.dashboard.DashboardDto;
import org.burningokr.dto.dashboard.LineChartOptionsDto;
import org.burningokr.dto.dashboard.PieChartOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.service.dashboard.ChartBuilderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;


@RunWith(MockitoJUnitRunner.class)
public class BaseChartOptionsMapperTest {
  private BaseChartOptionsMapper mapper;
  private ChartCreationOptions entityLine;
  private ChartCreationOptions entityPie;
  private BaseChartOptionsDto dtoLine;
  private BaseChartOptionsDto dtoPie;

  @Before
  public void init() {
    entityLine = new ChartCreationOptions();
    entityPie = new ChartCreationOptions();
    dtoLine = new LineChartOptionsDto();
    dtoPie = new PieChartOptionsDto();
    mapper = new BaseChartOptionsMapper();

    dtoLine.setId(5L);
    dtoLine.setTitle("expectedTitle");
    dtoLine.setChartType(0);
    dtoLine.setSelectedTeamIds(new ArrayList<Long>());

    dtoPie.setId(5L);
    dtoPie.setTitle("expectedTitle");
    dtoPie.setChartType(1);
    dtoPie.setSelectedTeamIds(new ArrayList<Long>());

    entityLine.setId(5L);
    entityLine.setTitle("expectedTitle");
    entityLine.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    entityLine.setTeamIds(new ArrayList<Long>());

    entityPie.setId(5L);
    entityPie.setTitle("expectedTitle");
    entityPie.setChartType(ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW);
    entityPie.setTeamIds(new ArrayList<Long>());
  }

  @Test
  public void test_mapDtoToEntity_line() {
    ChartCreationOptions mapped_entity = mapper.mapDtoToEntity(dtoLine);
    Assert.assertEquals(entityLine, mapped_entity);
  }
  @Test
  public void test_mapDtoToEntity_pie() {
    ChartCreationOptions mapped_entity = mapper.mapDtoToEntity(dtoPie);
    Assert.assertEquals(entityPie, mapped_entity);
  }

}