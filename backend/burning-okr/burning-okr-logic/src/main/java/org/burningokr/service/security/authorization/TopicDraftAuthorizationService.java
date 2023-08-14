package org.burningokr.service.security.authorization;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.burningokr.service.topicDraft.OkrTopicDraftService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicDraftAuthorizationService {

    private final OkrTopicDraftService topicDraftService;
    private AuthenticationUserContextService authenticationUserContextService;

  public boolean isInitiator(Long topicDraftId) {
    OkrTopicDraft topicDraft = topicDraftService.findById(topicDraftId);
      return authenticationUserContextService.getAuthenticatedUser().getId() == topicDraft.getInitiatorId();
  }
}
