package org.burningokr.service.okrUnit.departmentservices.unitServiceManagersTest;

import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OkrUnitServiceManagersTest_OkrBranch extends OkrUnitServiceManagersTest<OkrBranch> {

  @Override
  protected OkrBranch createUnit() {
    return new OkrBranch();
  }

  @Test
  public void updateBranch_expectedAllVariablesUnchanged() {
    OkrBranch insertedOkrBranch = new OkrBranch();

    insertedOkrBranch.setId(100L);
    insertedOkrBranch.setName("Insert");
    insertedOkrBranch.setOkrChildUnits(new ArrayList<>());

    OkrBranch insertedParentBranch = new OkrBranch();
    insertedParentBranch.setName("insertedParentBranch");
    insertedOkrBranch.setParentOkrUnit(insertedParentBranch);
    insertedOkrBranch.setObjectives(new ArrayList<>());

    OkrBranch originalOkrBranch = new OkrBranch();

    originalOkrBranch.setId(100L);
    originalOkrBranch.setName("Original");
    originalOkrBranch.setOkrChildUnits(new ArrayList<>());
    OkrDepartment originalParentBranch = new OkrDepartment();
    originalParentBranch.setName("originalParentBranch");
    originalOkrBranch.setParentOkrUnit(originalParentBranch);
    originalOkrBranch.setObjectives(new ArrayList<>());

    okrUnitServiceManagers.updateUnit(insertedOkrBranch, user);

    verify(unitRepository, times(0)).save(originalOkrBranch);
  }

  @Test
  public void updateBranch_expectNoSaveCall() {
    OkrBranch insertedDepartment = new OkrBranch();
    insertedDepartment.setId(100L);
    insertedDepartment.setName("Test");

    OkrBranch originalDepartment = new OkrBranch();
    originalDepartment.setId(100L);
    originalDepartment.setName("Test");

    OkrBranch actualDepartment = okrUnitServiceManagers.updateUnit(insertedDepartment, user);

    verify(unitRepository, times(0)).save(originalDepartment);
  }
}
