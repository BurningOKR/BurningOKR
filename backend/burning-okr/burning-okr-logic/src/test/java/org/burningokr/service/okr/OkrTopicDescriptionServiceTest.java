package org.burningokr.service.okr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.LocalUser;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.hibernate.event.spi.PostDeleteEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrTopicDescriptionServiceTest {

  @Mock private OkrTopicDescriptionRepository okrTopicDescriptionRepository;

  @Mock private ActivityService activityService;

  @Mock private OkrDepartmentRepository okrDepartmentRepository;

  @Mock private EntityManagerFactory entityManagerFactory;

  @Mock private EntityManager entityManager;

  @Mock private EntityTransaction entityTransaction;

  @InjectMocks private OkrTopicDescriptionService okrTopicDescriptionService;

  private OkrTopicDescription okrTopicDescription;

  @Before
  public void setUp() {
    okrTopicDescription = new OkrTopicDescription();
    okrTopicDescription.setId(10L);
  }

  @Test
  public void updateOkrTopicDescription_updates() {
    List<UUID> startTeam = new ArrayList<>();
    startTeam.add(UUID.randomUUID());
    startTeam.add(UUID.randomUUID());
    startTeam.add(UUID.randomUUID());

    List<UUID> stakeholder = new ArrayList<>();
    stakeholder.add(UUID.randomUUID());
    stakeholder.add(UUID.randomUUID());
    stakeholder.add(UUID.randomUUID());

    okrTopicDescription.setInitiatorId(UUID.randomUUID());
    okrTopicDescription.setAcceptanceCriteria("testCriteria");
    okrTopicDescription.setContributesTo("testContributesTo");
    okrTopicDescription.setDelimitation("testDelimitation");
    okrTopicDescription.setBeginning(LocalDate.of(2020, 3, 3));
    okrTopicDescription.setDependencies("testDependencies");
    okrTopicDescription.setResources("testResources");
    okrTopicDescription.setHandoverPlan("testHandoverPlan");
    okrTopicDescription.setStartTeam(startTeam);
    okrTopicDescription.setStakeholders(stakeholder);
    okrTopicDescription.setName("testName");

    when(okrTopicDescriptionRepository.findByIdOrThrow(ArgumentMatchers.any(Long.class)))
        .thenReturn(new OkrTopicDescription());
    when(okrTopicDescriptionRepository.save(ArgumentMatchers.any(OkrTopicDescription.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    OkrTopicDescription updatedDescription =
        okrTopicDescriptionService.updateOkrTopicDescription(okrTopicDescription, new LocalUser());

    assertEquals(okrTopicDescription.getInitiatorId(), updatedDescription.getInitiatorId());
    assertEquals(okrTopicDescription.getName(), updatedDescription.getName());
    assertEquals(
        okrTopicDescription.getAcceptanceCriteria(), updatedDescription.getAcceptanceCriteria());
    assertEquals(okrTopicDescription.getBeginning(), updatedDescription.getBeginning());
    assertEquals(okrTopicDescription.getContributesTo(), updatedDescription.getContributesTo());
    assertEquals(okrTopicDescription.getDelimitation(), updatedDescription.getDelimitation());
    assertEquals(okrTopicDescription.getDependencies(), updatedDescription.getDependencies());
    assertEquals(okrTopicDescription.getHandoverPlan(), updatedDescription.getHandoverPlan());
    assertEquals(okrTopicDescription.getResources(), updatedDescription.getResources());
    assertEquals(
        (long) okrTopicDescription.getStakeholders().size(),
        updatedDescription.getStakeholders().size());
    assertEquals(
        (long) okrTopicDescription.getStartTeam().size(), updatedDescription.getStartTeam().size());
  }

  @Test
  public void getOkrDepartmentsWithTopicDescription_returnsEmptyListWhenThereAreNoDepartments() {
    when(okrDepartmentRepository.findAll()).thenReturn(new ArrayList<>());

    List<OkrDepartment> okrDepartments =
        okrTopicDescriptionService.getOkrDepartmentsWithTopicDescription(
            okrTopicDescription.getId());

    assertEquals(0, okrDepartments.size());
  }

  @Test
  public void
      getOkrDepartmentsWithTopicDescription_returnsEmptyListWhenThereAreNoDepartmentsThatReferenceTheTopicDescriptionId() {
    OkrTopicDescription okrTopicDescription1 = new OkrTopicDescription();
    okrTopicDescription1.setId(11L);
    OkrTopicDescription okrTopicDescription2 = new OkrTopicDescription();
    okrTopicDescription2.setId(12L);
    OkrTopicDescription okrTopicDescription3 = new OkrTopicDescription();
    okrTopicDescription3.setId(13L);

    OkrDepartment department1 = new OkrDepartment();
    department1.setOkrTopicDescription(okrTopicDescription1);
    OkrDepartment department2 = new OkrDepartment();
    department2.setOkrTopicDescription(okrTopicDescription2);
    OkrDepartment department3 = new OkrDepartment();
    department3.setOkrTopicDescription(okrTopicDescription3);

    ArrayList<OkrDepartment> okrDepartments = new ArrayList<>();
    okrDepartments.add(department1);
    okrDepartments.add(department2);
    okrDepartments.add(department3);

    when(okrDepartmentRepository.findAll()).thenReturn(okrDepartments);

    List<OkrDepartment> actualOkrDepartments =
        okrTopicDescriptionService.getOkrDepartmentsWithTopicDescription(
            okrTopicDescription.getId());

    assertEquals(0, actualOkrDepartments.size());
  }

  @Test
  public void
      getOkrDepartmentsWithTopicDescription_returnsOnlyDepartmentsWithTheCorrectTopicDescription() {
    OkrTopicDescription okrTopicDescription1 = new OkrTopicDescription();
    okrTopicDescription1.setId(11L);

    OkrDepartment department1 = new OkrDepartment();
    department1.setOkrTopicDescription(okrTopicDescription1);
    OkrDepartment department2 = new OkrDepartment();
    department2.setOkrTopicDescription(okrTopicDescription);
    OkrDepartment department3 = new OkrDepartment();
    department3.setOkrTopicDescription(okrTopicDescription);

    ArrayList<OkrDepartment> okrDepartments = new ArrayList<>();
    okrDepartments.add(department1);
    okrDepartments.add(department2);
    okrDepartments.add(department3);

    when(okrDepartmentRepository.findAll()).thenReturn(okrDepartments);

    List<OkrDepartment> actualOkrDepartments =
        okrTopicDescriptionService.getOkrDepartmentsWithTopicDescription(
            okrTopicDescription.getId());

    assertEquals(2, actualOkrDepartments.size());
  }

  @Test
  public void
      getOkrDepartmentsWithTopicDescription_returnsAllDepartmentsWithTheCorrectTopicDescription() {
    OkrDepartment department1 = new OkrDepartment();
    department1.setOkrTopicDescription(okrTopicDescription);
    OkrDepartment department2 = new OkrDepartment();
    department2.setOkrTopicDescription(okrTopicDescription);
    OkrDepartment department3 = new OkrDepartment();
    department3.setOkrTopicDescription(okrTopicDescription);

    ArrayList<OkrDepartment> okrDepartments = new ArrayList<>();
    okrDepartments.add(department1);
    okrDepartments.add(department2);
    okrDepartments.add(department3);

    when(okrDepartmentRepository.findAll()).thenReturn(okrDepartments);

    List<OkrDepartment> actualOkrDepartments =
        okrTopicDescriptionService.getOkrDepartmentsWithTopicDescription(
            okrTopicDescription.getId());

    assertEquals(3, actualOkrDepartments.size());
  }

  @Test
  public void safeDeleteOkrTopicDescription_deletesOkrTopicDescription() {
    when(okrDepartmentRepository.findAll()).thenReturn(new ArrayList<>());
    when(entityManager.find(OkrTopicDescription.class, okrTopicDescription.getId()))
        .thenReturn(okrTopicDescription);
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(entityTransaction);

    okrTopicDescriptionService.safeDeleteOkrTopicDescription(
        okrTopicDescription.getId(), new LocalUser());

    verify(entityManager).remove(okrTopicDescription);
  }

  @Test
  public void safeDeleteOkrTopicDescription_doesNotDeleteWhenThereAreOtherDepartmentsReferencing() {
    OkrDepartment department1 = new OkrDepartment();
    department1.setOkrTopicDescription(okrTopicDescription);
    OkrDepartment department2 = new OkrDepartment();
    department2.setOkrTopicDescription(okrTopicDescription);
    OkrDepartment department3 = new OkrDepartment();
    department3.setOkrTopicDescription(okrTopicDescription);

    ArrayList<OkrDepartment> okrDepartments = new ArrayList<>();
    okrDepartments.add(department1);
    okrDepartments.add(department2);
    okrDepartments.add(department3);

    when(okrDepartmentRepository.findAll()).thenReturn(okrDepartments);

    okrTopicDescriptionService.safeDeleteOkrTopicDescription(
        okrTopicDescription.getId(), new LocalUser());

    verify(entityManager, never()).remove(any());
  }

  @Test
  public void onPostDelete_doesNothingWhenItIsNotAnOkrDepartment() {
    PostDeleteEvent postDeleteEvent = new PostDeleteEvent(new OkrBranch(), null, null, null, null);
    okrTopicDescriptionService.onPostDelete(postDeleteEvent);

    verify(entityManager, never()).remove(any());
  }

  @Test
  public void onPostDelete_deletesWhenItIsAnOkrDepartment() {
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setOkrTopicDescription(okrTopicDescription);

    PostDeleteEvent postDeleteEvent = new PostDeleteEvent(okrDepartment, null, null, null, null);

    when(okrDepartmentRepository.findAll()).thenReturn(new ArrayList<>());
    when(entityManager.find(OkrTopicDescription.class, okrTopicDescription.getId()))
        .thenReturn(okrTopicDescription);
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(entityTransaction);

    okrTopicDescriptionService.onPostDelete(postDeleteEvent);

    verify(entityManager).remove(okrTopicDescription);
  }

  @Test
  public void onPostDelete_doesNotDeleteWhenThereAreOtherDepartmentsReferencing() {
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setOkrTopicDescription(okrTopicDescription);

    PostDeleteEvent postDeleteEvent = new PostDeleteEvent(okrDepartment, null, null, null, null);

    OkrDepartment department1 = new OkrDepartment();
    department1.setOkrTopicDescription(okrTopicDescription);
    OkrDepartment department2 = new OkrDepartment();
    department2.setOkrTopicDescription(okrTopicDescription);
    OkrDepartment department3 = new OkrDepartment();
    department3.setOkrTopicDescription(okrTopicDescription);

    ArrayList<OkrDepartment> okrDepartments = new ArrayList<>();
    okrDepartments.add(department1);
    okrDepartments.add(department2);
    okrDepartments.add(department3);

    when(okrDepartmentRepository.findAll()).thenReturn(okrDepartments);

    okrTopicDescriptionService.onPostDelete(postDeleteEvent);

    verify(entityManager, never()).remove(any());
  }

  @Test
  public void requiresPostCommitHanding_returnsTrue() {
    assertTrue(okrTopicDescriptionService.requiresPostCommitHanding(null));
  }

  @Test
  public void requiresPostCommitHandling_returnsTrue() {
    assertTrue(okrTopicDescriptionService.requiresPostCommitHandling(null));
  }
}
