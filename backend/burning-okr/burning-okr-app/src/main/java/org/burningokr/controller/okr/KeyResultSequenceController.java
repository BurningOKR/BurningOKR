package org.burningokr.controller.okr;

import org.burningokr.annotation.RestApiController;
import org.burningokr.model.users.User;
import org.burningokr.service.okr.KeyResultService;
import org.burningokr.service.security.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@RestApiController
public class KeyResultSequenceController {

  private KeyResultService keyResultService;
  private AuthorizationService authorizationService;

  public KeyResultSequenceController(
    KeyResultService keyResultService, AuthorizationService authorizationService
  ) {
    this.keyResultService = keyResultService;
    this.authorizationService = authorizationService;
  }

  @PutMapping("/objective/{objectiveId}/keyresultsequence")
  @PreAuthorize("@authorizationService.hasMemberPrivilegeForObjective(#objectiveId)")
  public ResponseEntity updateSequenceOf(
    @PathVariable long objectiveId,
    @RequestBody Collection<Long> sequenceList, User user
  )
    throws Exception {
    keyResultService.updateSequence(objectiveId, sequenceList, user);
    return ResponseEntity.ok().build();
  }
}
