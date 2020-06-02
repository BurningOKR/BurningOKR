package org.burningokr.service.structureutil;

import java.util.Collection;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.structures.*;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.burningokr.repositories.structre.CompanyRepository;
import org.burningokr.repositories.structre.StructureRepository;
import org.burningokr.service.structure.departmentservices.StructureHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@RequiredArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CyclePreparationCloningService {

  private HashMap<Objective, Objective> clonedObjectives = new HashMap<>();

  private final CompanyRepository companyRepository;
  private final StructureRepository<SubStructure> subStructureRepository;
  private final ObjectiveRepository objectiveRepository;
  private final UserSettingsRepository userSettingsRepository;

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
    cloneSubStructureListIntoParentStructureForPreparation(
        companyToClone.getSubStructures(), companyCopy);
    cloneUserSettingsFromClonedCompanyIntoCompanyStructureForPreparation(
        companyToClone, companyCopy);
  }

  private void cloneSubStructureListIntoParentStructureForPreparation(
      Collection<SubStructure> subStructureListToClone, Structure structureToCloneInto) {
    subStructureListToClone.forEach(
        original ->
            cloneSubStructureIntoParentStructureForPreparation(original, structureToCloneInto));
  }

  private void cloneSubStructureIntoParentStructureForPreparation(
      SubStructure subStructureToClone, Structure structureToCloneInto) {
    SubStructure copy = subStructureToClone.getCopyWithoutRelations();

    if (structureToCloneInto instanceof ParentStructure) {
      copy.setParentStructure(structureToCloneInto);
      ((ParentStructure) structureToCloneInto).getSubStructures().add(copy);
    }

    subStructureRepository.save(copy);
    cloneObjectiveListIntoCompanyStructureForPreparation(subStructureToClone.getObjectives(), copy);

    if (subStructureToClone instanceof ParentStructure) {
      cloneSubStructureListIntoParentStructureForPreparation(
          ((ParentStructure) subStructureToClone).getSubStructures(), copy);
    }
  }

  private void cloneObjectiveListIntoCompanyStructureForPreparation(
      Collection<Objective> objectiveListToClone, Structure structureToCloneInto) {
    for (Objective original : objectiveListToClone) {
      cloneObjectiveIntoCompanyStructureForPreparation(original, structureToCloneInto);
    }
  }

  private void cloneObjectiveIntoCompanyStructureForPreparation(
      Objective objectiveToClone, Structure structureToCloneInto) {
    Objective copy = objectiveToClone.getCopyWithoutRelations();
    copy.setParentStructure(structureToCloneInto);
    structureToCloneInto.getObjectives().add(copy);
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
    Collection<Department> departments = StructureHelper.collectDepartments(company);

    for (Department department : departments) {
      if (department.getName().equals(oldTeam.getName())) {
        return department;
      }
    }

    return null;
  }
}
