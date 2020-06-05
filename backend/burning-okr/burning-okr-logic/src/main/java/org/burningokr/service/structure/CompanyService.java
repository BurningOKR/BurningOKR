package org.burningokr.service.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CompanyHistory;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.structures.*;
import org.burningokr.model.users.User;
import org.burningokr.repositories.cycle.CompanyHistoryRepository;
import org.burningokr.repositories.cycle.CycleRepository;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.CompanyRepository;
import org.burningokr.repositories.structre.DepartmentRepository;
import org.burningokr.repositories.structre.StructureRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

  private final Logger logger = LoggerFactory.getLogger(CompanyService.class);

  private CycleRepository cycleRepository;
  private CompanyHistoryRepository companyHistoryRepository;
  private CompanyRepository companyRepository;
  private StructureRepository<SubStructure> structureRepository;
  private ObjectiveRepository objectiveRepository;
  private ActivityService activityService;
  private EntityCrawlerService entityCrawlerService;

  /**
   * Initialize CompanyService.
   *
   * @param cycleRepository a {@link CycleRepository} object
   * @param companyHistoryRepository a {@link CompanyHistoryRepository} object
   * @param companyRepository a {@link CompanyRepository} object
   * @param departmentRepository a {@link DepartmentRepository} object
   * @param objectiveRepository an {@link ObjectiveRepository} object
   * @param activityService an {@link ActivityService} object
   * @param entityCrawlerService an {@link EntityCrawlerService} object
   */
  public CompanyService(
      CycleRepository cycleRepository,
      CompanyHistoryRepository companyHistoryRepository,
      CompanyRepository companyRepository,
      StructureRepository<SubStructure> structureRepository,
      ObjectiveRepository objectiveRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService) {
    this.cycleRepository = cycleRepository;
    this.companyHistoryRepository = companyHistoryRepository;
    this.companyRepository = companyRepository;
    this.structureRepository = structureRepository;
    this.objectiveRepository = objectiveRepository;
    this.activityService = activityService;
    this.entityCrawlerService = entityCrawlerService;
  }

  public Company findById(long companyId) {
    return this.companyRepository.findByIdOrThrow(companyId);
  }

  public Collection<Company> getAllCompanies() {
    return Lists.newArrayList(companyRepository.findAll());
  }

  public Collection<Company> findCompanyHistoryByCompanyId(long companyId) {
    Company company = findById(companyId);
    return company.getHistory().getCompanies();
  }

  /**
   * Finds all Cycles of a Company.
   *
   * @param companyId a long value
   * @return a {@link Collection} of {@link Cycle}
   */
  public Collection<Cycle> findCycleListByCompanyId(long companyId) {
    Company company = findById(companyId);
    ArrayList<Cycle> cycleList = new ArrayList<>();
    for (Company currentCompany : company.getHistory().getCompanies()) {
      cycleList.add(currentCompany.getCycle());
    }
    return cycleList;
  }

  public Collection<SubStructure> findSubStructuresOfCompany(long companyId) {
    Company company = findById(companyId);
    return company.getSubStructures();
  }

  /**
   * Creates a Company.
   *
   * @param company a {@link Company} object
   * @param user an {@link User} object
   * @return a {@link Company} object
   */
  @Transactional
  public Company createCompany(Company company, User user) {
    Cycle cycle = new Cycle("default");
    cycle.setCycleState(CycleState.ACTIVE);
    cycle.setVisible(true);
    cycleRepository.save(cycle);
    company.setCycle(cycle);

    CompanyHistory history = new CompanyHistory();
    companyHistoryRepository.save(history);
    company.setHistory(history);

    company = companyRepository.save(company);
    logger.info("Created Company " + company.getName());
    activityService.createActivity(user, company, Action.CREATED);

    return company;
  }

  /**
   * Updates a Company.
   *
   * @param updatedCompany a {@link Company} object
   * @param user an {@link User} object
   * @return a {@link Company} object
   */
  @Transactional
  public Company updateCompany(Company updatedCompany, User user) {
    Company referencedCompany = companyRepository.findByIdOrThrow(updatedCompany.getId());

    throwIfCompanyInClosedCycle(referencedCompany);

    final String oldName = referencedCompany.getName();
    referencedCompany.setName(updatedCompany.getName());
    referencedCompany.setLabel(updatedCompany.getLabel());

    referencedCompany = companyRepository.save(referencedCompany);
    logger.info(
        "Updated Company "
            + oldName
            + " (id: "
            + referencedCompany.getId()
            + "), updated name to: +"
            + referencedCompany.getName());
    activityService.createActivity(user, referencedCompany, Action.EDITED);

    return referencedCompany;
  }

  /**
   * Deletes a Company.
   *
   * @param companyId a long value
   * @param deleteWholeHistory a boolean value
   * @param user an {@link User} object
   */
  @Transactional
  public void deleteCompany(Long companyId, boolean deleteWholeHistory, User user) {
    Company company = findById(companyId);
    Collection<Company> companiesToDelete = new ArrayList<>();

    if (deleteWholeHistory && company.getHistory() != null) {
      companiesToDelete = company.getHistory().getCompanies();
    } else {
      companiesToDelete.add(company);
    }

    deleteCompanyList(companiesToDelete, user);
  }

  private void deleteCompanyList(Collection<Company> companiesToDelete, User user) {
    for (Company companyToDelete : companiesToDelete) {

      companyRepository.deleteById(companyToDelete.getId());
      logger.info("Deleted company with id: " + companyToDelete.getId());
      activityService.createActivity(user, companyToDelete, Action.DELETED);

      Cycle cycle = companyToDelete.getCycle();
      if (cycle != null && cycle.getCompanies() != null && cycle.getCompanies().isEmpty()) {
        deleteEmptyCycle(cycle, user);
      }

      CompanyHistory history = companyToDelete.getHistory();
      if (history != null && history.getCompanies() != null && history.getCompanies().isEmpty()) {
        deleteEmptyHistory(history, user);
      }
    }
  }

  /**
   * Create a Department.
   *
   * @param companyId a long value
   * @param department a {@link Department} object
   * @param user an {@link User} object
   * @return a {@link Department} object
   */
  @Transactional
  public Department createDepartment(Long companyId, Department department, User user) {
    Company referencedCompany = companyRepository.findByIdOrThrow(companyId);

    throwIfCompanyInClosedCycle(referencedCompany);

    department.setParentStructure(referencedCompany);

    department = structureRepository.save(department);
    logger.info(
        "Created department "
            + department.getName()
            + " into company: "
            + referencedCompany.getName()
            + "(id:"
            + companyId
            + ")");
    activityService.createActivity(user, department, Action.CREATED);

    return department;
  }

  public CorporateObjectiveStructure createCorporateObjectiveStructure(
      Long companyId, CorporateObjectiveStructure corporateObjectiveStructure, User user) {
    Company referencedCompany = companyRepository.findByIdOrThrow(companyId);

    throwIfCompanyInClosedCycle(referencedCompany);

    corporateObjectiveStructure.setParentStructure(referencedCompany);

    corporateObjectiveStructure = structureRepository.save(corporateObjectiveStructure);
    logger.info(
        "Created corporateObjectiveStructure: "
            + corporateObjectiveStructure.getName()
            + " into Company "
            + referencedCompany.getName()
            + "(id:"
            + companyId
            + ")");
    activityService.createActivity(user, corporateObjectiveStructure, Action.CREATED);
    return corporateObjectiveStructure;
  }

  private void throwIfCompanyInClosedCycle(Company companyToCheck) {
    if (entityCrawlerService.getCycleOfCompany(companyToCheck).getCycleState()
        == CycleState.CLOSED) {
      throw new ForbiddenException("Cannot modify this resource on a Company in a closed cycle.");
    }
  }

  private void deleteEmptyCycle(Cycle cycle, User user) {
    cycleRepository.deleteById(cycle.getId());
    logger.info("Deleted cycle with id: " + cycle.getId());
    activityService.createActivity(user, cycle, Action.DELETED);
  }

  private void deleteEmptyHistory(CompanyHistory history, User user) {
    companyHistoryRepository.deleteById(history.getId());
    logger.info("Deleted cycle with id: " + history.getId());
    activityService.createActivity(user, history, Action.DELETED);
  }
}
