package org.burningokr.service.userhandling;

import org.burningokr.model.users.AuditorUser;
import org.burningokr.repositories.users.AuditorUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuditorUserService {

  private AuditorUserRepository auditorUserRepository;
  private UserService userService;

  @Autowired
  public AuditorUserService(AuditorUserRepository auditorUserRepository, UserService userService) {
    this.auditorUserRepository = auditorUserRepository;
    this.userService = userService;
  }

  /**
   * Checks if the Current User is an Auditor.
   *
   * @return a Boolean value
   */
  public Boolean isCurrentUserAuditor() {
    UUID currentUserId = userService.getCurrentUser().getId();
    Optional<AuditorUser> currentUserAuditorOptional =
      auditorUserRepository.findById(currentUserId);

    return currentUserAuditorOptional.isPresent();
  }
}
