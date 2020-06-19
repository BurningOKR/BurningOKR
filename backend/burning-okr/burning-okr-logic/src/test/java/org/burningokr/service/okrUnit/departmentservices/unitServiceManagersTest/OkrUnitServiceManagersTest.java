package org.burningokr.service.okrUnit.departmentservices.unitServiceManagersTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceManagers;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

@RunWith(MockitoJUnitRunner.class)
public abstract class OkrUnitServiceManagersTest<T extends OkrChildUnit> {

  protected final Long departmentId = 1337L;
  protected final String departmentName = "Java Academy";
  protected T unit;

  @Mock protected UnitRepository<T> unitRepository;
  @Mock protected ObjectiveRepository objectiveRepository;
  @Mock protected ActivityService activityService;
  @Mock protected EntityCrawlerService entityCrawlerService;
  @Mock protected User user;
  @InjectMocks protected OkrUnitServiceManagers<T> okrUnitServiceManagers;

  protected abstract T createUnit();

  @Before
  public void setUp() {
    this.unit = createUnit();
    unit.setId(departmentId);
    unit.setName(departmentName);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(activeCycle);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createChildUnit_expectedThrow() {
    okrUnitServiceManagers.createChildUnit(departmentId, createUnit(), user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void removeUnit_expectedThrow() {
    okrUnitServiceManagers.deleteUnit(departmentId, user);
  }

  @Test
  public void createObjective_expectsParentUnitIdIsSet() {
    Objective objective = new Objective();
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    when(unitRepository.findByIdOrThrow(any(Long.class))).thenReturn(unit);

    okrUnitServiceManagers.createObjective(departmentId, objective, user);

    Assert.assertNotNull(objective.getParentOkrUnit().getId());
    Assert.assertEquals(departmentId, objective.getParentOkrUnit().getId());
  }

  @Test
  public void createObjective_expectsOtherObjectiveSequenceAdvanced() {
    Objective objective = new Objective();
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    Objective otherObjective0 = new Objective();
    otherObjective0.setSequence(5);
    Objective otherObjective1 = new Objective();
    otherObjective1.setSequence(6);
    Objective otherObjective2 = new Objective();
    otherObjective2.setSequence(10);

    T savedUnit = createUnit();
    Collection<Objective> otherObjectives =
        Arrays.asList(otherObjective0, otherObjective1, otherObjective2);
    savedUnit.setObjectives(otherObjectives);
    when(unitRepository.findByIdOrThrow(any(Long.class))).thenReturn(savedUnit);

    okrUnitServiceManagers.createObjective(departmentId, objective, user);

    Assert.assertEquals(6, otherObjective0.getSequence());
    Assert.assertEquals(7, otherObjective1.getSequence());
    Assert.assertEquals(11, otherObjective2.getSequence());
  }

  @Test(expected = ForbiddenException.class)
  public void createObjective_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(closedCycle);

    okrUnitServiceManagers.createObjective(10L, new Objective(), user);
  }

  @Test(expected = ForbiddenException.class)
  public void updateUnit_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(closedCycle);

    okrUnitServiceManagers.updateUnit(createUnit(), user);
  }
}
