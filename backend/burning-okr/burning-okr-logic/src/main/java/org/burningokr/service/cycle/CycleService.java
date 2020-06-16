package org.burningokr.service.cycle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.cycles.OkrCompanyHistory;
import org.burningokr.model.users.User;
import org.burningokr.repositories.cycle.CompanyHistoryRepository;
import org.burningokr.repositories.cycle.CycleRepository;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnitUtil.CyclePreparationCloningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@EnableScheduling
@Service
public class CycleService {

  private final Logger logger = LoggerFactory.getLogger(CycleService.class);
  private final String threeInTheMorningCronExpr = "0 0 3 * * ?";

  private CyclePreparationCloningService cyclePreparationCloningService;
  private CycleRepository cycleRepository;
  private CompanyHistoryRepository companyHistoryRepository;
  private CompanyService companyService;

  /**
   * Initialize CycleService.
   *
   * @param cyclePreparationCloningService a {@link CyclePreparationCloningService} object
   * @param cycleRepository a {@link CycleRepository} object
   * @param companyHistoryRepository a {@link CompanyHistoryRepository} object
   * @param companyService a {@link CompanyService} object
   */
  @Autowired
  public CycleService(
      CyclePreparationCloningService cyclePreparationCloningService,
      CycleRepository cycleRepository,
      CompanyHistoryRepository companyHistoryRepository,
      CompanyService companyService) {
    this.cyclePreparationCloningService = cyclePreparationCloningService;
    this.cycleRepository = cycleRepository;
    this.companyHistoryRepository = companyHistoryRepository;
    this.companyService = companyService;
  }

  /**
   * Returns all Cycles.
   *
   * @return a {@link Collection} of {@link Cycle}
   */
  public Collection<Cycle> getAllCycles() {
    Collection<Cycle> cycles = new ArrayList<>();
    for (Cycle cycle : cycleRepository.findAll()) {
      cycles.add(cycle);
    }
    return cycles;
  }

  public Cycle findById(Long cycleId) {
    return cycleRepository.findByIdOrThrow(cycleId);
  }

  /**
   * Update the Cycle.
   *
   * @param updatedCycle a {@link Cycle} object
   * @return a {@link Cycle} object
   */
  @Transactional
  public Cycle updateCycle(Cycle updatedCycle) {
    Cycle referencedCycle = cycleRepository.findByIdOrThrow(updatedCycle.getId());

    updateReferencedCycle(updatedCycle, referencedCycle);

    logger.info("Updated cycle with id: " + referencedCycle.getId());
    return cycleRepository.save(referencedCycle);
  }

  private void updateReferencedCycle(Cycle updatedCycle, Cycle referencedCycle) {
    referencedCycle.setVisible(updatedCycle.isVisible());
    referencedCycle.setName(updatedCycle.getName());
    referencedCycle.setPlannedStartDate(updatedCycle.getPlannedStartDate());
    referencedCycle.setPlannedEndDate(updatedCycle.getPlannedEndDate());
  }

  /**
   * Delete the Cycle, if it is not active.
   *
   * @param cycleId a long value
   * @param user an {@link User} object
   * @throws Exception if Cycle is active
   */
  @Transactional
  public void deleteCycle(Long cycleId, User user) throws Exception {
    Cycle cycle = cycleRepository.findByIdOrThrow(cycleId);
    if (cycle.getCycleState() != CycleState.ACTIVE) {
      cycle
          .getCompanies()
          .forEach(company -> companyService.deleteCompany(company.getId(), false, user));
      this.logger.info("Deleted (non active) cycle with id " + cycle.getId());
    } else {
      throw new Exception("An active Cycle can not be deleted");
    }
  }

  /**
   * Defines the Cycle.
   *
   * <p>If Cycle is active, replace the Cycle, otherwise create CycleClone.
   *
   * @param oldCycleId a long value
   * @param cycle a {@link Cycle} object
   * @return a {@link Cycle} object
   */
  @Transactional
  public Cycle defineCycle(Long oldCycleId, Cycle cycle) {
    if (cycle.getCycleState() == CycleState.ACTIVE) {
      return replaceCycle(oldCycleId, cycle);
    } else {
      return createCycleCloneInPreparation(oldCycleId, cycle);
    }
  }

  private Cycle replaceCycle(Long oldCycleId, Cycle newCycle) {
    Cycle oldCycle = findById(oldCycleId);

    LocalDate timeOfReplacement = LocalDate.now();
    oldCycle.setFactualEndDate(timeOfReplacement);
    newCycle.setFactualStartDate(timeOfReplacement);
    oldCycle.setCycleState(CycleState.CLOSED);

    cycleRepository.save(oldCycle);
    newCycle = cycleRepository.save(newCycle);

    cyclePreparationCloningService.cloneCompanyListIntoCycleForPreparation(
        oldCycle.getCompanies(), newCycle);

    logger.info(
        "Replaced Cycle: "
            + oldCycle.getName()
            + "(id:"
            + oldCycle.getId()
            + " with new Cycle:"
            + newCycle.getName()
            + "(id:"
            + newCycle.getId()
            + ")");
    return newCycle;
  }

  private Cycle createCycleCloneInPreparation(Long oldCycleId, Cycle cycle) {
    Cycle cycleToClone = findById(oldCycleId);
    cycle = cycleRepository.save(cycle);
    cyclePreparationCloningService.cloneCompanyListIntoCycleForPreparation(
        cycleToClone.getCompanies(), cycle);
    logger.info(
        "Cloned Cycle: "
            + cycleToClone.getName()
            + "(id:"
            + cycleToClone.getId()
            + " for use as new Cycle:"
            + cycle.getName()
            + "(id:"
            + cycle.getId()
            + ")");
    return cycle;
  }

  /** Event-Listener for Cycle processing (on Application start). */
  @EventListener(ApplicationReadyEvent.class)
  public void applicationStartCycleProcessing() {
    logger.info(
        "{0}: Application startup detected, processing automatic cycle switch if necessary.",
        CycleService.class.getName());
    processAutomaticCycleSwitch();
  }

  /** Cron-Job for daily Cycle processing (3am). */
  @Scheduled(cron = threeInTheMorningCronExpr)
  public void dailyCycleProcessing() {
    logger.info(
        "{0}: 3 in the morning detected, processing automatic cycle switch if necessary.",
        CycleService.class.getName());
    processAutomaticCycleSwitch();
  }

  @Transactional
  public void processAutomaticCycleSwitch() {
    HashMap<Boolean, ArrayList<Cycle>> cyclesToCycleSwitch = getCyclesDueForCycleStateSwitch();
    setCyclesToUpdateStates(cyclesToCycleSwitch);
  }

  // Suppress Warning for LineLength, since Method Name is to long
  @SuppressWarnings("checkstyle:linelength")
  private HashMap<Boolean, ArrayList<Cycle>> getCyclesDueForCycleStateSwitch() {
    HashMap<Boolean, ArrayList<Cycle>> cyclesToUpdate = new HashMap<>();
    ArrayList<Cycle> cyclesToSetActive = new ArrayList<>();
    ArrayList<Cycle> cyclesToSetClosed = new ArrayList<>();
    cyclesToUpdate.put(true, cyclesToSetActive);
    cyclesToUpdate.put(false, cyclesToSetClosed);

    for (OkrCompanyHistory currentOkrCompanyHistory : companyHistoryRepository.findAll()) {
      LocalDate currentDate = LocalDate.now();
      List<Cycle> cyclesToConsiderForStateSwitch;
      cyclesToConsiderForStateSwitch =
          cycleRepository
              .findByCompanyHistoryAndPlannedStartBeforeOrEqualAndNotCycleStateOrderByEndDateDescending(
                  currentOkrCompanyHistory, currentDate, CycleState.CLOSED);
      if (!cyclesToConsiderForStateSwitch.isEmpty()) {
        Cycle firstCycleInList =
            cyclesToConsiderForStateSwitch.get(
                0); // We only need to check the first cycle in the list sorted by end date
        if (firstCycleInList.getCycleState()
            == CycleState
                .PREPARATION) { // to find out whether the whole list is in need of a cycle switch
          cyclesToSetActive.add(firstCycleInList);
          cyclesToConsiderForStateSwitch.remove(0);
          cyclesToSetClosed.addAll(cyclesToConsiderForStateSwitch);
        }
      }
    }
    return cyclesToUpdate;
  }

  private void setCyclesToUpdateStates(HashMap<Boolean, ArrayList<Cycle>> cyclesToUpdate) {
    for (Cycle cycleToSetStateActive : cyclesToUpdate.get(true)) {
      setCycleStateToActiveAndSave(cycleToSetStateActive);
    }
    for (Cycle cycleToSetStateClosed : cyclesToUpdate.get(false)) {
      setCycleStateToClosedAndSave(cycleToSetStateClosed);
    }
  }

  private Cycle setCycleStateToActiveAndSave(Cycle cycleToActivate) {
    cycleToActivate.setCycleState(CycleState.ACTIVE);
    cycleToActivate.setFactualStartDate(LocalDate.now());
    logger.info(
        "Set Cycle State to ACTIVE: "
            + cycleToActivate.getName()
            + "(id:"
            + cycleToActivate.getId()
            + " )");
    return cycleRepository.save(cycleToActivate);
  }

  private Cycle setCycleStateToClosedAndSave(Cycle cycleToClose) {
    cycleToClose.setCycleState(CycleState.CLOSED);
    cycleToClose.setFactualEndDate(LocalDate.now());
    logger.info(
        "Set Cycle State to CLOSED: "
            + cycleToClose.getName()
            + "(id:"
            + cycleToClose.getId()
            + " )");
    return cycleRepository.save(cycleToClose);
  }
}
