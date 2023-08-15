package org.burningokr.service.okrUnitUtil;

import com.google.common.collect.Lists;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.repositories.okrUnit.OkrUnitRepository;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.burningokr.service.okr.TaskBoardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CyclePreparationCloningServiceTest {

  @Mock
  private CompanyRepository companyRepository;

  @Mock
  private OkrUnitRepository<OkrChildUnit> childUnitRepository;

  @Mock
  private ObjectiveRepository objectiveRepository;

  @Mock
  private UserSettingsRepository userSettingsRepository;

  @Mock
  private TaskBoardService taskBoardService;

  @InjectMocks
  private CyclePreparationCloningService cyclePreparationCloningService;
  private Collection<OkrCompany> unwrappedCompanyList;
  private OkrCompany unwrappedCompany;
  private Collection<Objective> unwrappedObjectiveList1;
  private Objective unwrappedObjective1;
  private Objective unwrappedChildObjective1;
  private Collection<OkrChildUnit> unwrappedChildUnitList;
  private OkrChildUnit unwrappedChildUnit;
  private Collection<Objective> unwrappedObjectiveList2;
  private Objective unwrappedObjective2;
  private Objective unwrappedChildObjective2;

  private Collection<OkrCompany> createCompanyList(int amountOfElements) {
    Collection<OkrCompany> companyList = new ArrayList<>();

    for (int i = 0; i < amountOfElements; i++) {
      OkrCompany okrCompanyToAdd = new OkrCompany();
      okrCompanyToAdd.setId((long) i);
      companyList.add(okrCompanyToAdd);
    }

    return companyList;
  }

  private void cloneCompanyListIntoCycle_TestAmountOfNewElements(int amountOfAdditions) {
    Cycle exampleCycle = new Cycle();
    Collection<OkrCompany> companysToAdd = createCompanyList(amountOfAdditions);
    CyclePreparationCloningService cyclePreparationCloningService1 =
      spy(cyclePreparationCloningService);

    cyclePreparationCloningService1.cloneCompanyListIntoCycleForPreparation(
      companysToAdd, exampleCycle);

    verify(cyclePreparationCloningService1, times(amountOfAdditions))
      .cloneCompanyIntoCycleForPreparation(any(), any());
  }

  @Test
  public void cloneCompanyListIntoCycle_CompanyCollection_expectedNoAdditions() {
    cloneCompanyListIntoCycle_TestAmountOfNewElements(0);
  }

  @Test
  public void cloneCompanyListIntoCycle_CompanyCollection_expectedOneAddition() {
    cloneCompanyListIntoCycle_TestAmountOfNewElements(1);
  }

  @Test
  public void cloneCompanyListIntoCycle_CompanyCollection_expectedTwoAdditions() {
    cloneCompanyListIntoCycle_TestAmountOfNewElements(2);
  }

  private void unwrapCycleContents(Cycle cycleToUnwrap) {
    // Cycle layer
    unwrappedCompanyList = cycleToUnwrap.getCompanies();
    for (OkrCompany x : unwrappedCompanyList) {
      unwrappedCompany = x;
    }
    // OkrCompany layer
    unwrappedObjectiveList1 = unwrappedCompany.getObjectives();
    unwrappedChildUnitList = unwrappedCompany.getOkrChildUnits();
    for (OkrChildUnit x : unwrappedChildUnitList) {
      unwrappedChildUnit = x;
    }
    // OkrDepartment layer
    unwrappedObjectiveList2 = unwrappedChildUnit.getObjectives();
    // Objectives layer
    Collection<Objective> tempUnwrappedObjectivesList = new ArrayList<>();
    tempUnwrappedObjectivesList.addAll(unwrappedObjectiveList1);
    tempUnwrappedObjectivesList.addAll(unwrappedObjectiveList2);
    for (Objective x : tempUnwrappedObjectivesList) {
      switch (x.getName()) {
        case "exampleObjective1" -> unwrappedObjective1 = x;
        case "exampleObjective2" -> unwrappedObjective2 = x;
        case "exampleChildObjective1" -> unwrappedChildObjective1 = x;
        case "exampleChildObjective2" -> unwrappedChildObjective2 = x;
        default -> fail("Cycle unwrap failed");
      }
    }
  }

  @Test
  public void cloneCompanyIntoCycle_ExampleCompany_expectedCorrectChildRelationships() {
    Cycle cycleToCloneInto = new Cycle();
    TestingCycleStructure testingCycleStructure = new TestingCycleStructure();
    OkrCompany okrCompanyToClone = testingCycleStructure.createTestingCompanyRelationship();

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(
            okrCompanyToClone, cycleToCloneInto);

    unwrapCycleContents(cycleToCloneInto);
    // The unwrap method working to set the following references is a proof of functional child
    // relationships
    assertEquals(1, unwrappedCompanyList.size());
    assertEquals(1, unwrappedChildUnitList.size());
    assertEquals(2, unwrappedObjectiveList1.size());
    assertEquals(2, unwrappedObjectiveList2.size());
    assertNotNull(unwrappedCompany);
    assertNotNull(unwrappedChildUnit);
    assertNotNull(unwrappedObjective1);
    assertNotNull(unwrappedObjective2);
    assertNotNull(unwrappedChildObjective1);
    assertNotNull(unwrappedChildObjective2);
  }

  @Test
  public void cloneCompanyIntoCycle_ExampleCompany_expectedCorrectParentRelationships() {
    Cycle cycleToCloneInto = new Cycle();
    TestingCycleStructure testingCycleStructure = new TestingCycleStructure();
    OkrCompany okrCompanyToClone = testingCycleStructure.createTestingCompanyRelationship();

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(
            okrCompanyToClone, cycleToCloneInto);

    unwrapCycleContents(cycleToCloneInto);
    // CyclePreparationCloningService properly sets up parent relations
    assertEquals(cycleToCloneInto, unwrappedCompany.getCycle());
    assertEquals(unwrappedCompany, unwrappedChildUnit.getParentOkrUnit());
    assertEquals(unwrappedCompany, unwrappedObjective1.getParentOkrUnit());
    assertEquals(unwrappedCompany, unwrappedChildObjective1.getParentOkrUnit());
    assertEquals(unwrappedChildUnit, unwrappedObjective2.getParentOkrUnit());
    assertEquals(unwrappedChildUnit, unwrappedChildObjective2.getParentOkrUnit());
    assertEquals(unwrappedObjective1, unwrappedChildObjective1.getParentObjective());
    assertEquals(unwrappedObjective2, unwrappedChildObjective2.getParentObjective());
  }

  @Test
  public void cloneCompanyIntoCycle_ExampleCompany_expectedRepositorySaveCalls() {
    Cycle cycleToCloneInto = new Cycle();
    TestingCycleStructure testingCycleStructure = new TestingCycleStructure();
    OkrCompany okrCompanyToClone = testingCycleStructure.createTestingCompanyRelationship();

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(
      okrCompanyToClone, cycleToCloneInto);

    unwrapCycleContents(cycleToCloneInto);
    ArgumentCaptor<OkrCompany> savedCompanyCaptor = ArgumentCaptor.forClass(OkrCompany.class);
    ArgumentCaptor<OkrDepartment> savedDepartmentCaptor =
      ArgumentCaptor.forClass(OkrDepartment.class);
    ArgumentCaptor<Objective> savedObjectiveCaptor = ArgumentCaptor.forClass(Objective.class);

    verify(companyRepository).save(savedCompanyCaptor.capture());
    verify(childUnitRepository, times(2)).save(savedDepartmentCaptor.capture());
    verify(objectiveRepository, times(4)).save(savedObjectiveCaptor.capture());

    OkrCompany savedOkrCompany = savedCompanyCaptor.getValue();
    OkrDepartment savedOkrDepartment = savedDepartmentCaptor.getValue();
    List<Objective> savedObjectives = savedObjectiveCaptor.getAllValues();
    Objective savedObjective1 = savedObjectives.get(0);
    Objective savedChildObjective1 = savedObjectives.get(1);
    Objective savedObjective2 = savedObjectives.get(2);
    Objective savedChildObjective2 = savedObjectives.get(3);

    assertEquals(unwrappedCompany, savedOkrCompany);
    assertEquals(unwrappedChildUnit, savedOkrDepartment);
    assertEquals(unwrappedObjective1, savedObjective1);
    assertEquals(unwrappedObjective2, savedObjective2);
    assertEquals(unwrappedChildObjective1, savedChildObjective1);
    assertEquals(unwrappedChildObjective2, savedChildObjective2);
  }

  @Test
  public void
  cloneCompanyIntoCycle_ExampleCompany_expectUserSettingsWithOldCompanyIdHasNewCompanyId() {
    TestingCycleStructure testingCycleStructure = new TestingCycleStructure();
    final OkrCompany okrCompanyToClone = testingCycleStructure.createTestingCompanyRelationship();
    final Cycle cycleToCloneInto = new Cycle();

    UserSettings userSettings1 = new UserSettings();
    userSettings1.setId(555L);
    userSettings1.setUserId(UUID.randomUUID());
    userSettings1.setDefaultOkrCompany(testingCycleStructure.exampleOkrCompany);
    UserSettings userSettings2 = new UserSettings();
    userSettings2.setId(666L);
    userSettings2.setUserId(UUID.randomUUID());
    Collection<UserSettings> userSettingsList =
      new ArrayList<>(Arrays.asList(userSettings1, userSettings2));

    when(userSettingsRepository.findAll()).thenReturn(userSettingsList);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(
      okrCompanyToClone, cycleToCloneInto);
    unwrapCycleContents(cycleToCloneInto);

    ArgumentCaptor<OkrCompany> savedCompanyCaptor = ArgumentCaptor.forClass(OkrCompany.class);
    ArgumentCaptor<UserSettings> savedUserSettingsCaptor =
      ArgumentCaptor.forClass(UserSettings.class);

    verify(userSettingsRepository, times(1)).save(savedUserSettingsCaptor.capture());
    verify(companyRepository).save(savedCompanyCaptor.capture());

    OkrCompany savedOkrCompany = savedCompanyCaptor.getValue();
    UserSettings userSettings1Captured = savedUserSettingsCaptor.getValue();

    assertEquals(userSettings1.getId(), userSettings1Captured.getId());
    assertEquals(userSettings1.getUserId(), userSettings1Captured.getUserId());
    assertEquals(savedOkrCompany, userSettings1Captured.getDefaultOkrCompany());
    assertNull(userSettings1Captured.getDefaultTeam());
  }

  @Test
  public void
  cloneCompanyIntoCycle_ExampleCompany_expectUserSettingsWithOldDepartmentIdHasNewDepartmentId() {
    TestingCycleStructure testingCycleStructure = new TestingCycleStructure();
    final OkrCompany okrCompanyToClone = testingCycleStructure.createTestingCompanyRelationship();
    final Cycle cycleToCloneInto = new Cycle();

    UserSettings userSettings1 = new UserSettings();
    userSettings1.setId(555L);
    userSettings1.setUserId(UUID.randomUUID());
    userSettings1.setDefaultOkrCompany(testingCycleStructure.exampleOkrCompany);
    userSettings1.setDefaultTeam(testingCycleStructure.exampleOkrDepartment);
    UserSettings userSettings2 = new UserSettings();
    userSettings2.setId(666L);
    userSettings2.setUserId(UUID.randomUUID());
    Collection<UserSettings> userSettingsList =
      new ArrayList<>(Arrays.asList(userSettings1, userSettings2));

    when(userSettingsRepository.findAll()).thenReturn(userSettingsList);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(
      okrCompanyToClone, cycleToCloneInto);
    unwrapCycleContents(cycleToCloneInto);

    ArgumentCaptor<OkrCompany> savedCompanyCaptor = ArgumentCaptor.forClass(OkrCompany.class);
    ArgumentCaptor<OkrDepartment> savedDepartmentCaptor =
      ArgumentCaptor.forClass(OkrDepartment.class);
    ArgumentCaptor<UserSettings> savedUserSettingsCaptor =
      ArgumentCaptor.forClass(UserSettings.class);

    verify(userSettingsRepository, times(1)).save(savedUserSettingsCaptor.capture());
    verify(childUnitRepository, times(2)).save(savedDepartmentCaptor.capture());
    verify(companyRepository).save(savedCompanyCaptor.capture());

    OkrCompany savedOkrCompany = savedCompanyCaptor.getValue();
    OkrDepartment savedOkrDepartment = savedDepartmentCaptor.getValue();
    UserSettings userSettings1Captured = savedUserSettingsCaptor.getValue();

    assertEquals(userSettings1.getId(), userSettings1Captured.getId());
    assertEquals(userSettings1.getUserId(), userSettings1Captured.getUserId());
    assertEquals(savedOkrCompany, userSettings1Captured.getDefaultOkrCompany());
    assertEquals(savedOkrDepartment, userSettings1Captured.getDefaultTeam());
  }

  @Test
  public void cloneCompanyIntoCycleForPreparation_shouldCloneCorrectParentObjective() {
    final List<Objective> objectives = new ArrayList<>();
    final List<OkrCompany> companies = new ArrayList<>();

    final Cycle cycle = new Cycle();
    final OkrCompany okrCompany = new OkrCompany();
    final Objective parentObjective = spy(new Objective());
    final Objective objective = new Objective();

    objectives.add(parentObjective);
    objectives.add(objective);
    companies.add(okrCompany);

    parentObjective.setId(5L);
    objective.setId(6L);
    objective.setParentObjective(parentObjective);
    cycle.setId(1L);
    cycle.setCompanies(companies);
    okrCompany.setId(2L);
    okrCompany.setObjectives(objectives);

    when(parentObjective.getSubObjectives()).thenReturn(Collections.singletonList(objective));

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(okrCompany, cycle);

    assertEquals(2, okrCompany.getObjectives().size());
    assertFalse(((Objective) okrCompany.getObjectives().toArray()[0]).hasParentObjective());
    assertTrue(((Objective) okrCompany.getObjectives().toArray()[1]).hasParentObjective());
    assertEquals(
      ((Objective) okrCompany.getObjectives().toArray()[1]).getParentObjective(),
      parentObjective
    );
    assertEquals(1, parentObjective.getSubObjectives().size());
    assertEquals(objective, parentObjective.getSubObjectives().toArray()[0]);
  }

  @Test
  public void cloneCompanyIntoCycleForPreparation_shouldCloneOkrBranch() {
    Cycle cycle = new Cycle();
    OkrCompany okrCompany = new OkrCompany();
    cycle.setCompanies(Lists.newArrayList(okrCompany));
    okrCompany.setName("testCompany");
    OkrBranch okrBranch = new OkrBranch();
    okrBranch.setName("testOkrBranch");
    okrBranch.setParentOkrUnit(okrCompany);
    final Collection<OkrChildUnit> childUnits = Collections.singletonList(okrBranch);
    okrCompany.setOkrChildUnits(childUnits);

    ArgumentCaptor<OkrCompany> companyCaptor = ArgumentCaptor.forClass(OkrCompany.class);
    ArgumentCaptor<OkrBranch> okrBranchArgumentCaptor = ArgumentCaptor.forClass(OkrBranch.class);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(okrCompany, cycle);

    verify(companyRepository, times(1)).save(companyCaptor.capture());
    verify(childUnitRepository, times(1)).save(okrBranchArgumentCaptor.capture());

    OkrCompany okrCompanyCaptured = companyCaptor.getValue();
    OkrBranch okrBranchCaptured = okrBranchArgumentCaptor.getValue();

    assertEquals(okrCompany.getName(), okrCompanyCaptured.getName());
    assertEquals(okrBranch.getName(), okrBranchCaptured.getName());
  }

  @Test
  public void cloneCompanyIntoCycleForPreparation_shouldCloneOkrBranchWithObjectives() {
    Cycle cycle = new Cycle();
    OkrCompany okrCompany = new OkrCompany();
    cycle.setCompanies(Lists.newArrayList(okrCompany));
    okrCompany.setName("testCompany");
    OkrBranch okrBranch = new OkrBranch();
    okrBranch.setName("testOkrBranch");
    okrBranch.setParentOkrUnit(okrCompany);
    final Collection<Objective> objectives = new ArrayList<>();
    Objective objective = new Objective();
    objective.setName("objectiveTest");
    objectives.add(objective);
    okrBranch.setObjectives(objectives);
    final Collection<OkrChildUnit> okrChildUnits = Collections.singletonList(okrBranch);
    okrCompany.setOkrChildUnits(okrChildUnits);

    ArgumentCaptor<OkrCompany> companyCaptor = ArgumentCaptor.forClass(OkrCompany.class);
    ArgumentCaptor<OkrBranch> okrBranchArgumentCaptor = ArgumentCaptor.forClass(OkrBranch.class);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(okrCompany, cycle);
    ArgumentCaptor<Objective> objectiveArgumentCaptor = ArgumentCaptor.forClass(Objective.class);

    verify(companyRepository, times(1)).save(companyCaptor.capture());
    verify(childUnitRepository, times(1)).save(okrBranchArgumentCaptor.capture());
    verify(objectiveRepository, times(1)).save(objectiveArgumentCaptor.capture());

    OkrCompany okrCompanyCaptured = companyCaptor.getValue();
    OkrBranch okrBranchCaptured = okrBranchArgumentCaptor.getValue();
    Objective objectiveCaptured = objectiveArgumentCaptor.getValue();

    assertEquals(okrCompany.getName(), okrCompanyCaptured.getName());
    assertEquals(okrBranch.getName(), okrBranchCaptured.getName());
    assertEquals(objective.getName(), objectiveCaptured.getName());
  }

  @Test
  public void cloneCompanyIntoCycleForPreparation_shouldOkrBranchWithChildOkrBranches() {
    Cycle cycle = new Cycle();
    OkrCompany okrCompany = new OkrCompany();
    cycle.setCompanies(Lists.newArrayList(okrCompany));
    okrCompany.setName("testCompany");
    OkrBranch okrBranch = new OkrBranch();
    okrBranch.setName("testOkrBranch");
    okrBranch.setParentOkrUnit(okrCompany);
    final Collection<OkrChildUnit> okrChildUnits = new ArrayList<>();
    OkrBranch childOkrBranch = new OkrBranch();
    okrChildUnits.add(childOkrBranch);
    childOkrBranch.setName("childUnit");
    okrBranch.setOkrChildUnits(okrChildUnits);
    final Collection<OkrChildUnit> okrChildUnits1 = Collections.singletonList(okrBranch);
    okrCompany.setOkrChildUnits(okrChildUnits1);

    ArgumentCaptor<OkrCompany> companyCaptor = ArgumentCaptor.forClass(OkrCompany.class);
    ArgumentCaptor<OkrBranch> okrBranchArgumentCaptor = ArgumentCaptor.forClass(OkrBranch.class);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(okrCompany, cycle);

    verify(companyRepository, times(1)).save(companyCaptor.capture());
    verify(childUnitRepository, times(2)).save(okrBranchArgumentCaptor.capture());

    OkrCompany okrCompanyCaptured = companyCaptor.getValue();
    OkrBranch okrBranchCaptured = okrBranchArgumentCaptor.getAllValues().get(0);
    OkrBranch childOkrBranchCaptured = okrBranchArgumentCaptor.getAllValues().get(1);

    assertEquals(okrCompany.getName(), okrCompanyCaptured.getName());
    assertEquals(okrBranch.getName(), okrBranchCaptured.getName());
    assertEquals(childOkrBranch.getName(), childOkrBranchCaptured.getName());
  }

  @Test
  public void cloneCompanyIntoCycleForPreparation_shouldCloneOkrBranchWithDepartments() {
    Cycle cycle = new Cycle();
    OkrCompany okrCompany = new OkrCompany();
    cycle.setCompanies(Lists.newArrayList(okrCompany));
    okrCompany.setName("testCompany");
    OkrBranch okrBranch = new OkrBranch();
    okrBranch.setName("testOkrBranch");
    okrBranch.setParentOkrUnit(okrCompany);
    final Collection<OkrChildUnit> departments = new ArrayList<>();
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setName("testDepartment");
    okrDepartment.setParentOkrUnit(okrBranch);
    departments.add(okrDepartment);
    okrBranch.setOkrChildUnits(departments);
    final Collection<OkrChildUnit> okrChildUnits = Collections.singletonList(okrBranch);
    okrCompany.setOkrChildUnits(okrChildUnits);

    ArgumentCaptor<OkrCompany> companyCaptor = ArgumentCaptor.forClass(OkrCompany.class);
    ArgumentCaptor<OkrDepartment> departmentArgumentCaptor =
      ArgumentCaptor.forClass(OkrDepartment.class);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(okrCompany, cycle);

    verify(companyRepository, times(1)).save(companyCaptor.capture());

    // Twice because it is saved once for the okrBranch and a second time for the
    // okrDepartment.
    verify(childUnitRepository, times(2)).save(departmentArgumentCaptor.capture());

    OkrCompany okrCompanyCaptured = companyCaptor.getValue();
    OkrDepartment okrDepartmentCaptured = departmentArgumentCaptor.getValue();

    assertEquals(okrCompany.getName(), okrCompanyCaptured.getName());
    assertEquals(okrDepartment.getName(), okrDepartmentCaptured.getName());
  }
}
