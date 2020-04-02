package org.burningokr.service.structureutil;

import java.util.ArrayList;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EntityCrawlerServiceTest {

  @InjectMocks private EntityCrawlerService entityCrawlerService;

  // Testing entities
  private Cycle cycle;
  private Company company;
  private Department departmentChildFirstDegree;
  private Department departmentChildSecondDegree;
  private Objective objective;
  private KeyResult keyResult;

  @Before
  public void setUp() {
    // We define the following entities with the following relationship:
    // cycle <-> company <-> departmentChildFirstDegree <-> departmentChildSecondDegree

    cycle = new Cycle();
    company = new Company();
    departmentChildFirstDegree = new Department();
    departmentChildSecondDegree = new Department();

    cycle.setCompanies(new ArrayList<>());
    cycle.getCompanies().add(company);
    company.setCycle(cycle);
    company.getDepartments().add(departmentChildFirstDegree);
    departmentChildFirstDegree.setParentStructure(company);
    departmentChildFirstDegree.getDepartments().add(departmentChildSecondDegree);
    departmentChildSecondDegree.setParentStructure(departmentChildFirstDegree);

    // The objective and KeyResult are only attached to each other, attach to department in test!
    objective = new Objective();
    keyResult = new KeyResult();
    objective.setKeyResults(new ArrayList<>());
    objective.getKeyResults().add(keyResult);
    keyResult.setParentObjective(objective);
  }

  private void attachObjectiveToDepartment(Objective objective, Department department) {
    department.getObjectives().add(objective);
    objective.setParentStructure(department);
  }

  @Test
  public void getCycleOfCompany_expectedCycle() {
    Cycle actualCycle = entityCrawlerService.getCycleOfCompany(company);

    Assert.assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfDepartment_companyIsFirstParent_expectedCycle() {
    Cycle actualCycle = entityCrawlerService.getCycleOfDepartment(departmentChildFirstDegree);

    Assert.assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfDepartment_companyIsNestedParent_expectedCycle() {
    Cycle actualCycle = entityCrawlerService.getCycleOfDepartment(departmentChildSecondDegree);

    Assert.assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfObjective_companyIsFirstParentOfParentDepartment_expectedCycle() {
    attachObjectiveToDepartment(objective, departmentChildFirstDegree);
    Cycle actualCycle = entityCrawlerService.getCycleOfObjective(objective);

    Assert.assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfObjective_companyIsNestedParentOfParentDepartment_expectedCycle() {
    attachObjectiveToDepartment(objective, departmentChildSecondDegree);
    Cycle actualCycle = entityCrawlerService.getCycleOfObjective(objective);

    Assert.assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfKeyResult_companyIsFirstParentOfParentDepartment_expectedCycle() {
    attachObjectiveToDepartment(objective, departmentChildFirstDegree);
    Cycle actualCycle = entityCrawlerService.getCycleOfKeyResult(keyResult);

    Assert.assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfKeyResult_companyIsNestedParentOfParentDepartment_expectedCycle() {
    attachObjectiveToDepartment(objective, departmentChildSecondDegree);
    Cycle actualCycle = entityCrawlerService.getCycleOfKeyResult(keyResult);

    Assert.assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCompanyOfDepartment_companyIsFirstParent_expectedCompany() {
    Company actualCompany = entityCrawlerService.getCompanyOfDepartment(departmentChildFirstDegree);

    Assert.assertEquals(company, actualCompany);
  }

  @Test
  public void getCompanyOfDepartment_companyIsNestedParent_expectedCompany() {
    Company actualCompany =
        entityCrawlerService.getCompanyOfDepartment(departmentChildSecondDegree);

    Assert.assertEquals(company, actualCompany);
  }
}
