package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.dto.okr.NoteObjectiveDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.NoteObjective;
import org.burningokr.model.okr.Objective;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class NoteObjectiveMapper extends NoteAbstractMapper
        implements DataMapper<NoteObjective, NoteObjectiveDto> {


    private NoteObjective noteToNoteObjective(Note note) {

        NoteObjective noteObjective = new NoteObjective();

        noteObjective.setId(note.getId());
        noteObjective.setText(note.getText());
        noteObjective.setDate(note.getDate());
        noteObjective.setUserId(note.getUserId());
        noteObjective.setParentObjective(null);

        return noteObjective;
    }

    @Override
    public NoteObjective mapDtoToEntity(NoteObjectiveDto input) {

        NoteObjective noteObjective = noteToNoteObjective(mapNoteDtoToEntity(input));
        Objective parentObjective = null;

        if(input.getParentObjectiveId() != null) {

            parentObjective = new Objective();
            parentObjective.setId(input.getParentObjectiveId());
        }
        noteObjective.setParentObjective(parentObjective);

        return noteObjective;
    }

    private NoteObjectiveDto noteDtoToNoteObjectiveDto(NoteDto noteDto) {

        NoteObjectiveDto noteObjectiveDto = new NoteObjectiveDto();

        noteObjectiveDto.setNoteId(noteDto.getNoteId());
        noteObjectiveDto.setNoteBody(noteDto.getNoteBody());
        noteObjectiveDto.setDate(noteDto.getDate());
        noteObjectiveDto.setUserId(noteDto.getUserId());
        noteObjectiveDto.setParentObjectiveId(null);

        return noteObjectiveDto;
    }

    @Override
    public NoteObjectiveDto mapEntityToDto(NoteObjective input) {

        NoteObjectiveDto noteObjectiveDto = noteDtoToNoteObjectiveDto(mapNoteEntityToDto(input));

        if(input.getParentObjective() != null) {

            noteObjectiveDto.setParentObjectiveId(input.getParentObjective().getId());
        }

        return noteObjectiveDto;
    }

    @Override
    public Collection<NoteObjective> mapDtosToEntities(Collection<NoteObjectiveDto> input) {

        Collection<NoteObjective> noteObjectives = new ArrayList<>();
        input.forEach(noteObjectiveDto -> noteObjectives.add(mapDtoToEntity(noteObjectiveDto)));

        return noteObjectives;
    }

    @Override
    public Collection<NoteObjectiveDto> mapEntitiesToDtos(Collection<NoteObjective> input) {

        Collection<NoteObjectiveDto> noteObjectiveDtos = new ArrayList<>();
        input.forEach(noteObjective -> noteObjectiveDtos.add(mapEntityToDto(noteObjective)));

        return noteObjectiveDtos;
    }
}
