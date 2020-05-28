package org.burningokr.service.structure.departmentservices.structureServiceManagersTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.StructureRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.structure.departmentservices.StructureServiceManagers;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

@RunWith(MockitoJUnitRunner.class)
public abstract class StructureServiceManagersTest<T extends SubStructure> {

  protected final Long departmentId = 1337L;
  protected final String departmentName = "Java Academy";
  protected T structure;

  @Mock protected StructureRepository<T> structureRepository;
  @Mock protected ObjectiveRepository objectiveRepository;
  @Mock protected ActivityService activityService;
  @Mock protected EntityCrawlerService entityCrawlerService;
  @Mock protected User user;
  @InjectMocks protected StructureServiceManagers<T> structureServiceManagers;

  protected abstract T createStructure();

  @Before
  public void setUp() {
    this.structure = createStructure();
    structure.setId(departmentId);
    structure.setName(departmentName);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfStructure(any())).thenReturn(activeCycle);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void createSubStructure_expectedThrow() {
    structureServiceManagers.createSubstructure(departmentId, createStructure(), user);
  }

  @Test(expected = UnauthorizedUserException.class)
  public void removeStructure_expectedThrow() {
    structureServiceManagers.deleteStructure(departmentId, user);
  }

  @Test
  public void createObjective_expectsParentStructureIdIsSet() {
    Objective objective = new Objective();
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    when(structureRepository.findByIdOrThrow(any(Long.class))).thenReturn(structure);

    structureServiceManagers.createObjective(departmentId, objective, user);

    Assert.assertNotNull(objective.getParentStructure().getId());
    Assert.assertEquals(departmentId, objective.getParentStructure().getId());
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

    T savedStructure = createStructure();
    Collection<Objective> otherObjectives =
        Arrays.asList(otherObjective0, otherObjective1, otherObjective2);
    savedStructure.setObjectives(otherObjectives);
    when(structureRepository.findByIdOrThrow(any(Long.class))).thenReturn(savedStructure);

    structureServiceManagers.createObjective(departmentId, objective, user);

    Assert.assertEquals(6, otherObjective0.getSequence());
    Assert.assertEquals(7, otherObjective1.getSequence());
    Assert.assertEquals(11, otherObjective2.getSequence());
  }

  @Test(expected = ForbiddenException.class)
  public void createObjective_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfStructure(any())).thenReturn(closedCycle);

    structureServiceManagers.createObjective(10L, new Objective(), user);
  }

  @Test(expected = ForbiddenException.class)
  public void updateStructure_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfStructure(any())).thenReturn(closedCycle);

    structureServiceManagers.updateStructure(createStructure(), user);
  }
}
