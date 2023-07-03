package org.burningokr.service.topicDraft;

import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.exceptions.NotApprovedException;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConvertTopicDraftToTeamServiceTest {

  @Mock
  private OkrTopicDraftService topicDraftService;
  @Mock
  private CompanyService companyService;
  @Mock
  private OkrChildUnitService<OkrDepartment> departmentService;
  @Mock
  private OkrDepartment department;
  @Mock
  private OkrTopicDescription topicDescription;

  @InjectMocks
  private ConvertTopicDraftToTeamService convertTopicDraftToTeamService;

  private OkrTopicDraft draftTopicDraft;
  private OkrTopicDraft submittedTopicDraft;
  private OkrTopicDraft approvedTopicDraft;
  private OkrTopicDraft rejectedTopicDraft;

  @BeforeEach
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
  }

  @Test
  public void convertTopicDraftToTeam_shouldThrowIfTheTopicDraftIsDraft() {
    when(topicDraftService.findById(anyLong())).thenReturn(draftTopicDraft);

    assertThrows(NotApprovedException.class, () -> convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0));
  }

  @Test
  public void convertTopicDraftToTeam_shouldThrowIfTheTopicDraftIsSubmitted() {
    when(topicDraftService.findById(anyLong())).thenReturn(submittedTopicDraft);

    assertThrows(NotApprovedException.class, () -> convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0));
  }

  @Test
  public void convertTopicDraftToTeam_shouldThrowIfTheTopicDraftIsRejected() {
    when(topicDraftService.findById(anyLong())).thenReturn(rejectedTopicDraft);

    assertThrows(NotApprovedException.class, () -> convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0));
  }

  @Test
  public void convertTopicDraftToTeam_shouldCallDeleteOperation() {
    when(topicDraftService.findById(anyLong())).thenReturn(approvedTopicDraft);
    when(companyService.getAllCompanies()).thenReturn(new ArrayList<>());
    when(departmentService.createChildUnit(anyLong(), any())).thenReturn(department);
    when(department.getOkrTopicDescription()).thenReturn(topicDescription);

    convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0);

    verify(topicDraftService).deleteTopicDraftById(anyLong());
  }

  @Test
  public void createDepartmentInDatabase_shouldSelectCompanyServiceWhenGivenACompanyId() {
    when(topicDraftService.findById(anyLong())).thenReturn(approvedTopicDraft);
    when(department.getOkrTopicDescription()).thenReturn(topicDescription);
    when(companyService.createDepartment(anyLong(), any())).thenReturn(department);

    Collection<OkrCompany> companyList = new ArrayList<>();
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setId(0L);
    companyList.add(okrCompany);
    when(companyService.getAllCompanies()).thenReturn(companyList);

    convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0);

    verify(companyService).createDepartment(anyLong(), any());
  }

  @Test
  public void createCreateChildUnitInDatabase_shouldSelectDepartmentServiceWhenNotGivenACompanyId() {
    when(topicDraftService.findById(anyLong())).thenReturn(approvedTopicDraft);
    when(companyService.getAllCompanies()).thenReturn(new ArrayList<>());
    when(departmentService.createChildUnit(anyLong(), any())).thenReturn(department);
    when(department.getOkrTopicDescription()).thenReturn(topicDescription);

    convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0);

    verify(departmentService).createChildUnit(anyLong(), any());
  }

  @Test
  public void createDepartmentInDatabase_shouldCallAllRequiredMethods() {
    when(topicDraftService.findById(anyLong())).thenReturn(approvedTopicDraft);
    when(companyService.getAllCompanies()).thenReturn(new ArrayList<>());
    when(departmentService.createChildUnit(anyLong(), any())).thenReturn(department);
    when(department.getOkrTopicDescription()).thenReturn(topicDescription);

    convertTopicDraftToTeamService.convertTopicDraftToTeam(0, 0);

    verify(departmentService).createChildUnit(anyLong(), any());
    verify(departmentService).createChildUnit(anyLong(), any());
    verify(companyService).getAllCompanies();
    verify(departmentService).updateUnit(any());
    verify(topicDraftService).deleteTopicDraftById(anyLong());
    verify(department).getOkrTopicDescription();
  }
}
