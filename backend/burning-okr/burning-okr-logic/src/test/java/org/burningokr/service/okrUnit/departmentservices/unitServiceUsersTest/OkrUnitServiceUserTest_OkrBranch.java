package org.burningokr.service.okrUnit.departmentservices.unitServiceUsersTest;

import org.burningokr.model.okrUnits.OkrBranch;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrUnitServiceUserTest_OkrBranch extends OkrUnitServiceUsersTest<OkrBranch> {

  @Override
  protected OkrBranch createUnit() {
    return new OkrBranch();
  }
}
