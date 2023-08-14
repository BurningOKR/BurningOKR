package org.burningokr.service.dashboard;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.activity.Action;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.model.users.User;
import org.burningokr.repositories.dashboard.ChartCreationOptionsRepository;
import org.burningokr.repositories.dashboard.DashboardCreationRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextServiceKeycloak;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {
  private static Long dashboardCreationId;
  private static String dashboardCreationTitle;
  private static UUID dashboardCreationCreator;
  private static Long dashboardCreationCompanyId;
  private static final Collection<ChartCreationOptions> chartCreationOptions = new ArrayList<>();
  private static User userMock;
  private static final List<DashboardCreation> dashboardCreationList = new ArrayList<>();

  @Mock
  private DashboardCreationRepository dashboardCreationRepository;
  @Mock
  private ChartCreationOptionsRepository chartCreationOptionsRepository;
  @Mock
  private ActivityService activityService;
  @Mock
  private AuthenticationUserContextServiceKeycloak authorizationUserContextService;
  @InjectMocks
  private DashboardService dashboardService;
  private DashboardCreation dashboardCreation;

  @BeforeAll
  public static void init() {
    dashboardCreationId = 100L;
    dashboardCreationTitle = "DashboardCreationTitle";
    dashboardCreationCreator = UUID.randomUUID();
    dashboardCreationCompanyId = 200L;
    chartCreationOptions.add(new ChartCreationOptions());

    UUID creatorId = UUID.randomUUID();
    userMock = new User();
    userMock.setId(creatorId);
  }

  @BeforeEach
  public void refresh() {
    dashboardCreation = createDashboardCreationMock();
  }

  public DashboardCreation createDashboardCreationMock() {
    DashboardCreation dashboardCreation = new DashboardCreation();
    dashboardCreation.setId(dashboardCreationId);
    dashboardCreation.setTitle(dashboardCreationTitle);
    dashboardCreation.setCreatorId(dashboardCreationCreator);
    dashboardCreation.setCompanyId(dashboardCreationCompanyId);
    dashboardCreation.setChartCreationOptions(chartCreationOptions);
    return dashboardCreation;
  }

  @Test()
  public void findDashboardCreationById_shouldThrowNotFoundException() {
    when(dashboardCreationRepository.findByIdOrThrow(dashboardCreationId)).thenThrow(new EntityNotFoundException());
    assertThrows(EntityNotFoundException.class, () -> {
      dashboardService.findDashboardCreationById(dashboardCreationId);
    });
  }

  @Test()
  public void findDashboardsOfCompany_shouldFindDashboardCreation() {
    dashboardCreationList.add(dashboardCreation);
    when(dashboardCreationRepository.findDashboardCreationsByCompanyId(dashboardCreationCompanyId)).thenReturn(dashboardCreationList);
    assertTrue(dashboardService.findDashboardsOfCompany(dashboardCreationCompanyId).contains(dashboardCreation));
  }

  @Test()
  public void findDashboardsOfCompany_shouldReturnEmptyCollection() {
    assertTrue(dashboardService.findDashboardsOfCompany(dashboardCreationId).isEmpty());
  }

  @Test
  public void createDashboard_shouldCallActivity() {
    when(authorizationUserContextService.getAuthenticatedUser()).thenReturn(userMock);
    when(dashboardCreationRepository.save(dashboardCreation)).thenReturn(dashboardCreation);

    dashboardService.createDashboard(dashboardCreation);

    verify(activityService).createActivity(dashboardCreation, Action.CREATED);
  }

  @Test
  public void updateDashboard_shouldCallActivity() {
    when(dashboardCreationRepository.save(dashboardCreation)).thenReturn(dashboardCreation);

    dashboardService.updateDashboard(dashboardCreation);

    verify(activityService).createActivity(dashboardCreation, Action.EDITED);
  }

  @Test
  public void deleteDashboard_shouldCallDeletion() {
    when(dashboardCreationRepository.findByIdOrThrow(dashboardCreationId)).thenReturn(dashboardCreation);

    dashboardService.deleteDashboard(dashboardCreationId);

    verify(dashboardCreationRepository).deleteById(dashboardCreationId);
  }

  @Test
  public void deleteDashboard_shouldCallActivity() {
    when(dashboardCreationRepository.findByIdOrThrow(dashboardCreationId)).thenReturn(dashboardCreation);

    dashboardService.deleteDashboard(dashboardCreationId);

    verify(activityService).createActivity(dashboardCreation, Action.DELETED);
  }

}
