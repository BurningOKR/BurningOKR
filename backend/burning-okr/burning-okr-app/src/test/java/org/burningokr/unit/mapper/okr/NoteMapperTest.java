package org.burningokr.unit.mapper.okr;

import org.burningokr.dto.okr.NoteDto;
import org.burningokr.mapper.okr.NoteMapper;
import org.burningokr.model.okr.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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
  public void mapEntityToDto_shouldMapBody() {
    String expected = "test";
    note.setText(expected);
    noteDto = noteMapper.mapEntityToDto(note);
    assertEquals(expected, noteDto.getNoteBody());
  }

  @Test
  public void mapEntityToDto_shouldMapId() {
    Long expected = 5L;
    note.setId(expected);
    noteDto = noteMapper.mapEntityToDto(note);
    assertEquals(expected, noteDto.getNoteId());
  }

  @Test
  public void mapEntityToDto_shouldMapUser() {
    UUID expectedUuid = UUID.randomUUID();
    note.setUserId(expectedUuid);
    noteDto = noteMapper.mapEntityToDto(note);
    assertEquals(expectedUuid, noteDto.getUserId());
  }

  @Test
  public void mapEntityToDto_shouldMapDate() {
    LocalDateTime expected = LocalDateTime.now();
    note.setDate(expected);
    noteDto = noteMapper.mapEntityToDto(note);
    assertEquals(expected, noteDto.getDate());
  }

  @Test
  public void mapEntitiesToDtos_shouldMapNoteEntitiesToDtos() {
    note.setId(12L);
    Collection<Note> expected = new ArrayList<>() {
      {
        add(note);
        add(note);
      }
    };
    Collection<NoteDto> actual = noteMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getNoteId());
  }
  // endregion

  // region DtoToEntity-Tests
  @Test
  public void mapDtoToEntity_shouldMapBody() {
    String expected = "test";
    noteDto.setNoteBody(expected);
    note = noteMapper.mapDtoToEntity(noteDto);
    assertEquals(expected, note.getText());
  }

  @Test
  public void mapDtoToEntity_shouldMapId() {
    Long expected = 5L;
    noteDto.setNoteId(expected);
    note = noteMapper.mapDtoToEntity(noteDto);
    assertEquals(expected, note.getId());
  }

  @Test
  public void mapDtoToEntity_shouldMapParent() {
    LocalDateTime expected = LocalDateTime.now();
    noteDto.setDate(expected);
    note = noteMapper.mapDtoToEntity(noteDto);
    assertEquals(expected, note.getDate());
  }

  @Test
  public void mapDtosToEntities_shouldMapNoteDtosToEntities() {
    noteDto.setNoteId(12L);
    Collection<NoteDto> expected = new ArrayList<>() {
      {
        add(noteDto);
        add(noteDto);
      }
    };
    Collection<Note> actual = noteMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getNoteId(), actual.stream().findFirst().orElseThrow().getId());
  }
  // endregion
}
