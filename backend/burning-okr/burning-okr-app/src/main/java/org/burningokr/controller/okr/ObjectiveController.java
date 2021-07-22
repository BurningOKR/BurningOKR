package org.burningokr.controller.okr;

import java.util.Collection;
import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteObjectiveDto;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteObjective;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.users.User;
import org.burningokr.service.okr.ObjectiveService;
import org.burningokr.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class ObjectiveController {

  private ObjectiveService objectiveService;
  private DataMapper<Objective, ObjectiveDto> objectiveMapper;
  private DataMapper<KeyResult, KeyResultDto> keyResultMapper;
  private DataMapper<NoteObjective, NoteObjectiveDto> noteObjectiveMapper;
  private AuthorizationService authorizationService;


  /**
   * Initialize ObjectiveController.
   *
   * @param objectiveService an {@link ObjectiveService} object
   * @param objectiveMapper a {@link DataMapper} object with {@link Objective} and {@link
   *     ObjectiveDto}
   * @param keyResultMapper a {@link DataMapper} object with {@link KeyResult} and {@link
   *     KeyResultDto}
   * @param noteObjectiveMapper a {@link DataMapper} object with {@link NoteObjective} and
   *    {@link NoteObjectiveDto}
   * @param authorizationService an {@link AuthorizationService} object
   */
  @Autowired
  public ObjectiveController(
      ObjectiveService objectiveService,
      DataMapper<Objective, ObjectiveDto> objectiveMapper,
      DataMapper<KeyResult, KeyResultDto> keyResultMapper,
      // DataMapper<NoteObjective, NoteObjectiveDto> noteObjectiveMapper,  IS NEEDED BUT CRASHES ON STARTUP FOR SOME REASON?
      // Creates following error: Says to create a Bean out of DataMapper Interface?!
      // TODO P.B. 22.07.2021: FIX ERROR
      AuthorizationService authorizationService) {
    this.objectiveService = objectiveService;
    this.objectiveMapper = objectiveMapper;
    this.keyResultMapper = keyResultMapper;
    this.noteObjectiveMapper = noteObjectiveMapper;
    this.authorizationService = authorizationService;
  }

  @GetMapping("/objectives/{objectiveId}")
  public ResponseEntity<ObjectiveDto> getObjectiveById(@PathVariable long objectiveId) {
    Objective objective = objectiveService.findById(objectiveId);
    return ResponseEntity.ok(objectiveMapper.mapEntityToDto(objective));
  }

  @GetMapping("/objectives/{objectiveId}/keyresults")
  public ResponseEntity<Collection<KeyResultDto>> getKeyResultsOfObjective(
      @PathVariable long objectiveId) {
    Collection<KeyResult> keyResults = objectiveService.findKeyResultsOfObjective(objectiveId);
    return ResponseEntity.ok(keyResultMapper.mapEntitiesToDtos(keyResults));
  }

  /**
   * API Endpoint to update an Objective.
   *
   * @param objectiveId a long value
   * @param objectiveDto an {@link ObjectiveDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with an Objective
   */
  @PutMapping("/objectives/{objectiveId}")
  @PreAuthorize("@authorizationService.hasMemberPrivilegeForObjective(#objectiveId)")
  public ResponseEntity<ObjectiveDto> updateObjectiveById(
      @PathVariable long objectiveId, @Valid @RequestBody ObjectiveDto objectiveDto, User user) {
    Objective objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    objective.setId(objectiveId);
    objective = this.objectiveService.updateObjective(objective, user);
    return ResponseEntity.ok(objectiveMapper.mapEntityToDto(objective));
  }

  /**
   * API Endpoint to add Key Result to an Objective.
   *
   * @param objectiveId a long value
   * @param keyResultDto a {@link KeyResultDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Key Result
   * @throws Exception if max Key Results reached or cycle is closed
   */
  @PostMapping("objectives/{objectiveId}/keyresults")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForObjective(#objectiveId)")
  public ResponseEntity<KeyResultDto> addKeyResultToObjective(
      @PathVariable long objectiveId, @Valid @RequestBody KeyResultDto keyResultDto, User user)
      throws Exception {
    KeyResult keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    keyResult.setId(null);
    keyResult = objectiveService.createKeyResult(objectiveId, keyResult, user);
    return ResponseEntity.ok(keyResultMapper.mapEntityToDto(keyResult));
  }

  @DeleteMapping("/objectives/{objectiveId}")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForObjective(#objectiveId)")
  public ResponseEntity deleteObjectiveById(@PathVariable Long objectiveId, User user) {
    objectiveService.deleteObjectiveById(objectiveId, user);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/objectives/notes")
  public ResponseEntity<ObjectiveDto> updateObjectiveKeyResult(@Valid @RequestBody NoteObjectiveDto noteObjectiveDto) {
    NoteObjective noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
    this.objectiveService.updateNote(noteObjective);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/objectives/{objectiveId}/notes")
  public ResponseEntity<NoteDto> addNoteToObjective(
          @PathVariable long objectiveId,
          @Valid @RequestBody NoteObjectiveDto noteObjectiveDto,
          User user
          ) {
    noteObjectiveDto.setParentObjectiveId(objectiveId);
    NoteObjective noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
    noteObjective.setId(null); // ToDo (C.K. check if needed)
    noteObjective = this.objectiveService.createNote(objectiveId, noteObjective, user);
    return ResponseEntity.ok(noteObjectiveMapper.mapEntityToDto(noteObjective));
  }
}
