package org.burningokr.controller.okr;

import java.util.Collection;
import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.model.users.User;
import org.burningokr.service.okr.KeyResultService;
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
public class KeyResultController {

  private KeyResultService keyResultService;
  private DataMapper<KeyResult, KeyResultDto> keyResultMapper;
  private DataMapper<Note, NoteDto> noteMapper;
  private AuthorizationService authorizationService;

  /**
   * Initialize KeyResultController.
   *
   * @param keyResultService a {@link KeyResultService} object
   * @param keyResultMapper a {@link DataMapper} object with {@link KeyResult} and {@link
   *     KeyResultDto}
   * @param noteMapper a {@link DataMapper} object with {@link Note} and {@link NoteDto}
   * @param authorizationService an {@link AuthorizationService} object
   */
  @Autowired
  public KeyResultController(
      KeyResultService keyResultService,
      DataMapper<KeyResult, KeyResultDto> keyResultMapper,
      DataMapper<Note, NoteDto> noteMapper,
      AuthorizationService authorizationService) {
    this.keyResultService = keyResultService;
    this.keyResultMapper = keyResultMapper;
    this.noteMapper = noteMapper;
    this.authorizationService = authorizationService;
  }

  @GetMapping("/keyresults/{keyResultId}")
  public ResponseEntity<KeyResultDto> getKeyResultById(@PathVariable Long keyResultId) {
    KeyResult keyResult = keyResultService.findById(keyResultId);
    return ResponseEntity.ok(keyResultMapper.mapEntityToDto(keyResult));
  }

  @GetMapping("/keyresults/{keyResultId}/notes")
  public ResponseEntity<Collection<NoteDto>> getNotesOfKeyResult(@PathVariable long keyResultId) {
    Collection<Note> notes = keyResultService.findNotesOfKeyResult(keyResultId);
    return ResponseEntity.ok(noteMapper.mapEntitiesToDtos(notes));
  }

  /**
   * API Endpoint to update a Key Result.
   *
   * @param keyResultId a long vlaue
   * @param keyResultDto a {@link KeyResultDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Key Result
   */
  @PutMapping("/keyresults/{keyResultId}")
  @PreAuthorize("@authorizationService.hasMemberPrivilegeForKeyResult(#keyResultId)")
  public ResponseEntity<KeyResultDto> updateKeyResultById(
      @PathVariable long keyResultId, @Valid @RequestBody KeyResultDto keyResultDto, User user) {
    KeyResult keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    keyResult.setId(keyResultId);
    keyResult = this.keyResultService.updateKeyResult(keyResult, user);
    return ResponseEntity.ok(keyResultMapper.mapEntityToDto(keyResult));
  }

  /**
   * API Endpoint to add a Note to a Key Result.
   *
   * @param keyResultId a long value
   * @param noteDto a {@link NoteDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Note
   */
  @PostMapping("/keyresults/{keyResultId}/notes")
  public ResponseEntity<NoteDto> addNoteToKeyResult(
      @PathVariable long keyResultId, @Valid @RequestBody NoteDto noteDto, User user) {
    Note note = noteMapper.mapDtoToEntity(noteDto);
    note.setId(null);
    note = this.keyResultService.createNote(keyResultId, note, user);
    return ResponseEntity.ok(noteMapper.mapEntityToDto(note));
  }

  @DeleteMapping("keyresults/{keyResultId}")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForKeyResult(#keyResultId)")
  public ResponseEntity deleteKeyResult(@PathVariable Long keyResultId, User user) {
    keyResultService.deleteKeyResult(keyResultId, user);
    return ResponseEntity.ok().build();
  }
}
