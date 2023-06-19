package org.burningokr.mapper.dashboard;

import org.burningokr.dto.dashboard.BaseChartOptionsDto;
import org.burningokr.dto.dashboard.DashboardDto;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.service.dashboard.ChartBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DashboardMapperTest {

  private DashboardCreation entity;
  private DashboardDto dto;
  @Mock
  private ChartBuilderService chartBuilderService;
  @Mock
  private BaseChartOptionsMapper baseChartOptionsMapper;
  @InjectMocks
  private DashboardMapper mapper;

  @BeforeEach
  public void init() {
    entity = new DashboardCreation();
    dto = new DashboardDto();
    mapper = new DashboardMapper(chartBuilderService, baseChartOptionsMapper);
    UUID myUUID = UUID.randomUUID();
    dto.setId(5L);
    dto.setTitle("expectedTitle");
    dto.setCompanyId(6L);
    dto.setCreatorId(myUUID);
    dto.setChartDtos(new ArrayList<BaseChartOptionsDto>());
    entity.setId(5L);
    entity.setTitle("expectedTitle");
    entity.setCompanyId(6L);
    entity.setCreatorId(myUUID);
  }

  @Test
  public void test_mapDtoToEntity() {
    DashboardCreation mapped_entity = mapper.mapDtoToEntity(dto);
    assertEquals(entity, mapped_entity);
  }

  @Test
  public void test_mapEntityToDto() {
    DashboardDto mapped_dto = mapper.mapEntityToDto(entity);
    assertEquals(dto, mapped_dto);
  }
}
