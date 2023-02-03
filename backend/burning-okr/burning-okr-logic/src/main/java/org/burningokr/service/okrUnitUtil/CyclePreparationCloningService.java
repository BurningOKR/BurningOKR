package org.burningokr.service.okrUnitUtil;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okrUnits.*;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.burningokr.service.okr.TaskBoardService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CyclePreparationCloningService {

  private final Logger logger = LoggerFactory.getLogger(CyclePreparationCloningService.class);
  private final CompanyRepository companyRepository;
  private final UnitRepository<OkrChildUnit> subUnitRepository;
  private final OkrDepartmentRepository okrDepartmentRepository;
  private final ObjectiveRepository objectiveRepository;
  private final UserSettingsRepository userSettingsRepository;
  private final TaskBoardService taskBoardService;
  private HashMap<Objective, Objective> clonedObjectives = new HashMap<>();

  public void cloneCompanyListIntoCycleForPreparation(
    Collection<OkrCompany> companiesToClone, Cycle cycleToCloneInto
  ) {
    companiesToClone.forEach(
      company -> cloneCompanyIntoCycleForPreparation(company, cycleToCloneInto));
  }

  /**
   * Clones the OkrCompany and adds it to the Cycle.
   *
   * @param okrCompanyToClone a {@link OkrCompany} object
   * @param cycleToCloneInto  a {@link Cycle} object
   */
  public void cloneCompanyIntoCycleForPreparation(
    OkrCompany okrCompanyToClone, Cycle cycleToCloneInto
  ) {
    OkrCompany okrCompanyCopy = okrCompanyToClone.getCopyWithoutRelations();
    cycleToCloneInto.getCompanies().add(okrCompanyCopy);
    okrCompanyCopy.setCycle(cycleToCloneInto);
    companyRepository.save(okrCompanyCopy);
    cloneObjectiveListIntoOkrUnitForPreparation(okrCompanyToClone.getObjectives(), okrCompanyCopy);
    cloneChildUnitListIntoParentUnitForPreparation(
      okrCompanyToClone.getOkrChildUnits(), okrCompanyCopy);
    cloneUserSettingsFromClonedCompanyIntoOkrBranchForPreparation(
      okrCompanyToClone, okrCompanyCopy);
  }

  private void cloneChildUnitListIntoParentUnitForPreparation(
    Collection<OkrChildUnit> okrChildUnitListToClone, OkrUnit okrUnitToCloneInto
  ) {
    okrChildUnitListToClone.forEach(
      original -> cloneChildUnitIntoParentUnitForPreparation(original, okrUnitToCloneInto));
  }

  @Transactional
  void cloneChildUnitIntoParentUnitForPreparation(
    OkrChildUnit okrChildUnitToClone, OkrUnit okrUnitToCloneInto
  ) {
    OkrChildUnit copy = okrChildUnitToClone.getCopyWithoutRelations();

    if (okrUnitToCloneInto instanceof OkrParentUnit) {
      copy.setParentOkrUnit(okrUnitToCloneInto);
      ((OkrParentUnit) okrUnitToCloneInto).getOkrChildUnits().add(copy);
    }

    subUnitRepository.save(copy);

    if (okrChildUnitToClone instanceof OkrDepartment) {
      this.logOkrDepartment((OkrDepartment) copy);

      TaskBoard newTaskBoard =
        taskBoardService.cloneTaskBoard(
          (OkrDepartment) copy, ((OkrDepartment) okrChildUnitToClone).getTaskBoard());
      taskBoardService.saveTaskBoard(newTaskBoard);

      subUnitRepository.save(copy);
    }

    this.logger.info("cloneChildUnitIntoParentUnitForPreparation - after taskboard");
    cloneObjectiveListIntoOkrUnitForPreparation(okrChildUnitToClone.getObjectives(), copy);

    if (okrChildUnitToClone instanceof OkrParentUnit) {
      cloneChildUnitListIntoParentUnitForPreparation(
        ((OkrParentUnit) okrChildUnitToClone).getOkrChildUnits(), copy);
    }
  }

  private void cloneObjectiveListIntoOkrUnitForPreparation(
    Collection<Objective> objectiveListToClone, OkrUnit okrUnitToCloneInto
  ) {
    for (Objective original : objectiveListToClone) {
      cloneObjectiveIntoOkrUnitForPreparation(original, okrUnitToCloneInto);
    }
  }

  private void cloneObjectiveIntoOkrUnitForPreparation(
    Objective objectiveToClone, OkrUnit okrUnitToCloneInto
  ) {
    Objective copy = objectiveToClone.getCopyWithoutRelations();
    copy.setParentOkrUnit(okrUnitToCloneInto);
    okrUnitToCloneInto.getObjectives().add(copy);
    if (objectiveToClone.hasParentObjective()) {
      Objective clonedParentObjective = clonedObjectives.get(objectiveToClone.getParentObjective());
      if (clonedParentObjective != null) {
        copy.setParentObjective(clonedParentObjective);
      }
    }
    clonedObjectives.put(objectiveToClone, copy);
    objectiveRepository.save(copy);
  }

  private void cloneUserSettingsFromClonedCompanyIntoOkrBranchForPreparation(
    OkrCompany okrCompanyToClone, OkrCompany okrCompanyCopy
  ) {
    Iterable<UserSettings> userSettingsIter = userSettingsRepository.findAll();
    for (UserSettings userSettings : userSettingsIter) {
      if (userSettings.getDefaultOkrCompany() != null
        && userSettings.getDefaultOkrCompany().equals(okrCompanyToClone)) {
        userSettings.setDefaultOkrCompany(okrCompanyCopy);
        if (userSettings.getDefaultTeam() != null) {
          OkrDepartment newTeamCopy =
            findNewTeamCopy(userSettings.getDefaultTeam(), okrCompanyCopy);
          userSettings.setDefaultTeam(newTeamCopy);
        }
        userSettingsRepository.save(userSettings);
      }
    }
  }

  private OkrDepartment findNewTeamCopy(OkrDepartment oldTeam, OkrCompany okrCompany) {
    Collection<OkrDepartment> okrDepartments = BranchHelper.collectDepartments(okrCompany);

    for (OkrDepartment okrDepartment : okrDepartments) {
      if (okrDepartment.getName().equals(oldTeam.getName())) {
        return okrDepartment;
      }
    }

    return null;
  }

  private void logOkrDepartment(OkrDepartment okrDepartment) {
    this.logger.info("Id" + okrDepartment.getId() + " Name: " + okrDepartment.getName());
  }
}
