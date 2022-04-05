package org.burningokr.service.topicDraft;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.users.User;
import org.burningokr.service.okrUnit.OkrUnitService;
import org.burningokr.service.okrUnit.OkrUnitServiceFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ConvertTopicDraftToTeamService {

  private final OkrTopicDraftService okrTopicDraftService;
  private final OkrUnitServiceFactory<OkrChildUnit> okrUnitServiceFactory;
  private final OkrUnitServiceFactory<OkrDepartment> okrDepartmentOkrUnitServiceFactory;

  @Transactional
  public OkrDepartment convertTopicDraftToTeam(long topicDraftId, long parentOkrUnitId, User user) {
    OkrTopicDraft topicDraft = this.okrTopicDraftService.findById(topicDraftId);
    OkrDepartment okrDepartment = createOkrDepartment(parentOkrUnitId, user);

    return okrDepartment;
  }

  public OkrTopicDescription convertTopicDraftToOkrTopicDescription(OkrTopicDraft topicDraft) {
    // TODO Copy-Constructor?
    OkrTopicDescription topicDescription = new OkrTopicDescription();
    topicDescription.setDescription(topicDraft.getDescription());
    topicDescription.setBeginning(topicDraft.getBeginning());
    topicDescription.setDelimitation(topicDraft.getDelimitation());
    topicDescription.setDependencies(topicDraft.getDependencies());
    topicDescription.setContributesTo(topicDraft.getContributesTo());
    topicDescription.setHandoverPlan(topicDraft.getHandoverPlan());
    topicDescription.setResources(topicDraft.getResources());

    return topicDescription;
  }

  private OkrDepartment createOkrDepartment(long parentOkrUnitId, User user) {
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setName("Test");
    return writeOkrDepartmentToDatabase(okrDepartment, parentOkrUnitId, user);
  }

  private OkrDepartment writeOkrDepartmentToDatabase(OkrDepartment okrDepartment, long parentOkrUnitId, User user) {
    OkrUnitService<OkrDepartment> okrDepartmentService = okrDepartmentOkrUnitServiceFactory.getRoleServiceForDepartment(parentOkrUnitId);
    return (OkrDepartment) okrDepartmentService.createChildUnit(parentOkrUnitId, okrDepartment, user);
  }
}
