package org.burningokr.service.topicDraft;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.service.exceptions.NotApprovedException;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrUnitService;
import org.burningokr.service.okrUnit.OkrUnitServiceFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ConvertTopicDraftToTeamService {
  private final OkrTopicDraftService okrTopicDraftService;
  private final CompanyService companyService;
  private final OkrUnitServiceFactory<OkrDepartment> okrDepartmentOkrUnitServiceFactory;

  @Transactional
  public OkrDepartment convertTopicDraftToTeam(long topicDraftId, long parentOkrUnitId, User user) {
    OkrTopicDraft topicDraft = okrTopicDraftService.findById(topicDraftId);

    if (!OkrTopicDraftStatusEnum.approved.equals(topicDraft.getCurrentStatus())) {
      throw new NotApprovedException(
        "TopicDraft has to be approved before it can be converted to a team");
    }

    OkrDepartment okrDepartment = createOkrDepartment(parentOkrUnitId, topicDraft.getName(), user);
    copyValuesFromOkrTopicDraftToOkrDepartment(topicDraft, okrDepartment);
    okrDepartment = writeOkrDepartmentToDatabase(okrDepartment, user);
    deleteTopicDraft(topicDraft, user);

    return okrDepartment;
  }

  private OkrDepartment createOkrDepartment(long parentOkrUnitId, String name, User user) {
    String standardLabel = "Team";

    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setName(name);
    okrDepartment.setLabel(standardLabel);

    return createOkrDepartmentInDatabase(okrDepartment, parentOkrUnitId, user);
  }

  private OkrDepartment createOkrDepartmentInDatabase(
    OkrDepartment okrDepartment, long parentOkrUnitId, User user
  ) {
    Collection<OkrCompany> okrCompanyCollection = companyService.getAllCompanies();
    if (okrCompanyCollection.stream().anyMatch(company -> company.getId() == parentOkrUnitId)) {
      return createOkrDepartmentUnderneathCompany(okrDepartment, parentOkrUnitId, user);
    } else {
      return createOkrDepartmentUnderneathBranch(okrDepartment, parentOkrUnitId, user);
    }
  }

  private OkrDepartment createOkrDepartmentUnderneathCompany(
    OkrDepartment okrDepartment, long parentOkrUnitId, User user
  ) {
    return companyService.createDepartment(parentOkrUnitId, okrDepartment, user);
  }

  private OkrDepartment createOkrDepartmentUnderneathBranch(
    OkrDepartment okrDepartment, long parentOkrUnitId, User user
  ) {
    OkrUnitService<OkrDepartment> okrDepartmentService =
      okrDepartmentOkrUnitServiceFactory.getRoleServiceForDepartment(parentOkrUnitId);
    return (OkrDepartment)
      okrDepartmentService.createChildUnit(parentOkrUnitId, okrDepartment, user);
  }

  private OkrDepartment writeOkrDepartmentToDatabase(OkrDepartment okrDepartment, User user) {
    OkrUnitService<OkrDepartment> okrDepartmentService =
      okrDepartmentOkrUnitServiceFactory.getRoleServiceForDepartment(okrDepartment.getId());
    return okrDepartmentService.updateUnit(okrDepartment, user);
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

  private void deleteTopicDraft(OkrTopicDraft topicDraft, User user) {
    okrTopicDraftService.deleteTopicDraftById(topicDraft.getId(), user);
  }
}
