// TODO fix test
//package org.burningokr.service.dashboard;
//
//import jakarta.persistence.EntityNotFoundException;
//import org.burningokr.model.activity.Action;
//import org.burningokr.model.dashboard.ChartCreationOptions;
//import org.burningokr.model.dashboard.DashboardCreation;
//import org.burningokr.model.users.IUser;
//import org.burningokr.repositories.dashboard.ChartCreationOptionsRepository;
//import org.burningokr.repositories.dashboard.DashboardCreationRepository;
//import org.burningokr.service.activity.ActivityService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class DashboardServiceTest {
//  private static Long dashboardCreationId;
//  private static String dashboardCreationTitle;
//  private static UUID dashboardCreationCreator;
//  private static Long dashboardCreationCompanyId;
//  private static Collection<ChartCreationOptions> chartCreationOptions = new ArrayList<>();
//  @Mock
//  private DashboardCreationRepository dashboardCreationRepository;
//  @Mock
//  private ChartCreationOptionsRepository chartCreationOptionsRepository;
//  @Mock
//  private ActivityService activityService;
//  @Mock
//  private IUser authorizedIUser;
//  @InjectMocks
//  private DashboardService dashboardService;
//  private DashboardCreation dashboardCreation;
//
//  @BeforeAll
//  public static void init() {
//    dashboardCreationId = 100L;
//    dashboardCreationTitle = "DashboardCreationTitle";
//    dashboardCreationCreator = UUID.randomUUID();
//    dashboardCreationCompanyId = 200L;
//    chartCreationOptions.add(new ChartCreationOptions());
//  }
//
//  @BeforeEach
//  public void Refresh() {
//    dashboardCreation = createDashboardCreation();
//  }
//
//  public DashboardCreation createDashboardCreation() {
//    DashboardCreation dashboardCreation = new DashboardCreation();
//    dashboardCreation.setId(dashboardCreationId);
//    dashboardCreation.setTitle(dashboardCreationTitle);
//    dashboardCreation.setCreatorId(dashboardCreationCreator);
//    dashboardCreation.setCompanyId(dashboardCreationCompanyId);
//    dashboardCreation.setChartCreationOptions(chartCreationOptions);
//    return dashboardCreation;
//  }
//
//  @Test()
//  public void findDashboardCreationById_expectedNotFoundException() {
//    when(dashboardCreationRepository.findByIdOrThrow(dashboardCreationId)).thenThrow(new EntityNotFoundException());
//    assertThrows(EntityNotFoundException.class, () -> {
//      dashboardService.findDashboardCreationById(dashboardCreationId);
//    });
//  }
//
//  @Test
//  public void deleteDashboard_expectDeleteCall() {
//    when(dashboardCreationRepository.findByIdOrThrow(dashboardCreationId)).thenReturn(dashboardCreation);
//
//    dashboardService.deleteDashboard(dashboardCreationId, authorizedIUser);
//
//    verify(dashboardCreationRepository).deleteById(dashboardCreationId);
//  }
//
//  @Test
//  public void deleteDashboard_expectedActivityCall() {
//    when(dashboardCreationRepository.findByIdOrThrow(dashboardCreationId)).thenReturn(dashboardCreation);
//
//    dashboardService.deleteDashboard(dashboardCreationId, authorizedIUser);
//
//    verify(activityService).createActivity(authorizedIUser, dashboardCreation, Action.DELETED);
//  }
//
//}
