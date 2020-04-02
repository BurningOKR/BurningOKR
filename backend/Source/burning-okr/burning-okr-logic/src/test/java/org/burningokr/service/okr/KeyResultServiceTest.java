package org.burningokr.service.okr;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.Unit;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultRepository;
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

@RunWith(MockitoJUnitRunner.class)
public class KeyResultServiceTest {

  @Mock private KeyResultRepository keyResultRepository;

  @Mock private EntityCrawlerService entityCrawlerService;

  @Mock private ActivityService activityService;

  @Mock ObjectiveService objectiveService;

  @Mock private User user;

  @InjectMocks private KeyResultService keyResultService;

  private final Long keyResultId = 1337L;
  private KeyResult keyResult;
  private KeyResult updateKeyResult;

  @Before
  public void setUp() {
    keyResult = new KeyResult();
    keyResult.setId(keyResultId);
    updateKeyResult = new KeyResult();

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(activeCycle);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteKeyResult_expectsEntityNotFoundExceptionIsThrown() {
    when(keyResultRepository.findByIdOrThrow(any(Long.class)))
        .thenThrow(new EntityNotFoundException());
    keyResultService.deleteKeyResult(keyResultId, user);
  }

  @Test(expected = ForbiddenException.class)
  public void deleteKeyResult_cycleOfKeyResultIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(closedCycle);

    keyResultService.deleteKeyResult(10L, user);
  }

  @Test
  public void deleteKeyResult_expectedOtherKeyResultsSequenceAdjusted() {
    Objective parentObjective = new Objective();
    KeyResult keyResultBelowInSequence = new KeyResult();
    KeyResult keyResultToDelete = new KeyResult();
    KeyResult keyResultAboveInSequence = new KeyResult();
    Collection<KeyResult> keyResultList =
        Arrays.asList(keyResultAboveInSequence, keyResultBelowInSequence, keyResultToDelete);
    parentObjective.setKeyResults(keyResultList);

    keyResultBelowInSequence.setSequence(5);
    keyResultToDelete.setSequence(6);
    keyResultAboveInSequence.setSequence(7);
    keyResultToDelete.setParentObjective(parentObjective);

    when(keyResultRepository.findByIdOrThrow(any(Long.class))).thenReturn(keyResultToDelete);

    keyResultService.deleteKeyResult(10L, user);

    Assert.assertEquals(5, keyResultBelowInSequence.getSequence());
    Assert.assertEquals(6, keyResultAboveInSequence.getSequence());
  }

  @Test(expected = ForbiddenException.class)
  public void updateKeyResult_cycleOfKeyResultIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfKeyResult(any())).thenReturn(closedCycle);
    when(keyResultRepository.findByIdOrThrow(any())).thenReturn(new KeyResult());

    KeyResult updatedKeyResult = new KeyResult();
    updatedKeyResult.setId(10L);
    keyResultService.updateKeyResult(updatedKeyResult, user);
  }

  @Test
  public void updateKeyResult_expectsNameIsUpdated() {
    String expectedName = "Test-Name";
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setName(expectedName);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult, user);

    Assert.assertEquals(expectedName, keyResult.getName());
  }

  @Test
  public void updateKeyResult_expectsDescriptionIsUpdated() {
    String expectedDescription = "Test-Description";
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setDescription(expectedDescription);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult, user);

    Assert.assertEquals(expectedDescription, keyResult.getDescription());
  }

  @Test
  public void updateKeyResult_expectsStartValueIsUpdated() {
    final long expectedStartValue = 1337L;
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setStartValue(1337);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult, user);

    Assert.assertEquals(expectedStartValue, keyResult.getStartValue());
  }

  @Test
  public void updateKeyResult_expectsCurrentValueIsUpdated() {
    final long expectedCurrentValue = 1337L;
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setCurrentValue(1337);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult, user);

    Assert.assertEquals(expectedCurrentValue, keyResult.getCurrentValue());
  }

  @Test
  public void updateKeyResult_expectsTargetValueIsUpdated() {
    final long expectedTargetValue = 1337L;
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setTargetValue(1337);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult, user);

    Assert.assertEquals(expectedTargetValue, keyResult.getTargetValue());
  }

  @Test
  public void updateKeyResult_expectsUnitIsUpdated() {
    Unit expectedUnit = Unit.EURO;
    updateKeyResult.setId(keyResultId);
    updateKeyResult.setUnit(expectedUnit);

    when(keyResultRepository.findByIdOrThrow(anyLong())).thenReturn(keyResult);
    when(keyResultRepository.save(any(KeyResult.class))).thenReturn(keyResult);

    keyResult = keyResultService.updateKeyResult(updateKeyResult, user);

    Assert.assertEquals(expectedUnit, keyResult.getUnit());
  }

  @Test(expected = Exception.class)
  public void updateSequence_NonEqualSizes_expectException() throws Exception {
    Objective objective = new Objective();
    objective.setId(42L);
    objective.setKeyResults(new ArrayList<>());
    Collection<Long> sequenceList = Collections.singletonList(1L);

    keyResultService.updateSequence(objective.getId(), sequenceList, user);
  }

  @Test(expected = Exception.class)
  public void updateSequence_WrongObjectiveIdsInSequence_expectException() throws Exception {
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

    keyResultService.updateSequence(objective.getId(), sequenceList, user);
  }

  @Test
  public void updateSequence_expectKeyResultsHaveNewSequence() throws Exception {
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

    keyResultService.updateSequence(objective.getId(), sequenceList, user);

    Assert.assertEquals(2, keyResult0.getSequence());
    Assert.assertEquals(1, keyResult1.getSequence());
    Assert.assertEquals(0, keyResult2.getSequence());
  }
}
