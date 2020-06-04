package org.burningokr.service.structureutil;

import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.Structure;
import org.burningokr.model.structures.SubStructure;
import org.springframework.stereotype.Service;

@Service
public class EntityCrawlerService {

  public Cycle getCycleOfCompany(Company companyToCheck) {
    return companyToCheck.getCycle();
  }

  /**
   * Gets the Cycle of a Department.
   *
   * @param departmentToCheck a {@link Department} object
   * @return a {@link Cycle} object
   */
  public Cycle getCycleOfStructure(Structure departmentToCheck) {
    if (departmentToCheck instanceof Company) {
      return this.getCycleOfCompany((Company) departmentToCheck);
    } else {
      return this.getCycleOfStructure(((SubStructure) departmentToCheck).getParentStructure());
    }
  }

  private Cycle getCycleOfParentStructure(Structure parentStructure) {
    if (parentStructure instanceof Company) {
      return this.getCycleOfCompany((Company) parentStructure);
    } else {
      return this.getCycleOfStructure((SubStructure) parentStructure);
    }
  }

  public Cycle getCycleOfObjective(Objective objectiveToCheck) {
    return this.getCycleOfParentStructure(objectiveToCheck.getParentStructure());
  }

  public Cycle getCycleOfKeyResult(KeyResult keyResultToCheck) {
    return this.getCycleOfObjective(keyResultToCheck.getParentObjective());
  }

  /**
   * Gets the Company of a Department.
   *
   * @param departmentToCheck a {@link Structure} object
   * @return a {@link Company} object
   */
  public Company getCompanyOfStructure(SubStructure departmentToCheck) {
    Structure parentStructure = departmentToCheck.getParentStructure();
    if (parentStructure instanceof Company) {
      return (Company) parentStructure;
    } else {
      return this.getCompanyOfStructure((SubStructure) parentStructure);
    }
  }
}
