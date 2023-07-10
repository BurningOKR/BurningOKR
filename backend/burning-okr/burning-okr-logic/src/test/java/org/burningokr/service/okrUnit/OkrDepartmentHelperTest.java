package org.burningokr.service.okrUnit;

import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OkrDepartmentHelperTest {

  @Test
  public void collectChildUnits_shouldOnlyCollectFlatChildUnitsOfCompaniesInChildUnitList() {
    OkrCompany company = new OkrCompany();
    OkrDepartment department = new OkrDepartment();
    OkrBranch branch = new OkrBranch();
    OkrDepartment department2 = new OkrDepartment();
    company.setOkrChildUnits(Arrays.asList(department, branch));
    branch.setOkrChildUnits(Collections.singletonList(department2));

    Collection<OkrChildUnit> actualDepartments = BranchHelper.collectChildUnits(company);

    assertEquals(3, actualDepartments.size());
    assertEquals(department, actualDepartments.toArray()[0]);
    assertEquals(branch, actualDepartments.toArray()[1]);
    assertEquals(department2, actualDepartments.toArray()[2]);
  }

  @Test
  public void collectDepartments_shouldOnlyCollectFlatDepartmentsOfCompaniesInList() {
    OkrCompany company = new OkrCompany();
    OkrDepartment department = new OkrDepartment();
    OkrBranch branch = new OkrBranch();
    OkrDepartment department2 = new OkrDepartment();
    company.setOkrChildUnits(Arrays.asList(department, branch));
    branch.setOkrChildUnits(Collections.singletonList(department2));

    Collection<OkrDepartment> actualDepartments = BranchHelper.collectDepartments(company);

    assertEquals(2, actualDepartments.size());
    assertEquals(department, actualDepartments.toArray()[0]);
    assertEquals(department2, actualDepartments.toArray()[1]);
  }

  @Test
  public void collectDepartmentsWithFilter_shouldShouldReturnNonFilteredList() {
    OkrCompany company = new OkrCompany();
    OkrDepartment department = new OkrDepartment();
    department.setId(10L);
    OkrBranch branch = new OkrBranch();
    OkrDepartment department2 = new OkrDepartment();
    department2.setId(20L);
    company.setOkrChildUnits(Arrays.asList(department, branch));
    branch.setOkrChildUnits(Collections.singletonList(department2));

    Collection<OkrDepartment> actualDepartments = BranchHelper.collectDepartments(new ArrayList<Long>(), company);

    assertEquals(2, actualDepartments.size());
    assertEquals(department, actualDepartments.toArray()[0]);
    assertEquals(department2, actualDepartments.toArray()[1]);
  }

  @Test
  public void collectDepartmentsWithFilter_shouldShouldReturnFilteredList() {
    OkrCompany company = new OkrCompany();
    OkrDepartment department = new OkrDepartment();
    department.setId(10L);
    OkrBranch branch = new OkrBranch();
    OkrDepartment department2 = new OkrDepartment();
    department2.setId(20L);
    company.setOkrChildUnits(Arrays.asList(department, branch));
    branch.setOkrChildUnits(Collections.singletonList(department2));

    Collection<OkrDepartment> actualDepartments = BranchHelper.collectDepartments(List.of(10L), company);

    assertEquals(1, actualDepartments.size());
    assertEquals(department, actualDepartments.toArray()[0]);
  }

  @Test
  public void collectDepartmentsWithFilter_shouldShouldReturnEmptyFilteredList() {
    OkrCompany company = new OkrCompany();
    OkrDepartment department = new OkrDepartment();
    department.setId(10L);
    OkrBranch branch = new OkrBranch();
    OkrDepartment department2 = new OkrDepartment();
    department2.setId(20L);
    company.setOkrChildUnits(Arrays.asList(department, branch));
    branch.setOkrChildUnits(Collections.singletonList(department2));

    Collection<OkrDepartment> actualDepartments = BranchHelper.collectDepartments(List.of(5L), company);

    assertEquals(0, actualDepartments.size());
  }

  @Test
  public void collectChildUnits_shouldOnlyCollectFlatDepartmentsOfCompaniesInListExpectsToBeEmpty() {
    OkrCompany company = new OkrCompany();

    Collection<OkrChildUnit> actualDepartments = BranchHelper.collectChildUnits(company);

    assertTrue(actualDepartments.isEmpty());
  }
}
