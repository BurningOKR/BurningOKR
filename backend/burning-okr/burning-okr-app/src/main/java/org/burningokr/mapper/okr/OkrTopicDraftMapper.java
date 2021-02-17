package org.burningokr.mapper.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.okr.OkrTopicDescriptionDto;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.OkrTopicDraft;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class OkrTopicDraftMapper implements DataMapper<OkrTopicDraft, OkrTopicDraftDto> {

    private final DataMapper<OkrTopicDescription, OkrTopicDescriptionDto> topicDescriptionMapper;

    @Override
    public OkrTopicDraft mapDtoToEntity(OkrTopicDraftDto input) {
        OkrTopicDraft entity;
        entity = (OkrTopicDraft) topicDescriptionMapper.mapDtoToEntity(input);
        return entity;
    }

    @Override
    public OkrTopicDraftDto mapEntityToDto(OkrTopicDraft input) {
        OkrTopicDraftDto entity;
        entity = (OkrTopicDraftDto) topicDescriptionMapper.mapEntityToDto(input);
        entity.setOkrParentUnitId(input.getParentUnit().getId());

        return entity;
    }

    @Override
    public Collection<OkrTopicDraft> mapDtosToEntities(Collection<OkrTopicDraftDto> input) {
        Collection<OkrTopicDraft> topicDrafts = new ArrayList<>();
        input.forEach(topicDraftDto -> topicDrafts.add(mapDtoToEntity(topicDraftDto)));
        return topicDrafts;
    }

    @Override
    public Collection<OkrTopicDraftDto> mapEntitiesToDtos(Collection<OkrTopicDraft> input) {
        Collection<OkrTopicDraftDto> topicDraftDtos = new ArrayList<>();
        input.forEach(topicDraft -> topicDraftDtos.add(mapEntityToDto(topicDraft)));
        return topicDraftDtos;
    }
}
