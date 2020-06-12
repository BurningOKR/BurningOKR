package org.burningokr.service.okrUnit.departmentservices.unitServiceAdminsTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceAdmins;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public abstract class OkrUnitServiceAdminsTest<T extends OkrChildUnit> {

  protected final Long departmentId = 1337L;
  protected final String departmentName = "Java Academy";
  @Mock protected UnitRepository<T> unitRepository;
  @Mock protected ObjectiveRepository objectiveRepository;
  @Mock protected User user;
  @Mock protected EntityCrawlerService entityCrawlerService;
  @Mock protected ActivityService activityService;
  @InjectMocks protected OkrUnitServiceAdmins<T> okrUnitServiceAdmins;
  protected T unit;

  protected abstract T createDepartment();

  protected abstract Class<T> getDepartment();

  @Before
  public void setUp() {
    unit = createDepartment();
    unit.setId(departmentId);
    unit.setName(departmentName);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(activeCycle);
  }

  @Test
  public void createChildUnit_expectesParentIdIsSet() {
    T childUnit = createDepartment();

    when(unitRepository.findByIdOrThrow(any(Long.class)))
        .thenAnswer(
            invocation -> {
              if (invocation.getArgument(0).equals(unit.getId())) {
                return unit;
              }
              throw new EntityNotFoundException();
            });
    when(unitRepository.save(any(getDepartment()))).thenReturn(childUnit);

    okrUnitServiceAdmins.createChildUnit(departmentId, childUnit, user);

    Assert.assertEquals(departmentId, childUnit.getParentOkrUnit().getId());

    verify(unitRepository).findByIdOrThrow(anyLong());
    verify(unitRepository).save(any(getDepartment()));
  }

  @Test(expected = ForbiddenException.class)
  public void createChildUnit_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(closedCycle);

    okrUnitServiceAdmins.createChildUnit(10L, createDepartment(), user);
  }

  @Test(expected = ForbiddenException.class)
  public void updateUnit_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(closedCycle);

    okrUnitServiceAdmins.updateUnit(createDepartment(), user);
  }

  @Test(expected = ForbiddenException.class)
  public void deletUnit_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(closedCycle);

    okrUnitServiceAdmins.deleteUnit(10L, user);
  }

  @Test
  public void createObjective_expectsParentUnitIdIsSet() {
    Objective objective = new Objective();
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    when(unitRepository.findByIdOrThrow(any(Long.class))).thenReturn(unit);

    okrUnitServiceAdmins.createObjective(departmentId, objective, user);

    Assert.assertNotNull(objective.getParentOkrUnit().getId());
    Assert.assertEquals(departmentId, objective.getParentOkrUnit().getId());
  }

  @Test
  public void updateUnit_expectsNameIsChanged() {
    T updatedUnit = createDepartment();
    String updateName = "Java Dudes";
    updatedUnit.setId(departmentId);
    updatedUnit.setName(updateName);

    when(unitRepository.save(any(getDepartment()))).thenReturn(updatedUnit);
    when(unitRepository.findByIdOrThrow(anyLong())).thenReturn(unit);

    unit = okrUnitServiceAdmins.updateUnit(updatedUnit, user);

    Assert.assertEquals(updateName, unit.getName());
  }

  @Test
  public void updateUnit_expectsIsActiveChanged() {
    T updateUnit = createDepartment();
    updateUnit.setId(departmentId);
    updateUnit.setActive(true);

    when(unitRepository.save(any(getDepartment()))).thenReturn(updateUnit);
    when(unitRepository.findByIdOrThrow(anyLong())).thenReturn(unit);

    unit = okrUnitServiceAdmins.updateUnit(updateUnit, user);

    Assert.assertTrue(unit.isActive());
  }
}
