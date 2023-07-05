package org.burningokr.mapper.dashboard;

import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.model.dashboard.DashboardDto;
import org.burningokr.service.dashboard.ChartBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DashboardMapperTest {
  private DashboardCreation dashboardCreation;
  private DashboardDto dashboardDto;
  @Mock
  private ChartBuilderService chartBuilderService;
  @Mock
  private BaseChartOptionsMapper baseChartOptionsMapper;
  @InjectMocks
  private DashboardMapper dashboardMapper;

  @BeforeEach
  public void init() {
    dashboardCreation = new DashboardCreation();
    dashboardDto = new DashboardDto();
    dashboardMapper = new DashboardMapper(chartBuilderService, baseChartOptionsMapper);
    UUID myUUID = UUID.randomUUID();
    dashboardDto.setId(5L);
    dashboardDto.setTitle("expectedTitle");
    dashboardDto.setCompanyId(6L);
    dashboardDto.setCreatorId(myUUID);
    dashboardDto.setChartDtos(new ArrayList<>());
    dashboardCreation.setId(5L);
    dashboardCreation.setTitle("expectedTitle");
    dashboardCreation.setCompanyId(6L);
    dashboardCreation.setCreatorId(myUUID);
  }

  @Test
  public void mapDtoToEntity_shouldMapDashboardDtoToEntity() {
    DashboardCreation mapped_entity = dashboardMapper.mapDtoToEntity(dashboardDto);
    assertEquals(dashboardCreation, mapped_entity);
  }

  @Test
  public void mapDtosToEntities_shouldMapDashboardDtosToEntities() {
    Collection<DashboardDto> expected = new ArrayList<>() {
      {
        add(dashboardDto);
        add(dashboardDto);
      }
    };
    Collection<DashboardCreation> actual = dashboardMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().get().getId(), actual.stream().findFirst().get().getId());
  }

  @Test
  public void mapEntityToDto_shouldMapDashboardEntityToDto() {
    DashboardDto mapped_dto = dashboardMapper.mapEntityToDto(dashboardCreation);
    assertEquals(dashboardDto, mapped_dto);
  }

  @Test
  public void mapEntitiesToDtos_shouldMapDashboardEntitiesToDtos() {
    Collection<DashboardCreation> expected = new ArrayList<>() {
      {
        add(dashboardCreation);
        add(dashboardCreation);
      }
    };
    Collection<DashboardDto> actual = dashboardMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().get().getId(), actual.stream().findFirst().get().getId());
  }
}
