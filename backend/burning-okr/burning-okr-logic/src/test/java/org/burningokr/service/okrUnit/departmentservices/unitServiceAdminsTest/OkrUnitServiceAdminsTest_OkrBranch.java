package org.burningokr.service.okrUnit.departmentservices.unitServiceAdminsTest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.exceptions.InvalidDeleteRequestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrUnitServiceAdminsTest_OkrBranch extends OkrUnitServiceAdminsTest<OkrBranch> {

  @Override
  protected OkrBranch createDepartment() {
    return new OkrBranch();
  }

  @Override
  protected Class<OkrBranch> getDepartment() {
    return OkrBranch.class;
  }

  @Test(expected = InvalidDeleteRequestException.class)
  public void deleteUnitWithChildUnit_expectsInvalidDeleteRequestException() {
    OkrBranch okrBranch = new OkrBranch();
    okrBranch.setId(1337L);
    List<OkrChildUnit> okrChildUnits = new ArrayList<>();
    okrChildUnits.add(new OkrDepartment());
    okrBranch.setOkrChildUnits(okrChildUnits);

    when(unitRepository.findByIdOrThrow(anyLong())).thenReturn(okrBranch);

    okrUnitServiceAdmins.deleteUnit(1337L, user);
  }
}
