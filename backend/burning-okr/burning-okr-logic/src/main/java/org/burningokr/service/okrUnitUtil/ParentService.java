package org.burningokr.service.okrUnitUtil;

import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnitSchema;
import org.burningokr.model.okrUnits.OkrUnit;
import org.springframework.stereotype.Service;

@Service
public class ParentService {

  /**
   * Validates if an {@link Objective} is a parent of another {@link Objective}.
   *
   * @param childObjective  a {@link Objective} object
   * @param parentObjective a {@link Objective} object
   */
  public void validateParentObjective(Objective childObjective, Objective parentObjective) {
    if (!isParentObjectiveLegal(childObjective, parentObjective)) {
      throw new IllegalArgumentException(
        "Objective with id " + parentObjective.getId() + " is not suitable as parentObjective.");
    }
  }

  /**
   * Test if an Objective is a child of another Objective.
   *
   * @param childObjective  an {@link Objective} object
   * @param parentObjective an {@link Objective} object
   * @return a boolean value
   */
  public boolean isParentObjectiveLegal(Objective childObjective, Objective parentObjective) {
    OkrUnit currentOkrUnit = childObjective.getParentOkrUnit();
    OkrUnit parentOkrUnit = parentObjective.getParentOkrUnit();

    return isUnitAChildUnit(currentOkrUnit, parentOkrUnit);
  }

  /**
   * Test if a okrUnit is a Child of another okrUnit.
   *
   * @param childOkrUnit  a {@link OkrUnit} object
   * @param parentOkrUnit a {@link OkrUnit} object
   * @return a boolean value
   */
  public boolean isUnitAChildUnit(OkrUnit childOkrUnit, OkrUnit parentOkrUnit) {
    OkrUnit currentOkrUnit = childOkrUnit;
    while (currentOkrUnit instanceof OkrChildUnitSchema) {
      currentOkrUnit = ((OkrChildUnitSchema) currentOkrUnit).getParentOkrUnit();
      if (currentOkrUnit == parentOkrUnit) {
        return true;
      }
    }
    return false;
  }
}
