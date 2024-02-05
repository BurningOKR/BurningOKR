package org.burningokr.mapper.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.model.okr.Note;
@RequiredArgsConstructor
public  class NoteAbstractMapper {

  public Note mapNoteDtoToEntity(NoteDto noteDto) {
    Note note = new Note();

    note.setText(noteDto.getNoteBody());
    note.setId(noteDto.getNoteId());
    note.setUserId(noteDto.getUserId());
    note.setDate(noteDto.getDate());

    return note;
  }

  public NoteDto mapNoteEntityToDto(Note note) {
    NoteDto noteDto = new NoteDto();

    noteDto.setNoteBody(note.getText());
    noteDto.setNoteId(note.getId());
    noteDto.setUserId(note.getUserId());
    noteDto.setDate(note.getDate());

    return noteDto;
  }
}
