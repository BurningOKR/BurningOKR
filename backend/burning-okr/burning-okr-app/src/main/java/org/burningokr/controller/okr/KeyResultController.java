package org.burningokr.controller.okr;

import java.util.Collection;
import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteKeyResult;
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
  private DataMapper<NoteKeyResult, NoteKeyResultDto> noteKeyResultMapper;
  private AuthorizationService authorizationService;

  /**
   * Initialize KeyResultController.
   *
   * @param keyResultService a {@link KeyResultService} object
   * @param keyResultMapper a {@link DataMapper} object with {@link KeyResult} and {@link
   *     KeyResultDto}
   * @param noteKeyResultMapper
   * @param authorizationService an {@link AuthorizationService} object
   */
  @Autowired
  public KeyResultController(
      KeyResultService keyResultService,
      DataMapper<KeyResult, KeyResultDto> keyResultMapper,
      DataMapper<NoteKeyResult, NoteKeyResultDto> noteKeyResultMapper,
      AuthorizationService authorizationService) {
    this.keyResultService = keyResultService;
    this.keyResultMapper = keyResultMapper;
    this.noteKeyResultMapper = noteKeyResultMapper;
    this.authorizationService = authorizationService;
  }

  @GetMapping("/keyresults/{keyResultId}")
  public ResponseEntity<KeyResultDto> getKeyResultById(@PathVariable Long keyResultId) {
    KeyResult keyResult = keyResultService.findById(keyResultId);
    return ResponseEntity.ok(keyResultMapper.mapEntityToDto(keyResult));
  }

  @GetMapping("/keyresults/{keyResultId}/notes")
  public ResponseEntity<Collection<NoteKeyResultDto>> getNotesOfKeyResult(
      @PathVariable long keyResultId) {
    Collection<NoteKeyResult> noteKeyResults = keyResultService.findNotesOfKeyResult(keyResultId);
    return ResponseEntity.ok(noteKeyResultMapper.mapEntitiesToDtos(noteKeyResults));
  }

  /**
   * API Endpoint to update a Key Result.
   *
   * @param keyResultId a long value
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
   * @param noteKeyResultDto a {@link NoteKeyResultDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Note
   */
  @PostMapping("/keyresults/{keyResultId}/notes")
  public ResponseEntity<NoteDto> addNoteToKeyResult(
      @PathVariable long keyResultId,
      @Valid @RequestBody NoteKeyResultDto noteKeyResultDto,
      User user) {
    noteKeyResultDto.setParentKeyResultId(keyResultId);
    NoteKeyResult noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    noteKeyResult.setId(null);
    noteKeyResult = this.keyResultService.createNote(keyResultId, noteKeyResult, user);
    return ResponseEntity.ok(noteKeyResultMapper.mapEntityToDto(noteKeyResult));
  }

  @PutMapping("/keyresults/notes")
  public ResponseEntity<NoteKeyResultDto> updateNoteKeyResult(
      @Valid @RequestBody NoteKeyResultDto noteKeyResultDto) {
    NoteKeyResult noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    this.keyResultService.updateNote(noteKeyResult);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("keyresults/{keyResultId}")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForKeyResult(#keyResultId)")
  public ResponseEntity deleteKeyResult(@PathVariable Long keyResultId, User user) {
    keyResultService.deleteKeyResult(keyResultId, user);
    return ResponseEntity.ok().build();
  }
}
