package org.burningokr.controller.okr;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteObjectiveDto;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.mapper.okr.KeyResultMapper;
import org.burningokr.mapper.okr.NoteObjectiveMapper;
import org.burningokr.mapper.okr.ObjectiveMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteObjective;
import org.burningokr.model.okr.Objective;
import org.burningokr.service.okr.ObjectiveService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestApiController
@RequiredArgsConstructor
public class ObjectiveController {

  private final ObjectiveService objectiveService;
  private final ObjectiveMapper objectiveMapper;
  private final KeyResultMapper keyResultMapper;
  private final NoteObjectiveMapper noteObjectiveMapper;

  @GetMapping("/objectives/{objectiveId}")
  public ResponseEntity<ObjectiveDto> getObjectiveById(@PathVariable long objectiveId) {
    Objective objective = objectiveService.findById(objectiveId);
    return ResponseEntity.ok(objectiveMapper.mapEntityToDto(objective));
  }

  @GetMapping("/objectives/{objectiveId}/keyresults")
  public ResponseEntity<Collection<KeyResultDto>> getKeyResultsOfObjective(
      @PathVariable long objectiveId
  ) {
    Collection<KeyResult> keyResults = objectiveService.findKeyResultsOfObjective(objectiveId);
    return ResponseEntity.ok(keyResultMapper.mapEntitiesToDtos(keyResults));
  }

  /**
   * API Endpoint to update an Objective.
   *
   * @param objectiveId  a long value
   * @param objectiveDto an {@link ObjectiveDto} object
   * @return a {@link ResponseEntity} ok with an Objective
   */
  @PutMapping(
      value = "/objectives/{objectiveId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @PreAuthorize("@objectiveAuthorizationService.hasMemberPrivilegesForObjective(#objectiveId)")
  public ResponseEntity<ObjectiveDto> updateObjectiveById(
      @PathVariable long objectiveId,
      @Valid @RequestBody ObjectiveDto objectiveDto) {
    objectiveDto.setId(objectiveId);
    Objective objective = objectiveMapper.mapDtoToEntity(objectiveDto);
//    objective.setId(objectiveId);
    objective = this.objectiveService.updateObjective(objective);
    return ResponseEntity.ok(objectiveMapper.mapEntityToDto(objective));
  }

  @GetMapping("/objectives/{objectiveId}/notes")
  public ResponseEntity<Collection<NoteObjectiveDto>> getNotesOfObjective(
      @PathVariable long objectiveId
  ) {
    Collection<NoteObjective> noteObjectives = objectiveService.findNotesOfObjective(objectiveId);
    return ResponseEntity.ok(noteObjectiveMapper.mapEntitiesToDtos(noteObjectives));
  }

  @GetMapping("/objectives/{objectiveId}/childobjectives")
  public ResponseEntity<Collection<ObjectiveDto>> getChildsOfObjective(
      @PathVariable long objectiveId
  ) {
    Collection<Objective> childObjectives = objectiveService.findChildObjectivesOfObjective(objectiveId);
    return ResponseEntity.ok(objectiveMapper.mapEntitiesToDtos(childObjectives));
  }

  @PostMapping(
      value = "objectives/{objectiveId}/keyresults",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @PreAuthorize("@objectiveAuthorizationService.hasMemberPrivilegesForObjective(#objectiveId)")
  public ResponseEntity<KeyResultDto> addKeyResultToObjective(
      @PathVariable long objectiveId,
      @Valid @RequestBody KeyResultDto keyResultDto
  ) throws Exception {
    KeyResult keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    keyResult = objectiveService.createKeyResult(objectiveId, keyResult);

    return ResponseEntity.ok(keyResultMapper.mapEntityToDto(keyResult));
  }

  @DeleteMapping("/objectives/{objectiveId}")
  @PreAuthorize("@objectiveAuthorizationService.hasManagerPrivilegesForObjective(#objectiveId)")
  public ResponseEntity deleteObjectiveById(@PathVariable Long objectiveId) {
    objectiveService.deleteObjectiveById(objectiveId);
    return ResponseEntity.ok().build();
  }

  @PutMapping(
      value = "/objectives/notes",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ObjectiveDto> updateNoteFromObjective(
      @Valid @RequestBody NoteObjectiveDto noteObjectiveDto) {
    NoteObjective noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
    this.objectiveService.updateNote(noteObjective);
    return ResponseEntity.ok().build();
  }

  @PostMapping(
      value = "/objectives/{objectiveId}/notes",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<NoteDto> addNoteToObjective(@PathVariable long objectiveId, @Valid @RequestBody NoteObjectiveDto noteObjectiveDto) {
    noteObjectiveDto.setParentObjectiveId(objectiveId);
    NoteObjective noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
    noteObjective = this.objectiveService.createNote(noteObjective);
    return ResponseEntity.ok(noteObjectiveMapper.mapEntityToDto(noteObjective));
  }
}
