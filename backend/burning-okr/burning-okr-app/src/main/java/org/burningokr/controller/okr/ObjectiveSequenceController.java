package org.burningokr.controller.okr;

import org.burningokr.annotation.RestApiController;
import org.burningokr.model.users.User;
import org.burningokr.service.okr.ObjectiveService;
import org.burningokr.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@RestApiController
public class ObjectiveSequenceController {

  private ObjectiveService objectiveService;
  private AuthorizationService authorizationService;

  /**
   * Initialize ObjectiveSequenceController.
   *
   * @param objectiveService     an {@link ObjectiveService} object
   * @param authorizationService an {@link AuthorizationService} object
   */
  @Autowired
  public ObjectiveSequenceController(
    ObjectiveService objectiveService, AuthorizationService authorizationService
  ) {
    this.objectiveService = objectiveService;
    this.authorizationService = authorizationService;
  }

  /**
   * API Endpoint to update a Sequence.
   *
   * @param okrUnitId    a long value
   * @param sequenceList a {@ling Collection} of long values
   * @param user         an {@link User} object
   * @return a {@link ResponseEntity} ok
   * @throws Exception if sequence list is invalid
   */
  @PutMapping("/units/{okrUnitId}/objectivesequence")
  @PreAuthorize("@authorizationService.hasMemberPrivilegeForDepartment(#okrUnitId)")
  public ResponseEntity updateSequenceOf(
    @PathVariable long okrUnitId,
    @RequestBody Collection<Long> sequenceList, User user
  )
    throws Exception {
    objectiveService.updateSequence(okrUnitId, sequenceList, user);

    return ResponseEntity.ok().build();
  }
}
