package org.burningokr.mapper.dashboard;

import org.burningokr.dto.dashboard.creation.ChartCreationOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ChartCreationOptionsMapperTest {
    private ChartCreationOptionsMapper chartCreationOptionsMapper;
    private ChartCreationOptionsDto chartCreationOptionsDto;
    private ChartCreationOptions chartCreationOptions;


    @BeforeEach
    public void init() {
        chartCreationOptionsMapper = new ChartCreationOptionsMapper();
        chartCreationOptionsDto = new ChartCreationOptionsDto();
        chartCreationOptions = new ChartCreationOptions();
    }

    @Test
    public void mapDtoToEntity_shouldMapId() {
        Long expected = 10L;
        chartCreationOptionsDto.setId(expected);

        ChartCreationOptions actual = chartCreationOptionsMapper.mapDtoToEntity(chartCreationOptionsDto);

        assertEquals(expected, actual.getId());
    }

    @Test
    public void mapDtoToEntity_shouldMapTitle() {
        String expected = "chart";
        chartCreationOptionsDto.setTitle(expected);

        ChartCreationOptions actual = chartCreationOptionsMapper.mapDtoToEntity(chartCreationOptionsDto);

        assertEquals(expected, actual.getTitle());
    }

    @Test
    public void mapDtoToEntity_shouldMapChartType() {
        ChartInformationTypeEnum expected = ChartInformationTypeEnum.LINE_PROGRESS;
        chartCreationOptionsDto.setChartType(expected);

        ChartCreationOptions actual = chartCreationOptionsMapper.mapDtoToEntity(chartCreationOptionsDto);

        assertEquals(expected, actual.getChartType());
    }

    @Test
    public void mapDtoToEntity_shouldMapTeamIds() {
        Collection<Long> expected = new ArrayList<>() {
            {
                add(1L);
                add(2L);
                add(3L);
            }
        };
        chartCreationOptionsDto.setSelectedTeamIds(expected);

        ChartCreationOptions actual = chartCreationOptionsMapper.mapDtoToEntity(chartCreationOptionsDto);

        assertEquals(expected, actual.getTeamIds());
    }

    @Test
    public void mapDtosToEntities_shouldMapDtosToEntites() {
        //wip add test
    }

    @Test
    public void mapEntityToDto_shouldMapId() {
        Long expected = 10L;
        chartCreationOptions.setId(expected);

        ChartCreationOptionsDto actual = chartCreationOptionsMapper.mapEntityToDto(chartCreationOptions);

        assertEquals(expected, actual.getId());
    }

    @Test
    public void mapEntityToDto_shouldMapTitle() {
        String expected = "chart";
        chartCreationOptions.setTitle(expected);

        ChartCreationOptionsDto actual = chartCreationOptionsMapper.mapEntityToDto(chartCreationOptions);

        assertEquals(expected, actual.getTitle());
    }

    @Test
    public void mapEntityToDto_shouldMapChartType() {
        ChartInformationTypeEnum expected = ChartInformationTypeEnum.LINE_PROGRESS;
        chartCreationOptions.setChartType(expected);

        ChartCreationOptionsDto actual = chartCreationOptionsMapper.mapEntityToDto(chartCreationOptions);

        assertEquals(expected, actual.getChartType());
    }

    @Test
    public void mapEntityToDto_shouldMapTeamIds() {
        Collection<Long> expected = new ArrayList<>() {
            {
                add(1L);
                add(2L);
                add(3L);
            }
        };
        chartCreationOptions.setTeamIds(expected);

        ChartCreationOptionsDto actual = chartCreationOptionsMapper.mapEntityToDto(chartCreationOptions);

        assertEquals(expected, actual.getSelectedTeamIds());
    }

    @Test
    public void mapEntitiesToDtos_shouldMapEntitiesToDtos() {
        //wip add test
    }

}
