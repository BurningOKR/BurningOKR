package org.burningokr.service.okr;

import org.burningokr.model.activity.Action;
import org.burningokr.model.configuration.Configuration;
import org.burningokr.model.configuration.ConfigurationName;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.KeyResultMilestone;
import org.burningokr.model.okr.NoteObjective;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.NoteObjectiveRepository;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.configuration.ConfigurationService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.exceptions.KeyResultOverflowException;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.okrUnitUtil.ParentService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObjectiveServiceTest {
  @Mock
  private ParentService parentService;
  @Mock
  private ObjectiveRepository objectiveRepository;
  @Mock
  private KeyResultRepository keyResultRepository;
  @Mock
  private KeyResultHistoryService keyResultHistoryService;
  @Mock
  private ActivityService activityService;
  @Mock
  private EntityCrawlerService entityCrawlerService;
  @Mock
  private ConfigurationService configurationService;
  @Mock
  private OkrChildUnitService<OkrChildUnit> departmentService;
  @Mock
  private KeyResultMilestoneService keyResultMilestoneService;
  @Mock
  private NoteObjectiveRepository noteObjectiveRepository;
  @Mock
  private AuthenticationUserContextService authenticationUserContextService;

  @InjectMocks
  private ObjectiveService objectiveService;

  private final Long objectiveId = 1337L;
  private Objective objective;
  private Objective updateObjective;
  private KeyResult keyResult;
  private NoteObjective noteObjective;
  private Configuration maxKeyResultsConfiguration;
  private Cycle activeCycle;

  @BeforeEach
  public void reset() {

    this.objective = new Objective();
    objective.setId(objectiveId);

    updateObjective = new Objective();
    updateObjective.setId(objectiveId);

    this.noteObjective = new NoteObjective();

    this.keyResult = new KeyResult();


    maxKeyResultsConfiguration = new Configuration();
    maxKeyResultsConfiguration.setName(ConfigurationName.MAX_KEY_RESULTS.getName());
    maxKeyResultsConfiguration.setValue("7");


    activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);

  }

  @Test
  public void createKeyResult_shouldCreateKeyResult() throws KeyResultOverflowException {
    when(objectiveRepository.findByIdOrThrow(any(Long.class))).thenReturn(objective);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(configurationService.getConfigurationByName(ConfigurationName.MAX_KEY_RESULTS.getName()))
            .thenReturn(maxKeyResultsConfiguration);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);
    keyResult.setId(12L);

    objectiveService.createKeyResult(10L, keyResult);

    verifyCreateKeyResult();
  }

  @Test
  public void createKeyResult_shouldSetParentId() throws KeyResultOverflowException {
    when(objectiveRepository.findByIdOrThrow(any(Long.class))).thenReturn(objective);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(configurationService.getConfigurationByName(ConfigurationName.MAX_KEY_RESULTS.getName()))
            .thenReturn(maxKeyResultsConfiguration);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);
    Long expected = 18L;
    objective.setId(expected);

    keyResult.setId(12L);

    objectiveService.createKeyResult(expected, keyResult);

    assertEquals(expected, keyResult.getParentObjective().getId());

    verifyCreateKeyResult();
  }

  @Test
  public void createKeyResult_shouldGetSequenceOfOtherObjectives()
    throws KeyResultOverflowException {
    when(objectiveRepository.findByIdOrThrow(any(Long.class))).thenReturn(objective);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(configurationService.getConfigurationByName(ConfigurationName.MAX_KEY_RESULTS.getName()))
            .thenReturn(maxKeyResultsConfiguration);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    Long expected = 18L;
    objective.setId(expected);

    keyResult.setId(12L);

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

    objectiveService.createKeyResult(expected, keyResult);

    assertEquals(5, otherKeyResult1.getSequence());
    assertEquals(6, otherKeyResult2.getSequence());
    assertEquals(10, otherKeyResult3.getSequence());
  }

  @Test()
  public void createKeyResult_shouldThrowForbiddenExceptionWhenCycleOfObjectiveIsClosed() {
    when(objectiveRepository.findByIdOrThrow(any(Long.class))).thenReturn(objective);
    when(configurationService.getConfigurationByName(ConfigurationName.MAX_KEY_RESULTS.getName()))
            .thenReturn(maxKeyResultsConfiguration);
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(closedCycle);

    assertThrows(ForbiddenException.class, () -> objectiveService.createKeyResult(10L, new KeyResult()));

  }

  @Test()
  public void
  createKeyResult_shouldThrowKeyResultOverflowExceptionBecauseOfMaximumNumberOfKeyResultsReached() {
    when(objectiveRepository.findByIdOrThrow(any(Long.class))).thenReturn(objective);
    when(configurationService.getConfigurationByName(ConfigurationName.MAX_KEY_RESULTS.getName()))
            .thenReturn(maxKeyResultsConfiguration);
    int maxKeyResultsPerObjective = 7;

    for (int i = 0; i < maxKeyResultsPerObjective; i++) {
      objective.getKeyResults().add(new KeyResult());
    }

    KeyResult keyResult = new KeyResult();

    assertThrows(KeyResultOverflowException.class, () -> objectiveService.createKeyResult(anyLong(), keyResult));

    verify(objectiveRepository).findByIdOrThrow(anyLong());
  }

  @Test
  public void createKeyResult_shouldSaveAllKeyResultMilestones() throws KeyResultOverflowException {
    when(objectiveRepository.findByIdOrThrow(any(Long.class))).thenReturn(objective);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(configurationService.getConfigurationByName(ConfigurationName.MAX_KEY_RESULTS.getName()))
            .thenReturn(maxKeyResultsConfiguration);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);
    List<KeyResultMilestone> milestoneList = new ArrayList<>();
    milestoneList.add(new KeyResultMilestone());
    milestoneList.add(new KeyResultMilestone());
    milestoneList.add(new KeyResultMilestone());

    keyResult.setMilestones(milestoneList);
    keyResult.setId(12L);

    objectiveService.createKeyResult(anyLong(), keyResult);

    verifyCreateKeyResult();
    verify(keyResultMilestoneService, times(3)).createKeyResultMilestone(anyLong(), any());
  }

  @Test
  public void createKeyResult_shouldNotSaveMilestonesWhenThereAreNoMilestones()
    throws KeyResultOverflowException {
    when(objectiveRepository.findByIdOrThrow(any(Long.class))).thenReturn(objective);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(configurationService.getConfigurationByName(ConfigurationName.MAX_KEY_RESULTS.getName()))
            .thenReturn(maxKeyResultsConfiguration);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    List<KeyResultMilestone> milestoneList = new ArrayList<>();
    keyResult.setMilestones(milestoneList);
    keyResult.setId(12L);

    objectiveService.createKeyResult(anyLong(), keyResult);

    verifyCreateKeyResult();
    verify(keyResultMilestoneService, never()).createKeyResultMilestone(anyLong(), any());
  }

  @Test
  public void updateObjective_shouldUpdateName() {
    String expectedTestName = "test";
    updateObjective.setName(expectedTestName);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    objective = objectiveService.updateObjective(updateObjective);

    assertEquals(expectedTestName, objective.getName());
  }

  @Test
  public void updateObjective_shouldUpdateDescription() {
    String expectedDescription = "test";
    updateObjective.setDescription(expectedDescription);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    objective = objectiveService.updateObjective(updateObjective);

    assertEquals(expectedDescription, objective.getDescription());
  }

  @Test
  public void updateObjetive_shouldUpdateRemark() {
    String expectedRemark = "test";
    updateObjective.setRemark(expectedRemark);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    objective = objectiveService.updateObjective(updateObjective);

    assertEquals(expectedRemark, objective.getRemark());
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

    objective = objectiveService.updateObjective(updateObjective);

    assertEquals(expectedTestReview, objective.getReview());
  }

  @Test
  public void updateObjective_cycleOfObjectiveIsClosed_expectedOtherVariablesUnchanged() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(closedCycle);

    updateObjective.setParentOkrUnit(new OkrCompany());
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

    objective = objectiveService.updateObjective(updateObjective);

    assertNull(objective.getParentOkrUnit());
    assertNull(objective.getParentObjective());
    assertFalse(objective.isActive());
    assertNull(objective.getContactPersonId());
    assertNull(objective.getDescription());
    assertNotSame(objective.getKeyResults(), keyresultList);
    assertNull(objective.getName());
    assertNull(objective.getRemark());
    assertNotSame(objective.getSubObjectives(), objectiveList);
  }

  @Test
  public void updateObjective_ExpectsContactPersonIdIsUpdated() {
    String expected = "124L";
    updateObjective.setContactPersonId(expected);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    objective = objectiveService.updateObjective(updateObjective);

    assertEquals(expected, objective.getContactPersonId());
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
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    objective = objectiveService.updateObjective(updateObjective);

    assertEquals(newParentObjective, objective.getParentObjective());
  }

  @Test
  public void updateObjective_ExpectedIsActiveUpdate() {
    boolean oldIsActive = false;
    boolean newIsActive = true;
    updateObjective.setActive(newIsActive);
    objective.setActive(oldIsActive);

    when(objectiveRepository.findByIdOrThrow(anyLong())).thenReturn(objective);
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    Objective actualObjective = objectiveService.updateObjective(updateObjective);

    assertEquals(newIsActive, actualObjective.isActive());
  }

  @Test
  public void test_deleteObjective_ExpectedObjectiveIsDeleted() {
    when(objectiveRepository.findByIdOrThrow(objectiveId)).thenReturn(objective);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    objectiveService.deleteObjectiveById(objectiveId);

    verify(objectiveRepository).deleteById(objectiveId);
  }

  @Test
  public void deleteObjective_expectedOtherObjectiveSequenceAdjusted() {
    OkrDepartment parentOkrDepartment = new OkrDepartment();
    Objective objectiveBelowInSequence = new Objective();
    objectiveBelowInSequence.setSequence(5);
    Objective objectiveToDelete = new Objective();
    objectiveToDelete.setParentOkrUnit(parentOkrDepartment);
    objectiveToDelete.setSequence(6);
    Objective objectiveAboveInSequence = new Objective();
    objectiveAboveInSequence.setSequence(7);

    Collection<Objective> otherObjectives =
      Arrays.asList(objectiveAboveInSequence, objectiveBelowInSequence, objectiveToDelete);
    parentOkrDepartment.setObjectives(otherObjectives);

    when(objectiveRepository.findByIdOrThrow(objectiveId)).thenReturn(objectiveToDelete);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    objectiveService.deleteObjectiveById(objectiveId);

    assertEquals(5, objectiveBelowInSequence.getSequence());
    assertEquals(6, objectiveAboveInSequence.getSequence());
  }

  @Test
  public void test_deleteObjective_ExpectSubObjectivesToSetParentObjectiveGotUpdated() {
    Objective parentObjective = new Objective();
    Long parentObjectiveId = 4231L;
    parentObjective.setId(parentObjectiveId);
    this.objective.setSubObjectives(Collections.singletonList(parentObjective));
    parentObjective.setParentObjective(this.objective);

    when(objectiveRepository.findByIdOrThrow(objectiveId)).thenReturn(objective);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    objectiveService.deleteObjectiveById(objectiveId);

    verify(objectiveRepository).save(parentObjective);
  }

  @Test
  public void test_deleteObjective_ExpectedActivityGotCreated() {
    when(objectiveRepository.findByIdOrThrow(objectiveId)).thenReturn(objective);
    when(entityCrawlerService.getCycleOfObjective(any())).thenReturn(activeCycle);

    objectiveService.deleteObjectiveById(objectiveId);

    verify(activityService).createActivity(this.objective, Action.DELETED);
  }

  @Test
  public void updateSequence_NonEqualSizes_expectException() {
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setId(42L);
    okrDepartment.setObjectives(new ArrayList<>());
    Collection<Long> sequenceList = Collections.singletonList(1L);

    when(departmentService.findById(okrDepartment.getId())).thenReturn(okrDepartment);

    assertThrows(Exception.class, () -> objectiveService.updateSequence(okrDepartment.getId(), sequenceList));
  }

  @Test
  public void updateSequence_WrongObjectiveIdsInSequence_expectException() {
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setId(50L);
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

    when(departmentService.findById(okrDepartment.getId())).thenReturn(okrDepartment);

    assertThrows(Exception.class, () -> objectiveService.updateSequence(okrDepartment.getId(), sequenceList));
  }

  @Test
  public void updateSequence_expectObjectivesHaveNewSequence() throws Exception {
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setId(50L);
    Objective objective0 = new Objective();
    objective0.setId(10L);
    objective0.setSequence(0);
    Objective objective1 = new Objective();
    objective1.setId(20L);
    objective1.setSequence(1);
    Objective objective2 = new Objective();
    objective2.setId(30L);
    objective2.setSequence(2);

    okrDepartment.setObjectives(Arrays.asList(objective0, objective1, objective2));

    Collection<Long> sequenceList = Arrays.asList(20L, 30L, 10L);

    when(departmentService.findById(okrDepartment.getId())).thenReturn(okrDepartment);

    objectiveService.updateSequence(okrDepartment.getId(), sequenceList);

    assertEquals(2, objective0.getSequence());
    assertEquals(0, objective1.getSequence());
    assertEquals(1, objective2.getSequence());
  }

  @Test
  public void createNote_expect_saveIsCalled() {
    when(authenticationUserContextService.getAuthenticatedUser()).thenReturn(new User());
    when(noteObjectiveRepository.save(any())).thenReturn(noteObjective);
    objectiveService.createNote(noteObjective);
    verify(noteObjectiveRepository).save(same(noteObjective));
  }

  @Test
  public void createNote_expect_createActivityIsCalled() {
    when(authenticationUserContextService.getAuthenticatedUser()).thenReturn(new User());
    when(noteObjectiveRepository.save(any())).thenReturn(noteObjective);
    objectiveService.createNote(noteObjective);
    verify(activityService).createActivity(same(noteObjective), any());
  }

  @Test
  public void createNote_expect_userIdIsSet() {
    NoteObjective noteObjectiveMock = mock(NoteObjective.class);
    UUID expected = new UUID(1337L, 1337L);
    User authUser = new User();
    authUser.setId(expected);
    when(authenticationUserContextService.getAuthenticatedUser()).thenReturn(authUser);

    when(noteObjectiveRepository.save(any())).thenReturn(noteObjectiveMock);

    objectiveService.createNote(noteObjectiveMock);
    verify(noteObjectiveMock).setUserId(eq(expected));
  }

  @Test
  public void createNot_expect_dateIsCalled() {
    NoteObjective noteObjectiveMock = mock(NoteObjective.class);
    when(authenticationUserContextService.getAuthenticatedUser()).thenReturn(new User());
    when(noteObjectiveRepository.save(any())).thenReturn(noteObjectiveMock);
    objectiveService.createNote(noteObjectiveMock);
    verify(noteObjectiveMock).setDate(any());
  }

  @Test
  public void updateNote_expect_userIdIsSet() {
    NoteObjective noteObjectiveMock = mock(NoteObjective.class);
    UUID expected = new UUID(1337L, 1337L);

    NoteObjective updatedNoteObjective = new NoteObjective();
    updatedNoteObjective.setUserId(expected);

    when(noteObjectiveRepository.findByIdOrThrow(any())).thenReturn(noteObjectiveMock);
    when(noteObjectiveRepository.save(any())).thenReturn(noteObjectiveMock);

    objectiveService.updateNote(updatedNoteObjective);
    verify(noteObjectiveMock).setUserId(same(expected));
  }

  @Test
  public void updateNote_expect_textIsSet() {
    NoteObjective noteObjectiveMock = mock(NoteObjective.class);
    String expected = "test";

    NoteObjective updatedNoteObjective = new NoteObjective();
    updatedNoteObjective.setText(expected);

    when(noteObjectiveRepository.findByIdOrThrow(any())).thenReturn(noteObjectiveMock);
    when(noteObjectiveRepository.save(any())).thenReturn(noteObjectiveMock);

    objectiveService.updateNote(updatedNoteObjective);
    verify(noteObjectiveMock).setText(same(expected));
  }

  @Test
  public void updateNote_expect_dateIsSet() {
    NoteObjective noteObjectiveMock = mock(NoteObjective.class);
    LocalDateTime expected = LocalDateTime.now();

    NoteObjective updatedNoteObjective = new NoteObjective();
    updatedNoteObjective.setDate(expected);

    when(noteObjectiveRepository.findByIdOrThrow(any())).thenReturn(noteObjectiveMock);
    when(noteObjectiveRepository.save(any())).thenReturn(noteObjectiveMock);

    objectiveService.updateNote(updatedNoteObjective);
    verify(noteObjectiveMock).setDate(same(expected));
  }

  @Test
  public void updateNote_expect_parentObjectiveIsSet() {
    NoteObjective noteObjectiveMock = mock(NoteObjective.class);
    Objective expected = new Objective();

    NoteObjective updatedNoteObjective = new NoteObjective();
    updatedNoteObjective.setParentObjective(expected);

    when(noteObjectiveRepository.findByIdOrThrow(any())).thenReturn(noteObjectiveMock);
    when(noteObjectiveRepository.save(any())).thenReturn(noteObjectiveMock);

    objectiveService.updateNote(updatedNoteObjective);
    verify(noteObjectiveMock).setParentObjective(same(expected));
  }

  @Test
  public void updateNote_expect_saveIsCalled() {

    when(noteObjectiveRepository.findByIdOrThrow(any())).thenReturn(noteObjective);
    when(noteObjectiveRepository.save(any())).thenReturn(noteObjective);

    objectiveService.updateNote(noteObjective);
    verify(noteObjectiveRepository).save(any());
  }

  @Test
  public void updateNote_expect_findIsCalled() {

    when(noteObjectiveRepository.findByIdOrThrow(any())).thenReturn(noteObjective);
    when(noteObjectiveRepository.save(any())).thenReturn(noteObjective);

    objectiveService.updateNote(noteObjective);
    verify(noteObjectiveRepository).findByIdOrThrow(any());
  }

  // region Hilfsfunktionen
  private void verifyCreateKeyResult() {
    verify(objectiveRepository).findByIdOrThrow(anyLong());
    verify(keyResultRepository).save(any(KeyResult.class));
  }

  // endregion
}
