package org.burningokr.service.okr;

import com.google.common.collect.Lists;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.transaction.Transactional;
import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.NoteTopicDraftRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.service.activity.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OkrTopicDraftService {

  private final Logger logger = LoggerFactory.getLogger(OkrTopicDraftService.class);

  private OkrTopicDraftRepository okrTopicDraftRepository;
  private NoteTopicDraftRepository noteTopicDraftRepository;
  private ActivityService activityService;

  public OkrTopicDraftService(
      OkrTopicDraftRepository okrTopicDraftRepository,
      NoteTopicDraftRepository noteTopicDraftRepository,
      ActivityService activityService) {
    this.okrTopicDraftRepository = okrTopicDraftRepository;
    this.noteTopicDraftRepository = noteTopicDraftRepository;
    this.activityService = activityService;
  }

  public OkrTopicDraft findById(long topicDraftId) {
    return okrTopicDraftRepository.findByIdOrThrow(topicDraftId);
  }

  public Collection<OkrTopicDraft> getAllTopicDrafts() {
    return Lists.newArrayList(okrTopicDraftRepository.findAll());
  }

  public Collection<NoteTopicDraft> getAllNotesForTopicDraft(long topicDraftId) {
    Collection<NoteTopicDraft> noteTopicDrafts =
        noteTopicDraftRepository.findNoteTopicDraftsByParentTopicDraft_Id(topicDraftId);
    return noteTopicDrafts;
  }

  public void deleteTopicDraftById(Long topicDraftId, User user) {
    OkrTopicDraft referencedTopicDraft = okrTopicDraftRepository.findByIdOrThrow(topicDraftId);
    okrTopicDraftRepository.deleteById(topicDraftId);
    activityService.createActivity(user, referencedTopicDraft, Action.DELETED);
  }

  public OkrTopicDraft createTopicDraft(OkrTopicDraft topicDraft, User user) {

    topicDraft.setParentUnit(null);

    topicDraft = okrTopicDraftRepository.save(topicDraft);
    logger.info("Created Topic Draft: " + topicDraft.getName());

    activityService.createActivity(user, topicDraft, Action.CREATED);

    return topicDraft;
  }

  /**
   * Creates a Note for a Topic Draft.
   *
   * @param topicDraftId a long value
   * @param noteTopicDraft a {@link NoteTopicDraft} object
   * @param user an {@link User} object
   * @return a {@link Note} object
   */
  @Transactional
  public NoteTopicDraft createNote(long topicDraftId, NoteTopicDraft noteTopicDraft, User user) {
    noteTopicDraft.setId(null);
    noteTopicDraft.setUserId(user.getId());
    noteTopicDraft.setDate(LocalDateTime.now());

    noteTopicDraft = noteTopicDraftRepository.save(noteTopicDraft);
    logger.info(
        "Added Note with id "
            + noteTopicDraft.getId()
            + " from User "
            + user.getGivenName()
            + " "
            + user.getSurname()
            + " to KeyResult "
            + topicDraftId);
    activityService.createActivity(user, noteTopicDraft, Action.CREATED);

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
      long topicDraftId, OkrTopicDraft updatedOkrTopicDraft) {
    OkrTopicDraft referencedOkrTopicDraft = findById(topicDraftId);
    referencedOkrTopicDraft.setCurrentStatus(updatedOkrTopicDraft.getCurrentStatus());
    referencedOkrTopicDraft = okrTopicDraftRepository.save(referencedOkrTopicDraft);

    return referencedOkrTopicDraft;
  }
}
