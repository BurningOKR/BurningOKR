package org.burningokr.service.structure.departmentservices;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.junit.Assert;
import org.junit.Test;

public class DepartmentHelperTest {

  @Test
  public void testCollectDepartmentsShouldFlatDepartmentsOfCompaniesInList() {
    Company company = new Company();
    Department department = new Department();
    Department department1 = new Department();
    Department department2 = new Department();
    company.setDepartments(Arrays.asList(department, department1));
    department1.setDepartments(Collections.singletonList(department2));

    Collection<Department> actualDepartments = DepartmentHelper.collectDepartments(company);

    Assert.assertEquals(3, actualDepartments.size());
    Assert.assertEquals(department, actualDepartments.toArray()[0]);
    Assert.assertEquals(department1, actualDepartments.toArray()[1]);
    Assert.assertEquals(department2, actualDepartments.toArray()[2]);
  }

  @Test
  public void testCollectionDepartmentsShouldFlatDepartmentsOfCompaniesInList_Empty() {
    Company company = new Company();

    Collection<Department> actualDepartments = DepartmentHelper.collectDepartments(company);

    Assert.assertTrue(actualDepartments.isEmpty());
  }
}
