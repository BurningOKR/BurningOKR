package org.burningokr.service.okrUnitUtil;

import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class EntityCrawlerServiceTest {

  @InjectMocks
  private EntityCrawlerService entityCrawlerService;

  // Testing entities
  private Cycle cycle;
  private OkrCompany okrCompany;
  private OkrBranch departmentChildFirstDegree;
  private OkrDepartment okrDepartmentChildSecondDegree;
  private Objective objective;
  private KeyResult keyResult;

  @BeforeEach
  public void setUp() {
    // We define the following entities with the following relationship:
    // cycle <-> okrCompany <-> departmentChildFirstDegree <-> okrDepartmentChildSecondDegree

    cycle = new Cycle();
    okrCompany = new OkrCompany();
    departmentChildFirstDegree = new OkrBranch();
    okrDepartmentChildSecondDegree = new OkrDepartment();

    cycle.setCompanies(new ArrayList<>());
    cycle.getCompanies().add(okrCompany);
    okrCompany.setCycle(cycle);
    okrCompany.getOkrChildUnits().add(departmentChildFirstDegree);
    departmentChildFirstDegree.setParentOkrUnit(okrCompany);
    departmentChildFirstDegree.getOkrChildUnits().add(okrDepartmentChildSecondDegree);
    okrDepartmentChildSecondDegree.setParentOkrUnit(departmentChildFirstDegree);

    // The objective and KeyResult are only attached to each other, attach to department in test!
    objective = new Objective();
    keyResult = new KeyResult();
    objective.setKeyResults(new ArrayList<>());
    objective.getKeyResults().add(keyResult);
    keyResult.setParentObjective(objective);
  }

  private void attachObjectiveToDepartment(Objective objective, OkrChildUnit okrChildUnit) {
    okrChildUnit.getObjectives().add(objective);
    objective.setParentOkrUnit(okrChildUnit);
  }

  @Test
  public void getCycleOfCompany_expectedCycle() {
    Cycle actualCycle = entityCrawlerService.getCycleOfCompany(okrCompany);

    assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfDepartment_companyIsFirstParent_expectedCycle() {
    Cycle actualCycle = entityCrawlerService.getCycleOfUnit(departmentChildFirstDegree);

    assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfDepartment_companyIsNestedParent_expectedCycle() {
    Cycle actualCycle = entityCrawlerService.getCycleOfUnit(okrDepartmentChildSecondDegree);

    assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfObjective_companyIsFirstParentOfParentDepartment_expectedCycle() {
    attachObjectiveToDepartment(objective, departmentChildFirstDegree);
    Cycle actualCycle = entityCrawlerService.getCycleOfObjective(objective);

    assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfObjective_companyIsNestedParentOfParentDepartment_expectedCycle() {
    attachObjectiveToDepartment(objective, okrDepartmentChildSecondDegree);
    Cycle actualCycle = entityCrawlerService.getCycleOfObjective(objective);

    assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfKeyResult_companyIsFirstParentOfParentDepartment_expectedCycle() {
    attachObjectiveToDepartment(objective, departmentChildFirstDegree);
    Cycle actualCycle = entityCrawlerService.getCycleOfKeyResult(keyResult);

    assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCycleOfKeyResult_companyIsNestedParentOfParentDepartment_expectedCycle() {
    attachObjectiveToDepartment(objective, okrDepartmentChildSecondDegree);
    Cycle actualCycle = entityCrawlerService.getCycleOfKeyResult(keyResult);

    assertEquals(cycle, actualCycle);
  }

  @Test
  public void getCompanyOfDepartment_companyIsFirstParent_expectedCompany() {
    OkrCompany actualOkrCompany = entityCrawlerService.getCompanyOfUnit(departmentChildFirstDegree);

    assertEquals(okrCompany, actualOkrCompany);
  }

  @Test
  public void getCompanyOfDepartment_companyIsNestedParent_expectedCompany() {
    OkrCompany actualOkrCompany =
      entityCrawlerService.getCompanyOfUnit(okrDepartmentChildSecondDegree);

    assertEquals(okrCompany, actualOkrCompany);
  }
}
