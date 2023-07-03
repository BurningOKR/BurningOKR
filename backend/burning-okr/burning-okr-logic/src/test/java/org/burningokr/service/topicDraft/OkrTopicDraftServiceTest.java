package org.burningokr.service.topicDraft;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.users.User;
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
  private User user;
  @Mock
  private ActivityService activityService;

  @Mock
  private UserService userService;

  @InjectMocks
  private OkrTopicDraftService okrTopicDraftService;

  @BeforeEach
  public void setUp() {
    OkrTopicDraft okrTopicDraft = new OkrTopicDraft();
    OkrTopicDraft okrTopicDraft2 = new OkrTopicDraft();
    OkrTopicDraft okrTopicDraft3 = new OkrTopicDraft();
    User currentUser = new User();
    UUID currentUserId = new UUID(1L, 1L);
    Long okrTopicDraftId = 10L;
    okrTopicDraft.setId(okrTopicDraftId);
    Long okrTopicDraftId2 = 11L;
    okrTopicDraft2.setId(okrTopicDraftId2);
    Long okrTopicDraftId3 = 12L;
    okrTopicDraft3.setId(okrTopicDraftId3);
    currentUser.setId(currentUserId);
  }

  @Test
  public void getAllTopicDrafts_shouldReturnEmptyCollectionWhenThereAreNoTopicDrafts() {
    when(okrTopicDraftService.getAllTopicDrafts()).thenReturn(new ArrayList<>());

    Collection<OkrTopicDraft> topicDrafts = okrTopicDraftService.getAllTopicDrafts();

    assertEquals(0, topicDrafts.size());
  }

//TODO (F. L. 27.06.2023) add tests
//
//  @Test
//  public void getAllTopicDrafts_shouldReturnOnlyTopicDraftsWithStatusNoDraft() {
//    okrTopicDraft.setInitiatorId(UUID.randomUUID());
//    okrTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
//    okrTopicDraft2.setInitiatorId(UUID.randomUUID());
//    okrTopicDraft2.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
//    okrTopicDraft3.setInitiatorId(UUID.randomUUID());
//    okrTopicDraft3.setCurrentStatus(OkrTopicDraftStatusEnum.approved);
//    List<OkrTopicDraft> topicDrafts = new ArrayList<>() {
//      {
//        add(okrTopicDraft);
//        add(okrTopicDraft2);
//        add(okrTopicDraft3);
//      }
//    };
//    when(userService.getCurrentUser()).thenReturn(currentUser);
//    when(okrTopicDraftService.getAllTopicDrafts()).thenReturn(topicDrafts);
//
//    Collection<OkrTopicDraft> topicDraftsResult = okrTopicDraftService.getAllTopicDrafts();
//
//    assertEquals(2, topicDraftsResult.size());
//  }
//  @Test
//  public void getAllTopicDrafts_shouldReturnAllTopicDraftsBecauseUserIsAdmin() {
//    okrTopicDraft.setInitiatorId(UUID.randomUUID());
//    okrTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
//    okrTopicDraft2.setInitiatorId(UUID.randomUUID());
//    okrTopicDraft2.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
//    okrTopicDraft3.setInitiatorId(UUID.randomUUID());
//    okrTopicDraft3.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
//    List<OkrTopicDraft> topicDrafts = new ArrayList<>() {
//      {
//          add(okrTopicDraft);
//          add(okrTopicDraft2);
//          add(okrTopicDraft3);
//      }
//    };
//
//    when(userService.getCurrentUser()).thenReturn(currentUser);
//    when(okrTopicDraftService.getAllTopicDrafts()).thenReturn(topicDrafts);
//
//    Collection<OkrTopicDraft> topicDraftsResult = okrTopicDraftService.getAllTopicDrafts();
//
//    assertEquals(3, topicDraftsResult.size());
//  }
//
//  @Test
//  public void getAllTopicDrafts_shouldReturnOnlyTopicDraftsWithStatusNotDraftOrUserCreated() {
//    okrTopicDraft.setInitiatorId(UUID.randomUUID());
//    okrTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
//    okrTopicDraft2.setInitiatorId(UUID.randomUUID());
//    okrTopicDraft2.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
//    okrTopicDraft3.setInitiatorId(currentUserId);
//    okrTopicDraft3.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
//    List<OkrTopicDraft> topicDrafts = new ArrayList<>() {
//      {
//          add(okrTopicDraft);
//          add(okrTopicDraft2);
//          add(okrTopicDraft3);
//      }
//    };
//
//    when(userService.getCurrentUser()).thenReturn(currentUser);
//    when(okrTopicDraftService.getAllTopicDrafts()).thenReturn(topicDrafts);
//
//    Collection<OkrTopicDraft> topicDraftsResult = okrTopicDraftService.getAllTopicDrafts();
//
//    assertEquals(2, topicDraftsResult.size());
//  }

  @Test
  public void deleteTopicDraft_shouldDeleteTopicDraft() {
    Long okrTopicDraftId = 12L;
    OkrTopicDraft okrTopicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepository.findByIdOrThrow(okrTopicDraftId)).thenReturn(okrTopicDraft);

    okrTopicDraftService.deleteTopicDraftById(okrTopicDraftId);

    verify(okrTopicDraftRepository).deleteById(okrTopicDraftId);
  }

  @Test
  public void deleteTopicDraft_shouldCreateActivity() {
    Long okrTopicDraftId = 12L;
    OkrTopicDraft okrTopicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepository.findByIdOrThrow(okrTopicDraftId)).thenReturn(okrTopicDraft);

    okrTopicDraftService.deleteTopicDraftById(okrTopicDraftId);

    verify(activityService).createActivity(okrTopicDraft, Action.DELETED);
  }

  @Test
  public void createTopicDraft_shouldSaveTopicDraftToDatabase() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft);

    verify(okrTopicDraftRepository).save(any());
  }

  @Test
  public void createTopicDraft_shouldCreateActivity() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft);

    verify(activityService).createActivity(any(), eq(Action.CREATED));
  }

  @Test
  public void createTopicDraft_shouldCheckThatParentUnitIsNull() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft);

    assertNull(topicDraft.getParentUnit());
  }

  @Test
  public void createTopicDraft_shouldBeEqualToStatusEnum() {
    OkrTopicDraft topicDraft = new OkrTopicDraft();
    when(okrTopicDraftRepository.save(any())).thenReturn(topicDraft);

    okrTopicDraftService.createTopicDraft(topicDraft);
    assertEquals(OkrTopicDraftStatusEnum.draft, topicDraft.getCurrentStatus());
  }
}
