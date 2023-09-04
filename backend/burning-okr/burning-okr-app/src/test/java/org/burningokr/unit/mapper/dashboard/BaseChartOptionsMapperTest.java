package org.burningokr.unit.mapper.dashboard;

import org.burningokr.mapper.dashboard.BaseChartOptionsMapper;
import org.burningokr.model.dashboard.BaseChartOptionsDto;
import org.burningokr.model.dashboard.LineChartOptionsDto;
import org.burningokr.model.dashboard.PieChartOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
  public void mapDtoToEntity_shouldMapChartCreationOptionsLine() {
    ChartCreationOptions mapped_entity = baseChartOptionsMapper.mapDtoToEntity(baseChartOptionsDtoLine);
    assertEquals(chartCreationOptionsLine, mapped_entity);
  }

  @Test
  public void mapDtoToEntity_shouldMapChartCreationOptionsPie() {
    ChartCreationOptions mapped_entity = baseChartOptionsMapper.mapDtoToEntity(baseChartOptionsDtoPie);
    assertEquals(chartCreationOptionsPie, mapped_entity);
  }

  @Test
  public void mapDtoToEntity_shouldThrowRuntimeException() throws RuntimeException {
    BaseChartOptionsDto errorChatOptionsDto = new ErrorChatOptionsDto();
    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> baseChartOptionsMapper.mapDtoToEntity(errorChatOptionsDto));
    assertEquals("Invalid BaseChartOptionsDto", runtimeException.getMessage());
  }

  @Test
  public void mapDtosToEntities_shouldMapChartCreationOptionsLine() {
    Collection<BaseChartOptionsDto> baseChartOptionsDtos = new ArrayList<>() {
      {
        add(new LineChartOptionsDto());
        add(new LineChartOptionsDto());
        add(new LineChartOptionsDto());
      }
    };
    Collection<ChartCreationOptions> actual = baseChartOptionsMapper.mapDtosToEntities(baseChartOptionsDtos);
    assertEquals(3, actual.size());
  }

  @Test
  public void mapDtosToEntities_shouldMapChartCreationOptionsPie() {
    Collection<BaseChartOptionsDto> pieChartOptionsDto = new ArrayList<>() {
      {
        add(new PieChartOptionsDto());
        add(new PieChartOptionsDto());
        add(new PieChartOptionsDto());
      }
    };
    Collection<ChartCreationOptions> actual = baseChartOptionsMapper.mapDtosToEntities(pieChartOptionsDto);
    assertEquals(3, actual.size());
  }

  @Test
  public void mapDtosToEntities_shouldMapChartCreationOptionsPiesAndLines() {
    Collection<BaseChartOptionsDto> baseChartOptionsDto = new ArrayList<>() {
      {
        add(new PieChartOptionsDto());
        add(new LineChartOptionsDto());
        add(new PieChartOptionsDto());
        add(new LineChartOptionsDto());
      }
    };
    Collection<ChartCreationOptions> actual = baseChartOptionsMapper.mapDtosToEntities(baseChartOptionsDto);
    assertEquals(4, actual.size());
  }

  private class ErrorChatOptionsDto extends BaseChartOptionsDto {

  }
}
