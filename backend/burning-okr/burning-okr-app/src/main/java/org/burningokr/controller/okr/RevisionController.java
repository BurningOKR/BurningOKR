package org.burningokr.controller.okr;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.RevisionDto;
import org.burningokr.mapper.okr.RevisionMapper;
import org.burningokr.model.okr.FieldRevision;
import org.burningokr.model.okr.Task;
import org.burningokr.service.okr.RevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

@RestApiController
public class RevisionController {

  private final RevisionService revisionService;
  private final RevisionMapper revisionMapper;

  @Autowired
  public RevisionController(RevisionService revisionService, RevisionMapper revisionMapper) {
    this.revisionService = revisionService;
    this.revisionMapper = revisionMapper;
  }

  @GetMapping("/tasks/revisions/{taskId}")
  public ResponseEntity<Collection<RevisionDto>> getTaskRevisions(@PathVariable long taskId) {
    Collection<FieldRevision> fieldRevisionCollection = revisionService.getRevisions(taskId, Task.class);
    Collection<RevisionDto> revisionDtos = revisionMapper.mapEntitiesToDtos(fieldRevisionCollection);
    return ResponseEntity.ok(revisionDtos);
  }

}
