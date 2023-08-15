package org.burningokr.mapper.okrUnit;

import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OkrDepartmentMapperTest {

  private OkrDepartment department;
  private OkrDepartmentDto departmentDto;
  private OkrDepartmentMapper departmentMapper;

  @BeforeEach
  public void reset() {
    this.departmentDto = new OkrDepartmentDto();
    this.department = new OkrDepartment();
    OkrDepartment parentOkrDepartment = new OkrDepartment();
    this.department.setParentOkrUnit(parentOkrDepartment);
    this.departmentMapper = new OkrDepartmentMapper();
  }

  @Test
  public void mapEntityToDto_shouldMapId() {
    Long expected = 4L;
    department.setId(expected);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertEquals(expected, departmentDto.getOkrUnitId());
  }

  @Test
  public void mapEntityToDto_shouldMapName() {
    String expected = "test";
    department.setName(expected);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertEquals(expected, departmentDto.getUnitName());
  }

  @Test
  public void mapEntityToDto_shouldMapLabel() {
    String expected = "test246";
    department.setLabel(expected);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertEquals(expected, departmentDto.getLabel());
  }

  @Test
  public void mapEntityToDto_shouldMapParentDepartment() {
    Long expected = 8L;
    OkrDepartment parentOkrDepartment = new OkrDepartment();
    parentOkrDepartment.setId(expected);
    department.setParentOkrUnit(parentOkrDepartment);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertEquals(expected, departmentDto.getParentUnitId());
    assertFalse(departmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_shouldMap() {
    OkrBranch parentOkrBranch = new OkrBranch();
    department.setParentOkrUnit(parentOkrBranch);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertTrue(departmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_shouldMapParentUnit() {
    OkrCompany company = new OkrCompany();
    department.setParentOkrUnit(company);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertFalse(departmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_shouldMapDepartmentParentUnitId() {
    Long expected = 10L;
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setId(expected);
    department.setParentOkrUnit(okrCompany);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertEquals(expected, departmentDto.getParentUnitId());
    assertFalse(departmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_shouldMapObjectiveSize() {
    int expected = 6;
    Collection<Objective> objectives = new ArrayList<>() {
      {
        add(new Objective());
        add(new Objective());
        add(new Objective());
        add(new Objective());
        add(new Objective());
        add(new Objective());
      }
    };
    department.setObjectives(objectives);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertEquals(expected, departmentDto.getObjectiveIds().size());
  }

  @Test
  public void mapEntityToDto_shouldMapMasterId() {
    UUID okrMasterUuid = UUID.randomUUID();
    department.setOkrMasterId(okrMasterUuid);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertEquals(okrMasterUuid, departmentDto.getOkrMasterId());
  }

  @Test
  public void mapEntityToDto_shouldMapTopicSponsorId() {
    UUID okrTopicSponsorUuid = UUID.randomUUID();
    department.setOkrTopicSponsorId(okrTopicSponsorUuid);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertEquals(okrTopicSponsorUuid, departmentDto.getOkrTopicSponsorId());
  }

  @Test
  public void mapEntityToDto_shouldMapMultipleMemberIds() {
    Collection<UUID> okrMemberIds = new ArrayList<>(){
      {
        add(UUID.randomUUID());
        add(UUID.randomUUID());
        add(UUID.randomUUID());
      }
    };
    department.setOkrMemberIds(okrMemberIds);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertEquals(okrMemberIds, departmentDto.getOkrMemberIds());
  }

  @Test
  public void mapEntityToDto_shouldMapIsActive() {
    department.setActive(true);

    departmentDto = departmentMapper.mapEntityToDto(department);

    assertTrue(departmentDto.getIsActive());
  }

  @Test
  public void mapEntitiesToDtos_shouldMapDepartmentEntitiesToDtos() {
    Collection<OkrDepartment> expected = new ArrayList<>() {
      {
        add(department);
        add(department);
      }
    };
    Collection<OkrDepartmentDto> actual = departmentMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getOkrMasterId(), actual.stream().findFirst().orElseThrow().getOkrMasterId());
  }

  @Test
  public void mapDtoToEntity_shouldMapUnitId() {
    Long expected = 4L;
    departmentDto.setOkrUnitId(expected);

    department = departmentMapper.mapDtoToEntity(departmentDto);

    assertEquals(expected, department.getId());
  }

  @Test
  public void mapDtoToEntity_shouldMapUnitName() {
    String expected = "test";
    departmentDto.setUnitName(expected);

    department = departmentMapper.mapDtoToEntity(departmentDto);

    assertEquals(expected, department.getName());
  }

  @Test
  public void mapDtoToEntity_shouldMapLabel() {
    String expected = "test25M";
    departmentDto.setLabel(expected);

    department = departmentMapper.mapDtoToEntity(departmentDto);

    assertEquals(expected, department.getLabel());
  }

  @Test
  public void mapDtoToEntity_shouldMapDtoToEntityWithParentUnitNull() {
    department = departmentMapper.mapDtoToEntity(departmentDto);

    assertNull(department.getParentOkrUnit());
  }

  @Test
  public void mapDtoToEntity_shouldMapDtoToEntityObjectivesSizeNotNull() {
    department = departmentMapper.mapDtoToEntity(departmentDto);

    assertNotNull(department.getObjectives());
  }

  @Test
  public void mapDtoToEntity_shouldMapOkrMasterId() {
    UUID okrMasterUuid = UUID.randomUUID();
    departmentDto.setOkrMasterId(okrMasterUuid);

    department = departmentMapper.mapDtoToEntity(departmentDto);

    assertEquals(okrMasterUuid, department.getOkrMasterId());
  }

  @Test
  public void mapDtoToEntity_shouldMapTopicSponsorId() {
    UUID okrTopicSponsorUuid = UUID.randomUUID();
    departmentDto.setOkrTopicSponsorId(okrTopicSponsorUuid);

    department = departmentMapper.mapDtoToEntity(departmentDto);

    assertEquals(okrTopicSponsorUuid, department.getOkrTopicSponsorId());
  }

  @Test
  public void mapDtoToEntity_shouldMapOkrMemberIds() {
    Collection<UUID> okrMembersUuids =
      Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    departmentDto.setOkrMemberIds(okrMembersUuids);

    department = departmentMapper.mapDtoToEntity(departmentDto);

    assertEquals(okrMembersUuids, department.getOkrMemberIds());
  }

  @Test
  public void mapDtoToEntity_shouldMapIsActive() {
    departmentDto.setIsActive(true);

    department = departmentMapper.mapDtoToEntity(departmentDto);

    assertTrue(department.isActive());
  }

  @Test
  public void mapDtosToEntities_shouldMapDepartmentDtosToEntities() {
    Collection<OkrDepartmentDto> expected = new ArrayList<>() {
      {
        add(departmentDto);
        add(departmentDto);
      }
    };
    Collection<OkrDepartment> actual = departmentMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getOkrMasterId(), actual.stream().findFirst().orElseThrow().getOkrMasterId());
  }
}
