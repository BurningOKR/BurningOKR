package org.burningokr.service.okr;

import com.google.common.collect.Lists;
import java.time.LocalDate;
import java.util.Collection;
import javax.transaction.Transactional;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.service.activity.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OkrTopicDraftService {

  private final Logger logger = LoggerFactory.getLogger(OkrTopicDraftService.class);

  private OkrTopicDraftRepository okrTopicDraftRepository;
  private ActivityService activityService;

  public OkrTopicDraftService(
      OkrTopicDraftRepository okrTopicDraftRepository, ActivityService activityService) {
    this.okrTopicDraftRepository = okrTopicDraftRepository;
    this.activityService = activityService;
  }

  public Collection<OkrTopicDraft> getAllTopicDrafts() {
    return Lists.newArrayList(okrTopicDraftRepository.findAll());
  }

  public void deleteTopicDraftById(Long topicDraftId, User user) {
    OkrTopicDraft referencedTopicDraft = okrTopicDraftRepository.findByIdOrThrow(topicDraftId);
    okrTopicDraftRepository.deleteById(topicDraftId);
    activityService.createActivity(user, referencedTopicDraft, Action.DELETED);
  }

  public OkrTopicDraft findById(long topicDraftId) {
    return okrTopicDraftRepository.findByIdOrThrow(topicDraftId);
  }

  /**
   * Updates a Topic Draft.
   *
   * @param updatedOkrTopicDraft a {@link OkrTopicDraft} object
   * @return a {@link OkrTopicDraft} object
   */
  @Transactional
  public OkrTopicDraft updateOkrTopicDraft(long topicDraftId, OkrTopicDraft updatedOkrTopicDraft) {
    OkrTopicDraft referencedOkrTopicDraft = findById(topicDraftId);

    referencedOkrTopicDraft.setAcceptanceCriteria(updatedOkrTopicDraft.getAcceptanceCriteria());
    referencedOkrTopicDraft.setBeginning(
        LocalDate.parse(updatedOkrTopicDraft.getBeginning().toString()));
    referencedOkrTopicDraft.setContributesTo(updatedOkrTopicDraft.getContributesTo());
    referencedOkrTopicDraft.setCurrentStatus(updatedOkrTopicDraft.getCurrentStatus());
    referencedOkrTopicDraft.setDelimitation(updatedOkrTopicDraft.getDelimitation());
    referencedOkrTopicDraft.setDependencies(updatedOkrTopicDraft.getDependencies());
    referencedOkrTopicDraft.setHandoverPlan(updatedOkrTopicDraft.getHandoverPlan());
    referencedOkrTopicDraft.setId(updatedOkrTopicDraft.getId());
    referencedOkrTopicDraft.setInitiatorId(updatedOkrTopicDraft.getInitiatorId());
    referencedOkrTopicDraft.setName(updatedOkrTopicDraft.getName());
    referencedOkrTopicDraft.setParentUnit(updatedOkrTopicDraft.getParentUnit());
    referencedOkrTopicDraft.setResources(updatedOkrTopicDraft.getResources());
    referencedOkrTopicDraft.setStakeholders(updatedOkrTopicDraft.getStakeholders());
    referencedOkrTopicDraft.setStartTeam(updatedOkrTopicDraft.getStartTeam());

    referencedOkrTopicDraft = okrTopicDraftRepository.save(referencedOkrTopicDraft);

    return referencedOkrTopicDraft;
  }

  /**
   * Updates the status of a Topic Draft.
   *
   * @param updatedOkrTopicDraft a {@link OkrTopicDraft} object
   * @return a {@link OkrTopicDraft} object
   */
  @Transactional
  public OkrTopicDraft updateOkrTopicDraftStatus(
      long topicDraftId, OkrTopicDraft updatedOkrTopicDraft) {
    OkrTopicDraft referencedOkrTopicDraft = findById(topicDraftId);
    referencedOkrTopicDraft.setCurrentStatus(updatedOkrTopicDraft.getCurrentStatus());
    referencedOkrTopicDraft = okrTopicDraftRepository.save(referencedOkrTopicDraft);

    return referencedOkrTopicDraft;
  }
}
