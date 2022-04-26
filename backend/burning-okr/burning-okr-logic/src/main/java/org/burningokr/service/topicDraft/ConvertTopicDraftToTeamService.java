package org.burningokr.service.topicDraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
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
    OkrDepartment okrDepartment = createOkrDepartment(parentOkrUnitId, topicDraft.getName(), user);

    okrDepartment = copyValuesFromOkrTopicDraftToOkrDepartment(topicDraft, okrDepartment);

    deleteTopicDraft(topicDraft);

    return writeOkrDepartmentToDatabase(okrDepartment, user);
  }

  public OkrDepartment copyValuesFromOkrTopicDraftToOkrDepartment(OkrTopicDraft topicDraft, OkrDepartment okrDepartment) {
    okrDepartment.setName(topicDraft.getName());
    okrDepartment.setOkrMemberIds(copyUserList(topicDraft.getStartTeam()));
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

    return okrDepartment;
  }

  private OkrDepartment createOkrDepartment(long parentOkrUnitId, String name, User user) {
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setName(name);
    okrDepartment.setLabel("Team");
    return createOkrDepartmentInDatabase(okrDepartment, parentOkrUnitId, user);
  }

  private OkrDepartment createOkrDepartmentInDatabase(OkrDepartment okrDepartment, long parentOkrUnitId, User user) {
    OkrUnitService<OkrDepartment> okrDepartmentService = okrDepartmentOkrUnitServiceFactory.getRoleServiceForDepartment(parentOkrUnitId);
    return (OkrDepartment) okrDepartmentService.createChildUnit(parentOkrUnitId, okrDepartment, user);
  }

  private OkrDepartment writeOkrDepartmentToDatabase(OkrDepartment okrDepartment, User user) {
    OkrUnitService<OkrDepartment> okrDepartmentService = okrDepartmentOkrUnitServiceFactory.getRoleServiceForDepartment(okrDepartment.getParentOkrUnit().getId());
    return okrDepartmentService.updateUnit(okrDepartment, user);
  }

  private Collection<UUID> copyUserList(Collection<UUID> userList) {
    return new ArrayList<>(userList);
  }

  private void deleteTopicDraft(OkrTopicDraft topicDraft) {

  }
}
