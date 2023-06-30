package org.burningokr.service.okr;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.KeyResultMilestone;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.Unit;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KeyResultServiceTest {

    private final Long keyResultId = 1337L;
    @Mock
    private KeyResultRepository keyResultRepository;
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
    private TaskService taskService;
    @InjectMocks
    private KeyResultService keyResultService;
    private KeyResult keyResult;
    private KeyResult updateKeyResult;

    @BeforeEach
    public void setUp() {
        keyResult = new KeyResult();
        keyResult.setId(keyResultId);
        updateKeyResult = new KeyResult();

        Cycle activeCycle = new Cycle();
        activeCycle.setCycleState(CycleState.ACTIVE);
        Mockito.lenient().when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
        Mockito.lenient().when(keyResultMilestoneService.updateMilestones(eq(updateKeyResult))).thenReturn(updateKeyResult);
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

        when(keyResultRepository.findByIdOrThrow(any(Long.class))).thenReturn(keyResultToDelete);
        when(taskService.findTasksForKeyResult(any(KeyResult.class))).thenReturn(new ArrayList<>());
        keyResultService.deleteKeyResult(10L);

        assertEquals(5, keyResultBelowInSequence.getSequence());
        assertEquals(6, keyResultAboveInSequence.getSequence());
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
        String expectedName = "Test-Name";
        updateKeyResult.setId(keyResultId);
        updateKeyResult.setName(expectedName);

        when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
        when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);

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
}
