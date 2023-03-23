package org.burningokr.service.okrUnit.departmentservices.unitServiceAdminsTest;

import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OkrUnitServiceAdminsTest_Okr_Department
  extends OkrUnitServiceAdminsTest<OkrDepartment> {

  @Test
  public void updateDepartment_expectsOkrMasterIdIsChanged() {
    OkrDepartment updateOkrDepartment = new OkrDepartment();
    updateOkrDepartment.setId(departmentId);
    updateOkrDepartment.setOkrMasterId(UUID.randomUUID());

    OkrDepartment persistedOkrDepartment = new OkrDepartment();
    unit.setId(departmentId);

    when(okrUnitRepository.save(any(OkrDepartment.class))).then(returnsFirstArg());
    when(okrUnitRepository.findByIdOrThrow(departmentId)).thenReturn(persistedOkrDepartment);

    unit = okrUnitServiceAdmins.updateUnit(updateOkrDepartment, IUser);

    assertEquals(updateOkrDepartment.getOkrMasterId(), unit.getOkrMasterId());
  }

  @Test
  public void updateDepartment_expectsOkrTopicSponsorIdIsChanged() {
    OkrDepartment updateOkrDepartment = new OkrDepartment();
    updateOkrDepartment.setId(departmentId);
    updateOkrDepartment.setOkrTopicSponsorId(UUID.randomUUID());

    OkrDepartment persistedOkrDepartment = new OkrDepartment();
    unit.setId(departmentId);

    when(okrUnitRepository.save(any(OkrDepartment.class))).then(returnsFirstArg());
    when(okrUnitRepository.findByIdOrThrow(departmentId)).thenReturn(persistedOkrDepartment);

    unit = okrUnitServiceAdmins.updateUnit(updateOkrDepartment, IUser);

    assertEquals(updateOkrDepartment.getOkrTopicSponsorId(), unit.getOkrTopicSponsorId());
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
    when(okrUnitRepository.save(any(OkrDepartment.class))).then(returnsFirstArg());
    when(okrUnitRepository.findByIdOrThrow(departmentId)).thenReturn(persistedOkrDepartment);

    unit = okrUnitServiceAdmins.updateUnit(updateOkrDepartment, IUser);

    assertEquals(okrMemberIds, unit.getOkrMemberIds());
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
