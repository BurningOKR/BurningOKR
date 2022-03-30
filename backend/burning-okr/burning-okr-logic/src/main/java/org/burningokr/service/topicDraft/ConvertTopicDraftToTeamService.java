package org.burningokr.service.topicDraft;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.service.okrUnit.OkrUnitService;
import org.burningokr.service.okrUnit.OkrUnitServiceFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ConvertTopicDraftToTeamService {

  private final OkrTopicDraftService okrTopicDraftService;
  private final OkrUnitServiceFactory<OkrChildUnit> okrUnitServiceFactory;

  @Transactional
  public OkrDepartment convertTopicDraftToTeam (int topicDraftId, int parentOkrUnitId) {
    OkrTopicDraft topicDraft = this.okrTopicDraftService.findById(topicDraftId);
    OkrUnitService<OkrChildUnit> okrUnitService = this.okrUnitServiceFactory.getUserOkrUnitService();
    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setId(null);
    copyDetailsFromTopicDraftToDepartment(topicDraft, okrDepartment);
    return okrDepartment;
  }

  public void copyDetailsFromTopicDraftToDepartment (OkrTopicDraft topicDraft, OkrDepartment okrDepartment) {
    okrDepartment.setActive(true);
    okrDepartment.setLabel("Abteilung");
    okrDepartment.setName(topicDraft.getName());
    okrDepartment.setOkrTopicDescription(convertTopicDraftToOkrTopicDescription(topicDraft));
  }

  public OkrTopicDescription convertTopicDraftToOkrTopicDescription (OkrTopicDraft topicDraft) {
    // TODO Copy-Constructor?
    OkrTopicDescription topicDescription = new OkrTopicDescription();
    topicDescription.setDescription(topicDraft.getDescription());
    topicDescription.setBeginning(topicDraft.getBeginning());
    topicDescription.setDelimitation(topicDraft.getDelimitation());
    topicDescription.setDependencies(topicDraft.getDependencies());
    topicDescription.setContributesTo(topicDraft.getContributesTo());
    topicDescription.setName(topicDraft.getName());
    topicDescription.setStartTeam(topicDraft.getStartTeam());
    topicDescription.setHandoverPlan(topicDraft.getHandoverPlan());
    topicDescription.setInitiatorId(topicDraft.getInitiatorId());
    topicDescription.setId(topicDraft.getId());
    topicDescription.setResources(topicDraft.getResources());
    topicDescription.setStakeholders(topicDraft.getStakeholders());

    return topicDescription;
  }
}
