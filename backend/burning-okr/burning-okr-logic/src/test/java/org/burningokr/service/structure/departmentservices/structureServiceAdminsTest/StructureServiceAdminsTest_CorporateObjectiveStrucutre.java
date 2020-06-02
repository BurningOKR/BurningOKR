package org.burningokr.service.structure.departmentservices.structureServiceAdminsTest;

import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.service.exceptions.InvalidDeleteRequestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

  @Test(expected = InvalidDeleteRequestException.class)
  public void deleteStructureWithSubStructures_expectsInvalidDeleteRequestException() {
    CorporateObjectiveStructure corporateObjectiveStructure = new CorporateObjectiveStructure();
    corporateObjectiveStructure.setId(1337L);
    List<SubStructure> subStructures = new ArrayList<>();
    subStructures.add(new Department());
    corporateObjectiveStructure.setSubStructures(subStructures);

    when(structureRepository.findByIdOrThrow(anyLong())).thenReturn(corporateObjectiveStructure);

    structureServiceAdmins.deleteStructure(1337L, user);
  }
}
