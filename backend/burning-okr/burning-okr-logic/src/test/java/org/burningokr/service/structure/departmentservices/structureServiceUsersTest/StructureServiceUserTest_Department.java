package org.burningokr.service.structure.departmentservices.structureServiceUsersTest;

import org.burningokr.model.structures.Department;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StructureServiceUserTest_Department extends StructureServiceUsersTest<Department> {
  @Override
  protected Department createStructure() {
    return new Department();
  }
}
