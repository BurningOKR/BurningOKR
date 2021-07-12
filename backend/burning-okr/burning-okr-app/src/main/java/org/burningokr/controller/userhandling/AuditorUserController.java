package org.burningokr.controller.userhandling;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.service.userhandling.AuditorUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiController
@RequiredArgsConstructor
public class AuditorUserController {

  private final AuditorUserService auditorUserService;
  /**
   * API Endpoint to check if the current user is an auditor.
   *
   * @return a {@link ResponseEntity} ok with a boolean
   */
  @GetMapping("/auditors/self")
  public ResponseEntity<Boolean> isCurrentUserAuditor() {
    return ResponseEntity.ok(auditorUserService.isCurrentUserAuditor());
  }
}
