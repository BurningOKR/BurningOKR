package org.burningokr.mapper.structure;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.dto.structure.DepartmentDtoRole;
import org.burningokr.dto.structure.StructureSchemaDto;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.ParentStructure;
import org.burningokr.model.structures.SubStructure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StructureSchemaMapperTest {

  @InjectMocks private StructureSchemaMapper structureSchemaMapper;

  private CorporateObjectiveStructure department1;
  private CorporateObjectiveStructure department2;
  private CorporateObjectiveStructure department3;
  private Department department4;
  private Department department5;
  private Department department6;

  private UUID currentUserId;

  @Before
  public void setUp() {
    department1 = new CorporateObjectiveStructure();
    department1.setId(10L);
    department1.setName("department1test");
    department1.setActive(true);
    department2 = new CorporateObjectiveStructure();
    department2.setId(20L);
    department2.setName("department2test");
    department2.setActive(false);
    department3 = new CorporateObjectiveStructure();
    department3.setId(30L);
    department3.setName("department3Test");
    department3.setActive(true);
    department4 = new Department();
    department4.setId(40L);
    department4.setName("department4Test");
    department4.setActive(false);
    department5 = new Department();
    department5.setId(50L);
    department5.setName("department5Test");
    department5.setActive(false);
    department6 = new Department();
    department6.setId(60L);
    department6.setName("department6Test");
    department6.setActive(false);

    currentUserId = UUID.randomUUID();
  }

  private void attachDepartmentToDepartment(SubStructure parent, SubStructure child) {
    if (parent instanceof ParentStructure) {
      ((ParentStructure) parent).getSubStructures().add(child);
      child.setParentStructure(parent);
    } else {
      fail(
          "Trying to attach a child structure to a child structure. This is an error with your test. See Method attachDepartmentToDepartment.");
    }
  }

  @Test
  public void mapDepartmentListToDepartmentStructureList_noDepartments_expectedEmptyList() {
    ArrayList<Department> emptyList = new ArrayList<>();

    Collection<StructureSchemaDto> actual =
        structureSchemaMapper.mapStructureListToStructureSchemaList(emptyList, currentUserId);

    Assert.assertEquals(0, actual.size());
  }

  @Test
  public void
      mapDepartmentListToDepartmentStructureList_oneDepartment_expectedCorrectlyMappedDto() {
    ArrayList<SubStructure> departmentList = new ArrayList<>();
    departmentList.add(department1);

    Collection<StructureSchemaDto> actual =
        structureSchemaMapper.mapStructureListToStructureSchemaList(departmentList, currentUserId);

    Assert.assertEquals(1, actual.size());
    for (StructureSchemaDto dto : actual) {
      Assert.assertEquals(department1.getId(), dto.getId());
      Assert.assertEquals(department1.getName(), dto.getName());
      Assert.assertEquals(department1.getSubStructures().size(), dto.getSubDepartments().size());
      Assert.assertEquals(DepartmentDtoRole.USER, dto.getUserRole());
    }
  }

  @Test
  public void mapDepartmentListToDepartmentStructureList_twoDepartments_expectedCorrectIds() {
    ArrayList<SubStructure> departmentList = new ArrayList<>();
    departmentList.add(department1);
    departmentList.add(department2);

    Collection<StructureSchemaDto> actual =
        structureSchemaMapper.mapStructureListToStructureSchemaList(departmentList, currentUserId);

    StructureSchemaDto dto1 = null;
    StructureSchemaDto dto2 = null;
    for (StructureSchemaDto dto : actual) {
      if (dto.getId().equals(department1.getId())) {
        dto1 = dto;
      } else if (dto.getId().equals(department2.getId())) {
        dto2 = dto;
      }
    }

    Assert.assertEquals(2, actual.size());
    Assert.assertNotNull(dto1);
    Assert.assertNotNull(dto2);
  }

  @Test
  public void mapDepartmentListToDepartmentStructureList_twoDepartments_expectedIsActiveCorrect() {
    ArrayList<SubStructure> departmentList = new ArrayList<>();
    departmentList.add(department1);
    departmentList.add(department2);

    Collection<StructureSchemaDto> actual =
        structureSchemaMapper.mapStructureListToStructureSchemaList(departmentList, currentUserId);

    StructureSchemaDto dto1 = null;
    StructureSchemaDto dto2 = null;
    for (StructureSchemaDto dto : actual) {
      if (dto.getId().equals(department1.getId())) {
        dto1 = dto;
      } else if (dto.getId().equals(department2.getId())) {
        dto2 = dto;
      }
    }

    Assert.assertEquals(2, actual.size());
    Assert.assertNotNull(dto1);
    Assert.assertNotNull(dto2);

    Assert.assertTrue(dto1.getIsActive());
    Assert.assertFalse(dto2.getIsActive());
  }

  @Test
  public void mapDepartmentListToDepartmentStructureList_threeDepartments_expectedCorrectRoles() {
    department5.setOkrMasterId(currentUserId);
    department6.getOkrMemberIds().add(currentUserId);

    ArrayList<SubStructure> departmentList = new ArrayList<>();
    departmentList.add(department4);
    departmentList.add(department5);
    departmentList.add(department6);

    Collection<StructureSchemaDto> actual =
        structureSchemaMapper.mapStructureListToStructureSchemaList(departmentList, currentUserId);

    StructureSchemaDto dto4 = null;
    StructureSchemaDto dto5 = null;
    StructureSchemaDto dto6 = null;
    for (StructureSchemaDto dto : actual) {
      if (dto.getId().equals(department4.getId())) {
        dto4 = dto;
      } else if (dto.getId().equals(department5.getId())) {
        dto5 = dto;
      } else if (dto.getId().equals(department6.getId())) {
        dto6 = dto;
      }
    }

    Assert.assertEquals(3, actual.size());
    Assert.assertEquals(DepartmentDtoRole.USER, dto4.getUserRole());
    Assert.assertEquals(DepartmentDtoRole.MANAGER, dto5.getUserRole());
    Assert.assertEquals(DepartmentDtoRole.MEMBER, dto6.getUserRole());
  }

  @Test
  public void mapDepartmentListToDepartmentStructureList_oneChildDepartment_expectedCorrectIds() {
    ArrayList<SubStructure> departmentList = new ArrayList<>();
    departmentList.add(department1);
    attachDepartmentToDepartment(department1, department2);

    Collection<StructureSchemaDto> actual =
        structureSchemaMapper.mapStructureListToStructureSchemaList(departmentList, currentUserId);

    Assert.assertEquals(1, actual.size());
    for (StructureSchemaDto dto : actual) {
      Assert.assertEquals(1, dto.getSubDepartments().size());
      for (StructureSchemaDto innerDto : dto.getSubDepartments()) {
        Assert.assertEquals(department2.getId(), innerDto.getId());
      }
    }
  }

  @Test
  public void mapDepartmentListToDepartmentStructureList_twoChildDepartments_expectedCorrectIds() {
    ArrayList<SubStructure> departmentList = new ArrayList<>();
    departmentList.add(department1);
    attachDepartmentToDepartment(department1, department2);
    attachDepartmentToDepartment(department1, department3);

    Collection<StructureSchemaDto> actual =
        structureSchemaMapper.mapStructureListToStructureSchemaList(departmentList, currentUserId);

    Assert.assertEquals(1, actual.size());
    for (StructureSchemaDto outerDto : actual) {
      Assert.assertEquals(2, outerDto.getSubDepartments().size());
      StructureSchemaDto dto2 = null;
      StructureSchemaDto dto3 = null;
      for (StructureSchemaDto dto : outerDto.getSubDepartments()) {
        if (dto.getId().equals(department2.getId())) {
          dto2 = dto;
        } else if (dto.getId().equals(department3.getId())) {
          dto3 = dto;
        }
      }
      Assert.assertNotNull(dto2);
      Assert.assertNotNull(dto3);
    }
  }
}
