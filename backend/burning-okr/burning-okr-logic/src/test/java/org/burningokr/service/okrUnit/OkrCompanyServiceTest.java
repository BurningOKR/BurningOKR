package org.burningokr.service.okrUnit;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import javax.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrCompanyServiceTest {

  @Mock private CompanyRepository companyRepository;

  @Mock private UnitRepository<OkrChildUnit> unitRepository;

  @Mock private EntityCrawlerService entityCrawlerService;

  @Mock private ObjectiveRepository objectiveRepository;

  @Mock private ActivityService activityService;

  @Mock private OkrTopicDescriptionRepository okrTopicDescriptionRepository;

  @Mock private User user;

  @InjectMocks private CompanyService companyService;

  private final Long companyId = 1337L;
  private final String companyName = "Brockhaus AG";
  private final String updatedCompanyName = "BAG";
  private OkrCompany okrCompany;

  @Before
  public void setUp() {
    okrCompany = new OkrCompany();
    okrCompany.setId(companyId);
    okrCompany.setName(companyName);

    Cycle activeCycle = new Cycle();
    activeCycle.setCycleState(CycleState.ACTIVE);
    when(entityCrawlerService.getCycleOfCompany(any())).thenReturn(activeCycle);
  }

  @Test
  public void createDepartment_expectsParentUnitIsSet() {
    OkrDepartment okrDepartment = new OkrDepartment();
    User user = mock(User.class);
    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(unitRepository.save(any(OkrDepartment.class))).thenReturn(okrDepartment);

    companyService.createDepartment(companyId, okrDepartment, user);

    Assert.assertEquals(okrCompany.getId(), okrDepartment.getParentOkrUnit().getId());

    verify(companyRepository).findByIdOrThrow(any(Long.class));
    verify(unitRepository).save(any(OkrDepartment.class));
  }

  @Test
  public void createDepartment_cycleOfDepartmentClosed_expectedForbiddenThrow() {
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    OkrDepartment okrDepartment = new OkrDepartment();
    User user = mock(User.class);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(entityCrawlerService.getCycleOfCompany(okrCompany)).thenReturn(closedCycle);
    try {
      companyService.createDepartment(companyId, okrDepartment, user);
      Assert.fail();
    } catch (Exception ex) {
      assertThat("Should only throw ForbiddenException.", ex, instanceOf(ForbiddenException.class));
    }
  }

  @Test
  public void createDepartment_expectOkrTopicDescriptionIsCreated() {
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setName("test");

    User user = mock(User.class);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(unitRepository.save(any(OkrDepartment.class))).thenReturn(okrDepartment);
    when(okrTopicDescriptionRepository.save(any()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    OkrDepartment created = companyService.createDepartment(companyId, okrDepartment, user);

    assertNotNull(created.getOkrTopicDescription());
    assertEquals(okrDepartment.getName(), created.getOkrTopicDescription().getName());
  }

  @Test
  public void updateCompany_expectsNameIsUpdated() {
    OkrCompany updateOkrCompany = new OkrCompany();
    updateOkrCompany.setId(companyId);
    updateOkrCompany.setName(updatedCompanyName);

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(okrCompany);
    when(companyRepository.save(any(OkrCompany.class))).thenReturn(updateOkrCompany);

    User user = mock(User.class);
    okrCompany = companyService.updateCompany(updateOkrCompany, user);

    Assert.assertEquals(updatedCompanyName, okrCompany.getName());

    verify(companyRepository).findByIdOrThrow(anyLong());
    verify(companyRepository).save(any(OkrCompany.class));
  }

  @Test
  public void updateCompany_cycleOfCompanyIsClosed_expectedForbiddenThrow() {
    OkrCompany updateOkrCompany = new OkrCompany();
    Cycle closedCycle = new Cycle();
    closedCycle.setCycleState(CycleState.CLOSED);
    when(entityCrawlerService.getCycleOfCompany(any())).thenReturn(closedCycle);

    User user = mock(User.class);
    try {
      okrCompany = companyService.updateCompany(updateOkrCompany, user);
      Assert.fail();
    } catch (Exception ex) {
      assertThat("Should only throw ForbiddenException.", ex, instanceOf(ForbiddenException.class));
    }
  }

  @Test
  public void updateCompany_expectsEntityNotFoundException() {
    OkrCompany updateOkrCompany = new OkrCompany();
    updateOkrCompany.setId(companyId);
    updateOkrCompany.setName(updatedCompanyName);

    when(companyRepository.findByIdOrThrow(anyLong())).thenThrow(new EntityNotFoundException());

    User user = mock(User.class);
    try {
      okrCompany = companyService.updateCompany(updateOkrCompany, user);
      Assert.fail();
    } catch (Exception ex) {
      assertThat(
          "Should only throw EntityNotFoundException.",
          ex,
          instanceOf(EntityNotFoundException.class));
    }

    verify(companyRepository).findByIdOrThrow(anyLong());
  }

  @Test
  public void deleteCompany_shouldDeleteCompany() {
    when(companyRepository.findByIdOrThrow(companyId)).thenReturn(okrCompany);

    companyService.deleteCompany(companyId, true, user);

    verify(companyRepository).deleteById(companyId);
  }

  @Test
  public void deleteCompany_expectsEntityNotFoundException() {
    Long notExistingCompanyId = 2000L;

    when(companyRepository.findByIdOrThrow(2000L)).thenThrow(EntityNotFoundException.class);

    try {
      companyService.deleteCompany(notExistingCompanyId, true, user);
      Assert.fail();
    } catch (Exception ex) {
      assertThat(
          "Should only throw EntityNotFoundException.",
          ex,
          instanceOf(EntityNotFoundException.class));
    }
  }
}
