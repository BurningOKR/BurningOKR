package org.burningokr.mapper.dashboard;

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
  }

  @Test
  public void test_mapDtoToEntity_expectIdIsMapped() {
    Long expectedId = 5L;
    dto.setId(expectedId);
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertEquals(expectedId, entity.getId());
  }
}
