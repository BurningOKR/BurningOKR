package org.burningokr.unit.mapper.okr;

import org.burningokr.dto.okr.NoteTopicDraftDto;
import org.burningokr.mapper.okr.NoteTopicDraftMapper;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NoteTopicDraftMapperTest {

  private NoteTopicDraft noteTopicDraft;
  private NoteTopicDraftDto noteTopicDraftDto;
  private NoteTopicDraftMapper noteTopicDraftMapper;

  @BeforeEach
  public void init() {
    this.noteTopicDraft = new NoteTopicDraft();
    this.noteTopicDraftDto = new NoteTopicDraftDto();
    this.noteTopicDraftMapper = new NoteTopicDraftMapper();
  }

  // region DtoToEntity-Tests
  @Test
  public void test_mapDtoToEntity_expectsIdIsMapped() {
    Long expected = 234L;
    noteTopicDraftDto.setNoteId(expected);
    noteTopicDraft = noteTopicDraftMapper.mapDtoToEntity(noteTopicDraftDto);
    assertEquals(expected, noteTopicDraft.getId());
  }

  @Test
  public void test_mapDtoToEntity_expectsTextIsMapped() {
    String expected = "An example for a text";
    noteTopicDraftDto.setNoteBody(expected);
    noteTopicDraft = noteTopicDraftMapper.mapDtoToEntity(noteTopicDraftDto);
    assertEquals(expected, noteTopicDraft.getText());
  }

  @Test
  public void test_mapDtoToEntity_expectsDateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteTopicDraftDto.setDate(expected);
    noteTopicDraft = noteTopicDraftMapper.mapDtoToEntity(noteTopicDraftDto);
    assertEquals(expected, noteTopicDraft.getDate());
  }

  @Test
  public void test_mapDtoToEntity_expectsUserIdIsMapped() {
    UUID expected = UUID.randomUUID();
    noteTopicDraftDto.setUserId(expected);
    noteTopicDraft = noteTopicDraftMapper.mapDtoToEntity(noteTopicDraftDto);
    assertEquals(expected, noteTopicDraft.getUserId());
  }

  @Test
  public void test_mapDtoToEntity_expectsParentTopicDraftIdIsMapped() {
    Long expected = 234L;
    noteTopicDraftDto.setParentTopicDraftId(expected);
    noteTopicDraft = noteTopicDraftMapper.mapDtoToEntity(noteTopicDraftDto);
    assertEquals(expected, noteTopicDraft.getParentTopicDraft().getId());
  }

  @Test
  public void test_mapDtoToEntity_expectsParentTopicDraftIdIsNull() {
    noteTopicDraft = noteTopicDraftMapper.mapDtoToEntity(noteTopicDraftDto);
    assertNull(noteTopicDraft.getParentTopicDraft());
  }
  // endregion

  // region EntityToDto-Tests
  @Test
  public void test_mapEntityToDto_expectsNoteIdIsMapped() {
    Long expected = 1234L;
    noteTopicDraft.setId(expected);
    noteTopicDraftDto = noteTopicDraftMapper.mapEntityToDto(noteTopicDraft);
    assertEquals(expected, noteTopicDraftDto.getNoteId());
  }

  @Test
  public void test_mapEntityToDto_expectsNoteBodyIsMapped() {
    String expected = "An example";
    noteTopicDraft.setText(expected);
    noteTopicDraftDto = noteTopicDraftMapper.mapEntityToDto(noteTopicDraft);
    assertEquals(expected, noteTopicDraftDto.getNoteBody());
  }

  @Test
  public void test_mapEntityToDto_expectsDateIsMapped() {
    LocalDateTime expected = LocalDateTime.now();
    noteTopicDraft.setDate(expected);
    noteTopicDraftDto = noteTopicDraftMapper.mapEntityToDto(noteTopicDraft);
    assertEquals(expected, noteTopicDraftDto.getDate());
  }

  @Test
  public void test_mapEntityToDto_expectsUserIdIsMapped() {
    UUID expected = UUID.randomUUID();
    noteTopicDraft.setUserId(expected);
    noteTopicDraftDto = noteTopicDraftMapper.mapEntityToDto(noteTopicDraft);
    assertEquals(expected, noteTopicDraftDto.getUserId());
  }

  @Test
  public void test_mapEntityToDto_expectsParentTopicDraftIsMapped() {
    OkrTopicDraft expected = new OkrTopicDraft();
    expected.setId(48L);
    noteTopicDraft.setParentTopicDraft(expected);
    noteTopicDraftDto = noteTopicDraftMapper.mapEntityToDto(noteTopicDraft);
    assertEquals(expected.getId(), noteTopicDraftDto.getParentTopicDraftId());
  }

  @Test
  public void test_mapEntityToDto_expectsParentTopicDraftIsNull() {
    noteTopicDraftDto = noteTopicDraftMapper.mapEntityToDto(noteTopicDraft);
    assertNull(noteTopicDraft.getParentTopicDraft());
  }
  // endregion
}
