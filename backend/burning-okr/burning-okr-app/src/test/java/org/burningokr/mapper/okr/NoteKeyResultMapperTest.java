package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteKeyResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NoteKeyResultMapperTest {

  private NoteKeyResult noteKeyResult;
  private NoteKeyResultDto noteKeyResultDto;
  private NoteKeyResultMapper noteKeyResultMapper;

  @BeforeEach
  public void init() {
    this.noteKeyResult = new NoteKeyResult();
    this.noteKeyResultDto = new NoteKeyResultDto();
    this.noteKeyResultMapper = new NoteKeyResultMapper();
  }

  // region DtoToEntity-Tests
  @Test
  public void mapDtoToEntity_shouldMapId() {
    Long expected = 249L;
    noteKeyResultDto.setNoteId(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertEquals(expected, noteKeyResult.getId());
  }

  @Test
  public void mapDtoToEntity_shouldMapText() {
    String expected = "An example for a text";
    noteKeyResultDto.setNoteBody(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertEquals(expected, noteKeyResult.getText());
  }

  @Test
  public void mapDtoToEntity_shouldMapDate() {
    LocalDateTime expected = LocalDateTime.now();
    noteKeyResultDto.setDate(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertEquals(expected, noteKeyResult.getDate());
  }

  @Test
  public void mapDtoToEntity_shouldMapUserId() {
    UUID expected = UUID.randomUUID();
    noteKeyResultDto.setUserId(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertEquals(expected, noteKeyResult.getUserId());
  }

  @Test
  public void mapDtoToEntity_shouldMapParentKeyResultId() {
    Long expected = 234L;
    noteKeyResultDto.setParentKeyResultId(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertEquals(expected, noteKeyResult.getParentKeyResult().getId());
  }

  @Test
  public void mapDtoToEntity_shouldMapParentKeyResultIdThatIsNull() {
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertNull(noteKeyResult.getParentKeyResult());
  }

  @Test
  public void mapDtosToEntities_shouldMapNoteKeyResultDtosToEntities() {
    noteKeyResultDto.setNoteId(12L);
    Collection<NoteKeyResultDto> expected = new ArrayList<>() {
      {
        add(noteKeyResultDto);
        add(noteKeyResultDto);
      }
    };
    Collection<NoteKeyResult> actual = noteKeyResultMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getNoteId(), actual.stream().findFirst().orElseThrow().getId());
  }
  // endregion

  // region EntityToDto-Tests
  @Test
  public void mapEntityToDto_shouldMapNoteId() {
    Long expected = 1234L;
    noteKeyResult.setId(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertEquals(expected, noteKeyResultDto.getNoteId());
  }

  @Test
  public void mapEntityToDto_shouldMapNoteBody() {
    String expected = "An example";
    noteKeyResult.setText(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertEquals(expected, noteKeyResultDto.getNoteBody());
  }

  @Test
  public void mapEntityToDto_shouldMapDate() {
    LocalDateTime expected = LocalDateTime.now();
    noteKeyResult.setDate(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertEquals(expected, noteKeyResultDto.getDate());
  }

  @Test
  public void mapEntityToDto_shouldMapUserId() {
    UUID expected = UUID.randomUUID();
    noteKeyResult.setUserId(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertEquals(expected, noteKeyResultDto.getUserId());
  }

  @Test
  public void mapEntityToDto_shouldMapParentKeyResult() {
    KeyResult expected = new KeyResult();
    expected.setId(34L);
    noteKeyResult.setParentKeyResult(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertEquals(expected.getId(), noteKeyResultDto.getParentKeyResultId());
  }

  @Test
  public void mapEntityToDto_shouldMapParentKeyResultThatIsNull() {
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertNull(noteKeyResultDto.getParentKeyResultId());
  }

  @Test
  public void mapEntitiesToDtos_shouldMapNoteKeyResultEntitiesToDtos() {
    noteKeyResult.setId(12L);
    Collection<NoteKeyResult> expected = new ArrayList<>() {
      {
        add(noteKeyResult);
        add(noteKeyResult);
      }
    };
    Collection<NoteKeyResultDto> actual = noteKeyResultMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getNoteId());
  }
  // endregion
}
