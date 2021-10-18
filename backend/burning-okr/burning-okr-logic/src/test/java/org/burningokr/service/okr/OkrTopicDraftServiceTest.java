package org.burningokr.service.okr;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.*;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.service.activity.ActivityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrTopicDraftServiceTest {

  @Mock private OkrTopicDraftRepository okrTopicDraftRepository;
  @Mock private User user;
  @Mock private ActivityService activityService;

  @InjectMocks private OkrTopicDraftService okrTopicDraftService;

  private OkrTopicDraft okrTopicDraft;
  private Long okrTopicDraftId = 10L;

  @Before
  public void setUp() {
    okrTopicDraft = new OkrTopicDraft();
    okrTopicDraft.setId(okrTopicDraftId);
  }

  @Test
  public void getAllTopicDrafts_returnsEmptyCollectionWhenThereAreNoTopicDrafts() {
    when(okrTopicDraftService.getAllTopicDrafts()).thenReturn(new ArrayList<>());

    Collection<OkrTopicDraft> topicDrafts = okrTopicDraftService.getAllTopicDrafts();

    assertEquals(0, topicDrafts.size());
  }

  @Test
  public void getAllTopicDrafts_returnsAllCreatedTopicDrafts() {
    List<UUID> startTeam = new ArrayList<>();
    startTeam.add(UUID.randomUUID());
    startTeam.add(UUID.randomUUID());
    startTeam.add(UUID.randomUUID());

    List<UUID> stakeholder = new ArrayList<>();
    stakeholder.add(UUID.randomUUID());
    stakeholder.add(UUID.randomUUID());
    stakeholder.add(UUID.randomUUID());

    okrTopicDraft.setInitiatorId(UUID.randomUUID());
    okrTopicDraft.setDescription("testCriteria");
    okrTopicDraft.setContributesTo("testContributesTo");
    okrTopicDraft.setDelimitation("testDelimitation");
    okrTopicDraft.setBeginning(LocalDate.of(2020, 3, 3));
    okrTopicDraft.setDependencies("testDependencies");
    okrTopicDraft.setResources("testResources");
    okrTopicDraft.setHandoverPlan("testHandoverPlan");
    okrTopicDraft.setStartTeam(startTeam);
    okrTopicDraft.setStakeholders(stakeholder);
    okrTopicDraft.setName("testName");

    List<OkrTopicDraft> topicDrafts = new ArrayList<>();
    topicDrafts.add(okrTopicDraft);
    topicDrafts.add(okrTopicDraft);

    when(okrTopicDraftService.getAllTopicDrafts()).thenReturn(topicDrafts);

    assertEquals(2, topicDrafts.size());
  }

  @Test
  public void deleteTopicDraft_expectedTopicDraftIsDeleted() {
    when(okrTopicDraftRepository.findByIdOrThrow(okrTopicDraftId)).thenReturn(okrTopicDraft);

    okrTopicDraftService.deleteTopicDraftById(okrTopicDraftId, user);

    verify(okrTopicDraftRepository).deleteById(okrTopicDraftId);
  }

  @Test
  public void test_deleteObjective_ExpectedActivityGotCreated() {
    when(okrTopicDraftRepository.findByIdOrThrow(okrTopicDraftId)).thenReturn(okrTopicDraft);

    okrTopicDraftService.deleteTopicDraftById(okrTopicDraftId, user);

    verify(activityService).createActivity(user, this.okrTopicDraft, Action.DELETED);
  }
}
