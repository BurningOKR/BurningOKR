package org.burningokr.unit.mapper.okrUnit;

import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.mapper.okrUnit.OkrBranchMapper;
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


//TODO (F. L. 26.06.2023) Refactor tests (name, etc.)
public class OkrBranchMapperTest {

  private OkrBranch okrBranch;
  private OkrBranchDto okrBranchDto;
  private OkrBranchMapper okrBranchMapper;

  @BeforeEach
  public void init() {
    okrBranch = new OkrBranch();
    okrBranchDto = new OkrBranchDto();
    okrBranchMapper = new OkrBranchMapper();
  }

  @Test
  public void test_mapEntityToDto_expectIdIsMapped() {
    Long expectedId = 5L;
    okrBranch.setId(expectedId);
    okrBranchDto = okrBranchMapper.mapEntityToDto(okrBranch);
    assertEquals(expectedId, okrBranchDto.getOkrUnitId());
  }

  @Test
  public void test_mapEntityToDto_expectNameIsMapped() {
    String expectedName = "myNameIs";
    okrBranch.setName(expectedName);
    okrBranchDto = okrBranchMapper.mapEntityToDto(okrBranch);
    assertEquals(expectedName, okrBranchDto.getUnitName());
  }

  @Test
  public void test_mapEntityToDto_expectParentUnitIdIsMapped() {
    Long expectedParentUnitId = 42L;
    OkrDepartment parentUnit = new OkrDepartment();
    parentUnit.setId(expectedParentUnitId);
    okrBranch.setParentOkrUnit(parentUnit);
    okrBranchDto = okrBranchMapper.mapEntityToDto(okrBranch);
    assertEquals(expectedParentUnitId, okrBranchDto.getParentUnitId());
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
    okrBranch.setOkrChildUnits(departments);
    okrBranchDto = okrBranchMapper.mapEntityToDto(okrBranch);
    assertEquals(expectedDepartmentCount, okrBranchDto.getOkrChildUnitIds().size());
    assertEquals(1L, okrBranchDto.getOkrChildUnitIds().toArray()[0]);
    assertEquals(2L, okrBranchDto.getOkrChildUnitIds().toArray()[1]);
    assertEquals(3L, okrBranchDto.getOkrChildUnitIds().toArray()[2]);
    assertEquals(4L, okrBranchDto.getOkrChildUnitIds().toArray()[3]);
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
    okrBranch.setObjectives(objectives);
    okrBranchDto = okrBranchMapper.mapEntityToDto(okrBranch);
    assertEquals(expectedObjectiveCount, okrBranchDto.getObjectiveIds().size());
    assertEquals(1L, okrBranchDto.getObjectiveIds().toArray()[0]);
    assertEquals(2L, okrBranchDto.getObjectiveIds().toArray()[1]);
    assertEquals(3L, okrBranchDto.getObjectiveIds().toArray()[2]);
  }

  @Test
  public void test_mapEntityToDto_parentIsOkrBranch_expectIsParentUnitABranchIsMapped() {
    OkrBranch branch2 = new OkrBranch();
    okrBranch.setParentOkrUnit(branch2);
    okrBranchDto = okrBranchMapper.mapEntityToDto(okrBranch);
    assertTrue(okrBranchDto.getIsParentUnitABranch());
  }

  @Test
  public void test_mapEntityToDto_parentIsNotOkrBranch_expectIsParentUnitABranchIsMapped() {
    OkrCompany company = new OkrCompany();
    okrBranch.setParentOkrUnit(company);
    okrBranchDto = okrBranchMapper.mapEntityToDto(okrBranch);
    assertFalse(okrBranchDto.getIsParentUnitABranch());
  }

  @Test
  public void test_mapEntityToDto_parentIsNotExistent_expectIsParentUnitABranchIsMapped() {
    okrBranch.setParentOkrUnit(null);
    okrBranchDto = okrBranchMapper.mapEntityToDto(okrBranch);
    assertFalse(okrBranchDto.getIsParentUnitABranch());
  }

  @Test
  public void test_mapDtoToEntity_expectIdIsMapped() {
    Long expectId = 4L;
    okrBranchDto.setOkrUnitId(expectId);
    okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDto);
    assertEquals(expectId, okrBranch.getId());
  }

  @Test
  public void test_mapDtoToEntity_expectNameIsMapped() {
    String expectedName = "myNameIS";
    okrBranchDto.setUnitName(expectedName);
    okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDto);
    assertEquals(expectedName, okrBranch.getName());
  }

  @Test
  public void test_mapDtoToEntity_expectParentUnitIsNull() {
    okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDto);
    assertNull(okrBranch.getParentOkrUnit());
  }

  @Test
  public void test_mapDtoToEntity_expectParentUnitIsNull2() {
    okrBranchDto.setParentUnitId(12L);
    okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDto);
    assertNull(okrBranch.getParentOkrUnit());
  }

  @Test
  public void test_mapDtoToEntity_expectObjectiveListEmpty() {
    okrBranchDto.setObjectiveIds(Arrays.asList(1L, 2L));
    okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDto);
    assertTrue(okrBranch.getObjectives().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectObjectiveListEmpty2() {
    okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDto);
    assertTrue(okrBranch.getObjectives().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectDepartmentListEmpty() {
    okrBranchDto.setOkrChildUnitIds(Arrays.asList(1L, 2L));
    okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDto);
    assertTrue(okrBranch.getOkrChildUnits().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectDepartmentListEmpty2() {
    okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDto);
    assertTrue(okrBranch.getOkrChildUnits().isEmpty());
  }

  @Test
  public void test_mapEntitiesToDots_expectAllAreMapped() {
    Collection<OkrBranch> entities =
      Arrays.asList(new OkrBranch(), new OkrBranch(), new OkrBranch());
    Collection<OkrBranchDto> dtos = okrBranchMapper.mapEntitiesToDtos(entities);
    assertEquals(entities.size(), dtos.size());
  }
}
