package org.burningokr.mapper.okr;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteKeyResult;
import org.springframework.stereotype.Service;

@Service
public class NoteKeyResultMapper extends NoteAbstractMapper
    implements DataMapper<NoteKeyResult, NoteKeyResultDto> {

  @Override
  public NoteKeyResult mapDtoToEntity(NoteKeyResultDto input) {
    NoteKeyResult noteKeyResult = new NoteKeyResult(this.mapNoteDtoToEntity(input));

    KeyResult parentKeyResult = null;
    if (input.getParentKeyResultId() != null) {
      parentKeyResult = new KeyResult();
      parentKeyResult.setId(input.getParentKeyResultId());
    }
    noteKeyResult.setParentKeyResult(parentKeyResult);

    return noteKeyResult;
  }

  @Override
  public NoteKeyResultDto mapEntityToDto(NoteKeyResult input) {
    NoteKeyResultDto noteKeyResultDto = new NoteKeyResultDto(this.mapNoteEntityToDto(input));

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
