package org.burningokr.service.structure.departmentservices;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.DepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceManagersTest {

  private final Long departmentId = 1337L;
  private final String departmentName = "Java Academy";
  @Mock private DepartmentRepository departmentRepository;
  @Mock private ObjectiveRepository objectiveRepository;
  @Mock private ActivityService activityService;
  @Mock private EntityCrawlerService entityCrawlerService;
  @Mock private User user;
  @InjectMocks private StructureServiceManagers departmentServiceManagers;
  private Department department;

  @Before
  public void setUp() {
    department = new Department();
    department.setId(departmentId);
    department.setName(departmentName);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfDepartment(any())).thenReturn(activeCycle);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createSubdepartment_expectedThrow() {
    departmentServiceManagers.createSubstructure(departmentId, new Department(), user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void removeDepartment_expectedThrow() {
    departmentServiceManagers.deleteStructure(departmentId, user);
  }

  @Test
  public void createObjective_expectsParentStructureIdIsSet() {
    Objective objective = new Objective();
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    when(departmentRepository.findByIdOrThrow(any(Long.class))).thenReturn(department);

    departmentServiceManagers.createObjective(departmentId, objective, user);

    Assert.assertNotNull(objective.getParentStructure().getId());
    Assert.assertEquals(departmentId, objective.getParentStructure().getId());
  }

  @Test
  public void createObjective_expectsOtherObjectiveSequenceAdvanced() {
    Objective objective = new Objective();
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    Objective otherObjective0 = new Objective();
    otherObjective0.setSequence(5);
    Objective otherObjective1 = new Objective();
    otherObjective1.setSequence(6);
    Objective otherObjective2 = new Objective();
    otherObjective2.setSequence(10);
    Department parentDepartment = new Department();

    Collection<Objective> otherObjectives =
        Arrays.asList(otherObjective0, otherObjective1, otherObjective2);
    parentDepartment.setObjectives(otherObjectives);
    when(departmentRepository.findByIdOrThrow(any(Long.class))).thenReturn(parentDepartment);

    departmentServiceManagers.createObjective(departmentId, objective, user);

    Assert.assertEquals(6, otherObjective0.getSequence());
    Assert.assertEquals(7, otherObjective1.getSequence());
    Assert.assertEquals(11, otherObjective2.getSequence());
  }

  @Test(expected = ForbiddenException.class)
  public void createObjective_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfDepartment(any())).thenReturn(closedCycle);

    departmentServiceManagers.createObjective(10L, new Objective(), user);
  }

  @Test(expected = ForbiddenException.class)
  public void updateDepartment_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfDepartment(any())).thenReturn(closedCycle);

    departmentServiceManagers.updateStructure(new Department(), user);
  }

  @Test
  public void updateDepartmentMemberList_expectedOkrMemberIdsAreChangedInSavedDepartment() {
    Department insertedDepartment = new Department();
    Collection<UUID> insertedOkrMemberIds =
        Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    insertedDepartment.setOkrMemberIds(insertedOkrMemberIds);
    insertedDepartment.setId(100L);
    insertedDepartment.setName("Test");

    Department originalDepartment = new Department();
    Collection<UUID> originalOkrMemberIds = new ArrayList<>();
    originalDepartment.setOkrMemberIds(originalOkrMemberIds);
    originalDepartment.setId(100L);
    originalDepartment.setName("Test");

    when(departmentRepository.findByIdOrThrow(100L)).thenReturn(originalDepartment);
    when(departmentRepository.save(any(Department.class))).then(returnsFirstArg());

    departmentServiceManagers.updateStructure(insertedDepartment, user);

    verify(departmentRepository).save(originalDepartment);
    Assert.assertEquals(originalDepartment.getOkrMemberIds(), insertedOkrMemberIds);
  }

  @Test
  public void updateDepartmentMemberList_expectedOtherVariablesUnchangedInSavedDepartment() {
    Department insertedDepartment = new Department();
    Collection<UUID> insertedOkrMemberIds =
        Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    insertedDepartment.setOkrMemberIds(insertedOkrMemberIds);
    insertedDepartment.setId(100L);
    insertedDepartment.setName("Insert");
    insertedDepartment.setOkrMasterId(UUID.randomUUID());
    insertedDepartment.setOkrTopicSponsorId(UUID.randomUUID());
    insertedDepartment.setDepartments(new ArrayList<>());
    Department insertedParentStructure = new Department();
    insertedParentStructure.setName("insertedParentStructure");
    insertedDepartment.setParentStructure(insertedParentStructure);
    insertedDepartment.setObjectives(new ArrayList<>());

    Department originalDepartment = new Department();
    Collection<UUID> originalOkrMemberIds = new ArrayList<>();
    originalDepartment.setOkrMemberIds(originalOkrMemberIds);
    originalDepartment.setId(100L);
    originalDepartment.setName("Original");
    originalDepartment.setOkrMasterId(UUID.randomUUID());
    originalDepartment.setOkrTopicSponsorId(UUID.randomUUID());
    originalDepartment.setDepartments(new ArrayList<>());
    Department originalParentStructure = new Department();
    originalParentStructure.setName("originalParentStructure");
    originalDepartment.setParentStructure(originalParentStructure);
    originalDepartment.setObjectives(new ArrayList<>());

    when(departmentRepository.findByIdOrThrow(100L)).thenReturn(originalDepartment);
    when(departmentRepository.save(any(Department.class))).then(returnsFirstArg());

    departmentServiceManagers.updateStructure(insertedDepartment, user);

    verify(departmentRepository).save(originalDepartment);
    Assert.assertNotEquals(originalDepartment.getName(), insertedDepartment.getName());
    Assert.assertNotEquals(
        originalDepartment.getOkrMasterId(), insertedDepartment.getOkrMasterId());
    Assert.assertNotEquals(
        originalDepartment.getOkrTopicSponsorId(), insertedDepartment.getOkrTopicSponsorId());
    Assert.assertNotSame(originalDepartment.getDepartments(), insertedDepartment.getDepartments());
    Assert.assertNotEquals(
        originalDepartment.getParentStructure(), insertedDepartment.getParentStructure());
    Assert.assertNotSame(originalDepartment.getObjectives(), insertedDepartment.getObjectives());
  }

  @Test
  public void updateDepartmentMemberList_expectedReturnsFromSaveCall() {
    Department insertedDepartment = new Department();
    Collection<UUID> insertedOkrMemberIds =
        Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    insertedDepartment.setOkrMemberIds(insertedOkrMemberIds);
    insertedDepartment.setId(100L);
    insertedDepartment.setName("Test");

    Department originalDepartment = new Department();
    Collection<UUID> originalOkrMemberIds = new ArrayList<>();
    originalDepartment.setOkrMemberIds(originalOkrMemberIds);
    originalDepartment.setId(100L);
    originalDepartment.setName("Test");

    Department saveCallReturnedDepartment = new Department();

    when(departmentRepository.findByIdOrThrow(100L)).thenReturn(originalDepartment);
    when(departmentRepository.save(any(Department.class))).thenReturn(saveCallReturnedDepartment);

    Department actualDepartment =
        departmentServiceManagers.updateStructure(insertedDepartment, user);

    verify(departmentRepository).save(originalDepartment);
    Assert.assertEquals(saveCallReturnedDepartment, actualDepartment);
  }
}
