package org.burningokr.service.structure.departmentservices.structureServiceUsersTest;

import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.model.users.User;
import org.burningokr.service.structure.departmentservices.StructureServiceUsers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

public abstract class StructureServiceUsersTest<T extends SubStructure> {

  @Mock private User user;
  @InjectMocks StructureServiceUsers<T> departmentServiceUsers;

  private T structure;

  protected abstract T createStructure();

  @Before
  public void setup() {
    structure = createStructure();
  }

  @Test(expected = UnauthorizedUserException.class)
  public void updateStructure_expectedThrow() {
    departmentServiceUsers.updateStructure(structure, user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createObjective_expectedThrow() {
    departmentServiceUsers.createObjective(100L, new Objective(), user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createSubStructure_expectedThrow() {
    departmentServiceUsers.createSubstructure(100L, structure, user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void removeStructure_expectedThrow() {
    departmentServiceUsers.deleteStructure(100L, user);
  }
}
