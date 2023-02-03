package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.KeyResultMilestoneDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.KeyResultMilestone;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class KeyResultMilestoneMapper
  implements DataMapper<KeyResultMilestone, KeyResultMilestoneDto> {
  @Override
  public KeyResultMilestone mapDtoToEntity(KeyResultMilestoneDto input) {
    KeyResultMilestone milestone = new KeyResultMilestone();
    milestone.setId(input.getId());
    milestone.setName(input.getName());
    milestone.setValue(input.getValue());

    KeyResult parentKeyResult = null;
    if (input.getParentKeyResultId() != null) {
      parentKeyResult = new KeyResult();
      parentKeyResult.setId(input.getParentKeyResultId());
    }

    milestone.setParentKeyResult(parentKeyResult);

    return milestone;
  }

  @Override
  public KeyResultMilestoneDto mapEntityToDto(KeyResultMilestone input) {
    KeyResultMilestoneDto dto = new KeyResultMilestoneDto();
    dto.setId(input.getId());
    dto.setName(input.getName());
    dto.setValue(input.getValue());
    dto.setParentKeyResultId(input.getParentKeyResult().getId());

    return dto;
  }

  @Override
  public Collection<KeyResultMilestone> mapDtosToEntities(Collection<KeyResultMilestoneDto> input) {
    return input.stream().map(this::mapDtoToEntity).collect(Collectors.toList());
  }

  @Override
  public Collection<KeyResultMilestoneDto> mapEntitiesToDtos(Collection<KeyResultMilestone> input) {
    return input.stream().map(this::mapEntityToDto).collect(Collectors.toList());
  }
}
