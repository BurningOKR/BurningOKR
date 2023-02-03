package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.Note;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class NoteMapper extends NoteAbstractMapper implements DataMapper<Note, NoteDto> {
  @Override
  public Note mapDtoToEntity(NoteDto input) {
    return this.mapNoteDtoToEntity(input);
  }

  @Override
  public NoteDto mapEntityToDto(Note input) {
    return this.mapNoteEntityToDto(input);
  }

  @Override
  public Collection<Note> mapDtosToEntities(Collection<NoteDto> input) {
    Collection<Note> notes = new ArrayList<>();
    input.forEach(noteDto -> notes.add(mapDtoToEntity(noteDto)));
    return notes;
  }

  @Override
  public Collection<NoteDto> mapEntitiesToDtos(Collection<Note> input) {
    Collection<NoteDto> noteDtos = new ArrayList<>();
    input.forEach(note -> noteDtos.add(mapEntityToDto(note)));
    return noteDtos;
  }
}
