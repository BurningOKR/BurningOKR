package org.burningokr.mapper.okr;

import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.NoteKeyResult;
import org.burningokr.model.okr.Objective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyResultMapper implements DataMapper<KeyResult, KeyResultDto> {

  private final KeyResultMilestoneMapper keyResultMilestoneMapper;

  private final Logger logger = LoggerFactory.getLogger(KeyResultMapper.class);

  @Override
  public KeyResult mapDtoToEntity(KeyResultDto keyResultDto) {
    KeyResult keyResult = new KeyResult();

    keyResult.setId(keyResultDto.getId());
    keyResult.setName(keyResultDto.getTitle());
    keyResult.setDescription(keyResultDto.getDescription());
    keyResult.setUnit(keyResultDto.getUnit());
    keyResult.setStartValue(keyResultDto.getStartValue());
    keyResult.setCurrentValue(keyResultDto.getCurrentValue());
    keyResult.setTargetValue(keyResultDto.getTargetValue());
    keyResult.setSequence(keyResultDto.getSequence());

    Objective parentObjective = null;
    if (keyResultDto.getParentObjectiveId() != null) {
      parentObjective = new Objective();
      parentObjective.setId(keyResultDto.getParentObjectiveId());
    }
    keyResult.setParentObjective(parentObjective);

    keyResult.setNotes(new ArrayList<>());

    keyResult.setMilestones(
        keyResultMilestoneMapper.mapDtosToEntities(keyResultDto.getKeyResultMilestoneDtos()));

    logger.info("Mapped KeyResultDto (id:" + keyResultDto.getNoteIds() + ") to KeyResult.");
    return keyResult;
  }

  @Override
  public KeyResultDto mapEntityToDto(KeyResult keyResult) {
    KeyResultDto keyResultDto = new KeyResultDto();

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
    for (NoteKeyResult noteKeyResult : keyResult.getNotes()) {
      noteIds.add(noteKeyResult.getId());
    }
    keyResultDto.setNoteIds(noteIds);

    keyResultDto.setKeyResultMilestoneDtos(
        keyResultMilestoneMapper.mapEntitiesToDtos(keyResult.getMilestones()));

    logger.info("Mapped KeyResult (id:" + keyResult.getId() + ") to KeyResultDto.");
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
