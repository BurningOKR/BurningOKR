package org.burningokr.mapper.dashboard;

import org.burningokr.dto.dashboard.creation.ChartCreationOptionsDto;
import org.burningokr.dto.dashboard.creation.DashboardCreationDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.DashboardCreation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DashboardCreationMapperTest {
    private DashboardCreationMapper dashboardCreationMapper;
    private DashboardCreationDto dashboardCreationDto;
    private DashboardCreation dashboardCreation;
    @Mock
    private ChartCreationOptionsMapper chartCreationOptionsMapper;

    @BeforeEach
    public void init() {

        dashboardCreationMapper = new DashboardCreationMapper(chartCreationOptionsMapper);
        dashboardCreationDto = new DashboardCreationDto();
        dashboardCreation = new DashboardCreation();
    }

    @Test
    public void mapDtoToEntity_shouldMapId() {
        Long expected = 10L;
        dashboardCreationDto.setId(expected);

        DashboardCreation actual = dashboardCreationMapper.mapDtoToEntity(dashboardCreationDto);

        assertEquals(expected, actual.getId());
    }

    @Test
    public void mapDtoToEntity_shouldMapTitle() {
        String expected = "title";
        dashboardCreationDto.setTitle(expected);

        DashboardCreation actual = dashboardCreationMapper.mapDtoToEntity(dashboardCreationDto);

        assertEquals(expected, actual.getTitle());
    }

    @Test
    public void mapDtoToEntity_shouldMapCreatorId() {
        UUID expected = UUID.randomUUID();
        dashboardCreationDto.setCreatorId(expected);

        DashboardCreation actual = dashboardCreationMapper.mapDtoToEntity(dashboardCreationDto);

        assertEquals(expected, actual.getCreatorId());
    }

    @Test
    public void mapDtoToEntity_shouldMapCompanyId() {
        Long expected = 12L;
        dashboardCreationDto.setCompanyId(expected);

        DashboardCreation actual = dashboardCreationMapper.mapDtoToEntity(dashboardCreationDto);

        assertEquals(expected, actual.getCompanyId());
    }

    @Test
    public void mapDtoToEntity_shouldMapChartCreationOptions() {
        Collection<ChartCreationOptions> expected = new ArrayList<>();
        when(chartCreationOptionsMapper.mapDtosToEntities(any())).thenReturn(expected);

        DashboardCreation actual = dashboardCreationMapper.mapDtoToEntity(dashboardCreationDto);

        assertSame(expected, actual.getChartCreationOptions());
    }

    @Test
    public void mapDtosToEntities_shouldMapThreeDtosToEntites() {
        Collection<DashboardCreationDto> dashboardCreationDtos = new ArrayList<>() {
            {
                add(new DashboardCreationDto());
                add(new DashboardCreationDto());
                add(new DashboardCreationDto());
            }
        };
        Collection<DashboardCreation> actual = dashboardCreationMapper.mapDtosToEntities(dashboardCreationDtos);
        assertEquals(3, actual.size());
    }

    @Test
    public void mapEntityToDto_shouldMapId() {
        Long expected = 10L;
        dashboardCreation.setId(expected);

        DashboardCreationDto actual = dashboardCreationMapper.mapEntityToDto(dashboardCreation);

        assertEquals(expected, actual.getId());
    }

    @Test
    public void mapEntityToDto_shouldMapTitle() {
        String expected = "title";
        dashboardCreation.setTitle(expected);

        DashboardCreationDto actual = dashboardCreationMapper.mapEntityToDto(dashboardCreation);

        assertEquals(expected, actual.getTitle());
    }

    @Test
    public void mapEntityToDto_shouldMapCreatorId() {
        UUID expected = UUID.randomUUID();
        dashboardCreation.setCreatorId(expected);

        DashboardCreationDto actual = dashboardCreationMapper.mapEntityToDto(dashboardCreation);

        assertEquals(expected, actual.getCreatorId());
    }

    @Test
    public void mapEntityToDto_shouldMapCompanyId() {
        Long expected = 12L;
        dashboardCreation.setCompanyId(expected);

        DashboardCreationDto actual = dashboardCreationMapper.mapEntityToDto(dashboardCreation);

        assertEquals(expected, actual.getCompanyId());
    }

    @Test
    public void mapEntityToDto_shouldMapChartCreationOptions() {
        Collection<ChartCreationOptionsDto> expected = new ArrayList<>();
        when(chartCreationOptionsMapper.mapEntitiesToDtos(any())).thenReturn(expected);

        DashboardCreationDto actual = dashboardCreationMapper.mapEntityToDto(dashboardCreation);

        assertSame(expected, actual.getChartCreationOptions());
    }

    @Test
    public void mapEntitiesToDtos_shouldMapThreeEntitiesToDtos() {
        Collection<DashboardCreation> dashboardCreations = new ArrayList<>() {
            {
                add(new DashboardCreation());
                add(new DashboardCreation());
                add(new DashboardCreation());
            }
        };
        Collection<DashboardCreationDto> actual = dashboardCreationMapper.mapEntitiesToDtos(dashboardCreations);
        assertEquals(3, actual.size());
    }
}
