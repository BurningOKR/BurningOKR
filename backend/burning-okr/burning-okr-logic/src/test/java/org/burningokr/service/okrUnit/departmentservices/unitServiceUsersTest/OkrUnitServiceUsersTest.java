package org.burningokr.service.okrUnit.departmentservices.unitServiceUsersTest;

import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceUsers;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

public abstract class OkrUnitServiceUsersTest<T extends OkrChildUnit> {

  @Mock protected UnitRepository<T> unitRepository;
  @Mock protected ObjectiveRepository objectiveRepository;
  @Mock protected OkrTopicDraftRepository okrTopicDraftRepository;
  @Mock protected ActivityService activityService;
  @Mock protected EntityCrawlerService entityCrawlerService;
  @Mock private User user;
  @InjectMocks OkrUnitServiceUsers<T> okrUnitServiceUsers;

  private T unit;

  protected abstract T createUnit();

  @Before
  public void setup() {
    unit = createUnit();

    unit.setId(1L);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void updateUnit_expectedThrow() {
    okrUnitServiceUsers.updateUnit(unit, user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createObjective_expectedThrow() {
    okrUnitServiceUsers.createObjective(100L, new Objective(), user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createChildUnit_expectedThrow() {
    okrUnitServiceUsers.createChildUnit(100L, unit, user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void removeUnit_expectedThrow() {
    okrUnitServiceUsers.deleteUnit(100L, user);
  }
}
