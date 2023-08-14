package org.burningokr.service.okrUnit;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.histories.OkrTopicDraftHistory;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrBranchHistory;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrCompanyHistory;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrDepartmentHistory;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrUnitHistory;
import org.burningokr.repositories.ExtendedRepository;
import org.burningokr.repositories.cycle.*;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.repositories.okrUnit.OkrUnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okr.TaskBoardService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

  private final Logger logger = LoggerFactory.getLogger(CompanyService.class);

  private final CycleRepository cycleRepository;
  private final CompanyHistoryRepository companyHistoryRepository;
  private final BranchHistoryRepository branchHistoryRepository;
  private final DepartmentHistoryRepository departmentHistoryRepository;
  private final CompanyRepository companyRepository;
  private final OkrUnitRepository<OkrChildUnit> okrUnitRepository;
  private final ActivityService activityService;
  private final EntityCrawlerService entityCrawlerService;
  private final OkrTopicDescriptionRepository okrTopicDescriptionRepository;
  private final TaskBoardService taskBoardService;
  private final OkrTopicDraftRepository okrTopicDraftRepository;
  private final TopicDraftHistoryRepository topicDraftHistoryRepository;

  public OkrCompany findById(long companyId) {
    return this.companyRepository.findByIdOrThrow(companyId);
  }

  public Collection<OkrCompany> getAllCompanies() {
    return Lists.newArrayList(companyRepository.findAll());
  }

  public Collection<OkrCompany> getCompaniesByActiveStatus(boolean active) {
    if (active) {
      return filterCompanies(CycleState.ACTIVE);
    } else {
      return filterCompanies(CycleState.CLOSED);
    }
  }

  private List<OkrCompany> filterCompanies(CycleState state) {
    return getAllCompanies().stream()
      .filter(okrCompany -> okrCompany.getCycle().getCycleState() == state)
      .collect(Collectors.toList());
  }

  public void attachCycleNameToCompanyName(Collection<OkrCompany> okrCompanies) {
    okrCompanies.forEach(
      okrCompany ->
        okrCompany.setName(
          okrCompany.getName() + " (" + okrCompany.getCycle().getName() + ")"));
  }

  public Collection<OkrCompany> findCompanyHistoryByCompanyId(long companyId) {
    OkrCompany okrCompany = findById(companyId);
    return okrCompany.getCompanyHistory().getUnits();
  }

  /**
   * Finds all Cycles of a OkrCompany.
   *
   * @param companyId a long value
   * @return a {@link Collection} of {@link Cycle}
   */
  public Collection<Cycle> findCycleListByCompanyId(long companyId) {
    OkrCompany okrCompany = findById(companyId);
    ArrayList<Cycle> cycleList = new ArrayList<>();
    for (OkrCompany currentOkrCompany : okrCompany.getCompanyHistory().getUnits()) {
      cycleList.add(currentOkrCompany.getCycle());
    }
    return cycleList;
  }

  @Transactional
  public OkrCompany createCompany(OkrCompany okrCompany) {
    Cycle cycle = new Cycle("default");
    cycle.setCycleState(CycleState.ACTIVE);
    cycle.setVisible(true);
    cycleRepository.save(cycle);
    okrCompany.setCycle(cycle);

    OkrCompanyHistory history = new OkrCompanyHistory();
    companyHistoryRepository.save(history);
    okrCompany.setCompanyHistory(history);

    okrCompany = companyRepository.save(okrCompany);
    logger.debug("Created OkrCompany " + okrCompany.getName());
    activityService.createActivity(okrCompany, Action.CREATED);

    return okrCompany;
  }

  /**
   * Updates a OkrCompany.
   *
   * @param updatedOkrCompany a {@link OkrCompany} object
   * @return a {@link OkrCompany} object
   */
  @Transactional
  public OkrCompany updateCompany(OkrCompany updatedOkrCompany) {
    OkrCompany referencedOkrCompany = companyRepository.findByIdOrThrow(updatedOkrCompany.getId());

    throwIfCompanyInClosedCycle(referencedOkrCompany);

    final String oldName = referencedOkrCompany.getName();
    referencedOkrCompany.setName(updatedOkrCompany.getName());
    referencedOkrCompany.setLabel(updatedOkrCompany.getLabel());

    referencedOkrCompany = companyRepository.save(referencedOkrCompany);
    logger.debug(
        "Updated OkrCompany "
            + oldName
            + " (id: "
            + referencedOkrCompany.getId()
            + "), updated name to: +"
            + referencedOkrCompany.getName());
    activityService.createActivity(referencedOkrCompany, Action.EDITED);

    return referencedOkrCompany;
  }

  /**
   * Deletes a OkrCompany.
   *
   * @param companyId          a long value
   * @param deleteWholeHistory a boolean value
   */
  @Transactional
  public void deleteCompany(Long companyId, boolean deleteWholeHistory) {
    OkrCompany okrCompany = findById(companyId);
    Collection<OkrCompany> companiesToDelete = new ArrayList<>();

    if (deleteWholeHistory && okrCompany.getCompanyHistory() != null) {
      companiesToDelete = okrCompany.getCompanyHistory().getUnits();
    } else {
      companiesToDelete.add(okrCompany);
    }

    deleteCompanyList(companiesToDelete);
  }

  private void deleteCompanyList(Collection<OkrCompany> companiesToDelete) {
    for (OkrCompany okrCompanyToDelete : companiesToDelete) {

      companyRepository.deleteById(okrCompanyToDelete.getId());
      logger.debug("Deleted company with id: " + okrCompanyToDelete.getId());
      activityService.createActivity(okrCompanyToDelete, Action.DELETED);

      Cycle cycle = okrCompanyToDelete.getCycle();
      if (cycle != null && cycle.getCompanies() != null && cycle.getCompanies().isEmpty()) {
        deleteEmptyCycle(cycle);
      }

      OkrUnitHistory<OkrCompany> history = okrCompanyToDelete.getCompanyHistory();
      if (history != null && history.getUnits() != null && history.getUnits().isEmpty()) {
        deleteEmptyHistory(history);
      }
    }
  }

  /**
   * Create a OkrDepartment.
   *
   * @param companyId     a long value
   * @param okrDepartment a {@link OkrDepartment} object
   * @return a {@link OkrDepartment} object
   */
  @Transactional
  public OkrDepartment createDepartment(Long companyId, OkrDepartment okrDepartment) {
    okrDepartment.setId(null);
    OkrCompany referencedOkrCompany = companyRepository.findByIdOrThrow(companyId);

    throwIfCompanyInClosedCycle(referencedOkrCompany);

    okrDepartment.setParentOkrUnit(referencedOkrCompany);
    okrDepartment.setDepartmentHistory(
      createHistory(okrDepartment, new OkrDepartmentHistory(), departmentHistoryRepository));

    OkrTopicDescription description = new OkrTopicDescription(okrDepartment.getName());
    description = okrTopicDescriptionRepository.save(description);
    okrDepartment.setOkrTopicDescription(description);


    TaskBoard taskBoard = taskBoardService.createNewTaskBoardWithDefaultStates(okrDepartment);
    okrDepartment.setTaskBoard(taskBoard);

    okrDepartment = okrUnitRepository.save(okrDepartment);

    logger.debug(
        "Created okrDepartment "
            + okrDepartment.getName()
            + " into company: "
            + referencedOkrCompany.getName()
            + "(id:"
            + companyId
            + ")");
    activityService.createActivity(okrDepartment, Action.CREATED);

    return okrDepartment;
  }

  @Transactional
  public OkrBranch createOkrBranch(Long companyId, OkrBranch okrBranch) throws ForbiddenException {
    okrBranch.setId(null);
    OkrCompany referencedOkrCompany = companyRepository.findByIdOrThrow(companyId);

    throwIfCompanyInClosedCycle(referencedOkrCompany);

    okrBranch.setParentOkrUnit(referencedOkrCompany);
    okrBranch.setBranchHistory(createHistory(okrBranch, new OkrBranchHistory(), branchHistoryRepository));

    okrBranch = okrUnitRepository.save(okrBranch);
    logger.debug(
        "Created okrBranch: "
            + okrBranch.getName()
            + " into OkrCompany "
            + referencedOkrCompany.getName()
            + "(id:"
            + companyId
            + ")");
    activityService.createActivity(okrBranch, Action.CREATED);

    return okrBranch;
  }

  protected <T extends OkrUnitHistory, U extends OkrChildUnit> T createHistory(
    U unit, T history, ExtendedRepository<T, Long> repository
  ) {
    history.addUnit(unit);
    history = repository.save(history);
    return history;
  }

  protected OkrTopicDraftHistory createTopicDraftHistory(
    OkrTopicDraft draft, OkrTopicDraftHistory history, TopicDraftHistoryRepository repository
  ) {
    history.addUnit(draft);
    history = repository.save(history);
    return history;
  }

  public OkrTopicDraft createTopicDraft(Long companyId, OkrTopicDraft topicDraft) throws
    ForbiddenException {
    OkrCompany referencedOkrCompany = companyRepository.findByIdOrThrow(companyId);

    throwIfCompanyInClosedCycle(referencedOkrCompany);

    topicDraft.setParentUnit(referencedOkrCompany);
    topicDraft.setHistory(
      createTopicDraftHistory(
        topicDraft, new OkrTopicDraftHistory(), topicDraftHistoryRepository));

    topicDraft = okrTopicDraftRepository.save(topicDraft);
    logger.debug(
        "Created Topic Draft: "
            + topicDraft.getName()
            + " into department "
            + referencedOkrCompany.getName()
            + "(id:"
            + companyId
            + ")");

    activityService.createActivity(topicDraft, Action.CREATED);

    return topicDraft;
  }

  private void throwIfCompanyInClosedCycle(OkrCompany okrCompanyToCheck) throws ForbiddenException {
    if (entityCrawlerService.getCycleOfCompany(okrCompanyToCheck).getCycleState()
      == CycleState.CLOSED) {
      throw new ForbiddenException(
        "Cannot modify this resource on a OkrCompany in a closed cycle.");
    }
  }

  private void deleteEmptyCycle(Cycle cycle) {
    cycleRepository.deleteById(cycle.getId());
    logger.debug("Deleted cycle with id: " + cycle.getId());
    activityService.createActivity(cycle, Action.DELETED);
  }

  private void deleteEmptyHistory(OkrUnitHistory<OkrCompany> history) {
    companyHistoryRepository.deleteById(history.getId());
    logger.debug("Deleted cycle with id: " + history.getId());
    activityService.createActivity(history, Action.DELETED);
  }
}
