package org.burningokr.service.topicDraft;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.exceptions.NotApprovedException;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrUnitServiceFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConvertTopicDraftToTeamServiceTest {

  @Mock private OkrTopicDraftService okrTopicDraftService;
  @Mock private CompanyService companyService;
  @Mock private OkrUnitServiceFactory<OkrDepartment> okrDepartmentOkrUnitServiceFactory;

  @InjectMocks private ConvertTopicDraftToTeamService convertTopicDraftToTeamService;

  private OkrTopicDraft draftTopicDraft;
  private OkrTopicDraft submittedTopicDraft;
  private OkrTopicDraft approvedTopicDraft;
  private OkrTopicDraft rejectedTopicDraft;
  private Collection<OkrTopicDraft> okrTopicDraftCollection;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() {
    okrTopicDraftCollection = new ArrayList<>();

    draftTopicDraft = new OkrTopicDraft();
    draftTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    draftTopicDraft.setInitiatorId(UUID.randomUUID());
    okrTopicDraftCollection.add(draftTopicDraft);

    submittedTopicDraft = new OkrTopicDraft();
    submittedTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    submittedTopicDraft.setInitiatorId(UUID.randomUUID());
    okrTopicDraftCollection.add(submittedTopicDraft);

    approvedTopicDraft = new OkrTopicDraft();
    approvedTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.approved);
    approvedTopicDraft.setInitiatorId(UUID.randomUUID());
    okrTopicDraftCollection.add(approvedTopicDraft);

    rejectedTopicDraft = new OkrTopicDraft();
    rejectedTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.rejected);
    rejectedTopicDraft.setInitiatorId(UUID.randomUUID());
    okrTopicDraftCollection.add(rejectedTopicDraft);
  }

  @Test(expected = NotApprovedException.class)
  public void convertTopicDraftToTeamService_throwsIfTheTopicDraftIsDraft() {
    when(okrTopicDraftService.findById(anyLong())).thenReturn(draftTopicDraft);
    convertTopicDraftToTeamService.convertTopicDraftToTeam(0,0,null);
  }

  @Test(expected = NotApprovedException.class)
  public void convertTopicDraftToTeamService_throwsIfTheTopicDraftIsSubmitted() {
    when(okrTopicDraftService.findById(anyLong())).thenReturn(submittedTopicDraft);
    convertTopicDraftToTeamService.convertTopicDraftToTeam(0,0,null);
  }

  @Test(expected = NotApprovedException.class)
  public void convertTopicDraftToTeamService_throwsIfTheTopicDraftIsRejected() {
    when(okrTopicDraftService.findById(anyLong())).thenReturn(rejectedTopicDraft);
    convertTopicDraftToTeamService.convertTopicDraftToTeam(0,0,null);
  }
}
