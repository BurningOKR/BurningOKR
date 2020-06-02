package org.burningokr.service.structure.departmentservices.structureServiceManagersTest;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.junit.Test;

public class StructureServiceManagersTest_CorporateObjectiveStructure
    extends StructureServiceManagersTest<CorporateObjectiveStructure> {

  @Override
  protected CorporateObjectiveStructure createStructure() {
    return new CorporateObjectiveStructure();
  }

  @Test
  public void updateCorporateObjectiveStructure_expectedAllVariablesUnchanged() {
    CorporateObjectiveStructure insertedCorporateObjectiveStructure =
        new CorporateObjectiveStructure();

    insertedCorporateObjectiveStructure.setId(100L);
    insertedCorporateObjectiveStructure.setName("Insert");
    insertedCorporateObjectiveStructure.setSubStructures(new ArrayList<>());

    CorporateObjectiveStructure insertedParentStructure = new CorporateObjectiveStructure();
    insertedParentStructure.setName("insertedParentStructure");
    insertedCorporateObjectiveStructure.setParentStructure(insertedParentStructure);
    insertedCorporateObjectiveStructure.setObjectives(new ArrayList<>());

    CorporateObjectiveStructure originalCorporateObjectiveStructure =
        new CorporateObjectiveStructure();

    originalCorporateObjectiveStructure.setId(100L);
    originalCorporateObjectiveStructure.setName("Original");
    originalCorporateObjectiveStructure.setSubStructures(new ArrayList<>());
    Department originalParentStructure = new Department();
    originalParentStructure.setName("originalParentStructure");
    originalCorporateObjectiveStructure.setParentStructure(originalParentStructure);
    originalCorporateObjectiveStructure.setObjectives(new ArrayList<>());

    structureServiceManagers.updateStructure(insertedCorporateObjectiveStructure, user);

    verify(structureRepository, times(0)).save(originalCorporateObjectiveStructure);
  }

  @Test
  public void updateCorporateObjectiveStructure_expectNoSaveCall() {
    CorporateObjectiveStructure insertedDepartment = new CorporateObjectiveStructure();
    insertedDepartment.setId(100L);
    insertedDepartment.setName("Test");

    CorporateObjectiveStructure originalDepartment = new CorporateObjectiveStructure();
    originalDepartment.setId(100L);
    originalDepartment.setName("Test");

    CorporateObjectiveStructure actualDepartment =
        structureServiceManagers.updateStructure(insertedDepartment, user);

    verify(structureRepository, times(0)).save(originalDepartment);
  }
}
