package org.burningokr.service.topicDraft;

import com.google.common.collect.Lists;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.NoteTopicDraftRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.security.AuthorizationUserContextService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OkrTopicDraftService {
  private final OkrTopicDraftRepository okrTopicDraftRepository;
  private final NoteTopicDraftRepository noteTopicDraftRepository;
  private final ActivityService activityService;
  private final AuthorizationUserContextService authorizationUserContextService;

  public OkrTopicDraft findById(long topicDraftId) {
    return okrTopicDraftRepository.findByIdOrThrow(topicDraftId);
  }

  public Collection<OkrTopicDraft> getAllTopicDrafts() {
    List<OkrTopicDraft> topicDrafts = Lists.newArrayList(okrTopicDraftRepository.findAll());
    List<OkrTopicDraft> filteredDrafts = new ArrayList<>();
    for (OkrTopicDraft draft : topicDrafts) {
      if (shouldUserSeeDraft(draft)) {
        filteredDrafts.add(draft);
      }
    }
    return filteredDrafts;
  }

  private boolean shouldUserSeeDraft(OkrTopicDraft draft) {
    User authenticatedUser = authorizationUserContextService.getAuthenticatedUser();

    return !draft.getCurrentStatus().equals(OkrTopicDraftStatusEnum.draft) ||
            draft.getInitiatorId().equals(authenticatedUser.getId()) ||
            authenticatedUser.isAdmin();
  }

  public Collection<NoteTopicDraft> getAllNotesForTopicDraft(long topicDraftId) {
    return noteTopicDraftRepository.findNoteTopicDraftsByParentTopicDraft_Id(topicDraftId);
  }

  public void deleteTopicDraftById(Long topicDraftId) {
    OkrTopicDraft referencedTopicDraft = okrTopicDraftRepository.findByIdOrThrow(topicDraftId);
    okrTopicDraftRepository.deleteById(topicDraftId);
    activityService.createActivity(referencedTopicDraft, Action.DELETED);
  }

  public OkrTopicDraft createTopicDraft(OkrTopicDraft topicDraft) {

    topicDraft.setParentUnit(null);
    topicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.draft);

    topicDraft = okrTopicDraftRepository.save(topicDraft);
    log.info("Created Topic Draft: " + topicDraft.getName());

    activityService.createActivity(topicDraft, Action.CREATED);

    return topicDraft;
  }

  @Transactional
  public NoteTopicDraft createNote(NoteTopicDraft noteTopicDraft) {
    noteTopicDraft.setId(null);
    noteTopicDraft.setUserId(authorizationUserContextService.getAuthenticatedUser().getId());
    noteTopicDraft.setDate(LocalDateTime.now());

    noteTopicDraft = noteTopicDraftRepository.save(noteTopicDraft);
    activityService.createActivity(noteTopicDraft, Action.CREATED);

    return noteTopicDraft;
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

    referencedOkrTopicDraft.setDescription(updatedOkrTopicDraft.getDescription());
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
    long topicDraftId, OkrTopicDraft updatedOkrTopicDraft
  ) {
    OkrTopicDraft referencedOkrTopicDraft = findById(topicDraftId);
    referencedOkrTopicDraft.setCurrentStatus(updatedOkrTopicDraft.getCurrentStatus());
    referencedOkrTopicDraft = okrTopicDraftRepository.save(referencedOkrTopicDraft);

    return referencedOkrTopicDraft;
  }
}
