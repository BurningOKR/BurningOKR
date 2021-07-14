package org.burningokr.controller.okr;

import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.NoteKeyResult;
import org.burningokr.model.users.User;
import org.burningokr.service.okr.NoteService;
import org.burningokr.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class NoteController {

  private NoteService noteService;
  private DataMapper<NoteKeyResult, NoteKeyResultDto> noteKeyResultMapper;
  private AuthorizationService authorizationService;

  /**
   * Initialize NoteController.
   *
   * @param noteService a {@link NoteService} object
   * @param noteKeyResultMapper a {@link DataMapper} object with {@link NoteKeyResult} and {@link NoteKeyResultDto}
   * @param authorizationService an {@link AuthorizationService} object
   */
  @Autowired
  public NoteController(
      NoteService noteService,
      DataMapper<NoteKeyResult, NoteKeyResultDto> noteKeyResultMapper,
      AuthorizationService authorizationService) {
    this.noteService = noteService;
    this.noteKeyResultMapper = noteKeyResultMapper;
    this.authorizationService = authorizationService;
  }

  @GetMapping("/notes/{noteId}")
  public ResponseEntity<NoteDto> getNoteById(@PathVariable Long noteId) {
    Note note = noteService.findById(noteId);
    return ResponseEntity.ok(noteKeyResultMapper.mapEntityToDto((NoteKeyResult) note));
  }

  @GetMapping("/notes/{parentType}/{noteId}")
  public ResponseEntity<NoteDto> getNotesByParentTypeAndId(@PathVariable String parentType, @PathVariable Long noteId) {
    Note note = noteService.findById(noteId);
    return ResponseEntity.ok(noteKeyResultMapper.mapEntityToDto((NoteKeyResult) note));
  }

  /**
   * API Endpoint to update a Note.
   *
   * @param noteId a long value
   * @param noteDto a {@link NoteKeyResultDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Note
   */
  @PutMapping("/notes/{noteId}")
  @PreAuthorize("@authorizationService.isNoteOwner(#noteId)")
  public ResponseEntity<NoteDto> updateNoteById(
      @PathVariable Long noteId, @Valid @RequestBody NoteDto noteDto, User user) {
    Note note = noteKeyResultMapper.mapDtoToEntity((NoteKeyResultDto) noteDto);
    note.setId(noteId);
    note = this.noteService.updateNote(note, user);
    return ResponseEntity.ok(noteKeyResultMapper.mapEntityToDto((NoteKeyResult) note));
  }

  @DeleteMapping("/notes/{noteId}")
  @PreAuthorize("@authorizationService.isNoteOwner(#noteId)")
  public ResponseEntity deleteNoteById(@PathVariable Long noteId, User user) {
    noteService.deleteNote(noteId, user);
    return ResponseEntity.ok().build();
  }
}
