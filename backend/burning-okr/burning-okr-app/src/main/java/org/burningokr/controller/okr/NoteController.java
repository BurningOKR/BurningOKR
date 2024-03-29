package org.burningokr.controller.okr;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.mapper.okr.NoteMapper;
import org.burningokr.model.okr.Note;
import org.burningokr.service.okr.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiController
@RequiredArgsConstructor
public class NoteController {

  private final NoteService noteService;
  private final NoteMapper noteMapper;

  @GetMapping("/notes/{noteId}")
  public ResponseEntity<NoteDto> getNoteById(
    @PathVariable Long noteId
  ) {
    Note note = noteService.findByIdExtendedRepositories(noteId);
    return ResponseEntity.ok(noteMapper.mapEntityToDto(note));
  }

  @PutMapping("/notes/{noteId}")
  @PreAuthorize("@noteAuthorizationService.isOwner(#noteId)")
  public ResponseEntity<NoteDto> updateNoteById(
    @PathVariable Long noteId,
    @Valid
    @RequestBody
    NoteDto noteDto
  ) {
    Note note = noteMapper.mapDtoToEntity(noteDto);
    note.setId(noteId);
    note = this.noteService.updateNote(note);
    return ResponseEntity.ok(noteMapper.mapEntityToDto(note));
  }

  @DeleteMapping("/notes/{noteId}")
  @PreAuthorize("@noteAuthorizationService.isOwner(#noteId)")
  public ResponseEntity deleteNoteById(
    @PathVariable Long noteId
  ) {
    noteService.deleteNote(noteId);
    return ResponseEntity.ok().build();
  }
}
