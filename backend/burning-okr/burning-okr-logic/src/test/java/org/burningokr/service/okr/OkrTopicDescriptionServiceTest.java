package org.burningokr.service.okr;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.repositories.okr.OkrTopicDescriptionRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.hibernate.event.spi.PostDeleteEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OkrTopicDescriptionServiceTest {
  @Mock
  private OkrTopicDescriptionRepository topicDescriptionRepository;
  @Mock
  private ActivityService activityService;
  @Mock
  private OkrDepartmentRepository departmentRepository;
  @Mock
  private EntityManagerFactory entityManagerFactory;

  @InjectMocks
  private OkrTopicDescriptionService topicDescriptionService;

  private OkrTopicDescription topicDescription;

  @BeforeEach
  public void setUp() {
    topicDescription = new OkrTopicDescription();
    topicDescription.setId(10L);
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

    topicDescription.setInitiatorId(UUID.randomUUID());
    topicDescription.setDescription("testCriteria");
    topicDescription.setContributesTo("testContributesTo");
    topicDescription.setDelimitation("testDelimitation");
    topicDescription.setBeginning(LocalDate.of(2020, 3, 3));
    topicDescription.setDependencies("testDependencies");
    topicDescription.setResources("testResources");
    topicDescription.setHandoverPlan("testHandoverPlan");
    topicDescription.setStartTeam(startTeam);
    topicDescription.setStakeholders(stakeholder);
    topicDescription.setName("testName");

    when(topicDescriptionRepository.findByIdOrThrow(ArgumentMatchers.any(Long.class)))
            .thenReturn(new OkrTopicDescription());
    when(topicDescriptionRepository.save(ArgumentMatchers.any(OkrTopicDescription.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    OkrTopicDescription updatedDescription =
            topicDescriptionService.updateOkrTopicDescription(topicDescription);

    assertEquals(topicDescription.getInitiatorId(), updatedDescription.getInitiatorId());
    assertEquals(topicDescription.getName(), updatedDescription.getName());
    assertEquals(topicDescription.getDescription(), updatedDescription.getDescription());
    assertEquals(topicDescription.getBeginning(), updatedDescription.getBeginning());
    assertEquals(topicDescription.getContributesTo(), updatedDescription.getContributesTo());
    assertEquals(topicDescription.getDelimitation(), updatedDescription.getDelimitation());
    assertEquals(topicDescription.getDependencies(), updatedDescription.getDependencies());
    assertEquals(topicDescription.getHandoverPlan(), updatedDescription.getHandoverPlan());
    assertEquals(topicDescription.getResources(), updatedDescription.getResources());
    assertEquals(
            (long) topicDescription.getStakeholders().size(),
            updatedDescription.getStakeholders().size()
    );
    assertEquals(
            (long) topicDescription.getStartTeam().size(), updatedDescription.getStartTeam().size());
  }

  @Test
  public void getOkrDepartmentsWithTopicDescription_shouldReturnEmptyListWhenThereAreNoDepartments() {
    when(departmentRepository.findAll()).thenReturn(new ArrayList<>());

    List<OkrDepartment> okrDepartments =
            topicDescriptionService.getOkrDepartmentsWithTopicDescription(
                    topicDescription.getId());

    assertEquals(0, okrDepartments.size());
  }

  @Test
  public void
  getOkrDepartmentsWithTopicDescription_shouldReturnEmptyListWhenThereAreNoDepartmentsThatReferenceTheTopicDescriptionId() {
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

    when(departmentRepository.findAll()).thenReturn(okrDepartments);

    List<OkrDepartment> actualOkrDepartments =
            topicDescriptionService.getOkrDepartmentsWithTopicDescription(
                    topicDescription.getId());

    assertEquals(0, actualOkrDepartments.size());
  }

  @Test
  public void
  getOkrDepartmentsWithTopicDescription_shouldReturnOnlyDepartmentsWithTheCorrectTopicDescription() {
    OkrTopicDescription okrTopicDescription1 = new OkrTopicDescription();
    okrTopicDescription1.setId(11L);

    OkrDepartment department1 = new OkrDepartment();
    department1.setOkrTopicDescription(okrTopicDescription1);
    OkrDepartment department2 = new OkrDepartment();
    department2.setOkrTopicDescription(topicDescription);
    OkrDepartment department3 = new OkrDepartment();
    department3.setOkrTopicDescription(topicDescription);

    ArrayList<OkrDepartment> okrDepartments = new ArrayList<>();
    okrDepartments.add(department1);
    okrDepartments.add(department2);
    okrDepartments.add(department3);

    when(departmentRepository.findAll()).thenReturn(okrDepartments);

    List<OkrDepartment> actualOkrDepartments =
            topicDescriptionService.getOkrDepartmentsWithTopicDescription(
                    topicDescription.getId());

    assertEquals(2, actualOkrDepartments.size());
  }

  @Test
  public void
  getOkrDepartmentsWithTopicDescription_shouldReturnAllDepartmentsWithTheCorrectTopicDescription() {
    OkrDepartment department1 = new OkrDepartment();
    department1.setOkrTopicDescription(topicDescription);
    OkrDepartment department2 = new OkrDepartment();
    department2.setOkrTopicDescription(topicDescription);
    OkrDepartment department3 = new OkrDepartment();
    department3.setOkrTopicDescription(topicDescription);

    ArrayList<OkrDepartment> okrDepartments = new ArrayList<>();
    okrDepartments.add(department1);
    okrDepartments.add(department2);
    okrDepartments.add(department3);

    when(departmentRepository.findAll()).thenReturn(okrDepartments);

    List<OkrDepartment> actualOkrDepartments =
            topicDescriptionService.getOkrDepartmentsWithTopicDescription(
                    topicDescription.getId());

    assertEquals(3, actualOkrDepartments.size());
  }

  @Test
  public void safeDeleteOkrTopicDescription_shouldDeleteOkrTopicDescription() {
    EntityManager entityManager = mock(EntityManager.class);
    EntityTransaction entityTransaction = mock(EntityTransaction.class);
    when(departmentRepository.findAll()).thenReturn(new ArrayList<>());
    when(entityManager.find(OkrTopicDescription.class, topicDescription.getId()))
            .thenReturn(topicDescription);
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(entityTransaction);

    topicDescriptionService.safeDeleteOkrTopicDescription(
            topicDescription.getId());

    verify(entityManager).remove(topicDescription);
  }

  @Test
  public void safeDeleteOkrTopicDescription_shouldNotDeleteWhenThereAreOtherDepartmentsReferencing() {
    EntityManager entityManager = mock(EntityManager.class);
    OkrDepartment department1 = new OkrDepartment();
    department1.setOkrTopicDescription(topicDescription);
    OkrDepartment department2 = new OkrDepartment();
    department2.setOkrTopicDescription(topicDescription);
    OkrDepartment department3 = new OkrDepartment();
    department3.setOkrTopicDescription(topicDescription);

    ArrayList<OkrDepartment> okrDepartments = new ArrayList<>();
    okrDepartments.add(department1);
    okrDepartments.add(department2);
    okrDepartments.add(department3);

    when(departmentRepository.findAll()).thenReturn(okrDepartments);

    topicDescriptionService.safeDeleteOkrTopicDescription(
            topicDescription.getId());

    verify(entityManager, never()).remove(any());
  }

  @Test
  public void onPostDelete_shouldDoNothingWhenItIsNotAnOkrDepartment() {
    EntityManager entityManager = mock(EntityManager.class);
    PostDeleteEvent postDeleteEvent = new PostDeleteEvent(new OkrBranch(), null, null, null, null);
    topicDescriptionService.onPostDelete(postDeleteEvent);

    verify(entityManager, never()).remove(any());
  }

  @Test
  public void onPostDelete_shouldDeleteWhenItIsAnOkrDepartment() {
    EntityManager entityManager = mock(EntityManager.class);
    EntityTransaction entityTransaction = mock(EntityTransaction.class);
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setOkrTopicDescription(topicDescription);

    PostDeleteEvent postDeleteEvent = new PostDeleteEvent(okrDepartment, null, null, null, null);

    when(departmentRepository.findAll()).thenReturn(new ArrayList<>());
    when(entityManager.find(OkrTopicDescription.class, topicDescription.getId()))
            .thenReturn(topicDescription);
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(entityTransaction);

    topicDescriptionService.onPostDelete(postDeleteEvent);

    verify(entityManager).remove(topicDescription);
  }

  @Test
  public void onPostDelete_shouldNotDeleteWhenThereAreOtherDepartmentsReferencing() {
    EntityManager entityManager = mock(EntityManager.class);
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setOkrTopicDescription(topicDescription);

    PostDeleteEvent postDeleteEvent = new PostDeleteEvent(okrDepartment, null, null, null, null);

    OkrDepartment department1 = new OkrDepartment();
    department1.setOkrTopicDescription(topicDescription);
    OkrDepartment department2 = new OkrDepartment();
    department2.setOkrTopicDescription(topicDescription);
    OkrDepartment department3 = new OkrDepartment();
    department3.setOkrTopicDescription(topicDescription);

    ArrayList<OkrDepartment> okrDepartments = new ArrayList<>();
    okrDepartments.add(department1);
    okrDepartments.add(department2);
    okrDepartments.add(department3);

    when(departmentRepository.findAll()).thenReturn(okrDepartments);

    topicDescriptionService.onPostDelete(postDeleteEvent);

    verify(entityManager, never()).remove(any());
  }

  @Test
  public void requiresPostCommitHanding_shouldReturnTrue() {
    assertTrue(topicDescriptionService.requiresPostCommitHandling(null));
  }

  @Test
  public void requiresPostCommitHandling_shouldReturnTrue() {
    assertTrue(topicDescriptionService.requiresPostCommitHandling(null));
  }
}
