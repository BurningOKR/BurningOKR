package org.burningokr.service.excel;

import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.excel.ObjectiveRow;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.Unit;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ObjectiveRowBuilderServiceTest {

  private final long departmentId = 42L;
  @Mock
  private OkrChildUnitService<OkrChildUnit> departmentServiceUsers;
  @Mock
  private CompanyService companyService;
  @InjectMocks
  private ObjectiveRowBuilderService objectiveRowBuilderService;
  private OkrDepartment okrDepartment;
  private Cycle cycle;
  private final long objectiveId = 55L;
  private OkrCompany okrCompany;
  private final long companyId = 400L;
  private Objective objective;
  private KeyResult keyResult;

  @BeforeEach()
  public void before() {
    cycle = new Cycle();
    cycle.setCycleState(CycleState.ACTIVE);
    LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
    LocalDate inTwoMonths = LocalDate.now().plusMonths(2);
    cycle.setPlannedStartDate(oneMonthAgo);
    cycle.setPlannedEndDate(inTwoMonths);
    okrCompany = new OkrCompany();
    okrCompany.setId(companyId);
    objective = new Objective();
    objective.setId(objectiveId);
    okrDepartment = new OkrDepartment();
    okrDepartment.setId(departmentId);
    okrDepartment.setParentOkrUnit(okrCompany);
    keyResult = new KeyResult();
    keyResult.setStartValue(0L);
    keyResult.setTargetValue(10L);
    keyResult.setCurrentValue(0L);

  }

  @Test
  public void
  generateObjectiveRowCollectionForDepartment_expectEmptyCollectionIfThereAreNoObjectives() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertTrue(rows.isEmpty());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void
  generateObjectiveRowCollectionForDepartment_expectOneRowIfThereIsOneObjectiveWithAKeyResult() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.getKeyResults().add(keyResult);
    okrDepartment.getObjectives().add(objective);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(1, rows.size());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void
  generateObjectiveRowCollectionForDepartment_expectEmptyCollectionIfThereIsOneObjectiveWithNoKeyResults() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    okrDepartment.getObjectives().add(objective);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertTrue(rows.isEmpty());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectTeamNameIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    String teamName = "teamName";
    okrDepartment.setName(teamName);
    objective.getKeyResults().add(keyResult);
    okrDepartment.getObjectives().add(objective);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(teamName, ((ObjectiveRow) rows.toArray()[0]).getTeam());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectObjectiveNameIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    String objectiveName = "objectiveName";
    objective.setName(objectiveName);
    objective.getKeyResults().add(keyResult);
    okrDepartment.getObjectives().add(objective);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(objectiveName, ((ObjectiveRow) rows.toArray()[0]).getObjective());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe100() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setCurrentValue(keyResult.getTargetValue());

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(1, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0);
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe0() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setCurrentValue(0L);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(0, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe50() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setCurrentValue(5L);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(0.5, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0);
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectFortschrittToBe60() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setCurrentValue(4L);
    KeyResult keyResult1 = new KeyResult();
    keyResult1.setStartValue(0L);
    keyResult1.setCurrentValue(4L);
    keyResult1.setTargetValue(5L);
    objective.getKeyResults().add(keyResult1);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(0.6, ((ObjectiveRow) rows.toArray()[0]).getProgress().getValue(), 0.001);
    assertEquals(0.6, ((ObjectiveRow) rows.toArray()[1]).getProgress().getValue(), 0.001);
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void
  generateObjectiveRowCollectionForDepartment_expectUebergeordnetesUnternehmenszielEmpty() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals("", ((ObjectiveRow) rows.toArray()[0]).getParentUnitGoal());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void
  generateObjectiveRowCollectionForDepartment_expectUebergeordnetesUnternehmenszielIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    Objective parentObjective = new Objective();
    String parentObjectiveName = "Parent";
    parentObjective.setName(parentObjectiveName);
    objective.setParentObjective(parentObjective);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(
            parentObjectiveName, ((ObjectiveRow) rows.toArray()[0]).getParentUnitGoal());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultNameIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    String keyResultName = "keyResultName";
    keyResult.setName(keyResultName);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(keyResultName, ((ObjectiveRow) rows.toArray()[0]).getKeyResult());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultDescriptionIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    String keyResultDescription = "keyResultDescription";
    keyResult.setDescription(keyResultDescription);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(keyResultDescription, ((ObjectiveRow) rows.toArray()[0]).getDescription());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectStartValueIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    Long start = 3L;
    keyResult.setStartValue(start);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(start, ((ObjectiveRow) rows.toArray()[0]).getStart());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultEndeIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    Long ende = 5L;
    keyResult.setTargetValue(ende);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(ende, ((ObjectiveRow) rows.toArray()[0]).getEnd());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectKeyResultAktuellIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    Long current = 4L;
    keyResult.setCurrentValue(current);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(current, ((ObjectiveRow) rows.toArray()[0]).getActual());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectEinheitAnzahlIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setUnit(Unit.NUMBER);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals("", ((ObjectiveRow) rows.toArray()[0]).getUnit());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectEinheitEuroIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setUnit(Unit.EURO);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals("€", ((ObjectiveRow) rows.toArray()[0]).getUnit());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generatedObjectiveRowCollectionForDepartment_expectEinheitProzentIsSet() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    keyResult.setUnit(Unit.PERCENT);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals("%", ((ObjectiveRow) rows.toArray()[0]).getUnit());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateObjectiveRowCollectionForDepartment_expectNoValueIsNull() {
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);

    Collection<ObjectiveRow> rows =
            objectiveRowBuilderService.generateForOkrChildUnit(departmentId);
    ObjectiveRow row = (ObjectiveRow) rows.toArray()[0];

    assertNotNull(row.getTeam());
    assertNotNull(row.getObjective());
    assertNotNull(row.getParentUnitGoal());
    assertNotNull(row.getKeyResult());
    assertNotNull(row.getDescription());
    assertNotNull(row.getUnit());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void
  generateObjectiveRowCollectionForCompany_expectEmptyCollectionIfThereAreNoDepartments() {
    when(companyService.findById(companyId)).thenReturn(okrCompany);
    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForCompany(companyId);

    assertTrue(rows.isEmpty());
    verify(companyService, times(1)).findById(companyId);
  }

  @Test
  public void generateObjectiveRowCollectionForCompany_expectOneRow() {
    when(companyService.findById(companyId)).thenReturn(okrCompany);
    objective.setParentOkrUnit(okrDepartment);
    okrDepartment.getObjectives().add(objective);
    objective.getKeyResults().add(keyResult);
    okrDepartment.setParentOkrUnit(okrCompany);
    okrCompany.getOkrChildUnits().add(okrDepartment);

    Collection<ObjectiveRow> rows = objectiveRowBuilderService.generateForCompany(companyId);

    assertEquals(1, rows.size());
    verify(companyService, times(1)).findById(companyId);
  }
}
