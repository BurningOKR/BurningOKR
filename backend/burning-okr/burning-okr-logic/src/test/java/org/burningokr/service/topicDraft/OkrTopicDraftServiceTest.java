package org.burningokr.service.topicDraft;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.userhandling.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OkrTopicDraftServiceTest {

  @Mock
  private OkrTopicDraftRepository okrTopicDraftRepository;
  @Mock
  private IUser IUser;
  @Mock
  private ActivityService activityService;

  @Mock
  private UserService userService;
  @Mock
  private AdminUserService adminUserService;

  @InjectMocks
  private OkrTopicDraftService okrTopicDraftService;

  private OkrTopicDraft okrTopicDraft;
  private OkrTopicDraft okrTopicDraft2;
  private OkrTopicDraft okrTopicDraft3;
  private IUser currentIUser;
  private Long okrTopicDraftId = 10L;
  private Long okrTopicDraftId2 = 11L;
  private Long okrTopicDraftId3 = 12L;
  private UUID currentUserId;

  @BeforeEach
  public void setUp() {
    okrTopicDraft = new OkrTopicDraft();
    okrTopicDraft2 = new OkrTopicDraft();
    okrTopicDraft3 = new OkrTopicDraft();
    currentIUser = new LocalUser();
    currentUserId = new UUID(1L, 1L);
    okrTopicDraft.setId(okrTopicDraftId);
    okrTopicDraft2.setId(okrTopicDraftId2);
    okrTopicDraft3.setId(okrTopicDraftId3);
    currentIUser.setId(currentUserId);
  }

  @Test
  public void getAllTopicDrafts_returnsEmptyCollectionWhenThereAreNoTopicDrafts() {
    when(okrTopicDraftService.getAllTopicDrafts()).thenReturn(new ArrayList<>());

    Collection<OkrTopicDraft> topicDrafts = okrTopicDraftService.getAllTopicDrafts();

    assertEquals(0, topicDrafts.size());
  }

  @Test
  public void getAllTopicDrafts_returnsOnlyTopicDraftsWithStatusNotDraft() {
    okrTopicDraft.setInitiatorId(UUID.randomUUID());
    okrTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    okrTopicDraft2.setInitiatorId(UUID.randomUUID());
    okrTopicDraft2.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    okrTopicDraft3.setInitiatorId(UUID.randomUUID());
    okrTopicDraft3.setCurrentStatus(OkrTopicDraftStatusEnum.approved);
    List<OkrTopicDraft> topicDrafts = new ArrayList<>();
    topicDrafts.add(okrTopicDraft);
    topicDrafts.add(okrTopicDraft2);
    topicDrafts.add(okrTopicDraft3);

    when(userService.getCurrentUser()).thenReturn(currentIUser);
    when(adminUserService.isCurrentUserAdmin()).thenReturn(false);
    when(okrTopicDraftService.getAllTopicDrafts()).thenReturn(topicDrafts);

    Collection<OkrTopicDraft> topicDraftsResult = okrTopicDraftService.getAllTopicDrafts();

    assertEquals(2, topicDraftsResult.size());
  }

  @Test
  public void getAllTopicDrafts_returnsAllTopicDraftsBecauseUserIsAdmin() {
    okrTopicDraft.setInitiatorId(UUID.randomUUID());
    okrTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    okrTopicDraft2.setInitiatorId(UUID.randomUUID());
    okrTopicDraft2.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    okrTopicDraft3.setInitiatorId(UUID.randomUUID());
    okrTopicDraft3.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    List<OkrTopicDraft> topicDrafts = new ArrayList<>();
    topicDrafts.add(okrTopicDraft);
    topicDrafts.add(okrTopicDraft2);
    topicDrafts.add(okrTopicDraft3);

    when(userService.getCurrentUser()).thenReturn(currentIUser);
    when(adminUserService.isCurrentUserAdmin()).thenReturn(true);
    when(okrTopicDraftService.getAllTopicDrafts()).thenReturn(topicDrafts);

    Collection<OkrTopicDraft> topicDraftsResult = okrTopicDraftService.getAllTopicDrafts();

    assertEquals(3, topicDraftsResult.size());
  }

  @Test
  public void getAllTopicDrafts_returnsOnlyTopicDraftsWithStatusNotDraftOrUserCreated() {
    okrTopicDraft.setInitiatorId(UUID.randomUUID());
    okrTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    okrTopicDraft2.setInitiatorId(UUID.randomUUID());
    okrTopicDraft2.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    okrTopicDraft3.setInitiatorId(currentUserId);
    okrTopicDraft3.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    List<OkrTopicDraft> topicDrafts = new ArrayList<>();
    topicDrafts.add(okrTopicDraft);
    topicDrafts.add(okrTopicDraft2);
    topicDrafts.add(okrTopicDraft3);

    when(userService.getCurrentUser()).thenReturn(currentIUser);
    when(adminUserService.isCurrentUserAdmin()).thenReturn(false);
    when(okrTopicDraftService.getAllTopicDrafts()).thenReturn(topicDrafts);

    Collection<OkrTopicDraft> topicDraftsResult = okrTopicDraftService.getAllTopicDrafts();

    assertEquals(2, topicDraftsResult.size());
  }

  @Test
  public void deleteTopicDraft_expectedTopicDraftIsDeleted() {
    when(okrTopicDraftRepository.findByIdOrThrow(okrTopicDraftId)).thenReturn(okrTopicDraft);

    okrTopicDraftService.deleteTopicDraftById(okrTopicDraftId, IUser);

    verify(okrTopicDraftRepository).deleteById(okrTopicDraftId);
  }

  @Test
  public void test_deleteObjective_ExpectedActivityGotCreated() {
    when(okrTopicDraftRepository.findByIdOrThrow(okrTopicDraftId)).thenReturn(okrTopicDraft);

    okrTopicDraftService.deleteTopicDraftById(okrTopicDraftId, IUser);

    verify(activityService).createActivity(IUser, this.okrTopicDraft, Action.DELETED);
  }

  @Test
  public void createOkrDraft_expectIsSavedToDatabase() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft, IUser);

    verify(okrTopicDraftRepository).save(any());
  }

  @Test
  public void createOkrDraft_expectCreatesActivity() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft, IUser);

    verify(activityService).createActivity(eq(IUser), any(), eq(Action.CREATED));
  }

  @Test
  public void createOkrDraft_expectParentUnitIsNull() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft, IUser);
    assertNull(topicDraft.getParentUnit());
  }

  @Test
  public void createOkrDraft_expectCurrentStatusEqualsTopic() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();

    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft, IUser);
    assertEquals(OkrTopicDraftStatusEnum.draft, topicDraft.getCurrentStatus());
  }
}
