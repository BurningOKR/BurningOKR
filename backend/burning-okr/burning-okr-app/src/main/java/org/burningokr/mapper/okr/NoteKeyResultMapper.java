package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteKeyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class NoteKeyResultMapper implements DataMapper<NoteKeyResult, NoteKeyResultDto> {

  private final Logger logger = LoggerFactory.getLogger(NoteKeyResultMapper.class);

  @Override
  public NoteKeyResult mapDtoToEntity(NoteKeyResultDto noteKeyResultDto) {
    NoteKeyResult noteKeyResult = new NoteKeyResult();

    noteKeyResult.setText(noteKeyResultDto.getNoteBody());
    noteKeyResult.setId(noteKeyResultDto.getNoteId());
    noteKeyResult.setUserId(noteKeyResultDto.getUserId());
    noteKeyResult.setDate(noteKeyResultDto.getDate());

    KeyResult parentKeyResult = null;
    if (noteKeyResultDto.getParentKeyResultId() != null) {
      parentKeyResult = new KeyResult();
      parentKeyResult.setId(noteKeyResultDto.getParentKeyResultId());
    }
    noteKeyResult.setParentKeyResult(parentKeyResult);

    logger.info("Mapped NoteDto (id:" + noteKeyResultDto.getNoteId() + ") successful to Note.");
    return noteKeyResult;
  }

  @Override
  public NoteKeyResultDto mapEntityToDto(NoteKeyResult noteKeyResult) {
    NoteKeyResultDto noteKeyResultDto = new NoteKeyResultDto();

    noteKeyResultDto.setNoteBody(noteKeyResult.getText());
    noteKeyResultDto.setNoteId(noteKeyResult.getId());
    noteKeyResultDto.setUserId(noteKeyResult.getUserId());
    noteKeyResultDto.setDate(noteKeyResult.getDate());
    noteKeyResultDto.setParentKeyResultId(noteKeyResult.getParentKeyResult().getId());

    logger.info("Mapped Note (id:" + noteKeyResult.getId() + ") successful to NoteDto.");
    return noteKeyResultDto;
  }

  @Override
  public Collection<NoteKeyResult> mapDtosToEntities(Collection<NoteKeyResultDto> input) {
    Collection<NoteKeyResult> notes = new ArrayList<>();
    input.forEach(noteKeyResultDto -> notes.add(mapDtoToEntity(noteKeyResultDto)));
    return notes;
  }

  @Override
  public Collection<NoteKeyResultDto> mapEntitiesToDtos(Collection<NoteKeyResult> notes) {
    Collection<NoteKeyResultDto> noteKeyResultDtos = new ArrayList<>();
    notes.forEach(noteKeyResult -> noteKeyResultDtos.add(mapEntityToDto(noteKeyResult)));
    return noteKeyResultDtos;
  }
}
