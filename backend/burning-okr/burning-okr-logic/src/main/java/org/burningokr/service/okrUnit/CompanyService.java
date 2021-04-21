package org.burningokr.service.okrUnit;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.histories.OkrTopicDraftHistory;
import org.burningokr.model.okrUnits.okrUnitHistories.*;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.OkrTopicDraft;
import org.burningokr.model.okrUnits.*;
import org.burningokr.model.users.User;
import org.burningokr.repositories.ExtendedRepository;
import org.burningokr.repositories.cycle.*;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

  private final Logger logger = LoggerFactory.getLogger(CompanyService.class);

  private CycleRepository cycleRepository;
  private CompanyHistoryRepository companyHistoryRepository;
  private BranchHistoryRepository branchHistoryRepository;
  private DepartmentHistoryRepository departmentHistoryRepository;
  private CompanyRepository companyRepository;
  private UnitRepository<OkrChildUnit> unitRepository;
  private ActivityService activityService;
  private EntityCrawlerService entityCrawlerService;
  private OkrTopicDescriptionRepository okrTopicDescriptionRepository;
  private OkrTopicDraftRepository okrTopicDraftRepository;
  private TopicDraftHistoryRepository topicDraftHistoryRepository;

  /**
   * Initialize CompanyService.
   *
   * @param cycleRepository a {@link CycleRepository} object
   * @param companyHistoryRepository a {@link CompanyHistoryRepository} object
   * @param companyRepository a {@link CompanyRepository} object
   * @param unitRepository a {@link UnitRepository} object
   * @param activityService an {@link ActivityService} object
   * @param entityCrawlerService an {@link EntityCrawlerService} object
   * @param okrTopicDescriptionRepository an {@link OkrTopicDescriptionRepository} object
   */
  public CompanyService(
      CycleRepository cycleRepository,
      CompanyHistoryRepository companyHistoryRepository,
      BranchHistoryRepository branchHistoryRepository,
      DepartmentHistoryRepository departmentHistoryRepository,
      CompanyRepository companyRepository,
      UnitRepository<OkrChildUnit> unitRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService,
      OkrTopicDescriptionRepository okrTopicDescriptionRepository,
      OkrTopicDraftRepository okrTopicDraftRepository,
      TopicDraftHistoryRepository topicDraftHistoryRepository) {
    this.cycleRepository = cycleRepository;
    this.companyHistoryRepository = companyHistoryRepository;
    this.branchHistoryRepository = branchHistoryRepository;
    this.departmentHistoryRepository = departmentHistoryRepository;
    this.companyRepository = companyRepository;
    this.unitRepository = unitRepository;
    this.activityService = activityService;
    this.entityCrawlerService = entityCrawlerService;
    this.okrTopicDescriptionRepository = okrTopicDescriptionRepository;
    this.okrTopicDraftRepository = okrTopicDraftRepository;
    this.topicDraftHistoryRepository = topicDraftHistoryRepository;
  }

  public OkrCompany findById(long companyId) {
    return this.companyRepository.findByIdOrThrow(companyId);
  }

  public Collection<OkrCompany> getAllCompanies() {
    return Lists.newArrayList(companyRepository.findAll());
  }

  public Collection<OkrCompany> findCompanyHistoryByCompanyId(long companyId) {
    OkrCompany okrCompany = findById(companyId);
    return okrCompany.getHistory().getUnits();
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
    for (OkrCompany currentOkrCompany : okrCompany.getHistory().getUnits()) {
      cycleList.add(currentOkrCompany.getCycle());
    }
    return cycleList;
  }

  public Collection<OkrChildUnit> findChildUnitOfCompany(long companyId) {
    OkrCompany okrCompany = findById(companyId);
    return okrCompany.getOkrChildUnits();
  }

  /**
   * Creates a OkrCompany.
   *
   * @param okrCompany a {@link OkrCompany} object
   * @param user an {@link User} object
   * @return a {@link OkrCompany} object
   */
  @Transactional
  public OkrCompany createCompany(OkrCompany okrCompany, User user) {
    Cycle cycle = new Cycle("default");
    cycle.setCycleState(CycleState.ACTIVE);
    cycle.setVisible(true);
    cycleRepository.save(cycle);
    okrCompany.setCycle(cycle);

    OkrCompanyHistory history = new OkrCompanyHistory();
    companyHistoryRepository.save(history);
    okrCompany.setHistory(history);

    okrCompany = companyRepository.save(okrCompany);
    logger.info("Created OkrCompany " + okrCompany.getName());
    activityService.createActivity(user, okrCompany, Action.CREATED);

    return okrCompany;
  }

  /**
   * Updates a OkrCompany.
   *
   * @param updatedOkrCompany a {@link OkrCompany} object
   * @param user an {@link User} object
   * @return a {@link OkrCompany} object
   */
  @Transactional
  public OkrCompany updateCompany(OkrCompany updatedOkrCompany, User user) {
    OkrCompany referencedOkrCompany = companyRepository.findByIdOrThrow(updatedOkrCompany.getId());

    throwIfCompanyInClosedCycle(referencedOkrCompany);

    final String oldName = referencedOkrCompany.getName();
    referencedOkrCompany.setName(updatedOkrCompany.getName());
    referencedOkrCompany.setLabel(updatedOkrCompany.getLabel());

    referencedOkrCompany = companyRepository.save(referencedOkrCompany);
    logger.info(
        "Updated OkrCompany "
            + oldName
            + " (id: "
            + referencedOkrCompany.getId()
            + "), updated name to: +"
            + referencedOkrCompany.getName());
    activityService.createActivity(user, referencedOkrCompany, Action.EDITED);

    return referencedOkrCompany;
  }

  /**
   * Deletes a OkrCompany.
   *
   * @param companyId a long value
   * @param deleteWholeHistory a boolean value
   * @param user an {@link User} object
   */
  @Transactional
  public void deleteCompany(Long companyId, boolean deleteWholeHistory, User user) {
    OkrCompany okrCompany = findById(companyId);
    Collection<OkrCompany> companiesToDelete = new ArrayList<>();

    if (deleteWholeHistory && okrCompany.getHistory() != null) {
      companiesToDelete = okrCompany.getHistory().getUnits();
    } else {
      companiesToDelete.add(okrCompany);
    }

    deleteCompanyList(companiesToDelete, user);
  }

  private void deleteCompanyList(Collection<OkrCompany> companiesToDelete, User user) {
    for (OkrCompany okrCompanyToDelete : companiesToDelete) {

      companyRepository.deleteById(okrCompanyToDelete.getId());
      logger.info("Deleted company with id: " + okrCompanyToDelete.getId());
      activityService.createActivity(user, okrCompanyToDelete, Action.DELETED);

      Cycle cycle = okrCompanyToDelete.getCycle();
      if (cycle != null && cycle.getCompanies() != null && cycle.getCompanies().isEmpty()) {
        deleteEmptyCycle(cycle, user);
      }

      OkrUnitHistory<OkrCompany> history = okrCompanyToDelete.getHistory();
      if (history != null && history.getUnits() != null && history.getUnits().isEmpty()) {
        deleteEmptyHistory(history, user);
      }
    }
  }

  /**
   * Create a OkrDepartment.
   *
   * @param companyId a long value
   * @param okrDepartment a {@link OkrDepartment} object
   * @param user an {@link User} object
   * @return a {@link OkrDepartment} object
   */
  @Transactional
  public OkrDepartment createDepartment(Long companyId, OkrDepartment okrDepartment, User user) {
    OkrCompany referencedOkrCompany = companyRepository.findByIdOrThrow(companyId);

    throwIfCompanyInClosedCycle(referencedOkrCompany);

    okrDepartment.setParentOkrUnit(referencedOkrCompany);
    okrDepartment.setHistory(createHistory(okrDepartment, new OkrDepartmentHistory(), departmentHistoryRepository));

    OkrTopicDescription description = new OkrTopicDescription(okrDepartment.getName());
    description = okrTopicDescriptionRepository.save(description);
    okrDepartment.setOkrTopicDescription(description);

    okrDepartment = unitRepository.save(okrDepartment);
    logger.info(
        "Created okrDepartment "
            + okrDepartment.getName()
            + " into company: "
            + referencedOkrCompany.getName()
            + "(id:"
            + companyId
            + ")");
    activityService.createActivity(user, okrDepartment, Action.CREATED);

    return okrDepartment;
  }

  @Transactional
  public OkrBranch createOkrBranch(Long companyId, OkrBranch okrBranch, User user) {
    OkrCompany referencedOkrCompany = companyRepository.findByIdOrThrow(companyId);

    throwIfCompanyInClosedCycle(referencedOkrCompany);

    okrBranch.setParentOkrUnit(referencedOkrCompany);
    okrBranch.setHistory(createHistory(okrBranch, new OkrBranchHistory(), branchHistoryRepository));

    okrBranch = unitRepository.save(okrBranch);
    logger.info(
        "Created okrBranch: "
            + okrBranch.getName()
            + " into OkrCompany "
            + referencedOkrCompany.getName()
            + "(id:"
            + companyId
            + ")");
    activityService.createActivity(user, okrBranch, Action.CREATED);

    return okrBranch;
  }

  protected  <T extends OkrUnitHistory, U extends OkrChildUnit> T createHistory(U unit, T history, ExtendedRepository<T, Long> repository) {
    history.addUnit(unit);
    history =  repository.save(history);
    return history;
  }

    protected  OkrTopicDraftHistory createTopicDraftHistory(OkrTopicDraft draft, OkrTopicDraftHistory history, TopicDraftHistoryRepository repository) {
        history.addUnit(draft);
        history =  repository.save(history);
        return history;
    }

  // TODO (R.J. 17.02.2021) create this method
  public OkrTopicDraft createTopicDraft(Long companyId, OkrTopicDraft topicDraft, User user) {
    OkrCompany referencedOkrCompany = companyRepository.findByIdOrThrow(companyId);

    throwIfCompanyInClosedCycle(referencedOkrCompany);

    topicDraft.setParentUnit(referencedOkrCompany);
    topicDraft.setHistory(createTopicDraftHistory(topicDraft, new OkrTopicDraftHistory(), topicDraftHistoryRepository));

    topicDraft = okrTopicDraftRepository.save(topicDraft);
    logger.info(
        "Created Topic Draft: "
            + topicDraft.getName()
            + " into department "
            + referencedOkrCompany.getName()
            + "(id:"
            + companyId
            + ")");

    activityService.createActivity(user, topicDraft, Action.CREATED);

    return topicDraft;
  }

  private void throwIfCompanyInClosedCycle(OkrCompany okrCompanyToCheck) {
    if (entityCrawlerService.getCycleOfCompany(okrCompanyToCheck).getCycleState()
        == CycleState.CLOSED) {
      throw new ForbiddenException(
          "Cannot modify this resource on a OkrCompany in a closed cycle.");
    }
  }

  private void deleteEmptyCycle(Cycle cycle, User user) {
    cycleRepository.deleteById(cycle.getId());
    logger.info("Deleted cycle with id: " + cycle.getId());
    activityService.createActivity(user, cycle, Action.DELETED);
  }

  private void deleteEmptyHistory(OkrUnitHistory<OkrCompany> history, User user) {
    companyHistoryRepository.deleteById(history.getId());
    logger.info("Deleted cycle with id: " + history.getId());
    activityService.createActivity(user, history, Action.DELETED);
  }
}
