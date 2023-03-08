package org.burningokr.service.topicDraft;

import com.google.common.collect.Lists;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.okr.NoteTopicDraftRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.service.activity.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OkrTopicDraftService {

  private final Logger logger = LoggerFactory.getLogger(OkrTopicDraftService.class);

  private OkrTopicDraftRepository okrTopicDraftRepository;
  private NoteTopicDraftRepository noteTopicDraftRepository;
  private ActivityService activityService;
  private AdminUserService adminUserService;

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
    if (!draft.getCurrentStatus().equals(OkrTopicDraftStatusEnum.draft)) {
      return true;
    }
    // TODO fix auth (jklein 23.02.2023)
//    return (draft.getInitiatorId().equals(userService.getCurrentUser().getId())
//      || adminUserService.isCurrentUserAdmin());
    return false;
  }

  public Collection<NoteTopicDraft> getAllNotesForTopicDraft(long topicDraftId) {
    Collection<NoteTopicDraft> noteTopicDrafts =
      noteTopicDraftRepository.findNoteTopicDraftsByParentTopicDraft_Id(topicDraftId);
    return noteTopicDrafts;
  }

  public void deleteTopicDraftById(Long topicDraftId, IUser IUser) {
    OkrTopicDraft referencedTopicDraft = okrTopicDraftRepository.findByIdOrThrow(topicDraftId);
    okrTopicDraftRepository.deleteById(topicDraftId);
    activityService.createActivity(IUser, referencedTopicDraft, Action.DELETED);
  }

  public OkrTopicDraft createTopicDraft(OkrTopicDraft topicDraft, IUser IUser) {

    topicDraft.setParentUnit(null);
    topicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.draft);

    topicDraft = okrTopicDraftRepository.save(topicDraft);
    logger.info("Created Topic Draft: " + topicDraft.getName());

    activityService.createActivity(IUser, topicDraft, Action.CREATED);

    return topicDraft;
  }

  /**
   * Creates a Note for a Topic Draft.
   *
   * @param topicDraftId   a long value
   * @param noteTopicDraft a {@link NoteTopicDraft} object
   * @param IUser          an {@link IUser} object
   * @return a {@link Note} object
   */
  @Transactional
  public NoteTopicDraft createNote(long topicDraftId, NoteTopicDraft noteTopicDraft, IUser IUser) {
    noteTopicDraft.setId(null);
    noteTopicDraft.setUserId(IUser.getId());
    noteTopicDraft.setDate(LocalDateTime.now());

    noteTopicDraft = noteTopicDraftRepository.save(noteTopicDraft);
    logger.info(
      "Added Note with id "
        + noteTopicDraft.getId()
        + " from User "
        + IUser.getGivenName()
        + " "
        + IUser.getSurname()
        + " to KeyResult "
        + topicDraftId);
    activityService.createActivity(IUser, noteTopicDraft, Action.CREATED);

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
