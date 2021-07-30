package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteObjectiveDto;
import org.burningokr.model.okr.NoteObjective;
import org.burningokr.model.okr.Objective;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;


public class NoteObjectiveMapperTest {

    private NoteObjective noteObjective;
    private NoteObjectiveDto noteObjectiveDto;
    private NoteObjectiveMapper noteObjectiveMapper;

    @Before
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
        Assert.assertEquals(expected, noteObjective.getId());
    }

    @Test
    public void mapDtoToEntity_expects_TextIsMapped() {
        String expected = "An Example for a text";
        noteObjectiveDto.setNoteBody(expected);
        noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
        Assert.assertEquals(expected, noteObjective.getText());
    }

    @Test
    public void mapDtoToEntity_expects_DateIsMapped() {
        LocalDateTime expected = LocalDateTime.now();
        noteObjectiveDto.setDate(expected);
        noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
        Assert.assertEquals(expected, noteObjective.getDate());
    }

    @Test
    public void mapDtoToEntity_expects_UserIdIsMapped() {
        UUID expected = UUID.randomUUID();
        noteObjectiveDto.setUserId(expected);
        noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
        Assert.assertEquals(expected, noteObjective.getUserId());
    }

    @Test
    public void mapDtoToEntity_expects_ParentObjectiveIsMapped() {
        Long expected = 234L;
        noteObjectiveDto.setParentObjectiveId(expected);
        noteObjective = noteObjectiveMapper.mapDtoToEntity(noteObjectiveDto);
        Assert.assertEquals(expected, noteObjective.getParentObjective().getId());
    }

    // Entity to Dto

    @Test
    public void mapEntityToDto_expectsNoteIdIsMappes(){
        Long expected = 123L;
        noteObjective.setId(expected);
        noteObjectiveDto = noteObjectiveMapper.mapEntityToDto(noteObjective);
        Assert.assertEquals(expected, noteObjectiveDto.getNoteId());
    }

    @Test
    public void mapEntityToDto_expectsNoteBodyIsMappes() {
        String expected = "Example Text";
        noteObjective.setText(expected);
        noteObjectiveDto = noteObjectiveMapper.mapEntityToDto(noteObjective);
        Assert.assertEquals(expected, noteObjectiveDto.getNoteBody());
    }

    @Test
    public void mapEntityToDto_expectsDateIsMapped() {
        LocalDateTime expected = LocalDateTime.now();
        noteObjective.setDate(expected);
        noteObjectiveDto = noteObjectiveMapper.mapEntityToDto(noteObjective);
        Assert.assertEquals(expected, noteObjectiveDto.getDate());
    }

    @Test
    public void mapEntityToDto_expectsUserIdIsMapped() {
        UUID expected = UUID.randomUUID();
        noteObjective.setUserId(expected);
        noteObjectiveDto = noteObjectiveMapper.mapEntityToDto(noteObjective);
        Assert.assertEquals(expected, noteObjectiveDto.getUserId());
    }

    @Test
    public void mapEntityToDto_expectsParentObjectiveIsMapped() {
        Objective expected = new Objective();
        noteObjective.setParentObjective(expected);
        noteObjectiveDto = noteObjectiveMapper.mapEntityToDto(noteObjective);
        Assert.assertEquals(expected.getId(), noteObjectiveDto.getParentObjectiveId());
    }
}