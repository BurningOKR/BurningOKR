package org.burningokr.mapper.dashboard;

import org.assertj.core.api.Assertions;
import org.burningokr.dto.dashboard.DashboardDto;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.service.dashboard.ChartBuilderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class DashboardMapperTest {

  private DashboardCreation entity;
  private DashboardDto dto;
  @Mock
  private ChartBuilderService chartBuilderService;
  @Mock
  private BaseChartOptionsMapper baseChartOptionsMapper;
  @InjectMocks
  private DashboardMapper mapper;

  @Before
  public void init() {
    entity = new DashboardCreation();
    dto = new DashboardDto();
    mapper = new DashboardMapper(chartBuilderService, baseChartOptionsMapper);
    UUID myUUID = UUID.randomUUID();
    dto.setId(5L);
    dto.setTitle("expectedTitle");
    dto.setCompanyId(6L);
    dto.setCreatorId(myUUID);
    entity.setId(5L);
    entity.setTitle("expectedTitle");
    entity.setCompanyId(6L);
    entity.setCreatorId(myUUID);
  }

  @Test
  public void test_mapDtoToEntity() {
    DashboardCreation mapped_entity = mapper.mapDtoToEntity(dto);
    Assert.assertEquals(entity, mapped_entity);
  }

  @Test
  public void test_mapEntityToDto() {
    DashboardDto mapped_dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(dto, mapped_dto);
  }
}
