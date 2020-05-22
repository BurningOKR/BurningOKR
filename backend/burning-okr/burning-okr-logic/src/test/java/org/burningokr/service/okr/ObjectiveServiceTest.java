package org.burningokr.service.okr;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.burningokr.model.activity.Action;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.model.configuration.ConfigurationName;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.configuration.ConfigurationService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.exceptions.KeyResultOverflowException;
import org.burningokr.service.structure.departmentservices.StructureServiceUsers;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.burningokr.service.structureutil.ParentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ObjectiveServiceTest {

  @Mock private ObjectiveRepository objectiveRepository;

  @Mock private KeyResultRepository keyResultRepository;

  @Mock private ActivityService activityService;

  @Mock private EntityCrawlerService entityCrawlerService;

  @Mock private ParentService parentService;

  @Mock private StructureServiceUsers departmentService;

  @Mock private User user;

  @Mock ConfigurationService configurationService;

  @InjectMocks private ObjectiveService objectiveService;

  private Long objectiveId = 1337L;
  private Objective objective;
  private Objective updateObjective;
  private KeyResult keyResult;

  @Before
  public void reset() throws NoSuchFieldException, IllegalAccessException {
    this.objective = new Objective();
    objective.setId(objectiveId);
    updateObjective = new Objective();
    updateObjective.setId(objectiveId);
    this.keyResult = new KeyResult();
    when(objectiveRepository.findByIdOrThrow(any(Long.class))).thenReturn(objective);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    Configuration maxKeyResultsConfiguration = new Configuration();
    maxKeyResultsConfiguration.setName(ConfigurationName.MAX_KEY_RESULTS.getName());
    maxKeyResultsConfiguration.setValue("7");
    when(configurationService.getConfigurationByName(ConfigurationName.MAX_KEY_RESULTS.getName()))
        .thenReturn(maxKeyResultsConfiguration);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);
  }

  @Test
  public void createKeyResult_expectsParentIdIsSet() throws KeyResultOverflowException {
    Long expected = 18L;
    objective.setId(expected);

    objectiveService.createKeyResult(expected, keyResult, user);

    Assert.assertEquals(expected, keyResult.getParentObjective().getId());

    veryfyCreateKeyResult();
  }

  @Test
  public void createKeyResult_expectedOtherObjectivesSequenceAdvanced()
      throws KeyResultOverflowException {
    Long expected = 18L;
    objective.setId(expected);

    KeyResult otherKeyResult1 = new KeyResult();
    otherKeyResult1.setSequence(5);
    KeyResult otherKeyResult2 = new KeyResult();
    otherKeyResult2.setSequence(6);
    KeyResult otherKeyResult3 = new KeyResult();
    otherKeyResult3.setSequence(10);
    Collection<KeyResult> otherKeyResults =
        Arrays.asList(otherKeyResult1, otherKeyResult2, otherKeyResult3);

    Objective parentObjective = new Objective();
    parentObjective.setKeyResults(otherKeyResults);

    when(objectiveRepository.findByIdOrThrow(any(Long.class))).thenReturn(parentObjective);

    objectiveService.createKeyResult(expected, keyResult, user);

    Assert.assertEquals(6, otherKeyResult1.getSequence());
    Assert.assertEquals(7, otherKeyResult2.getSequence());
    Assert.assertEquals(11, otherKeyResult3.getSequence());
  }

  @Test(expected = ForbiddenException.class)
  public void createKeyResult_cycleOfObjectiveIsClosed_expectedForbiddenThrow()
      throws KeyResultOverflowException {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(closedCycle);

    objectiveService.createKeyResult(10L, new KeyResult(), user);
  }

  @Test(expected = KeyResultOverflowException.class)
  public void
      createKeyResult_expectsKeyResultOverflowExceptionBecauseOfMAximumNumberOfKeyResultsReached()
          throws KeyResultOverflowException {

    int maxKeyResultsPerObjective = 7;

    for (int i = 0; i < maxKeyResultsPerObjective; i++) {
      objective.getKeyResults().add(new KeyResult());
    }

    KeyResult keyResult = new KeyResult();

    objectiveService.createKeyResult(anyLong(), keyResult, user);

    veryfyCreateKeyResult();
  }

  @Test
  public void updateObjective_ExpectsNameIsUpdated() {
    String expectedTestName = "test";
    updateObjective.setName(expectedTestName);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    objective = objectiveService.updateObjective(updateObjective, user);

    Assert.assertEquals(expectedTestName, objective.getName());
  }

  @Test
  public void updateObjective_ExpectsDescriptionIsUpdated() {
    String expectedDescription = "test";
    updateObjective.setDescription(expectedDescription);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    objective = objectiveService.updateObjective(updateObjective, user);

    Assert.assertEquals(expectedDescription, objective.getDescription());
  }

  @Test
  public void updateObjetive_ExpectsRemarkIsUpdated() {
    String expectedRemark = "test";
    updateObjective.setRemark(expectedRemark);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    objective = objectiveService.updateObjective(updateObjective, user);

    Assert.assertEquals(expectedRemark, objective.getRemark());
  }

  @Test
  public void updateObjective_cycleOfObjectiveIsClosed_ExpectsReviewIsUpdated() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(closedCycle);

    String expectedTestReview = "test";
    updateObjective.setReview(expectedTestReview);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    objective = objectiveService.updateObjective(updateObjective, user);

    Assert.assertEquals(expectedTestReview, objective.getReview());
  }

  @Test
  public void updateObjective_cycleOfObjectiveIsClosed_expectedOtherVariablesUnchanged() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(closedCycle);

    updateObjective.setParentStructure(new Company());
    updateObjective.setParentObjective(new Objective());
    updateObjective.setActive(true);
    updateObjective.setContactPersonId("blargh");
    updateObjective.setDescription("oof");
    ArrayList<KeyResult> keyresultList = new ArrayList<>();
    updateObjective.setKeyResults(keyresultList);
    updateObjective.setName("oof");
    updateObjective.setRemark("oof");
    ArrayList<Objective> objectiveList = new ArrayList<>();
    updateObjective.setSubObjectives(objectiveList);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    objective = objectiveService.updateObjective(updateObjective, user);

    Assert.assertNull(objective.getParentStructure());
    Assert.assertNull(objective.getParentObjective());
    Assert.assertFalse(objective.isActive());
    Assert.assertNull(objective.getContactPersonId());
    Assert.assertNull(objective.getDescription());
    Assert.assertNotSame(objective.getKeyResults(), keyresultList);
    Assert.assertNull(objective.getName());
    Assert.assertNull(objective.getRemark());
    Assert.assertNotSame(objective.getSubObjectives(), objectiveList);
  }

  @Test
  public void updateObjective_ExpectsContactPersonIdIsUpdated() {
    String expected = "124L";
    updateObjective.setContactPersonId(expected);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    objective = objectiveService.updateObjective(updateObjective, user);

    Assert.assertEquals(expected, objective.getContactPersonId());
  }

  @Test
  public void updateObjective_ExpectedParentObjectiveIsUpdated() {
    Long newParentObjectiveId = 400L;
    Objective newParentObjective = new Objective();
    newParentObjective.setId(newParentObjectiveId);
    updateObjective.setParentObjective(newParentObjective);

    when(objectiveRepository.findByIdOrThrow(newParentObjectiveId)).thenReturn(newParentObjective);
    when(objectiveRepository.findByIdOrThrow(objectiveId)).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    objective = objectiveService.updateObjective(updateObjective, user);

    Assert.assertEquals(newParentObjective, objective.getParentObjective());
  }

  @Test
  public void updateObjective_ExpectedIsActiveUpdate() {
    boolean oldIsActive = false;
    boolean newIsActive = true;
    updateObjective.setActive(newIsActive);
    objective.setActive(oldIsActive);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    Objective actualObjective = objectiveService.updateObjective(updateObjective, user);

    Assert.assertEquals(newIsActive, actualObjective.isActive());
  }

  @Test
  public void test_deleteObjective_ExpectedObjectiveIsDeleted() {
    when(objectiveRepository.findByIdOrThrow(objectiveId)).thenReturn(objective);

    objectiveService.deleteObjectiveById(objectiveId, user);

    verify(objectiveRepository).deleteById(objectiveId);
  }

  @Test
  public void deleteObjective_expectedOtherObjectiveSequenceAdjusted() {
    Department parentDepartment = new Department();
    Objective objectiveBelowInSequence = new Objective();
    objectiveBelowInSequence.setSequence(5);
    Objective objectiveToDelete = new Objective();
    objectiveToDelete.setParentStructure(parentDepartment);
    objectiveToDelete.setSequence(6);
    Objective objectiveAboveInSequence = new Objective();
    objectiveAboveInSequence.setSequence(7);

    Collection<Objective> otherObjectives =
        Arrays.asList(objectiveAboveInSequence, objectiveBelowInSequence, objectiveToDelete);
    parentDepartment.setObjectives(otherObjectives);

    when(objectiveRepository.findByIdOrThrow(objectiveId)).thenReturn(objectiveToDelete);

    objectiveService.deleteObjectiveById(objectiveId, user);

    Assert.assertEquals(5, objectiveBelowInSequence.getSequence());
    Assert.assertEquals(6, objectiveAboveInSequence.getSequence());
  }

  @Test
  public void test_deleteObjective_ExpectSubObjectivesToSetParentObjectiveGotUpdated() {
    Objective parentObjective = new Objective();
    Long parentObjectiveId = 4231L;
    parentObjective.setId(parentObjectiveId);
    this.objective.setSubObjectives(Collections.singletonList(parentObjective));
    parentObjective.setParentObjective(this.objective);

    when(objectiveRepository.findByIdOrThrow(objectiveId)).thenReturn(objective);

    objectiveService.deleteObjectiveById(objectiveId, user);

    verify(objectiveRepository).save(parentObjective);
  }

  @Test
  public void test_deleteObjective_ExpectedActivityGotCreated() {
    when(objectiveRepository.findByIdOrThrow(objectiveId)).thenReturn(objective);

    objectiveService.deleteObjectiveById(objectiveId, user);

    verify(activityService).createActivity(user, this.objective, Action.DELETED);
  }

  @Test(expected = Exception.class)
  public void updateSequence_NonEqualSizes_expectException() throws Exception {
    Department department = new Department();
    department.setId(42L);
    department.setObjectives(new ArrayList<>());
    Collection<Long> sequenceList = Collections.singletonList(1L);

    when(departmentService.findById(department.getId())).thenReturn(department);

    objectiveService.updateSequence(department.getId(), sequenceList, user);
  }

  @Test(expected = Exception.class)
  public void updateSequence_WrongObjectiveIdsInSequence_expectException() throws Exception {
    Department department = new Department();
    department.setId(50L);
    Objective objective0 = new Objective();
    objective0.setId(10L);
    objective0.setSequence(0);
    Objective objective1 = new Objective();
    objective1.setId(20L);
    objective1.setSequence(1);
    Objective objective2 = new Objective();
    objective2.setId(30L);
    objective2.setSequence(2);

    Collection<Long> sequenceList = Arrays.asList(20L, 30L, 40L);

    when(departmentService.findById(department.getId())).thenReturn(department);

    objectiveService.updateSequence(department.getId(), sequenceList, user);
  }

  @Test
  public void updateSequence_expectObjectivesHaveNewSequence() throws Exception {
    Department department = new Department();
    department.setId(50L);
    Objective objective0 = new Objective();
    objective0.setId(10L);
    objective0.setSequence(0);
    Objective objective1 = new Objective();
    objective1.setId(20L);
    objective1.setSequence(1);
    Objective objective2 = new Objective();
    objective2.setId(30L);
    objective2.setSequence(2);

    department.setObjectives(Arrays.asList(objective0, objective1, objective2));

    Collection<Long> sequenceList = Arrays.asList(20L, 30L, 10L);

    when(departmentService.findById(department.getId())).thenReturn(department);

    objectiveService.updateSequence(department.getId(), sequenceList, user);

    Assert.assertEquals(2, objective0.getSequence());
    Assert.assertEquals(0, objective1.getSequence());
    Assert.assertEquals(1, objective2.getSequence());
  }

  // region Hilfsfunktionen
  private void veryfyCreateKeyResult() {
    verify(objectiveRepository).findByIdOrThrow(anyLong());
    verify(keyResultRepository).save(any(KeyResult.class));
  }

  // endregion
}
