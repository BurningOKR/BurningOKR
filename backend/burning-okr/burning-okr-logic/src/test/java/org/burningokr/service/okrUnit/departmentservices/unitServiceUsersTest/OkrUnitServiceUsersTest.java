package org.burningokr.service.okrUnit.departmentservices.unitServiceUsersTest;

import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.users.User;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceUsers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

public abstract class OkrUnitServiceUsersTest<T extends OkrChildUnit> {

  @Mock private User user;
  @InjectMocks OkrUnitServiceUsers<T> departmentServiceUsers;

  private T OkrChildUnit;

  protected abstract T createUnit();

  @Before
  public void setup() {
    OkrChildUnit = createUnit();
  }

  @Test(expected = UnauthorizedUserException.class)
  public void updateUnit_expectedThrow() {
    departmentServiceUsers.updateUnit(OkrChildUnit, user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createObjective_expectedThrow() {
    departmentServiceUsers.createObjective(100L, new Objective(), user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createChildUnit_expectedThrow() {
    departmentServiceUsers.createChildUnit(100L, OkrChildUnit, user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void removeUnit_expectedThrow() {
    departmentServiceUsers.deleteUnit(100L, user);
  }
}
