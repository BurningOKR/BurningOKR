package org.burningokr.service.userhandling;

import lombok.RequiredArgsConstructor;
import org.burningokr.repositories.users.AuditorUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditorUserService {

  private final AuditorUserRepository auditorUserRepository;


  /**
   * Checks if the Current User is an Auditor.
   *
   * @return a Boolean value
   */
  public Boolean isCurrentUserAuditor() {
    // TODO fix auth (jklein 23.02.2023)
    return true;
//    UUID currentUserId = userService.getCurrentUser().getId();
//    Optional<AuditorUser> currentUserAuditorOptional =
//      auditorUserRepository.findById(currentUserId);
//
//    return currentUserAuditorOptional.isPresent();
  }
}
