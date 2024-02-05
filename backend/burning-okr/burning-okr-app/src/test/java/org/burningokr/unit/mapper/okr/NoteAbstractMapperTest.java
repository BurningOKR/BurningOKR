package org.burningokr.unit.mapper.okr;

import org.burningokr.dto.okr.NoteDto;
import org.burningokr.mapper.okr.NoteAbstractMapper;
import org.burningokr.model.okr.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoteAbstractMapperTest {

  private Note note;
  private NoteDto noteDto;
  private NoteMapper noteMapper;

  @BeforeEach
  public void reset() {
    noteDto = new NoteDto();
    note = new Note();
    noteMapper = new NoteMapper();
  }

  @Test
  public void mapNoteDtoToEntity_expects_NoteBodyIsMapped() {
    String expected = "test";
    noteDto.setNoteBody(expected);
    assertEquals(expected, noteMapper.mapNoteDtoToEntity(noteDto).getText());
  }

  // Dto to Entity

  @Test
  public void mapNoteDtoToEntity_expects_NoteIdIsMapped() {
    Long expected = 1L;
    noteDto.setNoteId(expected);
    assertEquals(expected, noteMapper.mapNoteDtoToEntity(noteDto).getId());
  }

  @Test
  public void mapNoteDtoToEntity_expects_UserIdIsMapped() {
    UUID expected = new UUID(1L, 1L);
    noteDto.setUserId(expected);
    assertEquals(expected, noteMapper.mapNoteDtoToEntity(noteDto).getUserId());
  }

  @Test
  public void mapNoteDtoToEntity_expects_DateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteDto.setDate(expected);
    assertEquals(expected, noteMapper.mapNoteDtoToEntity(noteDto).getDate());
  }

  @Test
  public void mapEntityToNoteDto_expects_NoteBodyIsMapped() {
    String expected = "test";
    note.setText(expected);
    assertEquals(expected, noteMapper.mapNoteEntityToDto(note).getNoteBody());
  }

  // Entity to Dto

  @Test
  public void mapEntityToNoteDto_expects_NoteIdIsMapped() {
    Long expected = 1L;
    note.setId(expected);
    assertEquals(expected, noteMapper.mapNoteEntityToDto(note).getNoteId());
  }

  @Test
  public void mapEntityToNoteDto_expects_UserIdIsMapped() {
    UUID expected = new UUID(1L, 1L);
    note.setUserId(expected);
    assertEquals(expected, noteMapper.mapNoteEntityToDto(note).getUserId());
  }

  @Test
  public void mapEntityToNoteDto_expects_DateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    note.setDate(expected);
    assertEquals(expected, noteMapper.mapNoteEntityToDto(note).getDate());
  }

  private static class NoteMapper extends NoteAbstractMapper {
  }
}
