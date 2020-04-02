package org.burningokr.service.structure.departmentservices;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.DepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.exceptions.InvalidDeleteRequestException;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceAdminsTest {

  private final Long departmentId = 1337L;
  private final String departmentName = "Java Academy";
  @Mock private DepartmentRepository departmentRepository;
  @Mock private ObjectiveRepository objectiveRepository;
  @Mock private User user;
  @Mock private EntityCrawlerService entityCrawlerService;
  @Mock private ActivityService activityService;
  @InjectMocks private DepartmentServiceAdmins departmentServiceAdmins;
  private Department department;

  @Before
  public void setUp() {
    department = new Department();
    department.setId(departmentId);
    department.setName(departmentName);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfDepartment(any())).thenReturn(activeCycle);
  }

  @Test
  public void createSubdepartment_expectesParentIdIsSet() {
    Department subDepartment = new Department();

    when(departmentRepository.findByIdOrThrow(any(Long.class)))
        .thenAnswer(
            invocation -> {
              if (invocation.getArgument(0).equals(department.getId())) {
                return department;
              }
              throw new EntityNotFoundException();
            });
    when(departmentRepository.save(any(Department.class))).thenReturn(subDepartment);

    departmentServiceAdmins.createSubdepartment(departmentId, subDepartment, user);

    Assert.assertEquals(departmentId, subDepartment.getParentStructure().getId());

    verify(departmentRepository).findByIdOrThrow(anyLong());
    verify(departmentRepository).save(any(Department.class));
  }

  @Test(expected = ForbiddenException.class)
  public void createSubdepartment_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfDepartment(any())).thenReturn(closedCycle);

    departmentServiceAdmins.createSubdepartment(10L, new Department(), user);
  }

  @Test(expected = ForbiddenException.class)
  public void updateDepartment_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfDepartment(any())).thenReturn(closedCycle);

    departmentServiceAdmins.updateDepartment(new Department(), user);
  }

  @Test(expected = ForbiddenException.class)
  public void deleteDepartment_cycleOfDepartmentIsClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfDepartment(any())).thenReturn(closedCycle);

    departmentServiceAdmins.deleteDepartment(10L, user);
  }

  @Test
  public void createObjective_expectsParentStructureIdIsSet() {
    Objective objective = new Objective();
    when(objectiveRepository.save(any(Objective.class))).thenReturn(objective);

    when(departmentRepository.findByIdOrThrow(any(Long.class))).thenReturn(department);

    departmentServiceAdmins.createObjective(departmentId, objective, user);

    Assert.assertNotNull(objective.getParentStructure().getId());
    Assert.assertEquals(departmentId, objective.getParentStructure().getId());
  }

  @Test(expected = InvalidDeleteRequestException.class)
  public void deleteDepartmentWithSubDepartments_expectsInvalidDeleteRequestException() {
    Department department = new Department();
    department.setId(1337L);
    List<Department> subdepartments = new ArrayList<>();
    subdepartments.add(new Department());
    department.setDepartments(subdepartments);

    when(departmentRepository.findByIdOrThrow(anyLong())).thenReturn(department);

    departmentServiceAdmins.deleteDepartment(1337L, user);
  }

  @Test
  public void updateDepartment_expectsNameIsChanged() {
    Department updateDepartment = new Department();
    String updateName = "Java Dudes";
    updateDepartment.setId(departmentId);
    updateDepartment.setName(updateName);

    when(departmentRepository.save(any(Department.class))).thenReturn(updateDepartment);
    when(departmentRepository.findByIdOrThrow(anyLong())).thenReturn(department);

    department = departmentServiceAdmins.updateDepartment(updateDepartment, user);

    Assert.assertEquals(updateName, department.getName());
  }

  @Test
  public void updateDepartment_expectsIsActiveChanged() {
    Department updateDepartment = new Department();
    updateDepartment.setId(departmentId);
    updateDepartment.setActive(true);

    when(departmentRepository.save(any(Department.class))).thenReturn(updateDepartment);
    when(departmentRepository.findByIdOrThrow(anyLong())).thenReturn(department);

    department = departmentServiceAdmins.updateDepartment(updateDepartment, user);

    Assert.assertTrue(department.isActive());
  }

  @Test
  public void updateDepartment_expectsOkrMasterIdIsChanged() {
    Department updateDepartment = new Department();
    updateDepartment.setId(departmentId);
    updateDepartment.setOkrMasterId(UUID.randomUUID());

    Department persistedDepartment = new Department();
    department.setId(departmentId);

    when(departmentRepository.save(any(Department.class))).then(returnsFirstArg());
    when(departmentRepository.findByIdOrThrow(departmentId)).thenReturn(persistedDepartment);

    department = departmentServiceAdmins.updateDepartment(updateDepartment, user);

    Assert.assertEquals(updateDepartment.getOkrMasterId(), department.getOkrMasterId());
  }

  @Test
  public void updateDepartment_expectsOkrTopicSponsorIdIsChanged() {
    Department updateDepartment = new Department();
    updateDepartment.setId(departmentId);
    updateDepartment.setOkrTopicSponsorId(UUID.randomUUID());

    Department persistedDepartment = new Department();
    department.setId(departmentId);

    when(departmentRepository.save(any(Department.class))).then(returnsFirstArg());
    when(departmentRepository.findByIdOrThrow(departmentId)).thenReturn(persistedDepartment);

    department = departmentServiceAdmins.updateDepartment(updateDepartment, user);

    Assert.assertEquals(updateDepartment.getOkrTopicSponsorId(), department.getOkrTopicSponsorId());
  }

  @Test
  public void updateDepartment_expectsOkrMemberIdsAreChanged() {
    Department updateDepartment = new Department();
    updateDepartment.setId(departmentId);
    Collection<UUID> okrMemberIds =
        Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    updateDepartment.setOkrMemberIds(okrMemberIds);
    Department persistedDepartment = new Department();
    department.setId(departmentId);
    when(departmentRepository.save(any(Department.class))).then(returnsFirstArg());
    when(departmentRepository.findByIdOrThrow(departmentId)).thenReturn(persistedDepartment);

    department = departmentServiceAdmins.updateDepartment(updateDepartment, user);

    Assert.assertEquals(okrMemberIds, department.getOkrMemberIds());
  }
}
