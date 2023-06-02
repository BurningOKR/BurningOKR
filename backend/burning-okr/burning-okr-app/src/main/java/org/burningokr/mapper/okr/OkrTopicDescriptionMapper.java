package org.burningokr.mapper.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.okr.OkrTopicDescriptionDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.service.util.DateMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class OkrTopicDescriptionMapper
  implements DataMapper<OkrTopicDescription, OkrTopicDescriptionDto> {

  private final DateMapper dateMapper;

  @Override
  public OkrTopicDescription mapDtoToEntity(OkrTopicDescriptionDto input) {
    OkrTopicDescription entity = new OkrTopicDescription();

    entity.setDescription(input.getDescription());
    entity.setBeginning(dateMapper.mapDateStringToDate(input.getBeginning()));
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
  public OkrTopicDescriptionDto mapEntityToDto(OkrTopicDescription input) {
    OkrTopicDescriptionDto dto = new OkrTopicDescriptionDto();

    dto.setDescription(input.getDescription());
    if (input.getBeginning() != null) {
      dto.setBeginning(dateMapper.mapDateToDateString(input.getBeginning()));
    }
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

    return dto;
  }

  @Override
  public Collection<OkrTopicDescription> mapDtosToEntities(
    Collection<OkrTopicDescriptionDto> input
  ) {
    Collection<OkrTopicDescription> descriptions = new ArrayList<>();
    input.forEach(descriptionDto -> descriptions.add(mapDtoToEntity(descriptionDto)));
    return descriptions;
  }

  @Override
  public Collection<OkrTopicDescriptionDto> mapEntitiesToDtos(
    Collection<OkrTopicDescription> input
  ) {
    Collection<OkrTopicDescriptionDto> dtos = new ArrayList<>();
    input.forEach(description -> dtos.add(mapEntityToDto(description)));
    return dtos;
  }
}
