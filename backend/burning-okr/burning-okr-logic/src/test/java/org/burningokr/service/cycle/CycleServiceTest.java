package org.burningokr.service.cycle;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrCompanyHistory;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.cycle.CompanyHistoryRepository;
import org.burningokr.repositories.cycle.CycleRepository;
import org.burningokr.service.okrUnitUtil.CyclePreparationCloningService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CycleServiceTest {

  private final LocalDate plannedStartDate = LocalDate.now().minusMonths(1);
  private final LocalDate plannedEndDate = LocalDate.now().plusMonths(1);
  private final LocalDate factualStartDate = plannedStartDate.plusDays(1);
  private final LocalDate factualEndDate = plannedEndDate.plusDays(1);
  @Mock
  private CycleRepository cycleRepository;
  @Mock
  private CyclePreparationCloningService cyclePreparationCloningService;
  @Mock
  private CompanyHistoryRepository companyHistoryRepository;
  @Mock
  private IUser mockedIUser;
  @InjectMocks
  private CycleService cycleService;

  @Test
  public void getAllCycles_expectedNoCycles() {
    Collection<Cycle> expectedCycles = new ArrayList<>();
    when(cycleRepository.findAll()).thenReturn(new ArrayList<>());

    Collection<Cycle> actualCycles = cycleService.getAllCycles();

    assertEquals(expectedCycles, actualCycles);
  }

  @Test
  public void getAllCycles_expectedOneCycle() {
    Cycle testCycle1 = new Cycle("TestCycle1");

    Collection<Cycle> expectedCycles = new ArrayList<>();
    expectedCycles.add(testCycle1);

    Collection<Cycle> outputCycles = new ArrayList<>();
    outputCycles.add(testCycle1);

    when(cycleRepository.findAll()).thenReturn(outputCycles);

    Collection<Cycle> actualCycles = cycleService.getAllCycles();

    assertEquals(expectedCycles, actualCycles);
  }

  @Test
  public void getAllCycles_expectedMultipleCycles() {
    Cycle testCycle1 = new Cycle("TestCycle1");
    Cycle testCycle2 = new Cycle("TestCycle2");

    Collection<Cycle> expectedCycles = new ArrayList<>();
    expectedCycles.add(testCycle1);
    expectedCycles.add(testCycle2);

    Collection<Cycle> outputCycles = new ArrayList<>();
    outputCycles.add(testCycle1);
    outputCycles.add(testCycle2);
    when(cycleRepository.findAll()).thenReturn(outputCycles);

    Collection<Cycle> actualCycles = cycleService.getAllCycles();

    assertEquals(expectedCycles, actualCycles);
  }

  @Test()
  public void findById_expectedNoMatch() {
    long searchId = 100L;

    when(cycleRepository.findByIdOrThrow(searchId)).thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> {
      cycleService.findById(searchId);
    });
  }

  @Test
  public void findById_expectedFoundResult() {
    long searchId = 100L;
    Cycle expectedCycle = new Cycle("TestCycle");

    when(cycleRepository.findByIdOrThrow(searchId)).thenReturn(expectedCycle);

    Cycle actualCycle = cycleService.findById(searchId);

    assertEquals(expectedCycle, actualCycle);
  }

  private Cycle createDummyCycle(String cycleName, long cycleId, int monthDelay) {
    Cycle newCycle = new Cycle();
    newCycle.setName(cycleName);
    newCycle.setId(cycleId);
    newCycle.setPlannedStartDate(plannedStartDate.plusMonths(monthDelay));
    newCycle.setPlannedEndDate(plannedEndDate.plusMonths(monthDelay));
    newCycle.setFactualStartDate(factualStartDate.plusMonths(monthDelay));
    newCycle.setFactualEndDate(factualEndDate.plusMonths(monthDelay));
    return newCycle;
  }

  @Test()
  public void updateCycle_noOriginalCycle_expectedNoMatchException() {
    // Mock Cycle data
    long cycleId = 100L;
    String oldCycleName = "OldCycleName";
    int oldMonthDelay = 0;
    // Mock Cycles
    Cycle cycleToUpdate = createDummyCycle(oldCycleName, cycleId, oldMonthDelay);

    when(cycleRepository.findByIdOrThrow(cycleId)).thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> {
      cycleService.updateCycle(cycleToUpdate);
    });
  }

  private void assertCycles(Cycle expectedCycle, Cycle actualCycle) {
    assertEquals(expectedCycle.getId(), actualCycle.getId());
    assertEquals(expectedCycle.getName(), actualCycle.getName());
    assertEquals(expectedCycle.getPlannedStartDate(), actualCycle.getPlannedStartDate());
    assertEquals(expectedCycle.getPlannedEndDate(), actualCycle.getPlannedEndDate());
  }

  @Test
  public void updateCycle_expectedUpdatedCycle() {
    // Mock Cycle data
    long cycleId = 100L;
    String oldCycleName = "OldCycleName";
    int oldCycleDelay = 1;
    String newCycleName = "NewCycleName";
    int newCycleDelay = 2;
    // Mock Cycles
    Cycle cycleToUpdate = createDummyCycle(oldCycleName, cycleId, oldCycleDelay);
    Cycle expectedCycle = createDummyCycle(newCycleName, cycleId, newCycleDelay);

    ArgumentCaptor<Cycle> capturedCycle = ArgumentCaptor.forClass(Cycle.class);
    when(cycleRepository.findByIdOrThrow(cycleId)).thenReturn(cycleToUpdate);
    when(cycleRepository.save(any(Cycle.class))).thenReturn(expectedCycle);

    Cycle actualCycle = cycleService.updateCycle(expectedCycle);

    verify(cycleRepository).save(capturedCycle.capture());
    assertCycles(expectedCycle, capturedCycle.getValue());
    assertCycles(expectedCycle, actualCycle);
  }

  @Test()
  public void deleteCycle_expectedEntityNotFoundException() throws Exception {
    Cycle cycle = createDummyCycle("c", 42, 1);
    when(cycleRepository.findByIdOrThrow(anyLong())).thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> {
      cycleService.deleteCycle(cycle.getId(), mockedIUser);
    });
  }

  @Test
  public void deleteCycle_expectedToBeDeleted() throws Exception {
    Cycle cycle = createDummyCycle("c", 42, 1);
    when(cycleRepository.findByIdOrThrow(cycle.getId())).thenReturn(cycle);
    cycleService.deleteCycle(cycle.getId(), mockedIUser);
  }

  @Test
  public void defineCycle_insertActiveCycle_expectedCorrectInformation() {
    Cycle newCycle = createDummyCycle("newCycle", 100L, 0);
    newCycle.setCycleState(CycleState.ACTIVE);
    Cycle oldCycle = createDummyCycle("oldCycle", 100L, 1);
    oldCycle.setCycleState(CycleState.PREPARATION);

    when(cycleService.findById(anyLong())).thenReturn(oldCycle);
    when(cycleRepository.save(any(Cycle.class))).thenReturn(new Cycle());

    cycleService.defineCycle(100L, newCycle);

    LocalDate currentTime = LocalDate.now();
    assertEquals(currentTime, oldCycle.getFactualEndDate());
    assertEquals(currentTime, newCycle.getFactualStartDate());
    assertEquals(CycleState.CLOSED, oldCycle.getCycleState());
    assertEquals(CycleState.ACTIVE, newCycle.getCycleState());
  }

  @Test
  public void defineCycle_insertActiveCycle_expectedSaveCall() {
    Cycle newCycle = new Cycle();
    newCycle.setCycleState(CycleState.ACTIVE);
    Cycle oldCycle = new Cycle();

    when(cycleService.findById(anyLong())).thenReturn(oldCycle);
    when(cycleRepository.save(any(Cycle.class))).thenReturn(new Cycle());

    cycleService.defineCycle(100L, newCycle);

    verify(cycleRepository, times(1)).save(oldCycle);
  }

  @Test
  public void defineCycle_insertActiveCycle_expectedArchivingServiceCall() {
    Collection<OkrCompany> companies = new ArrayList<>();
    Cycle newCycle = new Cycle();
    newCycle.setCycleState(CycleState.ACTIVE);
    Cycle oldCycle = new Cycle();
    oldCycle.setCompanies(companies);
    Cycle savedCycle = new Cycle();

    when(cycleService.findById(anyLong())).thenReturn(oldCycle);
    when(cycleRepository.save(any(Cycle.class))).thenReturn(savedCycle);

    cycleService.defineCycle(100L, newCycle);

    verify(cyclePreparationCloningService, times(1))
      .cloneCompanyListIntoCycleForPreparation(companies, savedCycle);
  }

  @Test
  public void defineCycle_insertActiveCycle_expectedCorrectReturn() {
    Cycle newCycle = new Cycle();
    newCycle.setCycleState(CycleState.ACTIVE);
    Cycle oldCycle = new Cycle();
    Cycle savedCycle = new Cycle();

    when(cycleService.findById(anyLong())).thenReturn(oldCycle);
    when(cycleRepository.save(any(Cycle.class))).thenReturn(savedCycle);

    Cycle actualCycle = cycleService.defineCycle(100L, newCycle);

    assertEquals(savedCycle, actualCycle);
  }

  private Cycle createDummyCycleWithStateAndEndDateInFuture(
    CycleState cycleState, int endDateDaysInFuture
  ) {
    Cycle dummyCycle = new Cycle();
    dummyCycle.setCycleState(cycleState);
    dummyCycle.setCompanies(new ArrayList<>());
    dummyCycle.setPlannedStartDate(LocalDate.now().minusDays(1));
    dummyCycle.setPlannedEndDate(LocalDate.now().plusDays(endDateDaysInFuture));
    return dummyCycle;
  }

  private OkrCompanyHistory createDummyHistory(long dummyId) {
    OkrCompanyHistory dummyOkrUnitHistory = new OkrCompanyHistory();
    dummyOkrUnitHistory.setId(dummyId);
    return dummyOkrUnitHistory;
  }

  @Test
  public void processAutomaticCycleSwitch_noCyclesFromNoCompanyHistories_expectedNoSaveCalls() {
    cycleService.processAutomaticCycleSwitch();

    verify(cycleRepository, never()).save(any());
  }

  @Test
  public void processAutomaticCycleSwitch_oneActiveCycleFromOneCompany_expectedNoCycleSwap() {
    OkrCompanyHistory okrUnitHistoryA = createDummyHistory(100L);
    Cycle cycleForHistoryA1 = createDummyCycleWithStateAndEndDateInFuture(CycleState.ACTIVE, 10);

    // Mock return data
    ArrayList<OkrCompanyHistory> companyHistoriesToReturn = new ArrayList<>();
    companyHistoriesToReturn.add(okrUnitHistoryA);
    ArrayList<Cycle> cyclesToReturnForCompanyHistoryA = new ArrayList<>();
    cyclesToReturnForCompanyHistoryA.add(cycleForHistoryA1);

    when(companyHistoryRepository.findAll()).thenReturn(companyHistoriesToReturn);
    when(cycleRepository
      .findByCompanyHistoryAndPlannedStartBeforeOrEqualAndNotCycleStateOrderByEndDateDescending(
        okrUnitHistoryA, LocalDate.now(), CycleState.CLOSED))
      .thenReturn(cyclesToReturnForCompanyHistoryA);

    cycleService.processAutomaticCycleSwitch();

    // We don't need to verify any save calls
    assertEquals(CycleState.ACTIVE, cycleForHistoryA1.getCycleState());
  }

  @Test
  public void
  processAutomaticCycleSwitch_oneActiveOnePreparationCyclesFromOneCompany_expectedCorrectCycleSwap() {
    OkrCompanyHistory okrUnitHistoryA = createDummyHistory(100L);
    Cycle cycleForCompanyA1 = createDummyCycleWithStateAndEndDateInFuture(CycleState.ACTIVE, 5);
    Cycle cycleForCompanyA2 =
      createDummyCycleWithStateAndEndDateInFuture(CycleState.PREPARATION, 10);

    // Mock return data
    ArrayList<OkrCompanyHistory> companyHistoriesToReturn = new ArrayList<>();
    companyHistoriesToReturn.add(okrUnitHistoryA);
    ArrayList<Cycle> cyclesToReturnForCompanyHistoryA = new ArrayList<>();
    cyclesToReturnForCompanyHistoryA.add(cycleForCompanyA2);
    cyclesToReturnForCompanyHistoryA.add(cycleForCompanyA1);

    when(companyHistoryRepository.findAll()).thenReturn(companyHistoriesToReturn);
    when(cycleRepository
      .findByCompanyHistoryAndPlannedStartBeforeOrEqualAndNotCycleStateOrderByEndDateDescending(
        okrUnitHistoryA, LocalDate.now(), CycleState.CLOSED))
      .thenReturn(cyclesToReturnForCompanyHistoryA);

    cycleService.processAutomaticCycleSwitch();

    verify(cycleRepository).save(cycleForCompanyA1);
    verify(cycleRepository).save(cycleForCompanyA2);

    assertEquals(CycleState.CLOSED, cycleForCompanyA1.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyA1.getFactualEndDate());
    assertEquals(CycleState.ACTIVE, cycleForCompanyA2.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyA2.getFactualStartDate());
  }

  @Test
  public void
  processAutomaticCycleSwitch_oneActiveTwoPreparationCyclesFromOneCompany_expectedCorrectCycleSwap() {
    OkrCompanyHistory okrUnitHistoryA = createDummyHistory(100L);
    Cycle cycleForCompanyA1 = createDummyCycleWithStateAndEndDateInFuture(CycleState.ACTIVE, 5);
    Cycle cycleForCompanyA2 =
      createDummyCycleWithStateAndEndDateInFuture(CycleState.PREPARATION, 10);
    Cycle cycleForCompanyA3 =
      createDummyCycleWithStateAndEndDateInFuture(CycleState.PREPARATION, 15);

    // Mock return data
    ArrayList<OkrCompanyHistory> companyHistoriesToReturn = new ArrayList<>();
    companyHistoriesToReturn.add(okrUnitHistoryA);
    ArrayList<Cycle> cyclesToReturnForCompanyHistoryA = new ArrayList<>();
    cyclesToReturnForCompanyHistoryA.add(cycleForCompanyA3);
    cyclesToReturnForCompanyHistoryA.add(cycleForCompanyA2);
    cyclesToReturnForCompanyHistoryA.add(cycleForCompanyA1);

    when(companyHistoryRepository.findAll()).thenReturn(companyHistoriesToReturn);
    when(cycleRepository
      .findByCompanyHistoryAndPlannedStartBeforeOrEqualAndNotCycleStateOrderByEndDateDescending(
        okrUnitHistoryA, LocalDate.now(), CycleState.CLOSED))
      .thenReturn(cyclesToReturnForCompanyHistoryA);

    cycleService.processAutomaticCycleSwitch();

    verify(cycleRepository).save(cycleForCompanyA1);
    verify(cycleRepository).save(cycleForCompanyA2);
    verify(cycleRepository).save(cycleForCompanyA3);

    assertEquals(CycleState.CLOSED, cycleForCompanyA1.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyA1.getFactualEndDate());
    assertEquals(CycleState.CLOSED, cycleForCompanyA2.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyA2.getFactualEndDate());
    assertEquals(CycleState.ACTIVE, cycleForCompanyA3.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyA3.getFactualStartDate());
  }

  @Test
  public void
  processAutomaticCycleSwitch_oneActiveTwoPreparationCyclesFromTwoCompanies_expectedCorrectCycleSwap() {
    OkrCompanyHistory okrUnitHistoryA = createDummyHistory(100L);
    Cycle cycleForCompanyA1 = createDummyCycleWithStateAndEndDateInFuture(CycleState.ACTIVE, 5);
    Cycle cycleForCompanyA2 =
      createDummyCycleWithStateAndEndDateInFuture(CycleState.PREPARATION, 10);
    Cycle cycleForCompanyA3 =
      createDummyCycleWithStateAndEndDateInFuture(CycleState.PREPARATION, 15);

    OkrCompanyHistory okrUnitHistoryB = createDummyHistory(200L);
    Cycle cycleForCompanyB1 = createDummyCycleWithStateAndEndDateInFuture(CycleState.ACTIVE, 3);
    Cycle cycleForCompanyB2 =
      createDummyCycleWithStateAndEndDateInFuture(CycleState.PREPARATION, 6);
    Cycle cycleForCompanyB3 =
      createDummyCycleWithStateAndEndDateInFuture(CycleState.PREPARATION, 12);

    // Mock return data
    ArrayList<OkrCompanyHistory> companyHistoriesToReturn = new ArrayList<>();
    companyHistoriesToReturn.add(okrUnitHistoryA);
    companyHistoriesToReturn.add(okrUnitHistoryB);
    ArrayList<Cycle> cyclesToReturnForCompanyHistoryA = new ArrayList<>();
    cyclesToReturnForCompanyHistoryA.add(cycleForCompanyA3);
    cyclesToReturnForCompanyHistoryA.add(cycleForCompanyA2);
    cyclesToReturnForCompanyHistoryA.add(cycleForCompanyA1);
    ArrayList<Cycle> cyclesToReturnForCompanyHistoryB = new ArrayList<>();
    cyclesToReturnForCompanyHistoryB.add(cycleForCompanyB3);
    cyclesToReturnForCompanyHistoryB.add(cycleForCompanyB2);
    cyclesToReturnForCompanyHistoryB.add(cycleForCompanyB1);

    when(companyHistoryRepository.findAll()).thenReturn(companyHistoriesToReturn);
    when(cycleRepository
      .findByCompanyHistoryAndPlannedStartBeforeOrEqualAndNotCycleStateOrderByEndDateDescending(
        okrUnitHistoryA, LocalDate.now(), CycleState.CLOSED))
      .thenReturn(cyclesToReturnForCompanyHistoryA);
    when(cycleRepository
      .findByCompanyHistoryAndPlannedStartBeforeOrEqualAndNotCycleStateOrderByEndDateDescending(
        okrUnitHistoryB, LocalDate.now(), CycleState.CLOSED))
      .thenReturn(cyclesToReturnForCompanyHistoryB);

    cycleService.processAutomaticCycleSwitch();

    verify(cycleRepository).save(cycleForCompanyA1);
    verify(cycleRepository).save(cycleForCompanyA2);
    verify(cycleRepository).save(cycleForCompanyA3);
    verify(cycleRepository).save(cycleForCompanyB1);
    verify(cycleRepository).save(cycleForCompanyB2);
    verify(cycleRepository).save(cycleForCompanyB3);

    assertEquals(CycleState.CLOSED, cycleForCompanyA1.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyA1.getFactualEndDate());
    assertEquals(CycleState.CLOSED, cycleForCompanyA2.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyA2.getFactualEndDate());
    assertEquals(CycleState.ACTIVE, cycleForCompanyA3.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyA3.getFactualStartDate());
    assertEquals(CycleState.CLOSED, cycleForCompanyB1.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyB1.getFactualEndDate());
    assertEquals(CycleState.CLOSED, cycleForCompanyB2.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyB2.getFactualEndDate());
    assertEquals(CycleState.ACTIVE, cycleForCompanyB3.getCycleState());
    assertEquals(LocalDate.now(), cycleForCompanyB3.getFactualStartDate());
  }
}
