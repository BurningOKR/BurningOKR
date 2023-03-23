package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// TODO fix auth
@Service
@RequiredArgsConstructor
public class AuthorizationService {

  private final AuthorizationUserContextService contextService;

  public boolean isAdmin() {
    return contextService.getAuthenticatedUser().isAdmin();
  }


  public boolean hasManagerPrivilegeForObjective(Long objectiveId) {
    return true;
  }

  public boolean hasManagerPrivilegeForKeyResult(Long keyResultId) {
    return true;
  }


  public boolean hasMemberPrivilegeForObjective(Long objectiveId) {
    return true;
  }

  public boolean hasMemberPrivilegeForKeyResult(Long keyResultId) {
    return true;
  }

  public boolean isNoteOwner(Long noteId) {
    return true;
  }

  public boolean isTopicDraftInitiator(Long topicDraftId) {
    return true;
  }
}
