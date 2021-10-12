package org.burningokr.mapper.okrUnit;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.dto.okrUnit.OkrDepartmentDtoRole;
import org.burningokr.dto.okrUnit.OkrUnitSchemaDto;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrParentUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrBranchSchemaMapperTest {

  @InjectMocks private OkrBranchSchemaMapper okrBranchSchemaMapper;

  private OkrBranch department1;
  private OkrBranch department2;
  private OkrBranch department3;
  private OkrDepartment okrDepartment4;
  private OkrDepartment okrDepartment5;
  private OkrDepartment okrDepartment6;

  private UUID currentUserId;

  @Before
  public void setUp() {
    department1 = new OkrBranch();
    department1.setId(10L);
    department1.setName("department1test");
    department1.setActive(true);
    department2 = new OkrBranch();
    department2.setId(20L);
    department2.setName("department2test");
    department2.setActive(false);
    department3 = new OkrBranch();
    department3.setId(30L);
    department3.setName("department3Test");
    department3.setActive(true);
    okrDepartment4 = new OkrDepartment();
    okrDepartment4.setId(40L);
    okrDepartment4.setName("department4Test");
    okrDepartment4.setActive(false);
    okrDepartment5 = new OkrDepartment();
    okrDepartment5.setId(50L);
    okrDepartment5.setName("department5Test");
    okrDepartment5.setActive(false);
    okrDepartment6 = new OkrDepartment();
    okrDepartment6.setId(60L);
    okrDepartment6.setName("department6Test");
    okrDepartment6.setActive(false);

    currentUserId = UUID.randomUUID();
  }

  private void attachDepartmentToDepartment(OkrChildUnit parent, OkrChildUnit child) {
    if (parent instanceof OkrParentUnit) {
      ((OkrParentUnit) parent).getOkrChildUnits().add(child);
      child.setParentOkrUnit(parent);
    } else {
      fail(
          "Trying to attach a child okrUnit to a child okrUnit. This is an error with your test. See Method attachDepartmentToDepartment.");
    }
  }

  @Test
  public void mapDepartmentListToOkrDepartmentList_noDepartments_expectedEmptyList() {
    ArrayList<OkrDepartment> emptyList = new ArrayList<>();

    Collection<OkrUnitSchemaDto> actual =
        okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(emptyList, currentUserId);

    Assert.assertEquals(0, actual.size());
  }

  @Test
  public void mapDepartmentListToDepartmentList_oneDepartment_expectedCorrectlyMappedDto() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(department1);

    Collection<OkrUnitSchemaDto> actual =
        okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
            departmentList, currentUserId);

    Assert.assertEquals(1, actual.size());
    for (OkrUnitSchemaDto dto : actual) {
      Assert.assertEquals(department1.getId(), dto.getId());
      Assert.assertEquals(department1.getName(), dto.getName());
      Assert.assertEquals(department1.getOkrChildUnits().size(), dto.getSubDepartments().size());
      Assert.assertEquals(OkrDepartmentDtoRole.USER, dto.getUserRole());
      Assert.assertFalse(dto.getIsTeam());
    }
  }

  @Test
  public void mapDepartmentListToDepartmentList_oneDepartment_expectedCorrectlyMappedDto_Team() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(okrDepartment4);

    Collection<OkrUnitSchemaDto> actual =
            okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
                    departmentList, currentUserId);

    Assert.assertEquals(1, actual.size());
    for (OkrUnitSchemaDto dto : actual) {
      Assert.assertEquals(okrDepartment4.getId(), dto.getId());
      Assert.assertEquals(okrDepartment4.getName(), dto.getName());
      Assert.assertEquals(OkrDepartmentDtoRole.USER, dto.getUserRole());
      Assert.assertTrue(dto.getIsTeam());
    }
  }

  @Test
  public void mapDepartmentListToDepartmentList_twoDepartments_expectedCorrectIds() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(department1);
    departmentList.add(department2);

    Collection<OkrUnitSchemaDto> actual =
        okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
            departmentList, currentUserId);

    OkrUnitSchemaDto dto1 = null;
    OkrUnitSchemaDto dto2 = null;
    for (OkrUnitSchemaDto dto : actual) {
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
  public void mapDepartmentListToDepartmentList_twoDepartments_expectedIsActiveCorrect() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(department1);
    departmentList.add(department2);

    Collection<OkrUnitSchemaDto> actual =
        okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
            departmentList, currentUserId);

    OkrUnitSchemaDto dto1 = null;
    OkrUnitSchemaDto dto2 = null;
    for (OkrUnitSchemaDto dto : actual) {
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
  public void mapDepartmentListToDepartmentList_threeDepartments_expectedCorrectRoles() {
    okrDepartment5.setOkrMasterId(currentUserId);
    okrDepartment6.getOkrMemberIds().add(currentUserId);

    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(okrDepartment4);
    departmentList.add(okrDepartment5);
    departmentList.add(okrDepartment6);

    Collection<OkrUnitSchemaDto> actual =
        okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
            departmentList, currentUserId);

    OkrUnitSchemaDto dto4 = null;
    OkrUnitSchemaDto dto5 = null;
    OkrUnitSchemaDto dto6 = null;
    for (OkrUnitSchemaDto dto : actual) {
      if (dto.getId().equals(okrDepartment4.getId())) {
        dto4 = dto;
      } else if (dto.getId().equals(okrDepartment5.getId())) {
        dto5 = dto;
      } else if (dto.getId().equals(okrDepartment6.getId())) {
        dto6 = dto;
      }
    }

    Assert.assertEquals(3, actual.size());
    Assert.assertEquals(OkrDepartmentDtoRole.USER, dto4.getUserRole());
    Assert.assertEquals(OkrDepartmentDtoRole.MANAGER, dto5.getUserRole());
    Assert.assertEquals(OkrDepartmentDtoRole.MEMBER, dto6.getUserRole());
  }

  @Test
  public void mapDepartmentListToDepartmentList_oneChildDepartment_expectedCorrectIds() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(department1);
    attachDepartmentToDepartment(department1, department2);

    Collection<OkrUnitSchemaDto> actual =
        okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
            departmentList, currentUserId);

    Assert.assertEquals(1, actual.size());
    for (OkrUnitSchemaDto dto : actual) {
      Assert.assertEquals(1, dto.getSubDepartments().size());
      for (OkrUnitSchemaDto innerDto : dto.getSubDepartments()) {
        Assert.assertEquals(department2.getId(), innerDto.getId());
      }
    }
  }

  @Test
  public void mapDepartmentListToDepartmentList_twoChildDepartments_expectedCorrectIds() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(department1);
    attachDepartmentToDepartment(department1, department2);
    attachDepartmentToDepartment(department1, department3);

    Collection<OkrUnitSchemaDto> actual =
        okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
            departmentList, currentUserId);

    Assert.assertEquals(1, actual.size());
    for (OkrUnitSchemaDto outerDto : actual) {
      Assert.assertEquals(2, outerDto.getSubDepartments().size());
      OkrUnitSchemaDto dto2 = null;
      OkrUnitSchemaDto dto3 = null;
      for (OkrUnitSchemaDto dto : outerDto.getSubDepartments()) {
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
