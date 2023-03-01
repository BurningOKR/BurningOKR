//package org.burningokr.service.okrUnit.departmentservices.unitServiceManagersTest;
//
//import org.burningokr.model.cycles.Cycle;
//import org.burningokr.model.cycles.CycleState;
//import org.burningokr.model.okr.Objective;
//import org.burningokr.model.okrUnits.OkrChildUnit;
//import org.burningokr.model.users.User;
//import org.burningokr.repositories.okr.ObjectiveRepository;
//import org.burningokr.repositories.okrUnit.UnitRepository;
//import org.burningokr.service.activity.ActivityService;
//import org.burningokr.service.exceptions.ForbiddenException;
//import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceManagers;
//import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
//
//import java.util.Arrays;
//import java.util.Collection;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(MockitoExtension.class)
//public abstract class OkrUnitServiceManagersTest<T extends OkrChildUnit> {
//
//  protected final Long departmentId = 1337L;
//  protected final String departmentName = "Java Academy";
//  protected T unit;
//
//  @Mock
//  protected UnitRepository<T> unitRepository;
//  @Mock
//  protected ObjectiveRepository objectiveRepository;
//  @Mock
//  protected ActivityService activityService;
//  @Mock
//  protected EntityCrawlerService entityCrawlerService;
//  @Mock
//  protected User user;
//  @InjectMocks
//  protected OkrUnitServiceManagers<T> okrUnitServiceManagers;
//
//  protected abstract T createUnit();
//
//  @BeforeEach
//  public void setUp() {
//    this.unit = createUnit();
//    unit.setId(departmentId);
//    unit.setName(departmentName);
//
//    Cycle activeCycle = new Cycle();
//    activeCycle.setCycleState(CycleState.ACTIVE);
//    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(activeCycle);
//  }
//
//  @Test
//  public void createChildUnit_expectedThrow() {
//    assertThrows(UnauthorizedUserException.class, () -> {
//      okrUnitServiceManagers.createChildUnit(departmentId, createUnit(), user);
//    });
//
//  }
//
//  @Test
//  public void removeUnit_expectedThrow() {
//    assertThrows(UnauthorizedUserException.class, () -> {
//      okrUnitServiceManagers.deleteUnit(departmentId, user);
//    });
//  }
//
//  @Test
//  public void createObjective_expectsParentUnitIdIsSet() {
//    Objective objective = new Objective();
//    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);
//
//    when(unitRepository.findByIdOrThrow(any(Long.class))).thenReturn(unit);
//
//    okrUnitServiceManagers.createObjective(departmentId, objective, user);
//
//    assertNotNull(objective.getParentOkrUnit().getId());
//    assertEquals(departmentId, objective.getParentOkrUnit().getId());
//  }
//
//  @Test
//  public void createObjective_expectsOtherObjectiveSequenceAdvanced() {
//    Objective objective = new Objective();
//    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);
//
//    Objective otherObjective0 = new Objective();
//    otherObjective0.setSequence(5);
//    Objective otherObjective1 = new Objective();
//    otherObjective1.setSequence(6);
//    Objective otherObjective2 = new Objective();
//    otherObjective2.setSequence(10);
//
//    T savedUnit = createUnit();
//    Collection<Objective> otherObjectives =
//      Arrays.asList(otherObjective0, otherObjective1, otherObjective2);
//    savedUnit.setObjectives(otherObjectives);
//    when(unitRepository.findByIdOrThrow(any(Long.class))).thenReturn(savedUnit);
//
//    okrUnitServiceManagers.createObjective(departmentId, objective, user);
//
//    assertEquals(5, otherObjective0.getSequence());
//    assertEquals(6, otherObjective1.getSequence());
//    assertEquals(10, otherObjective2.getSequence());
//  }
//
//  @Test
//  public void createObjective_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
//    Cycle closedCycle = new Cycle();
//    closedCycle.setCycleState(CycleState.CLOSED);
//    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(closedCycle);
//
//    assertThrows(ForbiddenException.class, () -> {
//      okrUnitServiceManagers.createObjective(10L, new Objective(), user);
//    });
//  }
//
//  @Test
//  public void updateUnit_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
//    Cycle closedCycle = new Cycle();
//    closedCycle.setCycleState(CycleState.CLOSED);
//    when(entityCrawlerService.getCycleOfUnit(any())).thenReturn(closedCycle);
//
//    assertThrows(ForbiddenException.class, () -> {
//      okrUnitServiceManagers.updateUnit(createUnit(), user);
//    });
//  }
//}
