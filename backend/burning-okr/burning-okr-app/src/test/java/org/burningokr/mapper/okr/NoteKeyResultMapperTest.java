package org.burningokr.mapper.okr;

import java.time.LocalDateTime;
import java.util.UUID;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteKeyResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NoteKeyResultMapperTest {

  private NoteKeyResult noteKeyResult;
  private NoteKeyResultDto noteKeyResultDto;
  private NoteKeyResultMapper noteKeyResultMapper;

  @Before
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
    Assert.assertEquals(expected, noteKeyResult.getId());
  }

  @Test
  public void test_mapDtoToEntity_expectsTextIsMapped() {
    String expected = "An example for a text";
    noteKeyResultDto.setNoteBody(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    Assert.assertEquals(expected, noteKeyResult.getText());
  }

  @Test
  public void test_mapDtoToEntity_expectsDateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteKeyResultDto.setDate(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    Assert.assertEquals(expected, noteKeyResult.getDate());
  }

  @Test
  public void test_mapDtoToEntity_expectsUserIdIsMapped() {
    UUID expected = UUID.randomUUID();
    noteKeyResultDto.setUserId(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    Assert.assertEquals(expected, noteKeyResult.getUserId());
  }

  @Test
  public void test_mapDtoToEntity_expectsParentKeyResultIdIsMapped() {
    Long expected = 234L;
    noteKeyResultDto.setParentKeyResultId(expected);
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    Assert.assertEquals(expected, noteKeyResult.getParentKeyResult().getId());
  }

  @Test
  public void test_mapDtoToEntity_expectsParentKeyResultIdIsNull() {
    noteKeyResult = noteKeyResultMapper.mapDtoToEntity(noteKeyResultDto);
    Assert.assertNull(noteKeyResult.getParentKeyResult());
  }
  // endregion

  // region EntityToDto-Tests
  @Test
  public void test_mapEntityToDto_expectsNoteIdIsMapped() {
    Long expected = 1234L;
    noteKeyResult.setId(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    Assert.assertEquals(expected, noteKeyResultDto.getNoteId());
  }

  @Test
  public void test_mapEntityToDto_expectsNoteBodyIsMapped() {
    String expected = "An example";
    noteKeyResult.setText(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    Assert.assertEquals(expected, noteKeyResultDto.getNoteBody());
  }

  @Test
  public void test_mapEntityToDto_expectsDateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteKeyResult.setDate(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    Assert.assertEquals(expected, noteKeyResultDto.getDate());
  }

  @Test
  public void test_mapEntityToDto_expectsUserIdIsMapped() {
    UUID expected = UUID.randomUUID();
    noteKeyResult.setUserId(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    Assert.assertEquals(expected, noteKeyResultDto.getUserId());
  }

  @Test
  public void test_mapEntityToDto_expectsParentKeyResultIsMapped() {
    KeyResult expected = new KeyResult();
    this.noteKeyResult.setParentKeyResult(expected);
    noteKeyResultDto = noteKeyResultMapper.mapEntityToDto(noteKeyResult);
    Assert.assertEquals(expected.getId(), noteKeyResultDto.getParentKeyResultId());
  }
  // endregion
}
