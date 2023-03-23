//package org.burningokr.service.okrUnit.departmentservices.unitServiceUsersTest;
//
//import org.burningokr.model.cycles.Cycle;
//import org.burningokr.model.cycles.CycleState;
//import org.burningokr.model.okr.Objective;
//import org.burningokr.model.okrUnits.OkrChildUnit;
//import org.burningokr.model.users.User;
//import org.burningokr.repositories.okr.ObjectiveRepository;
//import org.burningokr.repositories.okr.OkrTopicDraftRepository;
//import org.burningokr.repositories.okrUnit.OkrUnitRepository;
//import org.burningokr.service.activity.ActivityService;
//import org.burningokr.service.okrUnit.departmentservices.OkrChildUnitServiceUsers;
//import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public abstract class OkrUnitServiceUsersTest<T extends OkrChildUnit> {
//
//  @Mock
//  protected OkrUnitRepository<T> unitRepository;
//  @Mock
//  protected ObjectiveRepository objectiveRepository;
//  @Mock
//  protected OkrTopicDraftRepository okrTopicDraftRepository;
//  @Mock
//  protected ActivityService activityService;
//  @Mock
//  protected EntityCrawlerService entityCrawlerService;
//  @InjectMocks
//  OkrChildUnitServiceUsers<T> okrUnitServiceUsers;
//  @Mock
//  private User user;
//  private T unit;
//
//  protected abstract T createUnit();
//
//  @BeforeEach
//  public void setup() {
//    unit = createUnit();
//
//    unit.setId(1L);
//
//    Cycle activeCycle = new Cycle();
//    activeCycle.setCycleState(CycleState.ACTIVE);
//  }
//
//  @Test
//  public void updateUnit_expectedThrow() {
//    assertThrows(UnauthorizedUserException.class, () -> {
//      okrUnitServiceUsers.updateUnit(unit, user);
//    });
//  }
//
//  @Test
//  public void createObjective_expectedThrow() {
//    assertThrows(UnauthorizedUserException.class, () -> {
//      okrUnitServiceUsers.createObjective(100L, new Objective(), user);
//    });
//  }
//
//  @Test
//  public void createChildUnit_expectedThrow() {
//    assertThrows(UnauthorizedUserException.class, () -> {
//      okrUnitServiceUsers.createChildUnit(100L, unit, user);
//    });
//  }
//
//  @Test
//  public void removeUnit_expectedThrow() {
//    assertThrows(UnauthorizedUserException.class, () -> {
//      okrUnitServiceUsers.deleteUnit(100L, user);
//    });
//  }
//}
