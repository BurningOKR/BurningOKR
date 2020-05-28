package org.burningokr.service.structure.departmentservices;

import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceUsersTest {

  @Mock private User user;
  @InjectMocks StructureServiceUsers<Department> departmentServiceUsers;

  @Test(expected = UnauthorizedUserException.class)
  public void updateDepartment_expectedThrow() {
    departmentServiceUsers.updateStructure(new Department(), user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createObjective_expectedThrow() {
    departmentServiceUsers.createObjective(100L, new Objective(), user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createSubdepartment_expectedThrow() {
    departmentServiceUsers.createSubstructure(100L, new Department(), user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void removeDepartment_expectedThrow() {
    departmentServiceUsers.deleteStructure(100L, user);
  }
}
