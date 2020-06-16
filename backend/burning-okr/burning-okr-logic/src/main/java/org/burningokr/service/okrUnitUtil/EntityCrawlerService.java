package org.burningokr.service.okrUnitUtil;

import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.springframework.stereotype.Service;

@Service
public class EntityCrawlerService {

  public Cycle getCycleOfCompany(OkrCompany okrCompanyToCheck) {
    return okrCompanyToCheck.getCycle();
  }

  /**
   * Gets the Cycle of a OkrDepartment.
   *
   * @param departmentToCheck a {@link OkrDepartment} object
   * @return a {@link Cycle} object
   */
  public Cycle getCycleOfUnit(OkrUnit departmentToCheck) {
    if (departmentToCheck instanceof OkrCompany) {
      return this.getCycleOfCompany((OkrCompany) departmentToCheck);
    } else {
      return this.getCycleOfUnit(((OkrChildUnit) departmentToCheck).getParentOkrUnit());
    }
  }

  private Cycle getCycleOfParentUnit(OkrUnit parentOkrUnit) {
    if (parentOkrUnit instanceof OkrCompany) {
      return this.getCycleOfCompany((OkrCompany) parentOkrUnit);
    } else {
      return this.getCycleOfUnit((OkrChildUnit) parentOkrUnit);
    }
  }

  public Cycle getCycleOfObjective(Objective objectiveToCheck) {
    return this.getCycleOfParentUnit(objectiveToCheck.getParentOkrUnit());
  }

  public Cycle getCycleOfKeyResult(KeyResult keyResultToCheck) {
    return this.getCycleOfObjective(keyResultToCheck.getParentObjective());
  }

  /**
   * Gets the OkrCompany of a OkrDepartment.
   *
   * @param departmentToCheck a {@link OkrUnit} object
   * @return a {@link OkrCompany} object
   */
  public OkrCompany getCompanyOfUnit(OkrChildUnit departmentToCheck) {
    OkrUnit parentOkrUnit = departmentToCheck.getParentOkrUnit();
    if (parentOkrUnit instanceof OkrCompany) {
      return (OkrCompany) parentOkrUnit;
    } else {
      return this.getCompanyOfUnit((OkrChildUnit) parentOkrUnit);
    }
  }
}
