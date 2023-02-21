package org.burningokr.service.okrUnit.departmentservices.unitServiceManagersTest;

import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OkrUnitServiceManagersTest_Okr_Department
  extends OkrUnitServiceManagersTest<OkrDepartment> {

  @Test
  public void updateDepartmentMemberList_expectedOkrMemberIdsAreChangedInSavedDepartment() {
    OkrDepartment insertedOkrDepartment = new OkrDepartment();
    Collection<UUID> insertedOkrMemberIds =
      Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    insertedOkrDepartment.setOkrMemberIds(insertedOkrMemberIds);
    insertedOkrDepartment.setId(100L);
    insertedOkrDepartment.setName("Test");

    OkrDepartment originalOkrDepartment = new OkrDepartment();
    Collection<UUID> originalOkrMemberIds = new ArrayList<>();
    originalOkrDepartment.setOkrMemberIds(originalOkrMemberIds);
    originalOkrDepartment.setId(100L);
    originalOkrDepartment.setName("Test");

    when(unitRepository.findByIdOrThrow(100L)).thenReturn(originalOkrDepartment);
    when(unitRepository.save(any(OkrDepartment.class))).then(returnsFirstArg());

    okrUnitServiceManagers.updateUnit(insertedOkrDepartment, user);

    verify(unitRepository).save(originalOkrDepartment);
    assertEquals(originalOkrDepartment.getOkrMemberIds(), insertedOkrMemberIds);
  }

  @Test
  public void updateDepartmentMemberList_expectedOtherVariablesUnchangedInSavedDepartment() {
    OkrDepartment insertedOkrDepartment = new OkrDepartment();
    Collection<UUID> insertedOkrMemberIds =
      Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    insertedOkrDepartment.setOkrMemberIds(insertedOkrMemberIds);
    insertedOkrDepartment.setId(100L);
    insertedOkrDepartment.setName("Insert");
    insertedOkrDepartment.setOkrMasterId(UUID.randomUUID());
    insertedOkrDepartment.setOkrTopicSponsorId(UUID.randomUUID());
    OkrDepartment insertedParentUnit = new OkrDepartment();
    insertedParentUnit.setName("insertedParentUnit");
    insertedOkrDepartment.setParentOkrUnit(insertedParentUnit);
    insertedOkrDepartment.setObjectives(new ArrayList<>());

    OkrDepartment originalOkrDepartment = new OkrDepartment();
    Collection<UUID> originalOkrMemberIds = new ArrayList<>();
    originalOkrDepartment.setOkrMemberIds(originalOkrMemberIds);
    originalOkrDepartment.setId(100L);
    originalOkrDepartment.setName("Original");
    originalOkrDepartment.setOkrMasterId(UUID.randomUUID());
    originalOkrDepartment.setOkrTopicSponsorId(UUID.randomUUID());
    OkrDepartment originalParentUnit = new OkrDepartment();
    originalParentUnit.setName("originalParentUnit");
    originalOkrDepartment.setParentOkrUnit(originalParentUnit);
    originalOkrDepartment.setObjectives(new ArrayList<>());

    when(unitRepository.findByIdOrThrow(100L)).thenReturn(originalOkrDepartment);
    when(unitRepository.save(any(OkrDepartment.class))).then(returnsFirstArg());

    okrUnitServiceManagers.updateUnit(insertedOkrDepartment, user);

    verify(unitRepository).save(originalOkrDepartment);
    assertNotEquals(originalOkrDepartment.getName(), insertedOkrDepartment.getName());
    assertNotEquals(
      originalOkrDepartment.getOkrMasterId(), insertedOkrDepartment.getOkrMasterId());
    assertNotEquals(
      originalOkrDepartment.getOkrTopicSponsorId(), insertedOkrDepartment.getOkrTopicSponsorId());
    assertNotEquals(
      originalOkrDepartment.getParentOkrUnit(), insertedOkrDepartment.getParentOkrUnit());
    assertNotSame(
      originalOkrDepartment.getObjectives(), insertedOkrDepartment.getObjectives());
  }

  @Test
  public void updateDepartmentMemberList_expectedReturnsFromSaveCall() {
    OkrDepartment insertedOkrDepartment = new OkrDepartment();
    Collection<UUID> insertedOkrMemberIds =
      Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    insertedOkrDepartment.setOkrMemberIds(insertedOkrMemberIds);
    insertedOkrDepartment.setId(100L);
    insertedOkrDepartment.setName("Test");

    OkrDepartment originalOkrDepartment = new OkrDepartment();
    Collection<UUID> originalOkrMemberIds = new ArrayList<>();
    originalOkrDepartment.setOkrMemberIds(originalOkrMemberIds);
    originalOkrDepartment.setId(100L);
    originalOkrDepartment.setName("Test");

    OkrDepartment saveCallReturnedOkrDepartment = new OkrDepartment();

    when(unitRepository.findByIdOrThrow(100L)).thenReturn(originalOkrDepartment);
    when(unitRepository.save(any(OkrDepartment.class))).thenReturn(saveCallReturnedOkrDepartment);

    OkrDepartment actualOkrDepartment =
      okrUnitServiceManagers.updateUnit(insertedOkrDepartment, user);

    verify(unitRepository).save(originalOkrDepartment);
    assertEquals(saveCallReturnedOkrDepartment, actualOkrDepartment);
  }

  @Override
  protected OkrDepartment createUnit() {
    return new OkrDepartment();
  }
}
