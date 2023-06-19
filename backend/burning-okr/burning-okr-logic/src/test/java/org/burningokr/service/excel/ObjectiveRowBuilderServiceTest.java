// TODO fix test
//package org.burningokr.service.excel;
//
//import org.burningokr.model.cycles.Cycle;
//import org.burningokr.model.cycles.CycleState;
//import org.burningokr.model.excel.ObjectiveRow;
//import org.burningokr.model.okr.KeyResult;
//import org.burningokr.model.okr.Objective;
//import org.burningokr.model.okr.Unit;
//import org.burningokr.model.okrUnits.OkrCompany;
//import org.burningokr.model.okrUnits.OkrDepartment;
//import org.burningokr.service.okrUnit.CompanyService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.Collection;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//public class ObjectiveRowBuilderServiceTest {
//
//  private final long departmentId = 42L;
//  @Mock
//  private OkrChildUnitServiceUsers departmentServiceUsers;
//  @Mock
//  private CompanyService companyService;
//  @InjectMocks
//  private ObjectiveRowBuilderService objectiveRowBuilderService;
//  private OkrDepartment okrDepartment;
//  private Cycle cycle;
//  private long objectiveId = 55L;
//  private OkrCompany okrCompany;
//  private long companyId = 400L;
//  private Objective objective;
//  private KeyResult keyResult;
//
//  @BeforeEach()
//  public void before() {
//    cycle = new Cycle();
//    cycle.setCycleState(CycleState.ACTIVE);
//    LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
//    LocalDate inTwoMonths = LocalDate.now().plusMonths(2);
//    cycle.setPlannedStartDate(oneMonthAgo);
//    cycle.setPlannedEndDate(inTwoMonths);
//    okrCompany = new OkrCompany();
//    okrCompany.setId(companyId);
//    objective = new Objective();
//    objective.setId(objectiveId);
//    okrDepartment = new OkrDepartment();
//    okrDepartment.setId(departmentId);
//    okrDepartment.setParentOkrUnit(okrCompany);
//    keyResult = new KeyResult();
//    keyResult.setStartValue(0);
//    keyResult.setTargetValue(10);
//    keyResult.setCurrentValue(0);
//
//    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
//    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
//    when(companyService.findById(companyId)).thenReturn(okrCompany);
//  }
//
//  @Test
//  public void
//  generateObjectiveRowCollectionForDepartment_expectEmptyCollectionIfThereAreNoObjectives() {
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertTrue(rows.isEmpty());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void
//  generateObjectiveRowCollectionForDepartment_expectOneRowIfThereIsOneObjectiveWithAKeyResult() {
//    objective.getKeyResults().add(keyResult);
//    okrDepartment.getObjectives().add(objective);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(1, rows.size());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void
//  generateObjectiveRowCollectionForDepartment_expectEmptyCollectionIfThereIsOneObjectiveWithNoKeyResults() {
//    okrDepartment.getObjectives().add(objective);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertTrue(rows.isEmpty());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generateObjectiveRowCollectionForDepartment_expectTeamNameIsSet() {
//    String teamName = "teamName";
//    okrDepartment.setName(teamName);
//    objective.getKeyResults().add(keyResult);
//    okrDepartment.getObjectives().add(objective);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(teamName, ((ObjectiveRow) rows.toArray()[0]).getTeam());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generateObjectiveRowCollectionForDepartment_expectObjectiveNameIsSet() {
//    String objectiveName = "objectiveName";
//    objective.setName(objectiveName);
//    objective.getKeyResults().add(keyResult);
//    okrDepartment.getObjectives().add(objective);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(objectiveName, ((ObjectiveRow) rows.toArray()[0]).getObjective());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe100() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    keyResult.setCurrentValue(keyResult.getTargetValue());
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(1, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0);
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe0() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    keyResult.setCurrentValue(0);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(0, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0);
//  }
//
//  @Test
//  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe50() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    keyResult.setCurrentValue(5);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(0.5, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0);
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe60() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    keyResult.setCurrentValue(4);
//    KeyResult keyResult1 = new KeyResult();
//    keyResult1.setStartValue(0);
//    keyResult1.setCurrentValue(4);
//    keyResult1.setTargetValue(5);
//    objective.getKeyResults().add(keyResult1);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(0.6, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0.001);
//    assertEquals(0.6, ((ObjectiveRow) rows.toArray()[1]).getProgress().getValue(), 0.001);
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void
//  generateObjectiveRowCollectionForDepartment_expectUebergeordnetesUnternehmenszielEmpty() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals("", ((ObjectiveRow) rows.toArray()[0]).getParentUnitGoal());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void
//  generateObjectiveRowCollectionForDepartment_expectUebergeordnetesUnternehmenszielIsSet() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    Objective parentObjective = new Objective();
//    String parentObjectiveName = "Parent";
//    parentObjective.setName(parentObjectiveName);
//    objective.setParentObjective(parentObjective);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(
//      parentObjectiveName, ((ObjectiveRow) rows.toArray()[0]).getParentUnitGoal());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultNameIsSet() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    String keyResultName = "keyResultName";
//    keyResult.setName(keyResultName);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(keyResultName, ((ObjectiveRow) rows.toArray()[0]).getKeyResult());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultDescriptionIsSet() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    String keyResultDescription = "keyResultDescription";
//    keyResult.setDescription(keyResultDescription);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(keyResultDescription, ((ObjectiveRow) rows.toArray()[0]).getDescription());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generatedObjectiveRowCollectionForDepartment_expectStartValueIsSet() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    int start = 3;
//    keyResult.setStartValue(start);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(start, ((ObjectiveRow) rows.toArray()[0]).getStart());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultEndeIsSet() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    int ende = 5;
//    keyResult.setTargetValue(ende);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(ende, ((ObjectiveRow) rows.toArray()[0]).getEnd());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultAktuellIsSet() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    int current = 4;
//    keyResult.setCurrentValue(current);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals(current, ((ObjectiveRow) rows.toArray()[0]).getActual());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generatedObjectiveRowCollectionForDepartment_expectEinheitAnzahlIsSet() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    keyResult.setUnit(Unit.NUMBER);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals("", ((ObjectiveRow) rows.toArray()[0]).getUnit());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generatedObjectiveRowCollectionForDepartment_expectEinheitEuroIsSet() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    keyResult.setUnit(Unit.EURO);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals("€", ((ObjectiveRow) rows.toArray()[0]).getUnit());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generatedObjectiveRowCollectionForDepartment_expectEinheitProzentIsSet() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    keyResult.setUnit(Unit.PERCENT);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//
//    assertEquals("%", ((ObjectiveRow) rows.toArray()[0]).getUnit());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void generateObjectiveRowCollectionForDepartment_expectNoValueIsNull() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//
//    Collection<ObjectiveRow> rows =
//      objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
//    ObjectiveRow row = (ObjectiveRow) rows.toArray()[0];
//
//    assertNotNull(row.getTeam());
//    assertNotNull(row.getObjective());
//    assertNotNull(row.getParentUnitGoal());
//    assertNotNull(row.getKeyResult());
//    assertNotNull(row.getDescription());
//    assertNotNull(row.getUnit());
//    verify(departmentServiceUsers, times(1)).findById(departmentId);
//  }
//
//  @Test
//  public void
//  generateObjectiveRowCollectionForCompany_expectEmptyCollectionIfThereAreNoDepartments() {
//    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForCompany(companyId);
//
//    assertTrue(rows.isEmpty());
//    verify(companyService, times(1)).findById(companyId);
//  }
//
//  @Test
//  public void generateObjectiveRowCollectionForCompany_expectOneRow() {
//    objective.setParentOkrUnit(okrDepartment);
//    okrDepartment.getObjectives().add(objective);
//    objective.getKeyResults().add(keyResult);
//    okrDepartment.setParentOkrUnit(okrCompany);
//    okrCompany.getOkrChildUnits().add(okrDepartment);
//
//    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForCompany(companyId);
//
//    assertEquals(1, rows.size());
//    verify(companyService, times(1)).findById(companyId);
//  }
//}
