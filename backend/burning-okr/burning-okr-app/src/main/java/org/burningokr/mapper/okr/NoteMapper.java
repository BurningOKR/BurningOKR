package org.burningokr.mapper.okr;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.service.okr.NoteParentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NoteMapper implements DataMapper<Note, NoteDto> {

  private final Logger logger = LoggerFactory.getLogger(NoteMapper.class);

  @Override
  public Note mapDtoToEntity(NoteDto noteDto) {
    Note note = new Note();

    note.setText(noteDto.getNoteBody());
    note.setId(noteDto.getNoteId());
    note.setUserId(noteDto.getUserId());
    note.setDate(noteDto.getDate());
    note.setParentId(noteDto.getParentId());
    note.setNoteParentType(noteDto.getNoteParentType().toString());

    logger.info("Mapped NoteDto (id:" + noteDto.getNoteId() + ") successful to Note.");
    return note;
  }

  @Override
  public NoteDto mapEntityToDto(Note note) {
    NoteDto noteDto = new NoteDto();

    noteDto.setNoteBody(note.getText());
    noteDto.setNoteId(note.getId());
    noteDto.setUserId(note.getUserId());
    noteDto.setDate(note.getDate());
    noteDto.setParentId(note.getParentId());
    noteDto.setNoteParentType(NoteParentType.valueOf(note.getNoteParentType()));

    logger.info("Mapped Note (id:" + note.getId() + ") successful to NoteDto.");
    return noteDto;
  }

  @Override
  public Collection<Note> mapDtosToEntities(Collection<NoteDto> input) {
    Collection<Note> notes = new ArrayList<>();
    input.forEach(noteDto -> notes.add(mapDtoToEntity(noteDto)));
    return notes;
  }

  @Override
  public Collection<NoteDto> mapEntitiesToDtos(Collection<Note> notes) {
    Collection<NoteDto> noteDtos = new ArrayList<>();
    notes.forEach(note -> noteDtos.add(mapEntityToDto(note)));
    return noteDtos;
  }
}
