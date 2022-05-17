package org.burningokr.service.topicDraft;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.exceptions.NotApprovedException;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrUnitService;
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
  @Mock private OkrUnitService<OkrDepartment> okrDepartmentService;
  @Mock private OkrDepartment okrDepartment;
  @Mock private OkrTopicDescription okrTopicDescription;

  @InjectMocks private ConvertTopicDraftToTeamService convertTopicDraftToTeamService;

  private OkrTopicDraft draftTopicDraft;
  private OkrTopicDraft submittedTopicDraft;
  private OkrTopicDraft approvedTopicDraft;
  private OkrTopicDraft rejectedTopicDraft;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() {
    draftTopicDraft = new OkrTopicDraft();
    draftTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    draftTopicDraft.setInitiatorId(UUID.randomUUID());
    draftTopicDraft.setStartTeam(new ArrayList<>());

    submittedTopicDraft = new OkrTopicDraft();
    submittedTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    submittedTopicDraft.setInitiatorId(UUID.randomUUID());
    submittedTopicDraft.setStartTeam(new ArrayList<>());

    approvedTopicDraft = new OkrTopicDraft();
    approvedTopicDraft.setId(0L);
    approvedTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.approved);
    approvedTopicDraft.setInitiatorId(UUID.randomUUID());
    approvedTopicDraft.setStartTeam(new ArrayList<>());

    rejectedTopicDraft = new OkrTopicDraft();
    rejectedTopicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.rejected);
    rejectedTopicDraft.setInitiatorId(UUID.randomUUID());
    rejectedTopicDraft.setStartTeam(new ArrayList<>());

    when(okrDepartmentOkrUnitServiceFactory.getRoleServiceForDepartment(anyLong())).thenReturn(okrDepartmentService);
    when(companyService.createDepartment(anyLong(), any(), isNull())).thenReturn(okrDepartment);
    when(okrDepartmentService.createChildUnit(anyLong(), any(), any())).thenReturn(okrDepartment);
    when(okrDepartment.getOkrTopicDescription()).thenReturn(okrTopicDescription);
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

  @Test
  public void convertTopicDraftToTeam_callsDeleteOperation() {
    when(okrTopicDraftService.findById(anyLong())).thenReturn(approvedTopicDraft);
    when(companyService.getAllCompanies()).thenReturn(new ArrayList<>());

    convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0, null);
    verify(okrTopicDraftService).deleteTopicDraftById(anyLong(), isNull());
  }

  @Test
  public void createOkrDepartmentInDatabase_selectsCompanyServiceWhenGivenACompanyId() {
    when(okrTopicDraftService.findById(anyLong())).thenReturn(approvedTopicDraft);

    Collection<OkrCompany> companyList = new ArrayList<>();
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setId(0L);
    companyList.add(okrCompany);
    when(companyService.getAllCompanies()).thenReturn(companyList);

    convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0, null);
    verify(companyService).createDepartment(anyLong(), any(), isNull());
  }

  @Test
  public void createOkrDepartmentInDatabase_selectsDepartmentServiceWhenNotGivenACompanyId() {
    when(okrTopicDraftService.findById(anyLong())).thenReturn(approvedTopicDraft);
    when(companyService.getAllCompanies()).thenReturn(new ArrayList<>());

    convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0, null);
    verify(okrDepartmentService).createChildUnit(anyLong(), any(), isNull());
  }

  @Test
  public void createOkrDepartmentInDatabase_callsAllRequiredMethods() {
    when(okrTopicDraftService.findById(anyLong())).thenReturn(approvedTopicDraft);
    when(companyService.getAllCompanies()).thenReturn(new ArrayList<>());

    convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0, null);
    verify(okrDepartmentOkrUnitServiceFactory, times(2)).getRoleServiceForDepartment(anyLong());
    verify(okrDepartmentService).createChildUnit(anyLong(), any(), isNull());
    verify(okrDepartmentService).createChildUnit(anyLong(), any(), isNull());
    verify(companyService).getAllCompanies();
    verify(okrDepartmentService).updateUnit(any(), isNull());
    verify(okrTopicDraftService).deleteTopicDraftById(anyLong(), isNull());
    verify(okrDepartment).getOkrTopicDescription();
  }
}
