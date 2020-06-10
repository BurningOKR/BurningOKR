package org.burningokr.mapper.okrUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.burningokr.dto.okrUnit.OkrBranchDTO;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OkrBranchMapperTest {

  private OkrBranch entity;
  private OkrBranchDTO dto;
  private OkrBranchMapper mapper;

  @Before
  public void init() {
    entity = new OkrBranch();
    dto = new OkrBranchDTO();
    mapper = new OkrBranchMapper();
  }

  @Test
  public void test_mapEntityToDto_expectIdIsMapped() {
    Long expectedId = 5L;
    entity.setId(expectedId);
    dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(expectedId, dto.getOkrUnitId());
  }

  @Test
  public void test_mapEntityToDto_expectNameIsMapped() {
    String expectedName = "myNameIs";
    entity.setName(expectedName);
    dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(expectedName, dto.getUnitName());
  }

  @Test
  public void test_mapEntityToDto_expectParentUnitIdIsMapped() {
    Long expectedParentUnitId = 42L;
    OkrDepartment parentUnit = new OkrDepartment();
    parentUnit.setId(expectedParentUnitId);
    entity.setParentOkrUnit(parentUnit);
    dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(expectedParentUnitId, dto.getParentUnitId());
  }

  @Test
  public void test_mapEntityToDto_expectDepartmentIdsAreMapped() {
    int expectedDepartmentCount = 4;
    Collection<OkrChildUnit> departments = new ArrayList<>();
    for (int i = 1; i <= expectedDepartmentCount; i++) {
      OkrDepartment okrDepartment = new OkrDepartment();
      okrDepartment.setId((long) i);
      departments.add(okrDepartment);
    }
    entity.setOkrChildUnits(departments);
    dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(expectedDepartmentCount, dto.getChildUnits().size());
    Assert.assertEquals(1L, dto.getChildUnits().toArray()[0]);
    Assert.assertEquals(2L, dto.getChildUnits().toArray()[1]);
    Assert.assertEquals(3L, dto.getChildUnits().toArray()[2]);
    Assert.assertEquals(4L, dto.getChildUnits().toArray()[3]);
  }

  @Test
  public void test_mapEntityToDto_expectObjectiveIdsAreMapped() {
    int expectedObjectiveCount = 3;
    Collection<Objective> objectives = new ArrayList<>();
    for (int i = 1; i <= expectedObjectiveCount; i++) {
      Objective objective = new Objective();
      objective.setId((long) i);
      objectives.add(objective);
    }
    entity.setObjectives(objectives);
    dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(expectedObjectiveCount, dto.getObjectiveIds().size());
    Assert.assertEquals(1L, dto.getObjectiveIds().toArray()[0]);
    Assert.assertEquals(2L, dto.getObjectiveIds().toArray()[1]);
    Assert.assertEquals(3L, dto.getObjectiveIds().toArray()[2]);
  }

  @Test
  public void test_mapDtoToEntity_expectIdIsMapped() {
    Long expectId = 4L;
    dto.setOkrUnitId(expectId);
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertEquals(expectId, entity.getId());
  }

  @Test
  public void test_mapDtoToEntity_expectNameIsMapped() {
    String expectedName = "myNameIS";
    dto.setUnitName(expectedName);
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertEquals(expectedName, entity.getName());
  }

  @Test
  public void test_mapDtoToEntity_expectParentUnitIsNull() {
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertNull(entity.getParentOkrUnit());
  }

  @Test
  public void test_mapDtoToEntity_expectParentUnitIsNull2() {
    dto.setParentUnitId(12L);
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertNull(entity.getParentOkrUnit());
  }

  @Test
  public void test_mapDtoToEntity_expectObjectiveListEmpty() {
    dto.setObjectiveIds(Arrays.asList(1L, 2L));
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertTrue(entity.getObjectives().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectObjectiveListEmpty2() {
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertTrue(entity.getObjectives().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectDepartmentListEmpty() {
    dto.setChildUnits(Arrays.asList(1L, 2L));
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertTrue(entity.getOkrChildUnits().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectDepartmentListEmpty2() {
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertTrue(entity.getOkrChildUnits().isEmpty());
  }

  @Test
  public void test_mapEntitiesToDots_expectAllAreMapped() {
    Collection<OkrBranch> entities =
        Arrays.asList(new OkrBranch(), new OkrBranch(), new OkrBranch());
    Collection<OkrBranchDTO> dtos = mapper.mapEntitiesToDtos(entities);
    Assert.assertEquals(entities.size(), dtos.size());
  }
}
