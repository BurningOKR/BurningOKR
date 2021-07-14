package org.burningokr.mapper.okr;

import java.time.LocalDateTime;
import java.util.UUID;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NoteMapperTest {
  private Note note;
  private NoteDto noteDto;
  private NoteKeyResultMapper noteMapper;

  @Before
  public void reset() {
    this.noteDto = new NoteDto();
    this.note = new Note();
    KeyResult parentKeyResult = new KeyResult();
    parentKeyResult.setId(1337L);
    this.note.setParentKeyResult(parentKeyResult);
    this.noteMapper = new NoteKeyResultMapper();
  }

  // region EntityToDto-Tests
  @Test
  public void test_mapEntityToDto_expectsBodyIsMapped() {
    String expected = "test";
    note.setText(expected);
    noteDto = noteMapper.mapEntityToDto(note);
    Assert.assertEquals(expected, noteDto.getNoteBody());
  }

  @Test
  public void test_mapEntityToDto_expectsIdIsMapped() {
    Long expected = 5L;
    note.setId(expected);
    noteDto = noteMapper.mapEntityToDto(note);
    Assert.assertEquals(expected, noteDto.getNoteId());
  }

  @Test
  public void test_mapEntityToDto_expectsParentKeyResultIsMapped() {
    Long expected = 5L;
    KeyResult keyResult = new KeyResult();
    keyResult.setId(expected);

    note.setParentKeyResult(keyResult);
    noteDto = noteMapper.mapEntityToDto(note);
    Assert.assertEquals(expected, noteDto.getParentKeyResultId());
  }

  @Test
  public void test_mapEntityToDto_expectsUserIsMapped() {
    UUID expectedUuid = UUID.randomUUID();
    note.setUserId(expectedUuid);
    noteDto = noteMapper.mapEntityToDto(note);
    Assert.assertEquals(expectedUuid, noteDto.getUserId());
  }

  @Test
  public void test_mapEntityToDto_expectsDateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    note.setDate(expected);
    noteDto = noteMapper.mapEntityToDto(note);
    Assert.assertEquals(expected, noteDto.getDate());
  }
  // endregion

  // region DtoToEntity-Tests
  @Test
  public void test_mapDtoToEntity_expectsBodyIsMapped() {
    String expected = "test";
    noteDto.setNoteBody(expected);
    note = noteMapper.mapDtoToEntity(noteDto);
    Assert.assertEquals(expected, note.getText());
  }

  @Test
  public void test_mapDtoToEntity_expectsIdIsMapped() {
    Long expected = 5L;
    noteDto.setNoteId(expected);
    note = noteMapper.mapDtoToEntity(noteDto);
    Assert.assertEquals(expected, note.getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentIsNull() {
    note = noteMapper.mapDtoToEntity(noteDto);
    Assert.assertNull(note.getParentKeyResult());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentIdIsMapped() {
    Long expected = 123L;
    noteDto.setParentKeyResultId(expected);
    note = noteMapper.mapDtoToEntity(noteDto);
    Assert.assertEquals(expected, note.getParentKeyResult().getId());
  }

  @Test
  public void test_mapDtoToEntity_expectsParentIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteDto.setDate(expected);
    note = noteMapper.mapDtoToEntity(noteDto);
    Assert.assertEquals(expected, note.getDate());
  }
  // endregion
}
