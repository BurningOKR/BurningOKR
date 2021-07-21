package org.burningokr.mapper.okr;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.okr.NoteTopicDraftDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.springframework.stereotype.Service;

@Service
public class NoteTopicDraftMapper extends NoteAbstractMapper
        implements DataMapper<NoteTopicDraft, NoteTopicDraftDto> {

    @Override
    public NoteTopicDraft mapDtoToEntity(NoteTopicDraftDto input) {
        NoteTopicDraft noteTopicDraft = new NoteTopicDraft(this.mapNoteDtoToEntity(input));

        OkrTopicDraft parentTopicDraft = null;
        if (input.getParentTopicDraftId() != null) {
            parentTopicDraft = new OkrTopicDraft();
            parentTopicDraft.setId(input.getParentTopicDraftId());
        }
        noteTopicDraft.setParentTopicDraft(parentTopicDraft);

        return noteTopicDraft;
    }

    @Override
    public NoteTopicDraftDto mapEntityToDto(NoteTopicDraft input) {
        NoteTopicDraftDto noteTopicDraftDto = new NoteTopicDraftDto(this.mapNoteEntityToDto(input));

        if (input.getParentTopicDraft() != null) {
            noteTopicDraftDto.setParentTopicDraftId(input.getParentTopicDraft().getId());
        }

        return noteTopicDraftDto;
    }

    @Override
    public Collection<NoteTopicDraft> mapDtosToEntities(Collection<NoteTopicDraftDto> input) {
        Collection<NoteTopicDraft> noteTopicDrafts = new ArrayList<>();
        input.forEach(noteTopicDraftDto -> noteTopicDrafts.add(mapDtoToEntity(noteTopicDraftDto)));
        return noteTopicDrafts;
    }

    @Override
    public Collection<NoteTopicDraftDto> mapEntitiesToDtos(Collection<NoteTopicDraft> input) {
        Collection<NoteTopicDraftDto> noteTopicDraftDtos = new ArrayList<>();
        input.forEach(noteTopicDraft -> noteTopicDraftDtos.add(mapEntityToDto(noteTopicDraft)));
        return noteTopicDraftDtos;
    }
}
