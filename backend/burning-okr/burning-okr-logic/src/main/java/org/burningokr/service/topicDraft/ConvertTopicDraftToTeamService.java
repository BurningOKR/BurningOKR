package org.burningokr.service.topicDraft;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.exceptions.NotApprovedException;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ConvertTopicDraftToTeamService {
  private final OkrTopicDraftService okrTopicDraftService;
  private final CompanyService companyService;
  private final OkrChildUnitService<OkrDepartment> okrDepartmentService;

  @Transactional
  public OkrDepartment convertTopicDraftToTeam(long topicDraftId, long parentOkrUnitId) {
    OkrTopicDraft topicDraft = okrTopicDraftService.findById(topicDraftId);

    if (!OkrTopicDraftStatusEnum.approved.equals(topicDraft.getCurrentStatus())) {
      throw new NotApprovedException(
        "TopicDraft has to be approved before it can be converted to a team");
    }

    OkrDepartment okrDepartment = createOkrDepartment(parentOkrUnitId, topicDraft.getName());
    copyValuesFromOkrTopicDraftToOkrDepartment(topicDraft, okrDepartment);
    okrDepartment = writeOkrDepartmentToDatabase(okrDepartment);
    deleteTopicDraft(topicDraft);

    return okrDepartment;
  }

  private OkrDepartment createOkrDepartment(long parentOkrUnitId, String name) {
    String standardLabel = "Team";

    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setName(name);
    okrDepartment.setLabel(standardLabel);

    return createOkrDepartmentInDatabase(okrDepartment, parentOkrUnitId);
  }

  private OkrDepartment createOkrDepartmentInDatabase(
    OkrDepartment okrDepartment, long parentOkrUnitId
  ) {
    Collection<OkrCompany> okrCompanyCollection = companyService.getAllCompanies();
    if (okrCompanyCollection.stream().anyMatch(company -> company.getId() == parentOkrUnitId)) {
      return createOkrDepartmentUnderneathCompany(okrDepartment, parentOkrUnitId);
    } else {
      return createOkrDepartmentUnderneathBranch(okrDepartment, parentOkrUnitId);
    }
  }

  private OkrDepartment createOkrDepartmentUnderneathCompany(
    OkrDepartment okrDepartment, long parentOkrUnitId
  ) {
    return companyService.createDepartment(parentOkrUnitId, okrDepartment);
  }

  private OkrDepartment createOkrDepartmentUnderneathBranch(
    OkrDepartment okrDepartment, long parentOkrUnitId
  ) {
    return (OkrDepartment) okrDepartmentService.createChildUnit(parentOkrUnitId, okrDepartment);
  }

  private OkrDepartment writeOkrDepartmentToDatabase(OkrDepartment okrDepartment) {
    return okrDepartmentService.updateUnit(okrDepartment);
  }

  private void copyValuesFromOkrTopicDraftToOkrDepartment(
    OkrTopicDraft topicDraft, OkrDepartment okrDepartment
  ) {
    okrDepartment.setName(topicDraft.getName());
    okrDepartment.setOkrMemberIds(new ArrayList<>(topicDraft.getStartTeam()));
    okrDepartment.setOkrMasterId(topicDraft.getInitiatorId());
    okrDepartment.setActive(true);

    OkrTopicDescription topicDescription = okrDepartment.getOkrTopicDescription();
    topicDescription.setBeginning(topicDraft.getBeginning());
    topicDescription.setDescription(topicDraft.getDescription());
    topicDescription.setContributesTo(topicDraft.getContributesTo());
    topicDescription.setDelimitation(topicDraft.getDelimitation());
    topicDescription.setDependencies(topicDraft.getDependencies());
    topicDescription.setResources(topicDraft.getResources());
    topicDescription.setHandoverPlan(topicDraft.getHandoverPlan());
  }

  private void deleteTopicDraft(OkrTopicDraft topicDraft) {
    okrTopicDraftService.deleteTopicDraftById(topicDraft.getId());
  }
}
