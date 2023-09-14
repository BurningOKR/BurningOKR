package org.burningokr.unit.mapper.okr;

import org.burningokr.dto.okr.NoteObjectiveDto;
import org.burningokr.mapper.okr.NoteObjectiveMapper;
import org.burningokr.model.okr.NoteObjective;
import org.burningokr.model.okr.Objective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoteObjectiveMapperTest {

  private NoteObjective noteObjective;
  private NoteObjectiveDto noteObjectiveDto;
  private NoteObjectiveMapper noteObjectiveMapper;

  @BeforeEach
  public void reset() {
    noteObjective = new NoteObjective();
    noteObjectiveDto = new NoteObjectiveDto();
    noteObjectiveMapper = new NoteObjectiveMapper();
  }

  // Dto to Entity
  @Test
  public void mapDtoToEntity_expects_IdIsMapped() {
    Long expected = 15L;
    noteObjectiveDto.setNoteId(expected);
    noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
    assertEquals(expected, noteObjective.getId());
  }

  @Test
  public void mapDtoToEntity_expects_TextIsMapped() {
    String expected = "An Example for a text";
    noteObjectiveDto.setNoteBody(expected);
    noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
    assertEquals(expected, noteObjective.getText());
  }

  @Test
  public void mapDtoToEntity_expects_DateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteObjectiveDto.setDate(expected);
    noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
    assertEquals(expected, noteObjective.getDate());
  }

  @Test
  public void mapDtoToEntity_expects_UserIdIsMapped() {
    UUID expected = UUID.randomUUID();
    noteObjectiveDto.setUserId(expected);
    noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
    assertEquals(expected, noteObjective.getUserId());
  }

  @Test
  public void mapDtoToEntity_expects_ParentObjectiveIsMapped() {
    Long expected = 234L;
    noteObjectiveDto.setParentObjectiveId(expected);
    noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
    assertEquals(expected, noteObjective.getParentObjective().getId());
  }

  // Entity to Dto

  @Test
  public void mapEntityToDto_expectsNoteIdIsMapped() {
    Long expected = 123L;
    noteObjective.setId(expected);
    noteObjectiveDto = noteObjectiveMapper.mapEntityToDto(noteObjective);
    assertEquals(expected, noteObjectiveDto.getNoteId());
  }

  @Test
  public void mapEntityToDto_expectsNoteBodyIsMapped() {
    String expected = "Example Text";
    noteObjective.setText(expected);
    noteObjectiveDto = noteObjectiveMapper.mapEntityToDto(noteObjective);
    assertEquals(expected, noteObjectiveDto.getNoteBody());
  }

  @Test
  public void mapEntityToDto_expectsDateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteObjective.setDate(expected);
    noteObjectiveDto = noteObjectiveMapper.mapEntityToDto(noteObjective);
    assertEquals(expected, noteObjectiveDto.getDate());
  }

  @Test
  public void mapEntityToDto_expectsUserIdIsMapped() {
    UUID expected = UUID.randomUUID();
    noteObjective.setUserId(expected);
    noteObjectiveDto = noteObjectiveMapper.mapEntityToDto(noteObjective);
    assertEquals(expected, noteObjectiveDto.getUserId());
  }

  @Test
  public void mapEntityToDto_expectsParentObjectiveIsMapped() {
    Objective expected = new Objective();
    noteObjective.setParentObjective(expected);
    noteObjectiveDto = noteObjectiveMapper.mapEntityToDto(noteObjective);
    assertEquals(expected.getId(), noteObjectiveDto.getParentObjectiveId());
  }

  @Test
  public void mapDtosToEntities_shouldMapNoteDtosToEntities() {
    noteObjectiveDto.setNoteId(12L);
    Collection<NoteObjectiveDto> expected = new ArrayList<>() {
      {
        add(noteObjectiveDto);
        add(noteObjectiveDto);
      }
    };
    Collection<NoteObjective> actual = noteObjectiveMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getNoteId(), actual.stream().findFirst().orElseThrow().getId());
  }

  @Test
  public void mapDtosToEntities_shouldHandleEmptyList() {
    Collection<NoteObjectiveDto> expected = new ArrayList<>() {};
    Collection<NoteObjective> actual = noteObjectiveMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
  }

  @Test
  public void mapEntitiesToDtos_shouldMapNoteEntitiesToDtos() {
    noteObjective.setId(12L);
    Collection<NoteObjective> expected = new ArrayList<>() {
      {
        add(noteObjective);
        add(noteObjective);
      }
    };
    Collection<NoteObjectiveDto> actual = noteObjectiveMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getNoteId());
  }

  @Test
  public void mapEntitiesToDtos_shouldHandleEmptyList() {
    Collection<NoteObjective> expected = new ArrayList<>() {};
    Collection<NoteObjectiveDto> actual = noteObjectiveMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
  }
}
