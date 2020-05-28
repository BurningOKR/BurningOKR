package org.burningokr.service.structure.departmentservices.structureServiceManagersTest;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.model.structures.Department;
import org.junit.Assert;
import org.junit.Test;

public class StructureServiceManagersTest_Department
    extends StructureServiceManagersTest<Department> {

  @Test
  public void updateDepartmentMemberList_expectedOkrMemberIdsAreChangedInSavedDepartment() {
    Department insertedDepartment = new Department();
    Collection<UUID> insertedOkrMemberIds =
        Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    insertedDepartment.setOkrMemberIds(insertedOkrMemberIds);
    insertedDepartment.setId(100L);
    insertedDepartment.setName("Test");

    Department originalDepartment = new Department();
    Collection<UUID> originalOkrMemberIds = new ArrayList<>();
    originalDepartment.setOkrMemberIds(originalOkrMemberIds);
    originalDepartment.setId(100L);
    originalDepartment.setName("Test");

    when(structureRepository.findByIdOrThrow(100L)).thenReturn(originalDepartment);
    when(structureRepository.save(any(Department.class))).then(returnsFirstArg());

    structureServiceManagers.updateStructure(insertedDepartment, user);

    verify(structureRepository).save(originalDepartment);
    Assert.assertEquals(originalDepartment.getOkrMemberIds(), insertedOkrMemberIds);
  }

  @Test
  public void updateDepartmentMemberList_expectedOtherVariablesUnchangedInSavedDepartment() {
    Department insertedDepartment = new Department();
    Collection<UUID> insertedOkrMemberIds =
        Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    insertedDepartment.setOkrMemberIds(insertedOkrMemberIds);
    insertedDepartment.setId(100L);
    insertedDepartment.setName("Insert");
    insertedDepartment.setOkrMasterId(UUID.randomUUID());
    insertedDepartment.setOkrTopicSponsorId(UUID.randomUUID());
    insertedDepartment.setDepartments(new ArrayList<>());
    Department insertedParentStructure = new Department();
    insertedParentStructure.setName("insertedParentStructure");
    insertedDepartment.setParentStructure(insertedParentStructure);
    insertedDepartment.setObjectives(new ArrayList<>());

    Department originalDepartment = new Department();
    Collection<UUID> originalOkrMemberIds = new ArrayList<>();
    originalDepartment.setOkrMemberIds(originalOkrMemberIds);
    originalDepartment.setId(100L);
    originalDepartment.setName("Original");
    originalDepartment.setOkrMasterId(UUID.randomUUID());
    originalDepartment.setOkrTopicSponsorId(UUID.randomUUID());
    originalDepartment.setDepartments(new ArrayList<>());
    Department originalParentStructure = new Department();
    originalParentStructure.setName("originalParentStructure");
    originalDepartment.setParentStructure(originalParentStructure);
    originalDepartment.setObjectives(new ArrayList<>());

    when(structureRepository.findByIdOrThrow(100L)).thenReturn(originalDepartment);
    when(structureRepository.save(any(Department.class))).then(returnsFirstArg());

    structureServiceManagers.updateStructure(insertedDepartment, user);

    verify(structureRepository).save(originalDepartment);
    Assert.assertNotEquals(originalDepartment.getName(), insertedDepartment.getName());
    Assert.assertNotEquals(
        originalDepartment.getOkrMasterId(), insertedDepartment.getOkrMasterId());
    Assert.assertNotEquals(
        originalDepartment.getOkrTopicSponsorId(), insertedDepartment.getOkrTopicSponsorId());
    Assert.assertNotSame(originalDepartment.getDepartments(), insertedDepartment.getDepartments());
    Assert.assertNotEquals(
        originalDepartment.getParentStructure(), insertedDepartment.getParentStructure());
    Assert.assertNotSame(originalDepartment.getObjectives(), insertedDepartment.getObjectives());
  }

  @Test
  public void updateDepartmentMemberList_expectedReturnsFromSaveCall() {
    Department insertedDepartment = new Department();
    Collection<UUID> insertedOkrMemberIds =
        Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    insertedDepartment.setOkrMemberIds(insertedOkrMemberIds);
    insertedDepartment.setId(100L);
    insertedDepartment.setName("Test");

    Department originalDepartment = new Department();
    Collection<UUID> originalOkrMemberIds = new ArrayList<>();
    originalDepartment.setOkrMemberIds(originalOkrMemberIds);
    originalDepartment.setId(100L);
    originalDepartment.setName("Test");

    Department saveCallReturnedDepartment = new Department();

    when(structureRepository.findByIdOrThrow(100L)).thenReturn(originalDepartment);
    when(structureRepository.save(any(Department.class))).thenReturn(saveCallReturnedDepartment);

    Department actualDepartment =
        structureServiceManagers.updateStructure(insertedDepartment, user);

    verify(structureRepository).save(originalDepartment);
    Assert.assertEquals(saveCallReturnedDepartment, actualDepartment);
  }

  @Override
  protected Department createStructure() {
    return new Department();
  }
}
