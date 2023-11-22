package org.burningokr.mapper.okr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.Objective;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyResultMapper implements DataMapper<KeyResult, KeyResultDto> {

  private final KeyResultMilestoneMapper keyResultMilestoneMapper;

  @Override
  public KeyResult mapDtoToEntity(KeyResultDto keyResultDto) {
    KeyResult keyResult = new KeyResult();

    if (keyResultDto.getId() != null) {
      keyResult.setId(keyResultDto.getId());
    }

    if (keyResultDto.getTitle() != null) {
      keyResult.setName(keyResultDto.getTitle());
    }

    if (keyResultDto.getStartValue() != null) {
      keyResult.setStartValue(keyResultDto.getStartValue());
    }

    if (keyResultDto.getCurrentValue() != null) {
      keyResult.setCurrentValue(keyResultDto.getCurrentValue());
    }

    if (keyResultDto.getDescription() != null) {
      keyResult.setDescription(keyResultDto.getDescription());
    }

    if (keyResultDto.getTargetValue() != null) {
      keyResult.setTargetValue(keyResultDto.getTargetValue());
    }

    if (keyResultDto.getUnit() != null) {
      keyResult.setUnit(keyResultDto.getUnit());
    }

    keyResult.setSequence(keyResultDto.getSequence());

    Objective parentObjective = null;
    if (keyResultDto.getParentObjectiveId() != null) {
      parentObjective = new Objective();
      parentObjective.setId(keyResultDto.getParentObjectiveId());
    }
    keyResult.setParentObjective(parentObjective);

    keyResult.setNotes(new ArrayList<>());

    if (keyResultDto.getKeyResultMilestoneDtos() != null) {
      keyResult.setMilestones(
          keyResultMilestoneMapper.mapDtosToEntities(keyResultDto.getKeyResultMilestoneDtos()));
    }


    log.debug("Mapped KeyResultDto (id: %d) to KeyResult.".formatted(keyResultDto.getId()));
    return keyResult;
  }

  @Override
  public KeyResultDto mapEntityToDto(KeyResult keyResult) {
    KeyResultDto keyResultDto = KeyResultDto.builder().build();

    keyResultDto.setId(keyResult.getId());
    keyResultDto.setParentObjectiveId(keyResult.getParentObjective().getId());
    keyResultDto.setTitle(keyResult.getName());
    keyResultDto.setDescription(keyResult.getDescription());
    keyResultDto.setUnit(keyResult.getUnit());

    keyResultDto.setStartValue(keyResult.getStartValue());
    keyResultDto.setCurrentValue(keyResult.getCurrentValue());
    keyResultDto.setTargetValue(keyResult.getTargetValue());
    keyResultDto.setSequence(keyResult.getSequence());

    Collection<Long> noteIds = new ArrayList<>();
    for (Note note : keyResult.getNotes()) {
      noteIds.add(note.getId());
    }
    keyResultDto.setNoteIds(noteIds);

    keyResultDto.setKeyResultMilestoneDtos(
        keyResultMilestoneMapper.mapEntitiesToDtos(keyResult.getMilestones()));

    log.debug("Mapped KeyResult (id: %d) to KeyResultDto.".formatted(keyResult.getId()));
    return keyResultDto;
  }

  @Override
  public Collection<KeyResult> mapDtosToEntities(Collection<KeyResultDto> input) {
    Collection<KeyResult> keyResults = new ArrayList<>();
    input.forEach(keyResultDto -> keyResults.add(mapDtoToEntity(keyResultDto)));
    return keyResults;
  }

  @Override
  public Collection<KeyResultDto> mapEntitiesToDtos(Collection<KeyResult> keyResults) {
    Collection<KeyResultDto> keyResultDtos = new ArrayList<>();
    for (KeyResult keyResult : keyResults) {
      KeyResultDto keyResultDto = mapEntityToDto(keyResult);
      keyResultDtos.add(keyResultDto);
    }
    return keyResultDtos;
  }
}
