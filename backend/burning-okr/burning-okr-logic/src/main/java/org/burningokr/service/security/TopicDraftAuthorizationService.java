package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.service.topicDraft.OkrTopicDraftService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicDraftAuthorizationService {

  private final OkrTopicDraftService topicDraftService;
  private AuthorizationUserContextService authorizationUserContextService;

  public boolean isInitiator(Long topicDraftId) {
    OkrTopicDraft topicDraft = topicDraftService.findById(topicDraftId);
    return authorizationUserContextService.getAuthenticatedUser().getId() == topicDraft.getInitiatorId();
  }
}
