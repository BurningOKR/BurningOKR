package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteObjectiveDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.NoteObjective;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class NoteObjectiveMapper extends NoteAbstractMapper
        implements DataMapper<NoteObjective, NoteObjectiveDto> {


    @Override
    public NoteObjective mapDtoToEntity(NoteObjectiveDto input) {

        NoteObjective noteObjective = new NoteObjective();
        noteObjective.setDate(input.getDate());
        noteObjective.setUserId(input.getUserId());
        noteObjective.setParentObjective(null); // ToDo (C.K. check if stored in db correctly)

        return noteObjective;
    }

    @Override
    public NoteObjectiveDto mapEntityToDto(NoteObjective input) {

        NoteObjectiveDto noteObjectiveDto = new NoteObjectiveDto();


        return noteObjectiveDto;
    }

    @Override
    public Collection<NoteObjective> mapDtosToEntities(Collection<NoteObjectiveDto> input) {
        return null;
    }

    @Override
    public Collection<NoteObjectiveDto> mapEntitiesToDtos(Collection<NoteObjective> input) {
        return null;
    }
}
