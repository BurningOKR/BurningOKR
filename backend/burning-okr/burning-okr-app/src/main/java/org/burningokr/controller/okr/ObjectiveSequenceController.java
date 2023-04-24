package org.burningokr.controller.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.service.okr.ObjectiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@RestApiController
@RequiredArgsConstructor
public class ObjectiveSequenceController {

  private final ObjectiveService objectiveService;

  /**
   * API Endpoint to update a Sequence.
   *
   * @param okrUnitId    a long value
   * @param sequenceList a {@ling Collection} of long values
   * @return a {@link ResponseEntity} ok
   * @throws Exception if sequence list is invalid
   */
  @PutMapping("/units/{okrUnitId}/objectivesequence")
  @PreAuthorize("@childUnitAuthorizationService.hasMemberPrivilegesForChildUnit(#okrUnitId)")
  public ResponseEntity updateSequenceOf(
    @PathVariable long okrUnitId,
    @RequestBody Collection<Long> sequenceList
  )
    throws Exception {
    objectiveService.updateSequence(okrUnitId, sequenceList);

    return ResponseEntity.ok().build();
  }
}
