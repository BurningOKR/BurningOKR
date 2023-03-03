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

  // region Tests EntitiyToDto
  @Test
  public void test_mapEntityToDto_expects_idIsMapped() {
    Long expected = 4L;
    okrDepartment.setId(expected);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertEquals(expected, okrDepartmentDto.getOkrUnitId());
  }

  @Test
  public void test_mapEntityToDto_expects_nameIsMapped() {
    String expected = "test";
    okrDepartment.setName(expected);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertEquals(expected, okrDepartmentDto.getUnitName());
  }

  @Test
  public void test_mapEntityToDto_expects_LabelIsMapped() {
    String expected = "test246";
    okrDepartment.setLabel(expected);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertEquals(expected, okrDepartmentDto.getLabel());
  }

  @Test
  public void mapEntityToDto_parentIsDepartment_expectedParentDepartmentIsMapped() {
    Long expected = 8L;
    OkrDepartment parentOkrDepartment = new OkrDepartment();
    parentOkrDepartment.setId(expected);
    okrDepartment.setParentOkrUnit(parentOkrDepartment);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertEquals(expected, okrDepartmentDto.getParentUnitId());
    assertFalse(okrDepartmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_parentIsOkrBranch_expectIsParentUnitABranchIsMapped() {
    OkrBranch parentOkrBranch = new OkrBranch();
    okrDepartment.setParentOkrUnit(parentOkrBranch);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertTrue(okrDepartmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_parentIsOkrCompany_expectIsParentUnitABranchIsMapped() {
    OkrCompany company = new OkrCompany();
    okrDepartment.setParentOkrUnit(company);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertFalse(okrDepartmentDto.getIsParentUnitABranch());
  }

  @Test
  public void mapEntityToDto_parentIsCompany_expectedParentCompanyIsMapped() {
    Long expected = 10L;
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setId(expected);
    okrDepartment.setParentOkrUnit(okrCompany);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertEquals(expected, okrDepartmentDto.getParentUnitId());
    assertFalse(okrDepartmentDto.getIsParentUnitABranch());
  }

  @Test
  public void test_mapEntityToDto_expects_objectivesSizeIsEqual6() {
    int expected = 6;
    Collection<Objective> objectives = new ArrayList<>();
    for (int i = 1; i <= expected; i++) {
      objectives.add(new Objective());
    }
    okrDepartment.setObjectives(objectives);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertEquals(expected, okrDepartmentDto.getObjectiveIds().size());
  }

  @Test
  public void test_mapEntityToDto_expects_okrMasterIdIsMapped() {
    UUID okrMasterUuid = UUID.randomUUID();
    okrDepartment.setOkrMasterId(okrMasterUuid);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertEquals(okrMasterUuid, okrDepartmentDto.getOkrMasterId());
  }

  @Test
  public void test_mapEntityToDto_expects_okrTopicSponsorIdIsMapped() {
    UUID okrTopicSponsorUuid = UUID.randomUUID();
    okrDepartment.setOkrTopicSponsorId(okrTopicSponsorUuid);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertEquals(okrTopicSponsorUuid, okrDepartmentDto.getOkrTopicSponsorId());
  }

  @Test
  public void test_mapEntityToDto_expects_okrMemberIdsAreMapped() {
    Collection<UUID> okrMemberIds = new ArrayList<>();
    okrMemberIds.add(UUID.randomUUID());
    okrMemberIds.add(UUID.randomUUID());
    okrMemberIds.add(UUID.randomUUID());
    okrDepartment.setOkrMemberIds(okrMemberIds);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertEquals(okrMemberIds, okrDepartmentDto.getOkrMemberIds());
  }

  @Test
  public void test_mapEntityToDto_expects_isActiveIsMapped() {
    okrDepartment.setActive(true);
    okrDepartmentDto = okrDepartmentMapper.mapEntityToDto(okrDepartment);
    assertTrue(okrDepartmentDto.getIsActive());
  }
  // endregion

  // region Tests DtoToEntity
  @Test
  public void test_mapDtoToEntity_expects_idIsMapped() {
    Long expected = 4L;
    okrDepartmentDto.setOkrUnitId(expected);
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);
    assertEquals(expected, okrDepartment.getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_nameIsMapped() {
    String expected = "test";
    okrDepartmentDto.setUnitName(expected);
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);
    assertEquals(expected, okrDepartment.getName());
  }

  @Test
  public void test_mapDtoToEntity_expects_labelIsMapped() {
    String expected = "test25M";
    okrDepartmentDto.setLabel(expected);
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);
    assertEquals(expected, okrDepartment.getLabel());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentIsNull() {
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);
    assertNull(okrDepartment.getParentOkrUnit());
  }

  @Test
  public void test_mapDtoToEntity_expects_objectivesSizeNotNull() {
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);
    assertNotNull(okrDepartment.getObjectives());
  }

  @Test
  public void test_mapDtoToEntity_expects_OkrMasterIdIsMapped() {
    UUID okrMasterUuid = UUID.randomUUID();
    okrDepartmentDto.setOkrMasterId(okrMasterUuid);
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);
    assertEquals(okrMasterUuid, okrDepartment.getOkrMasterId());
  }

  @Test
  public void test_mapDtoToEntity_expects_OkrTopicSponsorIdIsMapped() {
    UUID okrTopicSponsorUuid = UUID.randomUUID();
    okrDepartmentDto.setOkrTopicSponsorId(okrTopicSponsorUuid);
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);
    assertEquals(okrTopicSponsorUuid, okrDepartment.getOkrTopicSponsorId());
  }

  @Test
  public void test_mapDtoToEntity_expects_okrMemberIdsAreMapped() {
    Collection<UUID> okrMembersUuids =
      Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    okrDepartmentDto.setOkrMemberIds(okrMembersUuids);
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);
    assertEquals(okrMembersUuids, okrDepartment.getOkrMemberIds());
  }

  @Test
  public void test_mapDtoToEntity_expects_isActiveIsMapped() {
    okrDepartmentDto.setIsActive(true);
    okrDepartment = okrDepartmentMapper.mapDtoToEntity(okrDepartmentDto);
    assertTrue(okrDepartment.isActive());
  }
  // endregion

}
