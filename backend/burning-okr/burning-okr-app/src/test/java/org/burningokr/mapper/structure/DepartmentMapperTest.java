package org.burningokr.mapper.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.dto.structure.DepartmentDto;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DepartmentMapperTest {

  private Department department;
  private DepartmentDto departmentDto;
  private DepartmentMapper departmentMapper;

  @Before
  public void reset() {
    this.departmentDto = new DepartmentDto();
    this.department = new Department();
    Department parentDepartment = new Department();
    this.department.setParentStructure(parentDepartment);
    this.departmentMapper = new DepartmentMapper();
  }

  // region Tests EntitiyToDto
  @Test
  public void test_mapEntityToDto_expects_idIsMapped() {
    Long expected = 4L;
    department.setId(expected);
    departmentDto = departmentMapper.mapEntityToDto(department);
    Assert.assertEquals(expected, departmentDto.getStructureId());
  }

  @Test
  public void test_mapEntityToDto_expects_nameIsMapped() {
    String expected = "test";
    department.setName(expected);
    departmentDto = departmentMapper.mapEntityToDto(department);
    Assert.assertEquals(expected, departmentDto.getStructureName());
  }

  @Test
  public void test_mapEntityToDto_expects_LabelIsMapped() {
    String expected = "test246";
    department.setLabel(expected);
    departmentDto = departmentMapper.mapEntityToDto(department);
    Assert.assertEquals(expected, departmentDto.getLabel());
  }

  @Test
  public void mapEntityToDto_parentIsDepartment_expectedParentDepartmentIsMapped() {
    Long expected = 8L;
    Department parentDepartment = new Department();
    parentDepartment.setId(expected);
    department.setParentStructure(parentDepartment);
    departmentDto = departmentMapper.mapEntityToDto(department);
    Assert.assertEquals(expected, departmentDto.getParentStructureId());
    Assert.assertTrue(departmentDto.getIsParentStructureADepartment());
  }

  @Test
  public void mapEntityToDto_parentIsCompany_expectedParentCompanyIsMapped() {
    Long expected = 10L;
    Company company = new Company();
    company.setId(expected);
    department.setParentStructure(company);
    departmentDto = departmentMapper.mapEntityToDto(department);
    Assert.assertEquals(expected, departmentDto.getParentStructureId());
    Assert.assertFalse(departmentDto.getIsParentStructureADepartment());
  }

  @Test
  public void test_mapEntityToDto_expects_objectivesSizeIsEqual6() {
    int expected = 6;
    Collection<Objective> objectives = new ArrayList<>();
    for (int i = 1; i <= expected; i++) {
      objectives.add(new Objective());
    }
    department.setObjectives(objectives);
    departmentDto = departmentMapper.mapEntityToDto(department);
    Assert.assertEquals(expected, departmentDto.getObjectiveIds().size());
  }

  @Test
  public void test_mapEntityToDto_expects_okrMasterIdIsMapped() {
    UUID okrMasterUuid = UUID.randomUUID();
    department.setOkrMasterId(okrMasterUuid);
    departmentDto = departmentMapper.mapEntityToDto(department);
    Assert.assertEquals(okrMasterUuid, departmentDto.getOkrMasterId());
  }

  @Test
  public void test_mapEntityToDto_expects_okrTopicSponsorIdIsMapped() {
    UUID okrTopicSponsorUuid = UUID.randomUUID();
    department.setOkrTopicSponsorId(okrTopicSponsorUuid);
    departmentDto = departmentMapper.mapEntityToDto(department);
    Assert.assertEquals(okrTopicSponsorUuid, departmentDto.getOkrTopicSponsorId());
  }

  @Test
  public void test_mapEntityToDto_expects_okrMemberIdsAreMapped() {
    Collection<UUID> okrMemberIds = new ArrayList<>();
    okrMemberIds.add(UUID.randomUUID());
    okrMemberIds.add(UUID.randomUUID());
    okrMemberIds.add(UUID.randomUUID());
    department.setOkrMemberIds(okrMemberIds);
    departmentDto = departmentMapper.mapEntityToDto(department);
    Assert.assertEquals(okrMemberIds, departmentDto.getOkrMemberIds());
  }

  @Test
  public void test_mapEntityToDto_expects_isActiveIsMapped() {
    department.setActive(true);
    departmentDto = departmentMapper.mapEntityToDto(department);
    Assert.assertTrue(departmentDto.getIsActive());
  }
  // endregion

  // region Tests DtoToEntity
  @Test
  public void test_mapDtoToEntity_expects_idIsMapped() {
    Long expected = 4L;
    departmentDto.setStructureId(expected);
    department = departmentMapper.mapDtoToEntity(departmentDto);
    Assert.assertEquals(expected, department.getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_nameIsMapped() {
    String expected = "test";
    departmentDto.setStructureName(expected);
    department = departmentMapper.mapDtoToEntity(departmentDto);
    Assert.assertEquals(expected, department.getName());
  }

  @Test
  public void test_mapDtoToEntity_expects_labelIsMapped() {
    String expected = "test25M";
    departmentDto.setLabel(expected);
    department = departmentMapper.mapDtoToEntity(departmentDto);
    Assert.assertEquals(expected, department.getLabel());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentIsNull() {
    department = departmentMapper.mapDtoToEntity(departmentDto);
    Assert.assertNull(department.getParentStructure());
  }

  @Test
  public void test_mapDtoToEntity_expects_objectivesSizeNotNull() {
    department = departmentMapper.mapDtoToEntity(departmentDto);
    Assert.assertNotNull(department.getObjectives());
  }

  @Test
  public void test_mapDtoToEntity_expects_OkrMasterIdIsMapped() {
    UUID okrMasterUuid = UUID.randomUUID();
    departmentDto.setOkrMasterId(okrMasterUuid);
    department = departmentMapper.mapDtoToEntity(departmentDto);
    Assert.assertEquals(okrMasterUuid, department.getOkrMasterId());
  }

  @Test
  public void test_mapDtoToEntity_expects_OkrTopicSponsorIdIsMapped() {
    UUID okrTopicSponsorUuid = UUID.randomUUID();
    departmentDto.setOkrTopicSponsorId(okrTopicSponsorUuid);
    department = departmentMapper.mapDtoToEntity(departmentDto);
    Assert.assertEquals(okrTopicSponsorUuid, department.getOkrTopicSponsorId());
  }

  @Test
  public void test_mapDtoToEntity_expects_okrMemberIdsAreMapped() {
    Collection<UUID> okrMembersUuids =
        Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    departmentDto.setOkrMemberIds(okrMembersUuids);
    department = departmentMapper.mapDtoToEntity(departmentDto);
    Assert.assertEquals(okrMembersUuids, department.getOkrMemberIds());
  }

  @Test
  public void test_mapDtoToEntity_expects_isActiveIsMapped() {
    departmentDto.setIsActive(true);
    department = departmentMapper.mapDtoToEntity(departmentDto);
    Assert.assertTrue(department.isActive());
  }
  // endregion

}
