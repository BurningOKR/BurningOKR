package org.burningokr.mapper.okrUnit;

import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class OkrBranchMapperTest {

  private OkrBranch entity;
  private OkrBranchDto dto;
  private OkrBranchMapper mapper;

  @BeforeEach
  public void init() {
    entity = new OkrBranch();
    dto = new OkrBranchDto();
    mapper = new OkrBranchMapper();
  }

  @Test
  public void test_mapEntityToDto_expectIdIsMapped() {
    Long expectedId = 5L;
    entity.setId(expectedId);
    dto = mapper.mapEntityToDto(entity);
    assertEquals(expectedId, dto.getOkrUnitId());
  }

  @Test
  public void test_mapEntityToDto_expectNameIsMapped() {
    String expectedName = "myNameIs";
    entity.setName(expectedName);
    dto = mapper.mapEntityToDto(entity);
    assertEquals(expectedName, dto.getUnitName());
  }

  @Test
  public void test_mapEntityToDto_expectParentUnitIdIsMapped() {
    Long expectedParentUnitId = 42L;
    OkrDepartment parentUnit = new OkrDepartment();
    parentUnit.setId(expectedParentUnitId);
    entity.setParentOkrUnit(parentUnit);
    dto = mapper.mapEntityToDto(entity);
    assertEquals(expectedParentUnitId, dto.getParentUnitId());
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
    assertEquals(expectedDepartmentCount, dto.getOkrChildUnitIds().size());
    assertEquals(1L, dto.getOkrChildUnitIds().toArray()[0]);
    assertEquals(2L, dto.getOkrChildUnitIds().toArray()[1]);
    assertEquals(3L, dto.getOkrChildUnitIds().toArray()[2]);
    assertEquals(4L, dto.getOkrChildUnitIds().toArray()[3]);
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
    assertEquals(expectedObjectiveCount, dto.getObjectiveIds().size());
    assertEquals(1L, dto.getObjectiveIds().toArray()[0]);
    assertEquals(2L, dto.getObjectiveIds().toArray()[1]);
    assertEquals(3L, dto.getObjectiveIds().toArray()[2]);
  }

  @Test
  public void test_mapEntityToDto_parentIsOkrBranch_expectIsParentUnitABranchIsMapped() {
    OkrBranch branch2 = new OkrBranch();
    entity.setParentOkrUnit(branch2);
    dto = mapper.mapEntityToDto(entity);
    assertTrue(dto.getIsParentUnitABranch());
  }

  @Test
  public void test_mapEntityToDto_parentIsNotOkrBranch_expectIsParentUnitABranchIsMapped() {
    OkrCompany company = new OkrCompany();
    entity.setParentOkrUnit(company);
    dto = mapper.mapEntityToDto(entity);
    assertFalse(dto.getIsParentUnitABranch());
  }

  @Test
  public void test_mapEntityToDto_parentIsNotExistent_expectIsParentUnitABranchIsMapped() {
    entity.setParentOkrUnit(null);
    dto = mapper.mapEntityToDto(entity);
    assertFalse(dto.getIsParentUnitABranch());
  }

  @Test
  public void test_mapDtoToEntity_expectIdIsMapped() {
    Long expectId = 4L;
    dto.setOkrUnitId(expectId);
    entity = mapper.mapDtoToEntity(dto);
    assertEquals(expectId, entity.getId());
  }

  @Test
  public void test_mapDtoToEntity_expectNameIsMapped() {
    String expectedName = "myNameIS";
    dto.setUnitName(expectedName);
    entity = mapper.mapDtoToEntity(dto);
    assertEquals(expectedName, entity.getName());
  }

  @Test
  public void test_mapDtoToEntity_expectParentUnitIsNull() {
    entity = mapper.mapDtoToEntity(dto);
    assertNull(entity.getParentOkrUnit());
  }

  @Test
  public void test_mapDtoToEntity_expectParentUnitIsNull2() {
    dto.setParentUnitId(12L);
    entity = mapper.mapDtoToEntity(dto);
    assertNull(entity.getParentOkrUnit());
  }

  @Test
  public void test_mapDtoToEntity_expectObjectiveListEmpty() {
    dto.setObjectiveIds(Arrays.asList(1L, 2L));
    entity = mapper.mapDtoToEntity(dto);
    assertTrue(entity.getObjectives().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectObjectiveListEmpty2() {
    entity = mapper.mapDtoToEntity(dto);
    assertTrue(entity.getObjectives().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectDepartmentListEmpty() {
    dto.setOkrChildUnitIds(Arrays.asList(1L, 2L));
    entity = mapper.mapDtoToEntity(dto);
    assertTrue(entity.getOkrChildUnits().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectDepartmentListEmpty2() {
    entity = mapper.mapDtoToEntity(dto);
    assertTrue(entity.getOkrChildUnits().isEmpty());
  }

  @Test
  public void test_mapEntitiesToDots_expectAllAreMapped() {
    Collection<OkrBranch> entities =
      Arrays.asList(new OkrBranch(), new OkrBranch(), new OkrBranch());
    Collection<OkrBranchDto> dtos = mapper.mapEntitiesToDtos(entities);
    assertEquals(entities.size(), dtos.size());
  }
}
