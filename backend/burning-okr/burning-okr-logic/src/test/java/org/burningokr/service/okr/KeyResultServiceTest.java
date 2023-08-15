package org.burningokr.service.okr;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.*;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.NoteKeyResultRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeyResultServiceTest {

  private final Long keyResultId = 1337L;
  @Mock
  private KeyResultRepository keyResultRepository;
  @Mock
  private NoteKeyResultRepository noteKeyResultRepository;
  @Mock
  private EntityCrawlerService entityCrawlerService;
  @Mock
  private ActivityService activityService;
  @Mock
  private ObjectiveService objectiveService;
  @Mock
  private KeyResultMilestoneService keyResultMilestoneService;
  @Mock
  private KeyResultHistoryService keyResultHistoryService;
  @Mock
  private AuthenticationUserContextService authenticationUserContextService;
  @Mock
  private OkrChildUnitService okrChildUnitService;
  @Mock
  private TaskService taskService;
  @InjectMocks
  private KeyResultService keyResultService;
  private KeyResult keyResult;
  private KeyResult updateKeyResult;
  private Cycle activeCycle;

  @BeforeEach
  public void setUp() {
    keyResult = new KeyResult();
    keyResult.setId(keyResultId);
    updateKeyResult = new KeyResult();

    activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
  }

  @Test
  public void deleteKeyResult_shouldThrowEntityNotFoundException() {
    when(keyResultRepository.findByIdOrThrow(any(Long.class))).thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> keyResultService.deleteKeyResult(keyResultId));
  }

  @Test
  public void deleteKeyResult_shouldThrowForbiddenExceptionWhenCycleOfKeyResultIsClosed() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(closedCycle);
    assertThrows(ForbiddenException.class, () -> keyResultService.deleteKeyResult(10L));
  }

  @Test
  public void deleteKeyResult_shouldAdjustSequenceOtherKeyResults() throws Exception {
    Objective parentObjective = new Objective();
    KeyResult keyResultBelowInSequence = new KeyResult();
    KeyResult keyResultToDelete = new KeyResult();
    KeyResult keyResultAboveInSequence = new KeyResult();
    Collection<KeyResult> keyResultList = Arrays.asList(
            keyResultAboveInSequence,
            keyResultBelowInSequence,
            keyResultToDelete
    );
    parentObjective.setKeyResults(keyResultList);

    keyResultBelowInSequence.setSequence(5);
    keyResultToDelete.setSequence(6);
    keyResultAboveInSequence.setSequence(7);
    keyResultToDelete.setParentObjective(parentObjective);
    keyResultToDelete.setId(10L);

    when(keyResultRepository.findByIdOrThrow(any(Long.class))).thenReturn(keyResultToDelete);
    when(taskService.findTasksForKeyResult(any(KeyResult.class))).thenReturn(new ArrayList<>());
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
    keyResultService.deleteKeyResult(10L);

    assertEquals(5, keyResultBelowInSequence.getSequence());
    assertEquals(6, keyResultAboveInSequence.getSequence());
  }

  @Test
  public void deleteKeyResult_shouldAccessRepoAndActivityService() throws Exception {
    Objective parentObjective = new Objective();
    KeyResult keyResultBelowInSequence = new KeyResult();
    KeyResult keyResultToDelete = new KeyResult();
    KeyResult keyResultAboveInSequence = new KeyResult();
    Collection<KeyResult> keyResultList = Arrays.asList(
            keyResultAboveInSequence,
            keyResultBelowInSequence,
            keyResultToDelete
    );
    parentObjective.setKeyResults(keyResultList);

    keyResultBelowInSequence.setSequence(5);
    keyResultToDelete.setSequence(6);
    keyResultAboveInSequence.setSequence(7);
    keyResultToDelete.setParentObjective(parentObjective);
    keyResultToDelete.setId(10L);

    when(keyResultRepository.findByIdOrThrow(any(Long.class))).thenReturn(keyResultToDelete);
    when(taskService.findTasksForKeyResult(any(KeyResult.class))).thenReturn(new ArrayList<>());
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
    keyResultService.deleteKeyResult(10L);

    verify(keyResultRepository).deleteById(keyResultToDelete.getId());
    verify(activityService).createActivity(keyResultToDelete, Action.DELETED);
  }

  @Test
  public void deleteKeyResult_shouldRemoveAssignedKeyResultOfTask() throws Exception {
    Objective parentObjective = new Objective();
    KeyResult keyResultBelowInSequence = new KeyResult();
    KeyResult keyResultToDelete = new KeyResult();
    KeyResult keyResultAboveInSequence = new KeyResult();
    Collection<KeyResult> keyResultList = Arrays.asList(
            keyResultAboveInSequence,
            keyResultBelowInSequence,
            keyResultToDelete
    );
    parentObjective.setKeyResults(keyResultList);

    keyResultBelowInSequence.setSequence(5);
    keyResultToDelete.setSequence(6);
    keyResultAboveInSequence.setSequence(7);
    keyResultToDelete.setParentObjective(parentObjective);

    ArrayList<Task> tasks = new ArrayList<>();
    Task task1 = new Task();
    task1.setId(20L);
    Task task2 = new Task();
    task2.setId(30L);
    tasks.add(task1);
    tasks.add(task2);

    when(keyResultRepository.findByIdOrThrow(any(Long.class))).thenReturn(keyResultToDelete);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
    when(taskService.findTasksForKeyResult(any(KeyResult.class))).thenReturn(tasks);
    keyResultService.deleteKeyResult(10L);

    verify(taskService, times(1)).updateTask(task1);
    verify(taskService, times(1)).updateTask(task2);
    assertNull(task1.getAssignedKeyResult());
    assertNull(task2.getAssignedKeyResult());
  }

  @Test
  public void updateKeyResult_shouldThrowForbiddenExceptionWhenCycleOfKeyResultIsClosed() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(closedCycle);
    when(keyResultRepository.findByIdOrThrow(any())).thenReturn(new KeyResult());

    KeyResult updatedKeyResult = new KeyResult();
    updatedKeyResult.setId(10L);
    assertThrows(ForbiddenException.class, () -> keyResultService.updateKeyResult(updatedKeyResult));
  }

  @Test
  public void updateKeyResult_shouldUpdateName() {
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
    String expectedName = "Test-Name";
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setName(expectedName);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(keyResultMilestoneService.updateMilestones(eq(updateKeyResult))).thenReturn(updateKeyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult);

    assertEquals(expectedName, keyResult.getName());
  }

  @Test
  public void updateKeyResult_shouldUpdateDescription() {
    String expectedDescription = "Test-Description";
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setDescription(expectedDescription);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
    when(keyResultMilestoneService.updateMilestones(eq(updateKeyResult))).thenReturn(updateKeyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult);

    assertEquals(expectedDescription, keyResult.getDescription());
  }

  @Test
  public void updateKeyResult_shouldUpdateStartValue() {
    final long expectedStartValue = 1337L;
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setStartValue(1337);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
    when(keyResultMilestoneService.updateMilestones(eq(updateKeyResult))).thenReturn(updateKeyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult);

    assertEquals(expectedStartValue, keyResult.getStartValue());
  }

  @Test
  public void updateKeyResult_shouldUpdateCurrentValue() {
    final long expectedCurrentValue = 1337L;
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setCurrentValue(1337);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
    when(keyResultMilestoneService.updateMilestones(eq(updateKeyResult))).thenReturn(updateKeyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult);

    assertEquals(expectedCurrentValue, keyResult.getCurrentValue());
  }

  @Test
  public void updateKeyResult_shouldUpdateTargetValue() {
    final long expectedTargetValue = 1337L;
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setTargetValue(1337);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
    when(keyResultMilestoneService.updateMilestones(eq(updateKeyResult))).thenReturn(updateKeyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult);

    assertEquals(expectedTargetValue, keyResult.getTargetValue());
  }

  @Test
  public void updateKeyResult_shouldUpdateUnit() {
    Unit expectedUnit = Unit.EURO;
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setUnit(expectedUnit);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
    when(keyResultMilestoneService.updateMilestones(eq(updateKeyResult))).thenReturn(updateKeyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult);

    assertEquals(expectedUnit, keyResult.getUnit());
  }

  @Test
  public void updateKeyResult_shouldUpdateMilestones() {
    List<KeyResultMilestone> milestones = new ArrayList<>();
    milestones.add(new KeyResultMilestone());
    milestones.add(new KeyResultMilestone());
    milestones.add(new KeyResultMilestone());

    updateKeyResult.setId(keyResultId);
    updateKeyResult.setMilestones(milestones);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
    when(keyResultMilestoneService.updateMilestones(eq(updateKeyResult))).thenReturn(updateKeyResult);

    keyResultService.updateKeyResult(updateKeyResult);

    verify(keyResultMilestoneService).updateMilestones(any());
  }

  @Test
  public void updateSequence_shouldThrowExceptionWhenSequenceAreNonEqualSizes() {
    Objective objective = new Objective();
    objective.setId(42L);
    objective.setKeyResults(new ArrayList<>());
    Collection<Long> sequenceList = Collections.singletonList(1L);
    assertThrows(Exception.class, () -> keyResultService.updateSequence(objective.getId(), sequenceList));
  }

  @Test
  public void updateSequence_shouldThrowExceptionWhenWrongObjectiveIdsAreInSequence() {
    Objective objective = new Objective();
    objective.setId(40L);
    KeyResult keyResult0 = new KeyResult();
    keyResult0.setSequence(0);
    keyResult0.setId(10L);
    KeyResult keyResult1 = new KeyResult();
    keyResult1.setSequence(1);
    keyResult1.setId(20L);
    KeyResult keyResult2 = new KeyResult();
    keyResult2.setSequence(2);
    keyResult2.setId(30L);
    objective.setKeyResults(Arrays.asList(keyResult0, keyResult1, keyResult2));

    Collection<Long> sequenceList = Arrays.asList(30L, 20L, 40L);
    assertThrows(Exception.class, () -> keyResultService.updateSequence(objective.getId(), sequenceList));
  }

  @Test
  public void updateSequence_shouldCreateNewSequenceForKeyResults() throws Exception {
    Objective objective = new Objective();
    objective.setId(40L);
    KeyResult keyResult0 = new KeyResult();
    keyResult0.setSequence(0);
    keyResult0.setId(10L);
    KeyResult keyResult1 = new KeyResult();
    keyResult1.setSequence(1);
    keyResult1.setId(20L);
    KeyResult keyResult2 = new KeyResult();
    keyResult2.setSequence(2);
    keyResult2.setId(30L);
    objective.setKeyResults(Arrays.asList(keyResult0, keyResult1, keyResult2));

    Collection<Long> sequenceList = Arrays.asList(30L, 20L, 10L);

    when(objectiveService.findById(objective.getId())).thenReturn(objective);

    keyResultService.updateSequence(objective.getId(), sequenceList);

    assertEquals(2, keyResult0.getSequence());
    assertEquals(1, keyResult1.getSequence());
    assertEquals(0, keyResult2.getSequence());
  }

  @Test
  public void findNotesOfKeyResult_shouldReturnOneNoteForOneKeyResult() {
    KeyResult keyResult0 = new KeyResult();
    keyResult0.setSequence(0);
    keyResult0.setId(10L);
    ArrayList<NoteKeyResult> notes = new ArrayList<>();
    NoteKeyResult note = new NoteKeyResult();
    notes.add(note);
    keyResult0.setNotes(notes);

    when(keyResultRepository.findByIdOrThrow(keyResult0.getId())).thenReturn(keyResult0);

    Collection<NoteKeyResult> result = keyResultService.findNotesOfKeyResult(keyResult0.getId());

    assertNotNull(result);
    assertEquals(1, result.size());
    assertSame(note, result.toArray(new NoteKeyResult[0])[0]);
  }

  @Test
  public void findNotesOfKeyResult_shouldReturnMultipleNotesForOneKeyResult() {
    KeyResult keyResult0 = new KeyResult();
    keyResult0.setSequence(0);
    keyResult0.setId(10L);
    ArrayList<NoteKeyResult> notes = new ArrayList<>();
    NoteKeyResult note = new NoteKeyResult();
    NoteKeyResult note2 = new NoteKeyResult();
    NoteKeyResult note3 = new NoteKeyResult();
    notes.add(note);
    notes.add(note2);
    notes.add(note3);
    keyResult0.setNotes(notes);

    when(keyResultRepository.findByIdOrThrow(keyResult0.getId())).thenReturn(keyResult0);

    Collection<NoteKeyResult> result = keyResultService.findNotesOfKeyResult(keyResult0.getId());

    assertNotNull(result);
    assertEquals(3, result.size());
  }

  @Test
  public void createNote_shouldSaveOneNoteAndAddCreateActivity() {
    NoteKeyResult noteKeyResult = new NoteKeyResult();
    noteKeyResult.setId(10L);
    UUID userId = new UUID(8, 10);
    noteKeyResult.setUserId(userId);
    LocalDateTime date = LocalDateTime.of(2023, 7, 23, 10, 10);
    noteKeyResult.setDate(date);
    noteKeyResult.setText("My note!");
    User myUser = new User();
    UUID myUserId = new UUID(8, 10);
    myUser.setId(myUserId);
    when(authenticationUserContextService.getAuthenticatedUser()).thenReturn(myUser);
    when(noteKeyResultRepository.save(noteKeyResult)).thenReturn(noteKeyResult);

    keyResultService.createNote(noteKeyResult);

    verify(noteKeyResultRepository).save(noteKeyResult);
    verify(activityService).createActivity(noteKeyResult, Action.CREATED);
    assertNull(noteKeyResult.getId());
    assertSame(myUserId, noteKeyResult.getUserId());
    assertNotEquals(date.format(DateTimeFormatter.ISO_DATE), noteKeyResult.getDate().format(DateTimeFormatter.ISO_DATE));
  }

  @Test
  public void updateNote_shouldUpdateOneNote() {
    NoteKeyResult noteKeyResult = new NoteKeyResult();
    noteKeyResult.setId(10L);
    UUID userId = new UUID(8, 10);
    noteKeyResult.setUserId(userId);
    LocalDateTime date = LocalDateTime.of(2023, 7, 23, 10, 10);
    noteKeyResult.setDate(date);
    noteKeyResult.setText("My note!");
    NoteKeyResult noteKeyResultUpdated = new NoteKeyResult();
    noteKeyResultUpdated.setId(20L);
    when(noteKeyResultRepository.save(noteKeyResult)).thenReturn(noteKeyResultUpdated);
    when(noteKeyResultRepository.findByIdOrThrow(10L)).thenReturn(noteKeyResultUpdated);

    NoteKeyResult result = keyResultService.updateNote(noteKeyResult);

    verify(noteKeyResultRepository).save(noteKeyResultUpdated);
    assertSame(result, noteKeyResultUpdated);
    assertEquals(20L, result.getId());
    assertEquals(noteKeyResult.getName(), result.getName());
    assertEquals(noteKeyResult.getDate(), result.getDate());
    assertEquals(noteKeyResult.getUserId(), result.getUserId());
  }

  @Test
  public void findKeyResultsOfUnit_shouldGetKeyResultsOfObjective() {
    KeyResult result1 = new KeyResult();
    KeyResult result2 = new KeyResult();
    Objective objective = new Objective();
    ArrayList<KeyResult> keyResults = new ArrayList<>();
    keyResults.add(result1);
    keyResults.add(result2);
    objective.setKeyResults(keyResults);
    ArrayList<Objective> objectives = new ArrayList<>();
    objectives.add(objective);
    OkrChildUnit okrChildUnit = new OkrDepartment();
    okrChildUnit.setObjectives(objectives);
    when(okrChildUnitService.findById(1L)).thenReturn(okrChildUnit);

    Collection<KeyResult> result = keyResultService.findKeyResultsOfUnit(1L);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertSame(result1, result.toArray(new KeyResult[2])[0]);
  }
}
