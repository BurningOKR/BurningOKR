package org.burningokr.mapper.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.okr.OkrTopicDescriptionDto;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.OkrTopicDraft;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class OkrTopicDraftMapper implements DataMapper<OkrTopicDraft, OkrTopicDraftDto> {

    private final DataMapper<OkrTopicDescription, OkrTopicDescriptionDto> topicDescriptionMapper;

    @Override
    public OkrTopicDraft mapDtoToEntity(OkrTopicDraftDto input) {
        OkrTopicDraft entity = new OkrTopicDraft();
        entity.setAcceptanceCriteria(input.getAcceptanceCriteria());
        entity.setBeginning(input.getBeginning());
        entity.setContributesTo(input.getContributesTo());
        entity.setDelimitation(input.getDelimitation());
        entity.setDependencies(input.getDependencies());
        entity.setHandoverPlan(input.getHandoverPlan());
        entity.setId(input.getId());
        entity.setInitiatorId(input.getInitiatorId());
        entity.setName(input.getName());
        entity.setResources(input.getResources());
        entity.setStakeholders(input.getStakeholders());
        entity.setStartTeam(input.getStartTeam());
        return entity;
    }

    @Override
    public OkrTopicDraftDto mapEntityToDto(OkrTopicDraft input) {
        OkrTopicDraftDto dto = new OkrTopicDraftDto();
        dto.setAcceptanceCriteria(input.getAcceptanceCriteria());
        dto.setBeginning(input.getBeginning());
        dto.setContributesTo(input.getContributesTo());
        dto.setDelimitation(input.getDelimitation());
        dto.setDependencies(input.getDependencies());
        dto.setHandoverPlan(input.getHandoverPlan());
        dto.setId(input.getId());
        dto.setInitiatorId(input.getInitiatorId());
        dto.setName(input.getName());
        dto.setResources(input.getResources());
        dto.setStakeholders(input.getStakeholders());
        dto.setStartTeam(input.getStartTeam());
        dto.setOkrParentUnitId(input.getParentUnit().getId());

        return dto;
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
