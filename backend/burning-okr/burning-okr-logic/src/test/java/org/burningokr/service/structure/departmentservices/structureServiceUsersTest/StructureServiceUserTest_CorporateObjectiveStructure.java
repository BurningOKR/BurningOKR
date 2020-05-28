package org.burningokr.service.structure.departmentservices.structureServiceUsersTest;

import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StructureServiceUserTest_CorporateObjectiveStructure
    extends StructureServiceUsersTest<CorporateObjectiveStructure> {

  @Override
  protected CorporateObjectiveStructure createStructure() {
    return new CorporateObjectiveStructure();
  }
}
