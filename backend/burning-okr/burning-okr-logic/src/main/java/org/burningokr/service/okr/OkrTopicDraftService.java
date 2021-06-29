package org.burningokr.service.okr;

import com.google.common.collect.Lists;
import java.util.Collection;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OkrTopicDraftService {

  private final Logger logger = LoggerFactory.getLogger(OkrTopicDraftService.class);

  private OkrTopicDraftRepository okrTopicDraftRepository;

  public OkrTopicDraftService(OkrTopicDraftRepository okrTopicDraftRepository) {
    this.okrTopicDraftRepository = okrTopicDraftRepository;
  }

  public Collection<OkrTopicDraft> getAllTopicDrafts() {
    return Lists.newArrayList(okrTopicDraftRepository.findAll());
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
  public OkrTopicDraft updateOkrTopicDraft(OkrTopicDraft updatedOkrTopicDraft) {
    logger.info("Updated TopicDraftZeugs");
    OkrTopicDraft referencedOkrTopicDraft = findById(updatedOkrTopicDraft.getId());

    referencedOkrTopicDraft.setName(updatedOkrTopicDraft.getName());
    referencedOkrTopicDraft.setAcceptanceCriteria(updatedOkrTopicDraft.getAcceptanceCriteria());

    referencedOkrTopicDraft = okrTopicDraftRepository.save(referencedOkrTopicDraft);
    System.out.println("Updated OKRTopicDraft");

    logger.info("Updated TopicDraftZeugs");

    return referencedOkrTopicDraft;
  }
}
