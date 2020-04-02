package org.burningokr.service.excel;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collection;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.excel.ObjectiveRow;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.Unit;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.service.structure.CompanyService;
import org.burningokr.service.structure.departmentservices.DepartmentServiceUsers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ObjectiveRowBuilderServiceTest {

  private final long departmentId = 42L;
  @Mock private DepartmentServiceUsers departmentServiceUsers;
  @Mock private CompanyService companyService;
  @InjectMocks private ObjectiveRowBuilderService objectiveRowBuilderService;
  private Department department;
  private Cycle cycle;
  private long objectiveId = 55L;
  private Company company;
  private long companyId = 400L;
  private Objective objective;
  private KeyResult keyResult;

  @Before()
  public void before() {
    cycle = new Cycle();
    cycle.setCycleState(CycleState.ACTIVE);
    LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
    LocalDate inTwoMonths = LocalDate.now().plusMonths(2);
    cycle.setPlannedStartDate(oneMonthAgo);
    cycle.setPlannedEndDate(inTwoMonths);
    company = new Company();
    company.setId(companyId);
    objective = new Objective();
    objective.setId(objectiveId);
    department = new Department();
    department.setId(departmentId);
    department.setParentStructure(company);
    keyResult = new KeyResult();
    keyResult.setStartValue(0);
    keyResult.setTargetValue(10);
    keyResult.setCurrentValue(0);

    when(departmentServiceUsers.findById(departmentId)).thenReturn(department);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(department);
    when(companyService.findById(companyId)).thenReturn(company);
  }

  @Test
  public void
      generateObjectiveRowCollectionForDepartment_expectEmptyCollectionIfThereAreNoObjectives() {
    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertTrue(rows.isEmpty());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void
      generateObjectiveRowCollectionForDepartment_expectOneRowIfThereIsOneObjectiveWithAKeyResult() {
    objective.getKeyResults().add(keyResult);
    department.getObjectives().add(objective);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(1, rows.size());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void
      generateObjectiveRowCollectionForDepartment_expectEmptyCollectionIfThereIsOneObjectiveWithNoKeyResults() {
    department.getObjectives().add(objective);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertTrue(rows.isEmpty());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectTeamNameIsSet() {
    String teamName = "teamName";
    department.setName(teamName);
    objective.getKeyResults().add(keyResult);
    department.getObjectives().add(objective);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(teamName, ((ObjectiveRow) rows.toArray()[0]).getTeam());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectObjectiveNameIsSet() {
    String objectiveName = "objectiveName";
    objective.setName(objectiveName);
    objective.getKeyResults().add(keyResult);
    department.getObjectives().add(objective);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(objectiveName, ((ObjectiveRow) rows.toArray()[0]).getObjective());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe100() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setCurrentValue(keyResult.getTargetValue());

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(1, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0);
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe0() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setCurrentValue(0);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(0, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe50() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setCurrentValue(5);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(0.5, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0);
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe60() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setCurrentValue(4);
    KeyResult keyResult1 = new KeyResult();
    keyResult1.setStartValue(0);
    keyResult1.setCurrentValue(4);
    keyResult1.setTargetValue(5);
    objective.getKeyResults().add(keyResult1);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(0.6, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0.001);
    Assert.assertEquals(0.6, ((ObjectiveRow) rows.toArray()[1]).getProgress().getValue(), 0.001);
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void
      generateObjectiveRowCollectionForDepartment_expectUebergeordnetesUnternehmenszielEmpty() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals("", ((ObjectiveRow) rows.toArray()[0]).getParentStructureGoal());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void
      generateObjectiveRowCollectionForDepartment_expectUebergeordnetesUnternehmenszielIsSet() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    Objective parentObjective = new Objective();
    String parentObjectiveName = "Parent";
    parentObjective.setName(parentObjectiveName);
    objective.setParentObjective(parentObjective);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(
        parentObjectiveName, ((ObjectiveRow) rows.toArray()[0]).getParentStructureGoal());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultNameIsSet() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    String keyResultName = "keyResultName";
    keyResult.setName(keyResultName);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(keyResultName, ((ObjectiveRow) rows.toArray()[0]).getKeyResult());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultDescriptionIsSet() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    String keyResultDescription = "keyResultDescription";
    keyResult.setDescription(keyResultDescription);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(keyResultDescription, ((ObjectiveRow) rows.toArray()[0]).getDescription());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectStartValueIsSet() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    int start = 3;
    keyResult.setStartValue(start);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(start, ((ObjectiveRow) rows.toArray()[0]).getStart());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultEndeIsSet() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    int ende = 5;
    keyResult.setTargetValue(ende);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(ende, ((ObjectiveRow) rows.toArray()[0]).getEnd());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultAktuellIsSet() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    int current = 4;
    keyResult.setCurrentValue(current);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(current, ((ObjectiveRow) rows.toArray()[0]).getActual());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectEinheitAnzahlIsSet() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setUnit(Unit.NUMBER);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals("", ((ObjectiveRow) rows.toArray()[0]).getUnit());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectEinheitEuroIsSet() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setUnit(Unit.EURO);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals("€", ((ObjectiveRow) rows.toArray()[0]).getUnit());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectEinheitProzentIsSet() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setUnit(Unit.PERCENT);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals("%", ((ObjectiveRow) rows.toArray()[0]).getUnit());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectNoValueIsNull() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForDepartment(departmentId);
    ObjectiveRow row = (ObjectiveRow) rows.toArray()[0];

    Assert.assertNotNull(row.getTeam());
    Assert.assertNotNull(row.getObjective());
    Assert.assertNotNull(row.getParentStructureGoal());
    Assert.assertNotNull(row.getKeyResult());
    Assert.assertNotNull(row.getDescription());
    Assert.assertNotNull(row.getUnit());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void
      generateObjectiveRowCollectionForCompany_expectEmptyCollectionIfThereAreNoDepartments() {
    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForCompany(companyId);

    Assert.assertTrue(rows.isEmpty());
    verify(companyService, times(1)).findById(companyId);
  }

  @Test
  public void generateObjectiveRowCollectionForCompany_expectOneRow() {
    objective.setParentStructure(department);
    department.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    department.setParentStructure(company);
    company.getDepartments().add(department);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForCompany(companyId);

    Assert.assertEquals(1, rows.size());
    verify(companyService, times(1)).findById(companyId);
  }
}
