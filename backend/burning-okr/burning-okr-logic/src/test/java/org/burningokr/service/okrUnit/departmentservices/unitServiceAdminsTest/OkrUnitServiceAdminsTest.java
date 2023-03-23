package org.burningokr.service.okrUnit.departmentservices.unitServiceAdminsTest;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okrUnit.OkrUnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okr.OkrTopicDescriptionService;
import org.burningokr.service.okr.TaskBoardService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class OkrUnitServiceAdminsTest<T extends OkrChildUnit> {

  protected final Long departmentId = 1337L;
  protected final String departmentName = "Java Academy";
  @Mock
  protected OkrUnitRepository<T> okrUnitRepository;
  @Mock
  protected ObjectiveRepository objectiveRepository;
  @Mock
  protected TaskBoardService taskBoardService;
  @Mock
  protected IUser IUser;
  @Mock
  protected EntityCrawlerService entityCrawlerService;
  @Mock
  protected ActivityService activityService;
  @Mock
  protected OkrTopicDescriptionRepository okrTopicDescriptionRepository;
  @Mock
  protected OkrTopicDescriptionService okrTopicDescriptionService;
  @InjectMocks
  protected OkrChildUnitServiceAdmins<T> okrUnitServiceAdmins;
  protected T unit;

  protected abstract T createDepartment();

  protected abstract Class<T> getDepartment();

  @BeforeEach
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

    when(okrUnitRepository.findByIdOrThrow(any(Long.class)))
      .thenAnswer(
        invocation -> {
          if (invocation.getArgument(0).equals(unit.getId())) {
            return unit;
          }
          throw new EntityNotFoundException();
        });
    when(okrUnitRepository.save(any(getDepartment()))).thenReturn(childUnit);

    okrUnitServiceAdmins.createChildUnit(departmentId, childUnit, IUser);

    assertEquals(departmentId, childUnit.getParentOkrUnit().getId());

    verify(okrUnitRepository).findByIdOrThrow(anyLong());
    verify(okrUnitRepository).save(any(getDepartment()));
  }

  @Test
  public void createChildUnit_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(closedCycle);

    assertThrows(ForbiddenException.class, () -> {
      okrUnitServiceAdmins.createChildUnit(10L, createDepartment(), IUser);
    });
  }

  @Test
  public void updateUnit_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(closedCycle);

    assertThrows(ForbiddenException.class, () -> {
      okrUnitServiceAdmins.updateUnit(createDepartment(), IUser);
    });
  }

  @Test
  public void deletUnit_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(closedCycle);

    assertThrows(ForbiddenException.class, () -> {
      okrUnitServiceAdmins.deleteChildUnit(10L, IUser);
    });
  }

  @Test
  public void createObjective_expectsParentUnitIdIsSet() {
    Objective objective = new Objective();
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    when(okrUnitRepository.findByIdOrThrow(any(Long.class))).thenReturn(unit);

    okrUnitServiceAdmins.createObjective(departmentId, objective, IUser);

    assertNotNull(objective.getParentOkrUnit().getId());
    assertEquals(departmentId, objective.getParentOkrUnit().getId());
  }

  @Test
  public void updateUnit_expectsNameIsChanged() {
    T updatedUnit = createDepartment();
    String updateName = "Java Dudes";
    updatedUnit.setId(departmentId);
    updatedUnit.setName(updateName);

    when(okrUnitRepository.save(any(getDepartment()))).thenReturn(updatedUnit);
    when(okrUnitRepository.findByIdOrThrow(anyLong())).thenReturn(unit);

    unit = okrUnitServiceAdmins.updateUnit(updatedUnit, IUser);

    assertEquals(updateName, unit.getName());
  }

  @Test
  public void updateUnit_expectsIsActiveChanged() {
    T updateUnit = createDepartment();
    updateUnit.setId(departmentId);
    updateUnit.setActive(true);

    when(okrUnitRepository.save(any(getDepartment()))).thenReturn(updateUnit);
    when(okrUnitRepository.findByIdOrThrow(anyLong())).thenReturn(unit);

    unit = okrUnitServiceAdmins.updateUnit(updateUnit, IUser);

    assertTrue(unit.isActive());
  }
}
