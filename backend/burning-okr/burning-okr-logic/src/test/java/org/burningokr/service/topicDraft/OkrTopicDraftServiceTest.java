package org.burningokr.service.topicDraft;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.NoteTopicDraftRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextServiceKeycloak;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OkrTopicDraftServiceTest {

  @Mock
  private OkrTopicDraftRepository okrTopicDraftRepositoryMock;
  @Mock
  private ActivityService activityServiceMock;
  @Mock
  private AuthenticationUserContextServiceKeycloak authorizationUserContextServiceMock;
  @Mock
  private NoteTopicDraftRepository noteTopicDraftRepositoryMock;
  @Mock
  private OkrUnit okrUnitMock;
  @Mock
  private OkrUnit okrUnitMock2;
  @InjectMocks
  private OkrTopicDraftService okrTopicDraftService;

  private OkrTopicDraft okrTopicDraftMock;
  private OkrTopicDraft okrTopicDraftMock2;
  private OkrTopicDraft okrTopicDraftMock3;
  private User currentUser;
  private NoteTopicDraft noteTopicDraft;

  private OkrTopicDraft createOkrTopicDraft() {
    OkrTopicDraft okrTopicDraftMock = new OkrTopicDraft();
    okrTopicDraftMock.setId(new Random().nextLong());
    okrTopicDraftMock.setBeginning(LocalDate.of(2023, 1, 1));
    okrTopicDraftMock.setContributesTo("");
    okrTopicDraftMock.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    okrTopicDraftMock.setDelimitation("");
    okrTopicDraftMock.setDependencies("");
    okrTopicDraftMock.setHandoverPlan("");
    okrTopicDraftMock.setInitiatorId(UUID.randomUUID());
    okrTopicDraftMock.setName("");
    okrTopicDraftMock.setParentUnit(okrUnitMock);
    okrTopicDraftMock.setResources("");
    okrTopicDraftMock.setStakeholders(createUUIDCollection());
    okrTopicDraftMock.setStartTeam(createUUIDCollection());

    return okrTopicDraftMock;
  }

  private Collection<UUID> createUUIDCollection() {
    Collection<UUID> uuidCollection = new ArrayList<>();
    uuidCollection.add(UUID.randomUUID());
    uuidCollection.add(UUID.randomUUID());
    uuidCollection.add(UUID.randomUUID());
    return uuidCollection;
  }

  @BeforeEach
  public void setUp() {
    okrTopicDraftMock = createOkrTopicDraft();
    okrTopicDraftMock2 = createOkrTopicDraft();
    okrTopicDraftMock3 = createOkrTopicDraft();


    currentUser = new User();
    UUID currentUserId = new UUID(1L, 1L);
    currentUser.setId(currentUserId);


    noteTopicDraft = new NoteTopicDraft();
    noteTopicDraft.setId(100L);
    noteTopicDraft.setText("Text");
    noteTopicDraft.setUserId(UUID.randomUUID());
    noteTopicDraft.setDate(LocalDateTime.now());
    noteTopicDraft.setParentTopicDraft(okrTopicDraftMock);
  }

  @Test
  public void findById_shouldReturnTopicDraftWithCorrectId() {
    long topicDraftIdToFind = 10L;
    OkrTopicDraft expected = new OkrTopicDraft();
    when(okrTopicDraftRepositoryMock.findByIdOrThrow(topicDraftIdToFind)).thenReturn(expected);

    OkrTopicDraft actual = okrTopicDraftService.findById(topicDraftIdToFind);

    assertSame(expected, actual);
  }

  @Test
  public void getAllTopicDrafts_shouldReturnEmptyCollectionWhenThereAreNoTopicDrafts() {
    currentUser.setAdmin(true);
    when(okrTopicDraftRepositoryMock.findAll()).thenReturn(new ArrayList<>());

    Collection<OkrTopicDraft> topicDrafts = okrTopicDraftService.getAllTopicDrafts();

    assertEquals(0, topicDrafts.size());
  }

  @Test
  public void getAllTopicDrafts_shouldReturnOnlyTopicDraftsWithStatusNoDraftForCurrentUser() {
    okrTopicDraftMock.setInitiatorId(UUID.randomUUID());
    okrTopicDraftMock.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    okrTopicDraftMock2.setInitiatorId(UUID.randomUUID());
    okrTopicDraftMock2.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    okrTopicDraftMock3.setInitiatorId(UUID.randomUUID());
    okrTopicDraftMock3.setCurrentStatus(OkrTopicDraftStatusEnum.approved);
    List<OkrTopicDraft> topicDrafts = new ArrayList<>() {
      {
        add(okrTopicDraftMock);
        add(okrTopicDraftMock2);
        add(okrTopicDraftMock3);
      }
    };

    when(okrTopicDraftRepositoryMock.findAll()).thenReturn(topicDrafts);
    when(authorizationUserContextServiceMock.getAuthenticatedUser()).thenReturn(currentUser);

    Collection<OkrTopicDraft> topicDraftsResult = okrTopicDraftService.getAllTopicDrafts();

    assertEquals(2, topicDraftsResult.size());
  }

  @Test
  public void getAllTopicDrafts_shouldReturnAllTopicDraftsBecauseUserIsAdmin() {
    currentUser.setAdmin(true);

    okrTopicDraftMock.setInitiatorId(UUID.randomUUID());
    okrTopicDraftMock.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    okrTopicDraftMock2.setInitiatorId(UUID.randomUUID());
    okrTopicDraftMock2.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    okrTopicDraftMock3.setInitiatorId(UUID.randomUUID());
    okrTopicDraftMock3.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    List<OkrTopicDraft> topicDrafts = new ArrayList<>() {
      {
          add(okrTopicDraftMock);
          add(okrTopicDraftMock2);
          add(okrTopicDraftMock3);
      }
    };

    when(okrTopicDraftRepositoryMock.findAll()).thenReturn(topicDrafts);
    when(authorizationUserContextServiceMock.getAuthenticatedUser()).thenReturn(currentUser);

    Collection<OkrTopicDraft> topicDraftsResult = okrTopicDraftService.getAllTopicDrafts();

    assertEquals(3, topicDraftsResult.size());
  }

  @Test
  public void getAllTopicDrafts_shouldReturnOnlyTopicDraftsWithStatusNotDraftOrUserCreated() {
    okrTopicDraftMock.setInitiatorId(UUID.randomUUID());
    okrTopicDraftMock.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    okrTopicDraftMock2.setInitiatorId(UUID.randomUUID());
    okrTopicDraftMock2.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    okrTopicDraftMock3.setInitiatorId(currentUser.getId());
    okrTopicDraftMock3.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    List<OkrTopicDraft> topicDrafts = new ArrayList<>() {
      {
          add(okrTopicDraftMock);
          add(okrTopicDraftMock2);
          add(okrTopicDraftMock3);
      }
    };

    when(okrTopicDraftRepositoryMock.findAll()).thenReturn(topicDrafts);
    when(authorizationUserContextServiceMock.getAuthenticatedUser()).thenReturn(currentUser);

    Collection<OkrTopicDraft> topicDraftsResult = okrTopicDraftService.getAllTopicDrafts();

    assertEquals(2, topicDraftsResult.size());
  }

  @Test
  public void deleteTopicDraft_shouldDeleteTopicDraft() {
    Long okrTopicDraftId = 12L;
    OkrTopicDraft okrTopicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepositoryMock.findByIdOrThrow(okrTopicDraftId)).thenReturn(okrTopicDraft);

    okrTopicDraftService.deleteTopicDraftById(okrTopicDraftId);

    verify(okrTopicDraftRepositoryMock).deleteById(okrTopicDraftId);
  }

  @Test
  public void deleteTopicDraft_shouldCreateActivity() {
    Long okrTopicDraftId = 12L;
    OkrTopicDraft okrTopicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepositoryMock.findByIdOrThrow(okrTopicDraftId)).thenReturn(okrTopicDraft);

    okrTopicDraftService.deleteTopicDraftById(okrTopicDraftId);

    verify(activityServiceMock).createActivity(okrTopicDraft, Action.DELETED);
  }

  @Test
  public void createTopicDraft_shouldSaveTopicDraftToDatabase() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepositoryMock.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft);

    verify(okrTopicDraftRepositoryMock).save(any());
  }

  @Test
  public void createTopicDraft_shouldCreateActivity() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepositoryMock.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft);

    verify(activityServiceMock).createActivity(any(), eq(Action.CREATED));
  }

  @Test
  public void createTopicDraft_shouldCheckThatParentUnitIsNull() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepositoryMock.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft);

    assertNull(topicDraft.getParentUnit());
  }

  @Test
  public void createTopicDraft_shouldBeEqualToStatusEnum() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepositoryMock.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft);
    assertEquals(OkrTopicDraftStatusEnum.draft, topicDraft.getCurrentStatus());
  }

  @Test
  public void createNote_shouldSaveNote() {
    when(authorizationUserContextServiceMock.getAuthenticatedUser()).thenReturn(currentUser);
    okrTopicDraftService.createNote(noteTopicDraft);
    verify(noteTopicDraftRepositoryMock).save(noteTopicDraft);
  }

  @Test
  public void createNote_shouldCreateAndReturnNewNote() {
    when(authorizationUserContextServiceMock.getAuthenticatedUser()).thenReturn(currentUser);
    when(noteTopicDraftRepositoryMock.save(noteTopicDraft)).thenReturn(noteTopicDraft);

    assertEquals(noteTopicDraft, okrTopicDraftService.createNote(noteTopicDraft));
  }

  @Test
  public void updateOkrTopicDraft_shouldUpdate(){
    //Arrange
    okrTopicDraftMock2.setBeginning(LocalDate.now());
    okrTopicDraftMock2.setContributesTo("Updated");
    okrTopicDraftMock2.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    okrTopicDraftMock2.setDelimitation("Updated");
    okrTopicDraftMock2.setDependencies("Updated");
    okrTopicDraftMock2.setHandoverPlan("Updated");
    okrTopicDraftMock2.setName("Updated");
    okrTopicDraftMock2.setParentUnit(okrUnitMock2);
    okrTopicDraftMock2.setResources("");
    okrTopicDraftMock2.setStakeholders(createUUIDCollection());
    okrTopicDraftMock2.setStartTeam(createUUIDCollection());

    doReturn(okrTopicDraftMock).when(okrTopicDraftRepositoryMock).findByIdOrThrow(okrTopicDraftMock.getId());
    doReturn(okrTopicDraftMock).when(okrTopicDraftRepositoryMock).save(okrTopicDraftMock);

    //Act
    okrTopicDraftService.updateOkrTopicDraft(okrTopicDraftMock.getId(), okrTopicDraftMock2);

    //Assert
    verify(okrTopicDraftRepositoryMock).save(okrTopicDraftMock);
    assertEquals(okrTopicDraftMock2, okrTopicDraftMock);
  }

}
