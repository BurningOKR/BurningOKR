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

  private OkrDepartment okrDepartment;
  private OkrDepartmentDto okrDepartmentDto;
  private OkrDepartmentMapper okrDepartmentMapper;

  @BeforeEach
  public void reset() {
    this.okrDepartmentDto = new OkrDepartmentDto();
    this.okrDepartment = new OkrDepartment();
    OkrDepartment parentOkrDepartment = new OkrDepartment();
    this.okrDepartment.setParentOkrUnit(parentOkrDepartment);
    this.okrDepartmentMapper = new OkrDepartmentMapper();
  }

  @Test
  public void mapEntityToDto_shouldMapId() {
    Long expected = 4L;
    okrDepartment.setId(expected);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertEquals(expected, okrDepartmentDto.getOkrUnitId());
  }

  @Test
  public void mapEntityToDto_shouldMapName() {
    String expected = "test";
    okrDepartment.setName(expected);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertEquals(expected, okrDepartmentDto.getUnitName());
  }

  @Test
  public void mapEntityToDto_shouldMapLabel() {
    String expected = "test246";
    okrDepartment.setLabel(expected);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertEquals(expected, okrDepartmentDto.getLabel());
  }

  @Test
  public void mapEntityToDto_shouldMapParrentDepartment() {
    Long expected = 8L;
    OkrDepartment parentOkrDepartment = new OkrDepartment();
    parentOkrDepartment.setId(expected);
    okrDepartment.setParentOkrUnit(parentOkrDepartment);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertEquals(expected, okrDepartmentDto.getParentUnitId());
    assertFalse(okrDepartmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_shouldMap() {
    OkrBranch parentOkrBranch = new OkrBranch();
    okrDepartment.setParentOkrUnit(parentOkrBranch);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertTrue(okrDepartmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_shouldMapOkrParentUnit() {
    OkrCompany company = new OkrCompany();
    okrDepartment.setParentOkrUnit(company);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertFalse(okrDepartmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_shouldMapOkrDepartmentParentUnitId() {
    Long expected = 10L;
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setId(expected);
    okrDepartment.setParentOkrUnit(okrCompany);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertEquals(expected, okrDepartmentDto.getParentUnitId());
    assertFalse(okrDepartmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_shouldMapObjectiveSize() {
    int expected = 6;
    Collection<Objective> objectives = new ArrayList<>() {
      {
        new Objective();
        new Objective();
        new Objective();
        new Objective();
        new Objective();
        new Objective();
      }
    };
    okrDepartment.setObjectives(objectives);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertEquals(expected, okrDepartmentDto.getObjectiveIds().size());
  }

  @Test
  public void mapEntityToDto_shouldMapOkrMasterId() {
    UUID okrMasterUuid = UUID.randomUUID();
    okrDepartment.setOkrMasterId(okrMasterUuid);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertEquals(okrMasterUuid, okrDepartmentDto.getOkrMasterId());
  }

  @Test
  public void mapEntityToDto_shouldMapOkrTopicSponsorId() {
    UUID okrTopicSponsorUuid = UUID.randomUUID();
    okrDepartment.setOkrTopicSponsorId(okrTopicSponsorUuid);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertEquals(okrTopicSponsorUuid, okrDepartmentDto.getOkrTopicSponsorId());
  }

  @Test
  public void mapEntityToDto_shouldMapMultipleOkrMemberIds() {
    Collection<UUID> okrMemberIds = new ArrayList<>(){
      {
        add(UUID.randomUUID());
        add(UUID.randomUUID());
        add(UUID.randomUUID());
      }
    };
    okrDepartment.setOkrMemberIds(okrMemberIds);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertEquals(okrMemberIds, okrDepartmentDto.getOkrMemberIds());
  }

  @Test
  public void mapEntityToDto_shouldMapIsActive() {
    okrDepartment.setActive(true);

    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);

    assertTrue(okrDepartmentDto.getIsActive());
  }

  @Test
  public void mapDtoToEntity_shouldMapOkrUnitId() {
    Long expected = 4L;
    okrDepartmentDto.setOkrUnitId(expected);

    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);

    assertEquals(expected, okrDepartment.getId());
  }

  @Test
  public void mapDtoToEntity_shouldMapUnitName() {
    String expected = "test";
    okrDepartmentDto.setUnitName(expected);

    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);

    assertEquals(expected, okrDepartment.getName());
  }

  @Test
  public void mapDtoToEntity_shouldMapLabel() {
    String expected = "test25M";
    okrDepartmentDto.setLabel(expected);

    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);

    assertEquals(expected, okrDepartment.getLabel());
  }

  @Test
  public void mapDtoToEntity_shouldMapDtoToEntityWithParentOkrUnitNull() {
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);

    assertNull(okrDepartment.getParentOkrUnit());
  }

  @Test
  public void mapDtoToEntity_shouldMapDtoToEntityObjectivesSizeNotNull() {
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);

    assertNotNull(okrDepartment.getObjectives());
  }

  @Test
  public void mapDtoToEntity_shouldMapOkrMasterId() {
    UUID okrMasterUuid = UUID.randomUUID();
    okrDepartmentDto.setOkrMasterId(okrMasterUuid);

    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);

    assertEquals(okrMasterUuid, okrDepartment.getOkrMasterId());
  }

  @Test
  public void mapDtoToEntity_shouldMapOkrTopicSponsorId() {
    UUID okrTopicSponsorUuid = UUID.randomUUID();
    okrDepartmentDto.setOkrTopicSponsorId(okrTopicSponsorUuid);

    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);

    assertEquals(okrTopicSponsorUuid, okrDepartment.getOkrTopicSponsorId());
  }

  @Test
  public void mapDtoToEntity_shouldMapOkrMemberIds() {
    Collection<UUID> okrMembersUuids =
      Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    okrDepartmentDto.setOkrMemberIds(okrMembersUuids);

    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);

    assertEquals(okrMembersUuids, okrDepartment.getOkrMemberIds());
  }

  @Test
  public void mapDtoToEntity_shouldMapIsActive() {
    okrDepartmentDto.setIsActive(true);

    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);

    assertTrue(okrDepartment.isActive());
  }
}
