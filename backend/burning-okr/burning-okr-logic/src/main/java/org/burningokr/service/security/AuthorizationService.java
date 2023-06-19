package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
  private final AuthorizationUserContextService contextService;

  public boolean isAdmin() {
    return contextService.getAuthenticatedUser().isAdmin();
  }
}
