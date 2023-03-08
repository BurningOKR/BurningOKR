package org.burningokr.controller.okr;

import jakarta.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteKeyResult;
import org.burningokr.model.users.IUser;
import org.burningokr.service.okr.KeyResultService;
import org.burningokr.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestApiController
public class KeyResultController {

  private KeyResultService keyResultService;
  private DataMapper<KeyResult, KeyResultDto> keyResultMapper;
  private DataMapper<NoteKeyResult, NoteKeyResultDto> noteKeyResultMapper;
  private AuthorizationService authorizationService;

  /**
   * Initialize KeyResultController.
   *
   * @param keyResultService     a {@link KeyResultService} object
   * @param keyResultMapper      a {@link DataMapper} object with {@link KeyResult} and {@link
   *                             KeyResultDto}
   * @param noteKeyResultMapper
   * @param authorizationService an {@link AuthorizationService} object
   */
  @Autowired
  public KeyResultController(
    KeyResultService keyResultService,
    DataMapper<KeyResult, KeyResultDto> keyResultMapper,
    DataMapper<NoteKeyResult, NoteKeyResultDto> noteKeyResultMapper,
    AuthorizationService authorizationService
  ) {
    this.keyResultService = keyResultService;
    this.keyResultMapper = keyResultMapper;
    this.noteKeyResultMapper = noteKeyResultMapper;
    this.authorizationService = authorizationService;
  }

  @GetMapping("/keyresults/{keyResultId}")
  public ResponseEntity<KeyResultDto> getKeyResultById(
    @PathVariable Long keyResultId
  ) {
    KeyResult keyResult = keyResultService.findById(keyResultId);
    return ResponseEntity.ok(keyResultMapper.mapEntityToDto(keyResult));
  }

  @GetMapping("/keyresults/{keyResultId}/notes")
  public ResponseEntity<Collection<NoteKeyResultDto>> getNotesOfKeyResult(
    @PathVariable long keyResultId
  ) {
    Collection<NoteKeyResult> noteKeyResults = keyResultService.findNotesOfKeyResult(keyResultId);
    return ResponseEntity.ok(noteKeyResultMapper.mapEntitiesToDtos(noteKeyResults));
  }

  /**
   * API Endpoint to update a Key Result.
   *
   * @param keyResultId  a long value
   * @param keyResultDto a {@link KeyResultDto} object
   * @param IUser        an {@link IUser} object
   * @return a {@link ResponseEntity} ok with a Key Result
   */
  @PutMapping("/keyresults/{keyResultId}")
  @PreAuthorize("@authorizationService.hasMemberPrivilegeForKeyResult(#keyResultId)")
  public ResponseEntity<KeyResultDto> updateKeyResultById(
    @PathVariable long keyResultId,
    @Valid
    @RequestBody
    KeyResultDto keyResultDto,
    IUser IUser
  ) {
    KeyResult keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    keyResult.setId(keyResultId);
    keyResult = this.keyResultService.updateKeyResult(keyResult, IUser);
    return ResponseEntity.ok(keyResultMapper.mapEntityToDto(keyResult));
  }

  /**
   * API Endpoint to add a Note to a Key Result.
   *
   * @param keyResultId      a long value
   * @param noteKeyResultDto a {@link NoteKeyResultDto} object
   * @param IUser            an {@link IUser} object
   * @return a {@link ResponseEntity} ok with a NoteKeyResultDto
   */
  @PostMapping("/keyresults/{keyResultId}/notes")
  public ResponseEntity<NoteKeyResultDto> addNoteToKeyResult(
    @PathVariable long keyResultId,
    @Valid
    @RequestBody
    NoteKeyResultDto noteKeyResultDto,
    IUser IUser
  ) {
    noteKeyResultDto.setParentKeyResultId(keyResultId);
    NoteKeyResult noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    noteKeyResult = this.keyResultService.createNote(keyResultId, noteKeyResult, IUser);
    return ResponseEntity.ok(noteKeyResultMapper.mapEntityToDto(noteKeyResult));
  }

  @PutMapping("/keyresults/notes")
  public ResponseEntity<NoteKeyResultDto> updateNoteKeyResult(
    @Valid
    @RequestBody
    NoteKeyResultDto noteKeyResultDto
  ) {
    NoteKeyResult noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    this.keyResultService.updateNote(noteKeyResult);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("keyresults/{keyResultId}")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForKeyResult(#keyResultId)")
  public ResponseEntity deleteKeyResult(
    @PathVariable Long keyResultId, IUser IUser
  )
    throws Exception {
    keyResultService.deleteKeyResult(keyResultId, IUser);
    return ResponseEntity.ok().build();
  }
}
