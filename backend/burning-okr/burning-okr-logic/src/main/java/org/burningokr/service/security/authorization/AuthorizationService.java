package org.burningokr.service.security.authorization;

import lombok.RequiredArgsConstructor;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
  private final AuthenticationUserContextService authenticationUserContextService;

  public boolean isAdmin() {
    return authenticationUserContextService.getAuthenticatedUser().isAdmin();
  }
}
