package org.burningokr.service.okrUnit.departmentservices.unitServiceAdminsTest;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrUnitServiceAdminsTest_Okr_Department
    extends OkrUnitServiceAdminsTest<OkrDepartment> {

  @Test
  public void updateDepartment_expectsOkrMasterIdIsChanged() {
    OkrDepartment updateOkrDepartment = new OkrDepartment();
    updateOkrDepartment.setId(departmentId);
    updateOkrDepartment.setOkrMasterId(UUID.randomUUID());

    OkrDepartment persistedOkrDepartment = new OkrDepartment();
    unit.setId(departmentId);

    when(unitRepository.save(any(OkrDepartment.class))).then(returnsFirstArg());
    when(unitRepository.findByIdOrThrow(departmentId)).thenReturn(persistedOkrDepartment);

    unit = okrUnitServiceAdmins.updateUnit(updateOkrDepartment, user);

    Assert.assertEquals(updateOkrDepartment.getOkrMasterId(), unit.getOkrMasterId());
  }

  @Test
  public void updateDepartment_expectsOkrTopicSponsorIdIsChanged() {
    OkrDepartment updateOkrDepartment = new OkrDepartment();
    updateOkrDepartment.setId(departmentId);
    updateOkrDepartment.setOkrTopicSponsorId(UUID.randomUUID());

    OkrDepartment persistedOkrDepartment = new OkrDepartment();
    unit.setId(departmentId);

    when(unitRepository.save(any(OkrDepartment.class))).then(returnsFirstArg());
    when(unitRepository.findByIdOrThrow(departmentId)).thenReturn(persistedOkrDepartment);

    unit = okrUnitServiceAdmins.updateUnit(updateOkrDepartment, user);

    Assert.assertEquals(updateOkrDepartment.getOkrTopicSponsorId(), unit.getOkrTopicSponsorId());
  }

  @Test
  public void updateDepartment_expectsOkrMemberIdsAreChanged() {
    OkrDepartment updateOkrDepartment = new OkrDepartment();
    updateOkrDepartment.setId(departmentId);
    Collection<UUID> okrMemberIds =
        Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    updateOkrDepartment.setOkrMemberIds(okrMemberIds);
    OkrDepartment persistedOkrDepartment = new OkrDepartment();
    unit.setId(departmentId);
    when(unitRepository.save(any(OkrDepartment.class))).then(returnsFirstArg());
    when(unitRepository.findByIdOrThrow(departmentId)).thenReturn(persistedOkrDepartment);

    unit = okrUnitServiceAdmins.updateUnit(updateOkrDepartment, user);

    Assert.assertEquals(okrMemberIds, unit.getOkrMemberIds());
  }

  @Test
  public void deleteDepartment_expectsOkrTopicDescriptionIsDeleted() {
    OkrDepartment updateOkrDepartment = new OkrDepartment();
    updateOkrDepartment.setId(departmentId);
    OkrTopicDescription okrTopicDescription = new OkrTopicDescription();
    okrTopicDescription.setId(5416L);
    OkrDepartment persistedOkrDepartment = new OkrDepartment();
    persistedOkrDepartment.setId(departmentId);
    persistedOkrDepartment.setOkrTopicDescription(okrTopicDescription);
    when(unitRepository.findByIdOrThrow(departmentId)).thenReturn(persistedOkrDepartment);

    okrUnitServiceAdmins.deleteUnit(departmentId, user);

    verify(unitRepository).deleteById(departmentId);
    verify(okrTopicDescriptionService).safeDeleteOkrTopicDescription(5416L, user);
  }

  @Override
  protected OkrDepartment createDepartment() {
    return new OkrDepartment();
  }

  @Override
  protected Class<OkrDepartment> getDepartment() {
    return OkrDepartment.class;
  }
}
