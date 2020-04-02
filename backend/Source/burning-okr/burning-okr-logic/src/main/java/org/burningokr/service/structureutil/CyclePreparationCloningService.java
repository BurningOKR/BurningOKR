package org.burningokr.service.structureutil;

import java.util.Collection;
import java.util.HashMap;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.CompanyStructure;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.burningokr.repositories.structre.CompanyRepository;
import org.burningokr.repositories.structre.CorporateObjectiveStructureRepository;
import org.burningokr.repositories.structre.DepartmentRepository;
import org.burningokr.service.structure.departmentservices.DepartmentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CyclePreparationCloningService {

  private HashMap<Objective, Objective> clonedObjectives = new HashMap<>();

  private CompanyRepository companyRepository;
  private CorporateObjectiveStructureRepository corporateObjectiveStructureRepository;
  private DepartmentRepository departmentRepository;
  private ObjectiveRepository objectiveRepository;
  private UserSettingsRepository userSettingsRepository;

  /**
   * Initialize CyclePreparationCloningService.
   *
   * @param companyRepository a {@link CompanyRepository} object
   * @param corporateObjectiveStructureRepository a {@link CorporateObjectiveStructureRepository}
   *     object
   * @param departmentRepository a {@link DepartmentRepository} object
   * @param objectiveRepository an {@link ObjectiveRepository} object
   * @param userSettingsRepository an {@link UserSettingsRepository} object
   */
  @Autowired
  public CyclePreparationCloningService(
      CompanyRepository companyRepository,
      CorporateObjectiveStructureRepository corporateObjectiveStructureRepository,
      DepartmentRepository departmentRepository,
      ObjectiveRepository objectiveRepository,
      UserSettingsRepository userSettingsRepository) {
    this.companyRepository = companyRepository;
    this.corporateObjectiveStructureRepository = corporateObjectiveStructureRepository;
    this.departmentRepository = departmentRepository;
    this.objectiveRepository = objectiveRepository;
    this.userSettingsRepository = userSettingsRepository;
  }

  public void cloneCompanyListIntoCycleForPreparation(
      Collection<Company> companiesToClone, Cycle cycleToCloneInto) {
    companiesToClone.forEach(
        company -> cloneCompanyIntoCycleForPreparation(company, cycleToCloneInto));
  }

  /**
   * Clones the Company and adds it to the Cycle.
   *
   * @param companyToClone a {@link Company} object
   * @param cycleToCloneInto a {@link Cycle} object
   */
  public void cloneCompanyIntoCycleForPreparation(Company companyToClone, Cycle cycleToCloneInto) {
    Company companyCopy = companyToClone.getCopyWithoutRelations();
    cycleToCloneInto.getCompanies().add(companyCopy);
    companyCopy.setCycle(cycleToCloneInto);
    companyRepository.save(companyCopy);
    cloneObjectiveListIntoCompanyStructureForPreparation(
        companyToClone.getObjectives(), companyCopy);
    cloneCorporateObjectiveStructuresIntoCompanyStructureForPreparation(
        companyToClone.getCorporateObjectiveStructures(), companyCopy);
    cloneDepartmentListIntoCompanyStructureForPreparation(
        companyToClone.getDepartments(), companyCopy);
    cloneUserSettingsFromClonedCompanyIntoCompanyStructureForPreparation(
        companyToClone, companyCopy);
  }

  private void cloneCorporateObjectiveStructuresIntoCompanyStructureForPreparation(
      Collection<CorporateObjectiveStructure> corporateObjectiveStructuresToClone,
      Company companyStructureToCloneInto) {
    corporateObjectiveStructuresToClone.forEach(
        original ->
            cloneCorporateObjectiveStructureIntoCompanyStructureForPreparation(
                original, companyStructureToCloneInto));
  }

  private void cloneCorporateObjectiveStructureIntoCompanyStructureForPreparation(
      CorporateObjectiveStructure corporateObjectiveStructureToClone,
      Company companyStructureToCloneInto) {
    CorporateObjectiveStructure copy = corporateObjectiveStructureToClone.getCopyWithOutRelations();
    copy.setParentStructure(companyStructureToCloneInto);
    companyStructureToCloneInto.getCorporateObjectiveStructures().add(copy);
    corporateObjectiveStructureRepository.save(copy);
    cloneObjectiveListIntoCompanyStructureForPreparation(
        corporateObjectiveStructureToClone.getObjectives(), copy);
    cloneCorporateObjectiveStructuresIntoCompanyStructureForPreparation(
        corporateObjectiveStructureToClone.getCorporateObjectiveStructures(),
        companyStructureToCloneInto);
    cloneDepartmentListIntoCompanyStructureForPreparation(
        corporateObjectiveStructureToClone.getDepartments(), companyStructureToCloneInto);
  }

  private void cloneDepartmentListIntoCompanyStructureForPreparation(
      Collection<Department> departmentListToClone, CompanyStructure companyStructureToCloneInto) {
    departmentListToClone.forEach(
        original ->
            cloneDepartmentIntoCompanyStructureForPreparation(
                original, companyStructureToCloneInto));
  }

  private void cloneDepartmentIntoCompanyStructureForPreparation(
      Department departmentToClone, CompanyStructure companyStructureToCloneInto) {
    Department copy = departmentToClone.getCopyWithoutRelations();
    copy.setParentStructure(companyStructureToCloneInto);
    companyStructureToCloneInto.getDepartments().add(copy);
    departmentRepository.save(copy);
    cloneObjectiveListIntoCompanyStructureForPreparation(departmentToClone.getObjectives(), copy);
    cloneDepartmentListIntoCompanyStructureForPreparation(departmentToClone.getDepartments(), copy);
  }

  private void cloneObjectiveListIntoCompanyStructureForPreparation(
      Collection<Objective> objectiveListToClone, CompanyStructure companyStructureToCloneInto) {
    for (Objective original : objectiveListToClone) {
      cloneObjectiveIntoCompanyStructureForPreparation(original, companyStructureToCloneInto);
    }
  }

  private void cloneObjectiveIntoCompanyStructureForPreparation(
      Objective objectiveToClone, CompanyStructure companyStructureToCloneInto) {
    Objective copy = objectiveToClone.getCopyWithoutRelations();
    copy.setParentStructure(companyStructureToCloneInto);
    companyStructureToCloneInto.getObjectives().add(copy);
    if (objectiveToClone.hasParentObjective()) {
      Objective clonedParentObjective = clonedObjectives.get(objectiveToClone.getParentObjective());
      if (clonedParentObjective != null) {
        copy.setParentObjective(clonedParentObjective);
      }
    }
    clonedObjectives.put(objectiveToClone, copy);
    objectiveRepository.save(copy);
  }

  private void cloneUserSettingsFromClonedCompanyIntoCompanyStructureForPreparation(
      Company companyToClone, Company companyCopy) {
    Iterable<UserSettings> userSettingsIter = userSettingsRepository.findAll();
    for (UserSettings userSettings : userSettingsIter) {
      if (userSettings.getDefaultCompany() != null
          && userSettings.getDefaultCompany().equals(companyToClone)) {
        userSettings.setDefaultCompany(companyCopy);
        if (userSettings.getDefaultTeam() != null) {
          Department newTeamCopy = findNewTeamCopy(userSettings.getDefaultTeam(), companyCopy);
          userSettings.setDefaultTeam(newTeamCopy);
        }
        userSettingsRepository.save(userSettings);
      }
    }
  }

  private Department findNewTeamCopy(Department oldTeam, Company company) {
    Collection<Department> departments = DepartmentHelper.collectDepartments(company);

    for (Department department : departments) {
      if (department.getName().equals(oldTeam.getName())) {
        return department;
      }
    }

    return null;
  }
}
