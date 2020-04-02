package org.burningokr.service.structure;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.CompanyRepository;
import org.burningokr.repositories.structre.DepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

  @Mock private CompanyRepository companyRepository;

  @Mock private DepartmentRepository departmentRepository;

  @Mock private EntityCrawlerService entityCrawlerService;

  @Mock private ObjectiveRepository objectiveRepository;

  @Mock private ActivityService activityService;

  @Mock private User user;

  @InjectMocks private CompanyService companyService;

  private final Long companyId = 1337L;
  private final String companyName = "Brockhaus AG";
  private final String updatedCompanyName = "BAG";
  private Company company;

  @Before
  public void setUp() {
    company = new Company();
    company.setId(companyId);
    company.setName(companyName);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfCompany(any())).thenReturn(activeCycle);
  }

  @Test
  public void createDepartment_exceptesParentStructureIsSet() {
    Department department = new Department();
    User user = mock(User.class);
    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(departmentRepository.save(any(Department.class))).thenReturn(department);

    companyService.createDepartment(companyId, department, user);

    Assert.assertEquals(company.getId(), department.getParentStructure().getId());

    verify(companyRepository).findByIdOrThrow(any(Long.class));
    verify(departmentRepository).save(any(Department.class));
  }

  @Test(expected = ForbiddenException.class)
  public void createDepartment_cycleOfDepartmentClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    Department department = new Department();
    User user = mock(User.class);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(entityCrawlerService.getCycleOfCompany(company)).thenReturn(closedCycle);
    companyService.createDepartment(companyId, department, user);
  }

  @Test
  public void updateCompany_expectsNameIsUpdated() {
    Company updateCompany = new Company();
    updateCompany.setId(companyId);
    updateCompany.setName(updatedCompanyName);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(company);
    when(companyRepository.save(any(Company.class))).thenReturn(updateCompany);

    User user = mock(User.class);
    company = companyService.updateCompany(updateCompany, user);

    Assert.assertEquals(updatedCompanyName, company.getName());

    verify(companyRepository).findByIdOrThrow(anyLong());
    verify(companyRepository).save(any(Company.class));
  }

  @Test(expected = ForbiddenException.class)
  public void updateCompany_cycleOfCompanyIsClosed_expectedForbiddenThrow() {
    Company updateCompany = new Company();
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfCompany(any())).thenReturn(closedCycle);

    User user = mock(User.class);
    company = companyService.updateCompany(updateCompany, user);
  }

  @Test(expected = EntityNotFoundException.class)
  public void updateCompany_expectsEntityNotFoundException() {
    Company updateCompany = new Company();
    updateCompany.setId(companyId);
    updateCompany.setName(updatedCompanyName);

    when(companyRepository.findByIdOrThrow(anyLong())).thenThrow(new EntityNotFoundException());

    User user = mock(User.class);
    company = companyService.updateCompany(updateCompany, user);

    verify(companyRepository).findByIdOrThrow(anyLong());
  }

  @Test
  public void deleteCompany_shouldDeleteCompany() {
    when(companyRepository.findByIdOrThrow(companyId)).thenReturn(company);

    companyService.deleteCompany(companyId, true, user);

    verify(companyRepository).deleteById(companyId);
  }

  @Test(expected = EntityNotFoundException.class)
  public void deleteCompany_expectsEntityNotFoundException() {
    Long notExistingCompanyId = 2000L;

    when(companyRepository.findByIdOrThrow(2000L)).thenThrow(EntityNotFoundException.class);

    companyService.deleteCompany(notExistingCompanyId, true, user);
  }
}
