package org.burningokr.service.structure.departmentservices.structureServiceAdminsTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.StructureRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.structure.departmentservices.StructureServiceAdmins;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public abstract class StructureServiceAdminsTest<T extends SubStructure> {

  protected final Long departmentId = 1337L;
  protected final String departmentName = "Java Academy";
  @Mock protected StructureRepository<T> structureRepository;
  @Mock protected ObjectiveRepository objectiveRepository;
  @Mock protected User user;
  @Mock protected EntityCrawlerService entityCrawlerService;
  @Mock protected ActivityService activityService;
  @InjectMocks protected StructureServiceAdmins<T> structureServiceAdmins;
  protected T structure;

  protected abstract T createStructure();

  protected abstract Class<T> getStructureClass();

  @Before
  public void setUp() {
    structure = createStructure();
    structure.setId(departmentId);
    structure.setName(departmentName);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfStructure(any())).thenReturn(activeCycle);
  }

  @Test
  public void createSubStructure_expectesParentIdIsSet() {
    T subStructure = createStructure();

    when(structureRepository.findByIdOrThrow(any(Long.class)))
        .thenAnswer(
            invocation -> {
              if (invocation.getArgument(0).equals(structure.getId())) {
                return structure;
              }
              throw new EntityNotFoundException();
            });
    when(structureRepository.save(any(getStructureClass()))).thenReturn(subStructure);

    structureServiceAdmins.createSubstructure(departmentId, subStructure, user);

    Assert.assertEquals(departmentId, subStructure.getParentStructure().getId());

    verify(structureRepository).findByIdOrThrow(anyLong());
    verify(structureRepository).save(any(getStructureClass()));
  }

  @Test(expected = ForbiddenException.class)
  public void createSubStructure_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfStructure(any())).thenReturn(closedCycle);

    structureServiceAdmins.createSubstructure(10L, createStructure(), user);
  }

  @Test(expected = ForbiddenException.class)
  public void updateStructure_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfStructure(any())).thenReturn(closedCycle);

    structureServiceAdmins.updateStructure(createStructure(), user);
  }

  @Test(expected = ForbiddenException.class)
  public void deleteStructure_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfStructure(any())).thenReturn(closedCycle);

    structureServiceAdmins.deleteStructure(10L, user);
  }

  @Test
  public void createObjective_expectsParentStructureIdIsSet() {
    Objective objective = new Objective();
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    when(structureRepository.findByIdOrThrow(any(Long.class))).thenReturn(structure);

    structureServiceAdmins.createObjective(departmentId, objective, user);

    Assert.assertNotNull(objective.getParentStructure().getId());
    Assert.assertEquals(departmentId, objective.getParentStructure().getId());
  }

  @Test
  public void updateStructure_expectsNameIsChanged() {
    T updateStructure = createStructure();
    String updateName = "Java Dudes";
    updateStructure.setId(departmentId);
    updateStructure.setName(updateName);

    when(structureRepository.save(any(getStructureClass()))).thenReturn(updateStructure);
    when(structureRepository.findByIdOrThrow(anyLong())).thenReturn(structure);

    structure = structureServiceAdmins.updateStructure(updateStructure, user);

    Assert.assertEquals(updateName, structure.getName());
  }

  @Test
  public void updateStructure_expectsIsActiveChanged() {
    T updateStructure = createStructure();
    updateStructure.setId(departmentId);
    updateStructure.setActive(true);

    when(structureRepository.save(any(getStructureClass()))).thenReturn(updateStructure);
    when(structureRepository.findByIdOrThrow(anyLong())).thenReturn(structure);

    structure = structureServiceAdmins.updateStructure(updateStructure, user);

    Assert.assertTrue(structure.isActive());
  }
}
