package org.burningokr.service.structure.departmentservices.structureServiceAdminsTest;

import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StructureServiceAdminsTest_CorporateObjectiveStrucutre
    extends StructureServiceAdminsTest<CorporateObjectiveStructure> {

  @Override
  protected CorporateObjectiveStructure createStructure() {
    return new CorporateObjectiveStructure();
  }

  @Override
  protected Class<CorporateObjectiveStructure> getStructureClass() {
    return CorporateObjectiveStructure.class;
  }
}
