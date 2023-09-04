package org.burningokr.unit.mapper.okrUnit;

import org.burningokr.dto.okrUnit.OkrDepartmentDtoRole;
import org.burningokr.dto.okrUnit.OkrUnitSchemaDto;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrParentUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OkrBranchSchemaMapperTest {

  @InjectMocks
  private OkrBranchSchemaMapper okrBranchSchemaMapper;

  private OkrBranch okrBranch1;
  private OkrBranch okrBranch2;
  private OkrBranch okrBranch3;
  private OkrDepartment okrDepartment1;
  private OkrDepartment okrDepartment2;
  private OkrDepartment okrDepartment3;

  private UUID currentUserId;

  @BeforeEach
  public void setUp() {
    okrBranch1 = new OkrBranch();
    okrBranch1.setId(10L);
    okrBranch1.setName("department1test");
    okrBranch1.setActive(true);

    okrBranch2 = new OkrBranch();
    okrBranch2.setId(20L);
    okrBranch2.setName("department2test");
    okrBranch2.setActive(false);

    okrBranch3 = new OkrBranch();
    okrBranch3.setId(30L);
    okrBranch3.setName("department3Test");
    okrBranch3.setActive(true);

    okrDepartment1 = new OkrDepartment();
    okrDepartment1.setId(40L);
    okrDepartment1.setName("department4Test");
    okrDepartment1.setActive(false);

    okrDepartment2 = new OkrDepartment();
    okrDepartment2.setId(50L);
    okrDepartment2.setName("department5Test");
    okrDepartment2.setActive(false);

    okrDepartment3 = new OkrDepartment();
    okrDepartment3.setId(60L);
    okrDepartment3.setName("department6Test");
    okrDepartment3.setActive(false);

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
  public void mapDepartmentListToOkrDepartmentList_shouldExpectEmptyListWhenDepartmentsAreNull() {
    ArrayList<OkrDepartment> departmentList = new ArrayList<>();
    int expected = 0;

    Collection<OkrUnitSchemaDto> actual =
      okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(departmentList, currentUserId);

    assertEquals(expected, actual.size());
  }

  @Test
  public void childUnitListToChildUnitSchemaList_shouldMapOkrBranchToOkrUnitSchemaDto() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(okrBranch1);

    Collection<OkrUnitSchemaDto> actual =
      okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
        departmentList, currentUserId);

    assertEquals(1, actual.size());
    for (OkrUnitSchemaDto okrUnitSchemaDto : actual) {
      assertEquals(okrBranch1.getId(), okrUnitSchemaDto.getId());
      assertEquals(okrBranch1.getName(), okrUnitSchemaDto.getName());
      assertEquals(okrBranch1.getOkrChildUnits().size(), okrUnitSchemaDto.getSubDepartments().size());
      assertEquals(OkrDepartmentDtoRole.USER, okrUnitSchemaDto.getUserRole());
      assertFalse(okrUnitSchemaDto.getIsTeam());
    }
  }

  @Test
  public void childUnitListToChildUnitSchemaList_shouldMapOkrDepartmentToOkrUnitSchemaDto() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(okrDepartment1);

    Collection<OkrUnitSchemaDto> actual =
      okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
        departmentList, currentUserId);

    assertEquals(1, actual.size());
    for (OkrUnitSchemaDto okrUnitSchemaDto : actual) {
      assertEquals(okrDepartment1.getId(), okrUnitSchemaDto.getId());
      assertEquals(okrDepartment1.getName(), okrUnitSchemaDto.getName());
      assertEquals(OkrDepartmentDtoRole.USER, okrUnitSchemaDto.getUserRole());
      assertTrue(okrUnitSchemaDto.getIsTeam());
    }
  }

  @Test
  public void mapOkrChildUnitListToOkrChildUnitSchemaList_shouldMapTwoDepartmentsWithCorrectIds() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(okrBranch1);
    departmentList.add(okrBranch2);

    Collection<OkrUnitSchemaDto> actual =
      okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
        departmentList, currentUserId);

    OkrUnitSchemaDto dto1 = null;
    OkrUnitSchemaDto dto2 = null;
    for (OkrUnitSchemaDto dto : actual) {
      if (dto.getId().equals(okrBranch1.getId())) {
        dto1 = dto;
      } else if (dto.getId().equals(okrBranch2.getId())) {
        dto2 = dto;
      }
    }

    assertEquals(2, actual.size());
    assertNotNull(dto1);
    assertNotNull(dto2);
  }

  @Test
  public void mapOkrChildUnitListToOkrChildUnitSchemaList_shouldMapIsActiveOfTwoDepartments() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(okrBranch1);
    departmentList.add(okrBranch2);

    Collection<OkrUnitSchemaDto> actual =
      okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
        departmentList, currentUserId);

    OkrUnitSchemaDto dto1 = null;
    OkrUnitSchemaDto dto2 = null;
    for (OkrUnitSchemaDto dto : actual) {
      if (dto.getId().equals(okrBranch1.getId())) {
        dto1 = dto;
      } else if (dto.getId().equals(okrBranch2.getId())) {
        dto2 = dto;
      }
    }

    assertEquals(2, actual.size());
    assertNotNull(dto1);
    assertNotNull(dto2);

    assertTrue(dto1.getIsActive());
    assertFalse(dto2.getIsActive());
  }

  @Test
  public void mapOkrChildUnitListToOkrChildUnitSchemaList_shouldMapOkrDepartmentDtoRolesOfThreeDepartments() {
    okrDepartment2.setOkrMasterId(currentUserId);
    okrDepartment3.getOkrMemberIds().add(currentUserId);

    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(okrDepartment1);
    departmentList.add(okrDepartment2);
    departmentList.add(okrDepartment3);

    Collection<OkrUnitSchemaDto> actual =
      okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
        departmentList, currentUserId);

    OkrUnitSchemaDto dto4 = null;
    OkrUnitSchemaDto dto5 = null;
    OkrUnitSchemaDto dto6 = null;
    for (OkrUnitSchemaDto dto : actual) {
      if (dto.getId().equals(okrDepartment1.getId())) {
        dto4 = dto;
      } else if (dto.getId().equals(okrDepartment2.getId())) {
        dto5 = dto;
      } else if (dto.getId().equals(okrDepartment3.getId())) {
        dto6 = dto;
      }
    }

    assertEquals(3, actual.size());
    assertEquals(OkrDepartmentDtoRole.USER, dto4.getUserRole());
    assertEquals(OkrDepartmentDtoRole.MANAGER, dto5.getUserRole());
    assertEquals(OkrDepartmentDtoRole.MEMBER, dto6.getUserRole());
  }

  @Test
  public void mapOkrChildUnitListToOkrChildUnitSchemaList_shouldMapIdsOfChildDepartment() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(okrBranch1);
    attachDepartmentToDepartment(okrBranch1, okrBranch2);

    Collection<OkrUnitSchemaDto> actual =
      okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
        departmentList, currentUserId);

    assertEquals(1, actual.size());
    for (OkrUnitSchemaDto dto : actual) {
      assertEquals(1, dto.getSubDepartments().size());
      for (OkrUnitSchemaDto innerDto : dto.getSubDepartments()) {
        assertEquals(okrBranch2.getId(), innerDto.getId());
      }
    }
  }

  @Test
  public void mapOkrChildUnitListToOkrChildUnitSchemaList_shouldMapIdsOfTwoChildDepartments() {
    ArrayList<OkrChildUnit> departmentList = new ArrayList<>();
    departmentList.add(okrBranch1);
    attachDepartmentToDepartment(okrBranch1, okrBranch2);
    attachDepartmentToDepartment(okrBranch1, okrBranch3);

    Collection<OkrUnitSchemaDto> actual =
      okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
        departmentList, currentUserId);

    assertEquals(1, actual.size());
    for (OkrUnitSchemaDto outerDto : actual) {
      assertEquals(2, outerDto.getSubDepartments().size());
      OkrUnitSchemaDto dto2 = null;
      OkrUnitSchemaDto dto3 = null;
      for (OkrUnitSchemaDto dto : outerDto.getSubDepartments()) {
        if (dto.getId().equals(okrBranch2.getId())) {
          dto2 = dto;
        } else if (dto.getId().equals(okrBranch3.getId())) {
          dto3 = dto;
        }
      }
      assertNotNull(dto2);
      assertNotNull(dto3);
    }
  }
}
