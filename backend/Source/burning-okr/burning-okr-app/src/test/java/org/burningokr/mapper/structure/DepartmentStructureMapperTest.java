package org.burningokr.mapper.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.dto.structure.DepartmentDtoRole;
import org.burningokr.dto.structure.DepartmentStructureDto;
import org.burningokr.model.structures.Department;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentStructureMapperTest {

  @InjectMocks private DepartmentStructureMapper departmentStructureMapper;

  private Department department1;
  private Department department2;
  private Department department3;
  private Department department4;

  private UUID currentUserId;

  @Before
  public void setUp() {
    department1 = new Department();
    department1.setId(10L);
    department1.setName("department1test");
    department1.setActive(true);
    department2 = new Department();
    department2.setId(20L);
    department2.setName("department2test");
    department2.setActive(false);
    department3 = new Department();
    department3.setId(30L);
    department3.setName("department3Test");
    department3.setActive(true);
    department4 = new Department();
    department4.setId(40L);
    department4.setName("department4Test");
    department4.setActive(false);

    currentUserId = UUID.randomUUID();
  }

  private void attachDepartmentToDepartment(Department parent, Department child) {
    parent.getDepartments().add(child);
    child.setParentStructure(parent);
  }

  @Test
  public void mapDepartmentListToDepartmentStructureList_noDepartments_expectedEmptyList() {
    ArrayList<Department> emptyList = new ArrayList<>();

    Collection<DepartmentStructureDto> actual =
        departmentStructureMapper.mapDepartmentListToDepartmentStructureList(
            emptyList, currentUserId);

    Assert.assertEquals(0, actual.size());
  }

  @Test
  public void
      mapDepartmentListToDepartmentStructureList_oneDepartment_expectedCorrectlyMappedDto() {
    ArrayList<Department> departmentList = new ArrayList<>();
    departmentList.add(department1);

    Collection<DepartmentStructureDto> actual =
        departmentStructureMapper.mapDepartmentListToDepartmentStructureList(
            departmentList, currentUserId);

    Assert.assertEquals(1, actual.size());
    for (DepartmentStructureDto dto : actual) {
      Assert.assertEquals(department1.getId(), dto.getId());
      Assert.assertEquals(department1.getName(), dto.getName());
      Assert.assertEquals(department1.getDepartments().size(), dto.getSubDepartments().size());
      Assert.assertEquals(DepartmentDtoRole.USER, dto.getUserRole());
    }
  }

  @Test
  public void mapDepartmentListToDepartmentStructureList_twoDepartments_expectedCorrectIds() {
    ArrayList<Department> departmentList = new ArrayList<>();
    departmentList.add(department1);
    departmentList.add(department2);

    Collection<DepartmentStructureDto> actual =
        departmentStructureMapper.mapDepartmentListToDepartmentStructureList(
            departmentList, currentUserId);

    DepartmentStructureDto dto1 = null;
    DepartmentStructureDto dto2 = null;
    for (DepartmentStructureDto dto : actual) {
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
    ArrayList<Department> departmentList = new ArrayList<>();
    departmentList.add(department1);
    departmentList.add(department2);

    Collection<DepartmentStructureDto> actual =
        departmentStructureMapper.mapDepartmentListToDepartmentStructureList(
            departmentList, currentUserId);

    DepartmentStructureDto dto1 = null;
    DepartmentStructureDto dto2 = null;
    for (DepartmentStructureDto dto : actual) {
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
    department1.setOkrMasterId(currentUserId);
    department2.getOkrMemberIds().add(currentUserId);

    ArrayList<Department> departmentList = new ArrayList<>();
    departmentList.add(department1);
    departmentList.add(department2);
    departmentList.add(department3);

    Collection<DepartmentStructureDto> actual =
        departmentStructureMapper.mapDepartmentListToDepartmentStructureList(
            departmentList, currentUserId);

    DepartmentStructureDto dto1 = null;
    DepartmentStructureDto dto2 = null;
    DepartmentStructureDto dto3 = null;
    for (DepartmentStructureDto dto : actual) {
      if (dto.getId().equals(department1.getId())) {
        dto1 = dto;
      } else if (dto.getId().equals(department2.getId())) {
        dto2 = dto;
      } else if (dto.getId().equals(department3.getId())) {
        dto3 = dto;
      }
    }

    Assert.assertEquals(3, actual.size());
    Assert.assertEquals(DepartmentDtoRole.MANAGER, dto1.getUserRole());
    Assert.assertEquals(DepartmentDtoRole.MEMBER, dto2.getUserRole());
    Assert.assertEquals(DepartmentDtoRole.USER, dto3.getUserRole());
  }

  @Test
  public void mapDepartmentListToDepartmentStructureList_oneChildDepartment_expectedCorrectIds() {
    ArrayList<Department> departmentList = new ArrayList<>();
    departmentList.add(department1);
    attachDepartmentToDepartment(department1, department2);

    Collection<DepartmentStructureDto> actual =
        departmentStructureMapper.mapDepartmentListToDepartmentStructureList(
            departmentList, currentUserId);

    Assert.assertEquals(1, actual.size());
    for (DepartmentStructureDto dto : actual) {
      Assert.assertEquals(1, dto.getSubDepartments().size());
      for (DepartmentStructureDto innerDto : dto.getSubDepartments()) {
        Assert.assertEquals(department2.getId(), innerDto.getId());
      }
    }
  }

  @Test
  public void mapDepartmentListToDepartmentStructureList_twoChildDepartments_expectedCorrectIds() {
    ArrayList<Department> departmentList = new ArrayList<>();
    departmentList.add(department1);
    attachDepartmentToDepartment(department1, department2);
    attachDepartmentToDepartment(department1, department3);

    Collection<DepartmentStructureDto> actual =
        departmentStructureMapper.mapDepartmentListToDepartmentStructureList(
            departmentList, currentUserId);

    Assert.assertEquals(1, actual.size());
    for (DepartmentStructureDto outerDto : actual) {
      Assert.assertEquals(2, outerDto.getSubDepartments().size());
      DepartmentStructureDto dto2 = null;
      DepartmentStructureDto dto3 = null;
      for (DepartmentStructureDto dto : outerDto.getSubDepartments()) {
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
