package org.burningokr.controller.okr;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.mapper.okr.KeyResultMapper;
import org.burningokr.mapper.okr.NoteKeyResultMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteKeyResult;
import org.burningokr.service.okr.KeyResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestApiController
@RequiredArgsConstructor
public class KeyResultController {

  private final KeyResultService keyResultService;
  private final KeyResultMapper keyResultMapper;
  private final NoteKeyResultMapper noteKeyResultMapper;


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

  @PutMapping("/keyresults/{keyResultId}")
  @PreAuthorize("@keyResultAuthorizationService.hasMemberPrivilegesForKeyResult(#keyResultId)")
  public ResponseEntity<KeyResultDto> updateKeyResultById(
    @PathVariable long keyResultId,
    @Valid
    @RequestBody
    KeyResultDto keyResultDto
  ) {
    KeyResult keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    keyResult.setId(keyResultId);
    keyResult = this.keyResultService.updateKeyResult(keyResult);
    return ResponseEntity.ok(keyResultMapper.mapEntityToDto(keyResult));
  }

  @PostMapping("/keyresults/{keyResultId}/notes")
  public ResponseEntity<NoteKeyResultDto> addNoteToKeyResult(
    @PathVariable long keyResultId,
    @Valid
    @RequestBody
    NoteKeyResultDto noteKeyResultDto
  ) {
    noteKeyResultDto.setParentKeyResultId(keyResultId);
    NoteKeyResult noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    noteKeyResult = this.keyResultService.createNote(noteKeyResult);
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
  @PreAuthorize("@keyResultAuthorizationService.hasManagerPrivilegesForKeyResult(#keyResultId)")
  public ResponseEntity deleteKeyResult(
    @PathVariable Long keyResultId
  )
    throws Exception {
    keyResultService.deleteKeyResult(keyResultId);
    return ResponseEntity.ok().build();
  }
}
