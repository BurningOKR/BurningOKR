package org.burningokr.mapper.okr;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
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

    KeyResult parentKeyResult = null;
    if (noteDto.getParentKeyResultId() != null) {
      parentKeyResult = new KeyResult();
      parentKeyResult.setId(noteDto.getParentKeyResultId());
    }
    note.setParentKeyResult(parentKeyResult);

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
    noteDto.setParentKeyResultId(note.getParentKeyResult().getId());

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
    for (Note note : notes) {
      NoteDto noteDto = mapEntityToDto(note);
      noteDtos.add(noteDto);
    }
    return noteDtos;
  }
}
