package org.burningokr.service.structure.departmentservices;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;
import org.junit.Assert;
import org.junit.Test;

public class DepartmentHelperTest {

  @Test
  public void testCollectSubStructuresShouldFlatSubStructuresOfCompaniesInList() {
    Company company = new Company();
    Department department = new Department();
    CorporateObjectiveStructure corporateObjectiveStructure = new CorporateObjectiveStructure();
    Department department2 = new Department();
    company.setSubStructures(Arrays.asList(department, corporateObjectiveStructure));
    corporateObjectiveStructure.setSubStructures(Collections.singletonList(department2));

    Collection<SubStructure> actualDepartments = StructureHelper.collectSubStructures(company);

    Assert.assertEquals(3, actualDepartments.size());
    Assert.assertEquals(department, actualDepartments.toArray()[0]);
    Assert.assertEquals(corporateObjectiveStructure, actualDepartments.toArray()[1]);
    Assert.assertEquals(department2, actualDepartments.toArray()[2]);
  }

  @Test
  public void testCollectDepartmentsShouldOnlyFlatDepartmentsOfCompaniesInList() {
    Company company = new Company();
    Department department = new Department();
    CorporateObjectiveStructure corporateObjectiveStructure = new CorporateObjectiveStructure();
    Department department2 = new Department();
    company.setSubStructures(Arrays.asList(department, corporateObjectiveStructure));
    corporateObjectiveStructure.setSubStructures(Collections.singletonList(department2));

    Collection<Department> actualDepartments = StructureHelper.collectDepartments(company);

    Assert.assertEquals(2, actualDepartments.size());
    Assert.assertEquals(department, actualDepartments.toArray()[0]);
    Assert.assertEquals(department2, actualDepartments.toArray()[1]);
  }

  @Test
  public void testCollectionDepartmentsShouldFlatDepartmentsOfCompaniesInList_Empty() {
    Company company = new Company();

    Collection<SubStructure> actualDepartments = StructureHelper.collectSubStructures(company);

    Assert.assertTrue(actualDepartments.isEmpty());
  }
}
