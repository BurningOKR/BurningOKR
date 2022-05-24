package org.burningokr.service.dashboard;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.burningokr.model.activity.Action;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.model.users.User;
import org.burningokr.repositories.dashboard.ChartCreationOptionsRepository;
import org.burningokr.repositories.dashboard.DashboardCreationRepository;
import org.burningokr.service.activity.ActivityService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DashboardServiceTest {
  @Mock private DashboardCreationRepository dashboardCreationRepository;
  @Mock private ChartCreationOptionsRepository chartCreationOptionsRepository;
  @Mock private ActivityService activityService;

  @Mock private User authorizedUser;

  @InjectMocks private DashboardService dashboardService;

  private static Long dashboardCreationId;
  private static String dashboardCreationTitle;
  private static UUID dashboardCreationCreator;
  private static Long dashboardCreationCompanyId;
  private static Collection<ChartCreationOptions> chartCreationOptions = new ArrayList<>();

  private DashboardCreation dashboardCreation;

  @BeforeClass
  public static void init() {
    dashboardCreationId = 100L;
    dashboardCreationTitle = "DashboardCreationTitle";
    dashboardCreationCreator = UUID.randomUUID();
    dashboardCreationCompanyId = 200L;
    chartCreationOptions.add(new ChartCreationOptions());
  }

  @Before
  public void Refresh() {
    dashboardCreation = createDashboardCreation();
  }

  public DashboardCreation createDashboardCreation() {
    DashboardCreation dashboardCreation = new DashboardCreation();
    dashboardCreation.setId(dashboardCreationId);
    dashboardCreation.setTitle(dashboardCreationTitle);
    dashboardCreation.setCreatorId(dashboardCreationCreator);
    dashboardCreation.setCompanyId(dashboardCreationCompanyId);
    dashboardCreation.setChartCreationOptions(chartCreationOptions);
    return dashboardCreation;
  }

  @Test(expected = EntityNotFoundException.class)
  public void findDashboardCreationById_expectedNotFoundException() {
    when(dashboardCreationRepository.findByIdOrThrow(dashboardCreationId))
        .thenThrow(new EntityNotFoundException());

    dashboardService.findDashboardCreationById(dashboardCreationId);
  }

  @Test
  public void deleteDashboard_expectDeleteCall() {
    when(dashboardCreationRepository.findByIdOrThrow(dashboardCreationId))
        .thenReturn(dashboardCreation);

    dashboardService.deleteDashboard(dashboardCreationId, authorizedUser);

    verify(dashboardCreationRepository).deleteById(dashboardCreationId);
  }

  @Test
  public void deleteDashboard_expectedActivityCall() {
    when(dashboardCreationRepository.findByIdOrThrow(dashboardCreationId))
        .thenReturn(dashboardCreation);

    dashboardService.deleteDashboard(dashboardCreationId, authorizedUser);

    verify(activityService).createActivity(authorizedUser, dashboardCreation, Action.DELETED);
  }
}
