package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteKeyResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
  public void test_mapDtoToEntity_expectsIdIsMapped() {
    Long expected = 249L;
    noteKeyResultDto.setNoteId(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertEquals(expected, noteKeyResult.getId());
  }

  @Test
  public void test_mapDtoToEntity_expectsTextIsMapped() {
    String expected = "An example for a text";
    noteKeyResultDto.setNoteBody(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertEquals(expected, noteKeyResult.getText());
  }

  @Test
  public void test_mapDtoToEntity_expectsDateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteKeyResultDto.setDate(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertEquals(expected, noteKeyResult.getDate());
  }

  @Test
  public void test_mapDtoToEntity_expectsUserIdIsMapped() {
    UUID expected = UUID.randomUUID();
    noteKeyResultDto.setUserId(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertEquals(expected, noteKeyResult.getUserId());
  }

  @Test
  public void test_mapDtoToEntity_expectsParentKeyResultIdIsMapped() {
    Long expected = 234L;
    noteKeyResultDto.setParentKeyResultId(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertEquals(expected, noteKeyResult.getParentKeyResult().getId());
  }

  @Test
  public void test_mapDtoToEntity_expectsParentKeyResultIdIsNull() {
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    assertNull(noteKeyResult.getParentKeyResult());
  }
  // endregion

  // region EntityToDto-Tests
  @Test
  public void test_mapEntityToDto_expectsNoteIdIsMapped() {
    Long expected = 1234L;
    noteKeyResult.setId(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertEquals(expected, noteKeyResultDto.getNoteId());
  }

  @Test
  public void test_mapEntityToDto_expectsNoteBodyIsMapped() {
    String expected = "An example";
    noteKeyResult.setText(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertEquals(expected, noteKeyResultDto.getNoteBody());
  }

  @Test
  public void test_mapEntityToDto_expectsDateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteKeyResult.setDate(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertEquals(expected, noteKeyResultDto.getDate());
  }

  @Test
  public void test_mapEntityToDto_expectsUserIdIsMapped() {
    UUID expected = UUID.randomUUID();
    noteKeyResult.setUserId(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertEquals(expected, noteKeyResultDto.getUserId());
  }

  @Test
  public void test_mapEntityToDto_expectsParentKeyResultIsMapped() {
    KeyResult expected = new KeyResult();
    expected.setId(34L);
    noteKeyResult.setParentKeyResult(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertEquals(expected.getId(), noteKeyResultDto.getParentKeyResultId());
  }

  @Test
  public void test_mapEntityToDto_expectsParentKeyResultIsNull() {
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    assertNull(noteKeyResultDto.getParentKeyResultId());
  }
  // endregion
}
