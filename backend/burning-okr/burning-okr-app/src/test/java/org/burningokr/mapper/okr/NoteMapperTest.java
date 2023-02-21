package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteDto;
import org.burningokr.model.okr.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoteMapperTest {
  private Note note;
  private NoteDto noteDto;
  private NoteMapper noteMapper;

  @BeforeEach
  public void reset() {
    this.noteDto = new NoteDto();
    this.note = new Note();
    this.noteMapper = new NoteMapper();
  }

  // region EntityToDto-Tests
  @Test
  public void test_mapEntityToDto_expectsBodyIsMapped() {
    String expected = "test";
    note.setText(expected);
    noteDto = noteMapper.mapEntityToDto(note);
    assertEquals(expected, noteDto.getNoteBody());
  }

  @Test
  public void test_mapEntityToDto_expectsIdIsMapped() {
    Long expected = 5L;
    note.setId(expected);
    noteDto = noteMapper.mapEntityToDto(note);
    assertEquals(expected, noteDto.getNoteId());
  }

  @Test
  public void test_mapEntityToDto_expectsUserIsMapped() {
    UUID expectedUuid = UUID.randomUUID();
    note.setUserId(expectedUuid);
    noteDto = noteMapper.mapEntityToDto(note);
    assertEquals(expectedUuid, noteDto.getUserId());
  }

  @Test
  public void test_mapEntityToDto_expectsDateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    note.setDate(expected);
    noteDto = noteMapper.mapEntityToDto(note);
    assertEquals(expected, noteDto.getDate());
  }
  // endregion

  // region DtoToEntity-Tests
  @Test
  public void test_mapDtoToEntity_expectsBodyIsMapped() {
    String expected = "test";
    noteDto.setNoteBody(expected);
    note = noteMapper.mapDtoToEntity(noteDto);
    assertEquals(expected, note.getText());
  }

  @Test
  public void test_mapDtoToEntity_expectsIdIsMapped() {
    Long expected = 5L;
    noteDto.setNoteId(expected);
    note = noteMapper.mapDtoToEntity(noteDto);
    assertEquals(expected, note.getId());
  }

  @Test
  public void test_mapDtoToEntity_expectsParentIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteDto.setDate(expected);
    note = noteMapper.mapDtoToEntity(noteDto);
    assertEquals(expected, note.getDate());
  }
  // endregion
}
