package org.burningokr.mapper.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.NoteKeyResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class NoteKeyResultMapper extends NoteAbstractMapper
  implements DataMapper<NoteKeyResult, NoteKeyResultDto> {

  private NoteKeyResult noteToNoteKeyResult(Note note) {

    NoteKeyResult noteKeyResult = new NoteKeyResult();

    noteKeyResult.setId(note.getId());
    noteKeyResult.setText(note.getText());
    noteKeyResult.setDate(note.getDate());
    noteKeyResult.setUserId(note.getUserId());
    noteKeyResult.setParentKeyResult(null);

    return noteKeyResult;
  }

  @Override
  public NoteKeyResult mapDtoToEntity(NoteKeyResultDto input) {

    NoteKeyResult noteKeyResult = this.noteToNoteKeyResult(this.mapNoteDtoToEntity(input));
    KeyResult parentKeyResult = null;

    if (input.getParentKeyResultId() != null) {

      parentKeyResult = new KeyResult();
      parentKeyResult.setId(input.getParentKeyResultId());
    }
    noteKeyResult.setParentKeyResult(parentKeyResult);

    return noteKeyResult;
  }

  private NoteKeyResultDto noteDtoToNoteKeyResultDto(NoteDto noteDto) {
    NoteKeyResultDto noteKeyResultDto = new NoteKeyResultDto();

    noteKeyResultDto.setNoteId(noteDto.getNoteId());
    noteKeyResultDto.setNoteBody(noteDto.getNoteBody());
    noteKeyResultDto.setDate(noteDto.getDate());
    noteKeyResultDto.setUserId(noteDto.getUserId());
    noteKeyResultDto.setParentKeyResultId(null);

    return noteKeyResultDto;
  }

  @Override
  public NoteKeyResultDto mapEntityToDto(NoteKeyResult input) {
    NoteKeyResultDto noteKeyResultDto =
      this.noteDtoToNoteKeyResultDto(this.mapNoteEntityToDto(input));

    if (input.getParentKeyResult() != null) {
      noteKeyResultDto.setParentKeyResultId(input.getParentKeyResult().getId());
    }

    return noteKeyResultDto;
  }

  @Override
  public Collection<NoteKeyResult> mapDtosToEntities(Collection<NoteKeyResultDto> input) {
    Collection<NoteKeyResult> noteKeyResults = new ArrayList<>();
    input.forEach(noteKeyResultDto -> noteKeyResults.add(mapDtoToEntity(noteKeyResultDto)));
    return noteKeyResults;
  }

  @Override
  public Collection<NoteKeyResultDto> mapEntitiesToDtos(Collection<NoteKeyResult> input) {
    Collection<NoteKeyResultDto> noteKeyResultDtos = new ArrayList<>();
    input.forEach(noteKeyResult -> noteKeyResultDtos.add(mapEntityToDto(noteKeyResult)));
    return noteKeyResultDtos;
  }
}
