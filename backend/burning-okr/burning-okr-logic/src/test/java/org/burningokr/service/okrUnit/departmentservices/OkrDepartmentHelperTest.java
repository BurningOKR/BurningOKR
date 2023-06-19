package org.burningokr.service.okrUnit.departmentservices;

import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OkrDepartmentHelperTest {

  @Test
  public void testCollectChildUnitsShouldFlatChildUnitOfCompaniesInList() {
    OkrCompany okrCompany = new OkrCompany();
    OkrDepartment okrDepartment = new OkrDepartment();
    OkrBranch okrBranch = new OkrBranch();
    OkrDepartment okrDepartment2 = new OkrDepartment();
    okrCompany.setOkrChildUnits(Arrays.asList(okrDepartment, okrBranch));
    okrBranch.setOkrChildUnits(Collections.singletonList(okrDepartment2));

    Collection<OkrChildUnit> actualDepartments = BranchHelper.collectChildUnits(okrCompany);

    assertEquals(3, actualDepartments.size());
    assertEquals(okrDepartment, actualDepartments.toArray()[0]);
    assertEquals(okrBranch, actualDepartments.toArray()[1]);
    assertEquals(okrDepartment2, actualDepartments.toArray()[2]);
  }

  @Test
  public void testCollectDepartmentsShouldOnlyFlatDepartmentsOfCompaniesInList() {
    OkrCompany okrCompany = new OkrCompany();
    OkrDepartment okrDepartment = new OkrDepartment();
    OkrBranch okrBranch = new OkrBranch();
    OkrDepartment okrDepartment2 = new OkrDepartment();
    okrCompany.setOkrChildUnits(Arrays.asList(okrDepartment, okrBranch));
    okrBranch.setOkrChildUnits(Collections.singletonList(okrDepartment2));

    Collection<OkrDepartment> actualOkrDepartments = BranchHelper.collectDepartments(okrCompany);

    assertEquals(2, actualOkrDepartments.size());
    assertEquals(okrDepartment, actualOkrDepartments.toArray()[0]);
    assertEquals(okrDepartment2, actualOkrDepartments.toArray()[1]);
  }

  @Test
  public void testCollectionDepartmentsShouldFlatDepartmentsOfCompaniesInList_Empty() {
    OkrCompany okrCompany = new OkrCompany();

    Collection<OkrChildUnit> actualDepartments = BranchHelper.collectChildUnits(okrCompany);

    assertTrue(actualDepartments.isEmpty());
  }
}
