package org.burningokr.service.okrUnit.departmentservices.unitServiceUsersTest;

import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OkrUnitServiceUserTest_Okr_Department extends OkrUnitServiceUsersTest<OkrDepartment> {
  @Override
  protected OkrDepartment createUnit() {
    return new OkrDepartment();
  }
}
