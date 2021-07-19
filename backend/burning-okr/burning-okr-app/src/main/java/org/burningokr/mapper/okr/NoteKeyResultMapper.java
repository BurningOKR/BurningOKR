package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.NoteKeyResult;
import org.burningokr.model.okr.Objective;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class NoteKeyResultMapper extends NoteAbstractMapper implements DataMapper<NoteKeyResult, NoteKeyResultDto> {
    @Override
    public NoteKeyResult mapDtoToEntity(NoteKeyResultDto input) {
        NoteKeyResult noteKeyResult = (NoteKeyResult) this.mapNoteDtoToEntity(input);

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
        NoteKeyResultDto noteKeyResultDto = (NoteKeyResultDto) this.mapNoteEntityToDto(input);

        noteKeyResultDto.setParentKeyResultId(input.getParentKeyResult().getId());

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
