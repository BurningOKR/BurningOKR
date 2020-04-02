package org.burningokr.service.structureutil;

import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.ChildStructure;
import org.burningokr.model.structures.CompanyStructure;
import org.springframework.stereotype.Service;

@Service
public class ParentService {

  /**
   * Validates if an {@link Objective} is a parent of another {@link Objective}.
   *
   * @param childObjective a {@link Objective} object
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
   * @param childObjective an {@link Objective} object
   * @param parentObjective an {@link Objective} object
   * @return a boolean value
   */
  public boolean isParentObjectiveLegal(Objective childObjective, Objective parentObjective) {
    CompanyStructure currentStructure = childObjective.getParentStructure();
    CompanyStructure parentStructure = parentObjective.getParentStructure();

    return isStructureChildStructure(currentStructure, parentStructure);
  }

  /**
   * Test if a Structure is a Child of another Structure.
   *
   * @param childStructure a {@link CompanyStructure} object
   * @param parentStructure a {@link CompanyStructure} object
   * @return a boolean value
   */
  public boolean isStructureChildStructure(
      CompanyStructure childStructure, CompanyStructure parentStructure) {
    CompanyStructure currentStructure = childStructure;
    while (currentStructure instanceof ChildStructure) {
      currentStructure = ((ChildStructure) currentStructure).getParentStructure();
      if (currentStructure == parentStructure) {
        return true;
      }
    }
    return false;
  }
}
