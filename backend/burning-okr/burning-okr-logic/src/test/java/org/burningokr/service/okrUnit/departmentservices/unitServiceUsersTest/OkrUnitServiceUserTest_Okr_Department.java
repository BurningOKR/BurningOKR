package org.burningokr.service.okrUnit.departmentservices.unitServiceUsersTest;

import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrUnitServiceUserTest_Okr_Department extends OkrUnitServiceUsersTest<OkrDepartment> {
  @Override
  protected OkrDepartment createUnit() {
    return new OkrDepartment();
  }
}
