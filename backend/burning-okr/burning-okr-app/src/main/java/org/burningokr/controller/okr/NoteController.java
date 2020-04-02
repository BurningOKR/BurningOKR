package org.burningokr.controller.okr;

import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.Note;
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
  private DataMapper<Note, NoteDto> noteMapper;
  private AuthorizationService authorizationService;

  /**
   * Initialize NoteController.
   *
   * @param noteService a {@link NoteService} object
   * @param noteMapper a {@link DataMapper} object with {@link Note} and {@link NoteDto}
   * @param authorizationService an {@link AuthorizationService} object
   */
  @Autowired
  public NoteController(
      NoteService noteService,
      DataMapper<Note, NoteDto> noteMapper,
      AuthorizationService authorizationService) {
    this.noteService = noteService;
    this.noteMapper = noteMapper;
    this.authorizationService = authorizationService;
  }

  @GetMapping("/notes/{noteId}")
  public ResponseEntity<NoteDto> getNoteById(@PathVariable Long noteId) {
    Note note = noteService.findById(noteId);
    return ResponseEntity.ok(noteMapper.mapEntityToDto(note));
  }

  /**
   * API Endpoint to update a Note.
   *
   * @param noteId a long value
   * @param noteDto a {@link NoteDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Note
   */
  @PutMapping("/notes/{noteId}")
  @PreAuthorize("@authorizationService.isNoteOwner(#noteId)")
  public ResponseEntity<NoteDto> updateNoteById(
      @PathVariable Long noteId, @Valid @RequestBody NoteDto noteDto, User user) {
    Note note = noteMapper.mapDtoToEntity(noteDto);
    note.setId(noteId);
    note = this.noteService.updateNote(note, user);
    return ResponseEntity.ok(noteMapper.mapEntityToDto(note));
  }

  @DeleteMapping("/notes/{noteId}")
  @PreAuthorize("@authorizationService.isNoteOwner(#noteId)")
  public ResponseEntity deleteNoteById(@PathVariable Long noteId, User user) {
    noteService.deleteNote(noteId, user);
    return ResponseEntity.ok().build();
  }
}
