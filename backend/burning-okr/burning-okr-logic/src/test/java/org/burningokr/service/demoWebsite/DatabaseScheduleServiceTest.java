package org.burningokr.service.demoWebsite;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.burningokr.model.configuration.SystemProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseScheduleServiceTest {
  @Mock
  private SystemProperties systemProperties;
  @Mock
  private EntityManagerFactory entityManagerFactory;

  @InjectMocks
  private DatabaseScheduleService databaseScheduleService;

  @Test
  public void Test_scheduledDeletion_loadsSqlAndRunsIfSystemIsInDemoMode()
  {
    when(systemProperties.isDemoMode()).thenReturn(true);
    EntityTransaction transaction = mock(EntityTransaction.class);
    EntityManager entityManager = mock(EntityManager.class);
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(transaction);
    when(entityManager.createNativeQuery(any())).thenReturn(mock(Query.class));

    databaseScheduleService.scheduledDeletion();

    long nextSchedule = databaseScheduleService.getNextSchedule();
    assert(120*60*1000 - 2*1000 < nextSchedule);
    assert(120*60*1000 + 2*1000 > nextSchedule); // Two Seconds buffer for test run

    verify(entityManager).createNativeQuery(anyString());
    verify(transaction).commit();
  }
}
