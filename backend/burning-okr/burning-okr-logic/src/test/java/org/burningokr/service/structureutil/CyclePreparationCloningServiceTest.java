package org.burningokr.service.structureutil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.burningokr.repositories.structre.CompanyRepository;
import org.burningokr.repositories.structre.StructureRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CyclePreparationCloningServiceTest {

  @Mock private CompanyRepository companyRepository;

  @Mock private StructureRepository<SubStructure> subStructureRepository;

  @Mock private ObjectiveRepository objectiveRepository;

  @Mock private UserSettingsRepository userSettingsRepository;

  @InjectMocks private CyclePreparationCloningService cyclePreparationCloningService;
  private Collection<Company> unwrappedCompanyList;
  private Company unwrappedCompany;
  private Collection<Objective> unwrappedObjectiveList1;
  private Objective unwrappedObjective1;
  private Objective unwrappedChildObjective1;
  private Collection<SubStructure> unwrappedSubStructureList;
  private SubStructure unwrappedSubStructure;
  private Collection<Objective> unwrappedObjectiveList2;
  private Objective unwrappedObjective2;
  private Objective unwrappedChildObjective2;

  private Collection<Company> createCompanyList(int amountOfElements) {
    Collection<Company> companyList = new ArrayList<>();

    for (int i = 0; i < amountOfElements; i++) {
      Company companyToAdd = new Company();
      companyToAdd.setId((long) i);
      companyList.add(companyToAdd);
    }

    return companyList;
  }

  private void cloneCompanyListIntoCycle_TestAmountOfNewElements(int amountOfAdditions) {
    Cycle exampleCycle = new Cycle();
    Collection<Company> companysToAdd = createCompanyList(amountOfAdditions);
    CyclePreparationCloningService cyclePreparationCloningService1 =
        spy(cyclePreparationCloningService);
    doNothing()
        .when(cyclePreparationCloningService1)
        .cloneCompanyIntoCycleForPreparation(any(), any());

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
    for (Company x : unwrappedCompanyList) {
      unwrappedCompany = x;
    }
    // Company layer
    unwrappedObjectiveList1 = unwrappedCompany.getObjectives();
    unwrappedSubStructureList = unwrappedCompany.getSubStructures();
    for (SubStructure x : unwrappedSubStructureList) {
      unwrappedSubStructure = x;
    }
    // Department layer
    unwrappedObjectiveList2 = unwrappedSubStructure.getObjectives();
    // Objectives layer
    Collection<Objective> tempUnwrappedObjectivesList = new ArrayList<>();
    tempUnwrappedObjectivesList.addAll(unwrappedObjectiveList1);
    tempUnwrappedObjectivesList.addAll(unwrappedObjectiveList2);
    for (Objective x : tempUnwrappedObjectivesList) {
      switch (x.getName()) {
        case "exampleObjective1":
          unwrappedObjective1 = x;
          break;
        case "exampleObjective2":
          unwrappedObjective2 = x;
          break;
        case "exampleChildObjective1":
          unwrappedChildObjective1 = x;
          break;
        case "exampleChildObjective2":
          unwrappedChildObjective2 = x;
          break;
        default:
          Assert.fail("Cycle unwrap failed");
      }
    }
  }

  @Test
  public void cloneCompanyIntoCycle_ExampleCompany_expectedCorrectChildRelationships() {
    Cycle cycleToCloneInto = new Cycle();
    TestingCycleStructure testingCycleStructure = new TestingCycleStructure();
    Company companyToClone = testingCycleStructure.createTestingCompanyRelationship();

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(
        companyToClone, cycleToCloneInto);

    unwrapCycleContents(cycleToCloneInto);
    // The unwrap method working to set the following references is a proof of functional child
    // relationships
    Assert.assertEquals(1, unwrappedCompanyList.size());
    Assert.assertEquals(1, unwrappedSubStructureList.size());
    Assert.assertEquals(2, unwrappedObjectiveList1.size());
    Assert.assertEquals(2, unwrappedObjectiveList2.size());
    Assert.assertNotNull(unwrappedCompany);
    Assert.assertNotNull(unwrappedSubStructure);
    Assert.assertNotNull(unwrappedObjective1);
    Assert.assertNotNull(unwrappedObjective2);
    Assert.assertNotNull(unwrappedChildObjective1);
    Assert.assertNotNull(unwrappedChildObjective2);
  }

  @Test
  public void cloneCompanyIntoCycle_ExampleCompany_expectedCorrectParentRelationships() {
    Cycle cycleToCloneInto = new Cycle();
    TestingCycleStructure testingCycleStructure = new TestingCycleStructure();
    Company companyToClone = testingCycleStructure.createTestingCompanyRelationship();

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(
        companyToClone, cycleToCloneInto);

    unwrapCycleContents(cycleToCloneInto);
    // CyclePreparationCloningService properly sets up parent relations
    Assert.assertEquals(cycleToCloneInto, unwrappedCompany.getCycle());
    Assert.assertEquals(unwrappedCompany, unwrappedSubStructure.getParentStructure());
    Assert.assertEquals(unwrappedCompany, unwrappedObjective1.getParentStructure());
    Assert.assertEquals(unwrappedCompany, unwrappedChildObjective1.getParentStructure());
    Assert.assertEquals(unwrappedSubStructure, unwrappedObjective2.getParentStructure());
    Assert.assertEquals(unwrappedSubStructure, unwrappedChildObjective2.getParentStructure());
    Assert.assertEquals(unwrappedObjective1, unwrappedChildObjective1.getParentObjective());
    Assert.assertEquals(unwrappedObjective2, unwrappedChildObjective2.getParentObjective());
  }

  @Test
  public void cloneCompanyIntoCycle_ExampleCompany_expectedRepositorySaveCalls() {
    Cycle cycleToCloneInto = new Cycle();
    TestingCycleStructure testingCycleStructure = new TestingCycleStructure();
    Company companyToClone = testingCycleStructure.createTestingCompanyRelationship();

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(
        companyToClone, cycleToCloneInto);

    unwrapCycleContents(cycleToCloneInto);
    ArgumentCaptor<Company> savedCompanyCaptor = ArgumentCaptor.forClass(Company.class);
    ArgumentCaptor<Department> savedDepartmentCaptor = ArgumentCaptor.forClass(Department.class);
    ArgumentCaptor<Objective> savedObjectiveCaptor = ArgumentCaptor.forClass(Objective.class);

    verify(companyRepository).save(savedCompanyCaptor.capture());
    verify(subStructureRepository).save(savedDepartmentCaptor.capture());
    verify(objectiveRepository, times(4)).save(savedObjectiveCaptor.capture());

    Company savedCompany = savedCompanyCaptor.getValue();
    Department savedDepartment = savedDepartmentCaptor.getValue();
    List<Objective> savedObjectives = savedObjectiveCaptor.getAllValues();
    Objective savedObjective1 = savedObjectives.get(0);
    Objective savedChildObjective1 = savedObjectives.get(1);
    Objective savedObjective2 = savedObjectives.get(2);
    Objective savedChildObjective2 = savedObjectives.get(3);

    Assert.assertEquals(unwrappedCompany, savedCompany);
    Assert.assertEquals(unwrappedSubStructure, savedDepartment);
    Assert.assertEquals(unwrappedObjective1, savedObjective1);
    Assert.assertEquals(unwrappedObjective2, savedObjective2);
    Assert.assertEquals(unwrappedChildObjective1, savedChildObjective1);
    Assert.assertEquals(unwrappedChildObjective2, savedChildObjective2);
  }

  @Test
  public void
      cloneCompanyIntoCycle_ExampleCompany_expectUserSettingsWithOldCompanyIdHasNewCompanyId() {
    TestingCycleStructure testingCycleStructure = new TestingCycleStructure();
    final Company companyToClone = testingCycleStructure.createTestingCompanyRelationship();
    final Cycle cycleToCloneInto = new Cycle();

    UserSettings userSettings1 = new UserSettings();
    userSettings1.setId(555L);
    userSettings1.setUserId(UUID.randomUUID());
    userSettings1.setDefaultCompany(testingCycleStructure.exampleCompany);
    UserSettings userSettings2 = new UserSettings();
    userSettings2.setId(666L);
    userSettings2.setUserId(UUID.randomUUID());
    Collection<UserSettings> userSettingsList =
        new ArrayList<>(Arrays.asList(userSettings1, userSettings2));

    when(userSettingsRepository.findAll()).thenReturn(userSettingsList);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(
        companyToClone, cycleToCloneInto);
    unwrapCycleContents(cycleToCloneInto);

    ArgumentCaptor<Company> savedCompanyCaptor = ArgumentCaptor.forClass(Company.class);
    ArgumentCaptor<UserSettings> savedUserSettingsCaptor =
        ArgumentCaptor.forClass(UserSettings.class);

    verify(userSettingsRepository, times(1)).save(savedUserSettingsCaptor.capture());
    verify(companyRepository).save(savedCompanyCaptor.capture());

    Company savedCompany = savedCompanyCaptor.getValue();
    UserSettings userSettings1Captured = savedUserSettingsCaptor.getValue();

    Assert.assertEquals(userSettings1.getId(), userSettings1Captured.getId());
    Assert.assertEquals(userSettings1.getUserId(), userSettings1Captured.getUserId());
    Assert.assertEquals(savedCompany, userSettings1Captured.getDefaultCompany());
    Assert.assertNull(userSettings1Captured.getDefaultTeam());
  }

  @Test
  public void
      cloneCompanyIntoCycle_ExampleCompany_expectUserSettingsWithOldDepartmentIdHasNewDepartmentId() {
    TestingCycleStructure testingCycleStructure = new TestingCycleStructure();
    final Company companyToClone = testingCycleStructure.createTestingCompanyRelationship();
    final Cycle cycleToCloneInto = new Cycle();

    UserSettings userSettings1 = new UserSettings();
    userSettings1.setId(555L);
    userSettings1.setUserId(UUID.randomUUID());
    userSettings1.setDefaultCompany(testingCycleStructure.exampleCompany);
    userSettings1.setDefaultTeam(testingCycleStructure.exampleDepartment);
    UserSettings userSettings2 = new UserSettings();
    userSettings2.setId(666L);
    userSettings2.setUserId(UUID.randomUUID());
    Collection<UserSettings> userSettingsList =
        new ArrayList<>(Arrays.asList(userSettings1, userSettings2));

    when(userSettingsRepository.findAll()).thenReturn(userSettingsList);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(
        companyToClone, cycleToCloneInto);
    unwrapCycleContents(cycleToCloneInto);

    ArgumentCaptor<Company> savedCompanyCaptor = ArgumentCaptor.forClass(Company.class);
    ArgumentCaptor<Department> savedDepartmentCaptor = ArgumentCaptor.forClass(Department.class);
    ArgumentCaptor<UserSettings> savedUserSettingsCaptor =
        ArgumentCaptor.forClass(UserSettings.class);

    verify(userSettingsRepository, times(1)).save(savedUserSettingsCaptor.capture());
    verify(subStructureRepository).save(savedDepartmentCaptor.capture());
    verify(companyRepository).save(savedCompanyCaptor.capture());

    Company savedCompany = savedCompanyCaptor.getValue();
    Department savedDepartment = savedDepartmentCaptor.getValue();
    UserSettings userSettings1Captured = savedUserSettingsCaptor.getValue();

    Assert.assertEquals(userSettings1.getId(), userSettings1Captured.getId());
    Assert.assertEquals(userSettings1.getUserId(), userSettings1Captured.getUserId());
    Assert.assertEquals(savedCompany, userSettings1Captured.getDefaultCompany());
    Assert.assertEquals(savedDepartment, userSettings1Captured.getDefaultTeam());
  }

  @Test
  public void cloneCompanyIntoCycleForPreparation_shouldCloneCorrectParentObjective() {
    final List<Objective> objectives = new ArrayList<>();
    final List<Company> companies = new ArrayList<>();

    final Cycle cycle = new Cycle();
    final Company company = new Company();
    final Objective parentObjective = spy(new Objective());
    final Objective objective = new Objective();

    objectives.add(parentObjective);
    objectives.add(objective);
    companies.add(company);

    parentObjective.setId(5L);
    objective.setId(6L);
    objective.setParentObjective(parentObjective);
    cycle.setId(1L);
    cycle.setCompanies(companies);
    company.setId(2L);
    company.setObjectives(objectives);

    when(parentObjective.getSubObjectives()).thenReturn(Collections.singletonList(objective));

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(company, cycle);

    Assert.assertEquals(2, company.getObjectives().size());
    Assert.assertFalse(((Objective) company.getObjectives().toArray()[0]).hasParentObjective());
    Assert.assertTrue(((Objective) company.getObjectives().toArray()[1]).hasParentObjective());
    Assert.assertEquals(
        ((Objective) company.getObjectives().toArray()[1]).getParentObjective(), parentObjective);
    Assert.assertEquals(1, parentObjective.getSubObjectives().size());
    Assert.assertEquals(objective, parentObjective.getSubObjectives().toArray()[0]);
  }

  @Test
  public void cloneCompanyIntoCycleForPreparation_shouldCloneCorporateObjectiveStructure() {
    Cycle cycle = new Cycle();
    Company company = new Company();
    cycle.setCompanies(Lists.newArrayList(company));
    company.setName("testCompany");
    CorporateObjectiveStructure corporateObjectiveStructure = new CorporateObjectiveStructure();
    corporateObjectiveStructure.setName("testCorporateObjectiveStructure");
    corporateObjectiveStructure.setParentStructure(company);
    final Collection<SubStructure> corporateObjectiveStructures =
        Collections.singletonList(corporateObjectiveStructure);
    company.setSubStructures(corporateObjectiveStructures);

    ArgumentCaptor<Company> companyCaptor = ArgumentCaptor.forClass(Company.class);
    ArgumentCaptor<CorporateObjectiveStructure> corporateObjectiveStructureArgumentCaptor =
        ArgumentCaptor.forClass(CorporateObjectiveStructure.class);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(company, cycle);

    verify(companyRepository, times(1)).save(companyCaptor.capture());
    verify(subStructureRepository, times(1))
        .save(corporateObjectiveStructureArgumentCaptor.capture());

    Company companyCaptured = companyCaptor.getValue();
    CorporateObjectiveStructure corporateObjectiveStructureCaptured =
        corporateObjectiveStructureArgumentCaptor.getValue();

    Assert.assertEquals(company.getName(), companyCaptured.getName());
    Assert.assertEquals(
        corporateObjectiveStructure.getName(), corporateObjectiveStructureCaptured.getName());
  }

  @Test
  public void
      cloneCompanyIntoCycleForPreparation_shouldCloneCorporateObjectiveStructureWithObjectives() {
    Cycle cycle = new Cycle();
    Company company = new Company();
    cycle.setCompanies(Lists.newArrayList(company));
    company.setName("testCompany");
    CorporateObjectiveStructure corporateObjectiveStructure = new CorporateObjectiveStructure();
    corporateObjectiveStructure.setName("testCorporateObjectiveStructure");
    corporateObjectiveStructure.setParentStructure(company);
    final Collection<Objective> objectives = new ArrayList<>();
    Objective objective = new Objective();
    objective.setName("objectiveTest");
    objectives.add(objective);
    corporateObjectiveStructure.setObjectives(objectives);
    final Collection<SubStructure> corporateObjectiveStructures =
        Collections.singletonList(corporateObjectiveStructure);
    company.setSubStructures(corporateObjectiveStructures);

    ArgumentCaptor<Company> companyCaptor = ArgumentCaptor.forClass(Company.class);
    ArgumentCaptor<CorporateObjectiveStructure> corporateObjectiveStructureArgumentCaptor =
        ArgumentCaptor.forClass(CorporateObjectiveStructure.class);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(company, cycle);
    ArgumentCaptor<Objective> objectiveArgumentCaptor = ArgumentCaptor.forClass(Objective.class);

    verify(companyRepository, times(1)).save(companyCaptor.capture());
    verify(subStructureRepository, times(1))
        .save(corporateObjectiveStructureArgumentCaptor.capture());
    verify(objectiveRepository, times(1)).save(objectiveArgumentCaptor.capture());

    Company companyCaptured = companyCaptor.getValue();
    CorporateObjectiveStructure corporateObjectiveStructureCaptured =
        corporateObjectiveStructureArgumentCaptor.getValue();
    Objective objectiveCaptured = objectiveArgumentCaptor.getValue();

    Assert.assertEquals(company.getName(), companyCaptured.getName());
    Assert.assertEquals(
        corporateObjectiveStructure.getName(), corporateObjectiveStructureCaptured.getName());
    Assert.assertEquals(objective.getName(), objectiveCaptured.getName());
  }

  @Test
  public void
      cloneCompanyIntoCycleForPreparation_shouldCloneCorporateObjectiveStructureWithChildCorporateObjectiveStructures() {
    Cycle cycle = new Cycle();
    Company company = new Company();
    cycle.setCompanies(Lists.newArrayList(company));
    company.setName("testCompany");
    CorporateObjectiveStructure corporateObjectiveStructure = new CorporateObjectiveStructure();
    corporateObjectiveStructure.setName("testCorporateObjectiveStructure");
    corporateObjectiveStructure.setParentStructure(company);
    final Collection<SubStructure> childCorporateObjectiveStructures = new ArrayList<>();
    CorporateObjectiveStructure childCorporateObjectiveStructure =
        new CorporateObjectiveStructure();
    childCorporateObjectiveStructures.add(childCorporateObjectiveStructure);
    childCorporateObjectiveStructure.setName("childCorporateObjective");
    corporateObjectiveStructure.setSubStructures(childCorporateObjectiveStructures);
    final Collection<SubStructure> corporateObjectiveStructures =
        Collections.singletonList(corporateObjectiveStructure);
    company.setSubStructures(corporateObjectiveStructures);

    ArgumentCaptor<Company> companyCaptor = ArgumentCaptor.forClass(Company.class);
    ArgumentCaptor<CorporateObjectiveStructure> corporateObjectiveStructureArgumentCaptor =
        ArgumentCaptor.forClass(CorporateObjectiveStructure.class);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(company, cycle);

    verify(companyRepository, times(1)).save(companyCaptor.capture());
    verify(subStructureRepository, times(2))
        .save(corporateObjectiveStructureArgumentCaptor.capture());

    Company companyCaptured = companyCaptor.getValue();
    CorporateObjectiveStructure corporateObjectiveStructureCaptured =
        corporateObjectiveStructureArgumentCaptor.getAllValues().get(0);
    CorporateObjectiveStructure childCorporateObjectiveStructureCaptured =
        corporateObjectiveStructureArgumentCaptor.getAllValues().get(1);

    Assert.assertEquals(company.getName(), companyCaptured.getName());
    Assert.assertEquals(
        corporateObjectiveStructure.getName(), corporateObjectiveStructureCaptured.getName());
    Assert.assertEquals(
        childCorporateObjectiveStructure.getName(),
        childCorporateObjectiveStructureCaptured.getName());
  }

  @Test
  public void
      cloneCompanyIntoCycleForPreparation_shouldCloneCorporateObjectiveStructureWithDepartments() {
    Cycle cycle = new Cycle();
    Company company = new Company();
    cycle.setCompanies(Lists.newArrayList(company));
    company.setName("testCompany");
    CorporateObjectiveStructure corporateObjectiveStructure = new CorporateObjectiveStructure();
    corporateObjectiveStructure.setName("testCorporateObjectiveStructure");
    corporateObjectiveStructure.setParentStructure(company);
    final Collection<SubStructure> departments = new ArrayList<>();
    Department department = new Department();
    department.setName("testDepartment");
    department.setParentStructure(corporateObjectiveStructure);
    departments.add(department);
    corporateObjectiveStructure.setSubStructures(departments);
    final Collection<SubStructure> corporateObjectiveStructures =
        Collections.singletonList(corporateObjectiveStructure);
    company.setSubStructures(corporateObjectiveStructures);

    ArgumentCaptor<Company> companyCaptor = ArgumentCaptor.forClass(Company.class);
    ArgumentCaptor<Department> departmentArgumentCaptor = ArgumentCaptor.forClass(Department.class);

    cyclePreparationCloningService.cloneCompanyIntoCycleForPreparation(company, cycle);

    verify(companyRepository, times(1)).save(companyCaptor.capture());

    // Twice because it is saved once for the corporateObjectiveStructure and a second time for the
    // department.
    verify(subStructureRepository, times(2)).save(departmentArgumentCaptor.capture());

    Company companyCaptured = companyCaptor.getValue();
    Department departmentCaptured = departmentArgumentCaptor.getValue();

    Assert.assertEquals(company.getName(), companyCaptured.getName());
    Assert.assertEquals(department.getName(), departmentCaptured.getName());
  }
}
