package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteDto;
import org.burningokr.model.okr.Note;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class NoteAbstractMapperTest {

    private class NoteMapper extends NoteAbstractMapper{}

    private Note note;
    private NoteDto noteDto;
    private NoteMapper noteMapper;

    @Before
    public void reset() {
        noteDto = new NoteDto();
        note = new Note();
        noteMapper = new NoteMapper();
    }

    // Dto to Entity

    @Test
    public void mapNoteDtoToEntity_expects_NoteBodyIsMapped() {
        String expected = "test";
        noteDto.setNoteBody(expected);
        Assert.assertEquals(expected, noteMapper.mapNoteDtoToEntity(noteDto).getText());
    }

    @Test
    public void mapNoteDtoToEntity_expects_NoteIdIsMapped() {
        Long expected = 1L;
        noteDto.setNoteId(expected);
        Assert.assertEquals(expected, noteMapper.mapNoteDtoToEntity(noteDto).getId());
    }

    @Test
    public void mapNoteDtoToEntity_expects_UserIdIsMapped() {
        UUID expected = new UUID(1L, 1L);
        noteDto.setUserId(expected);
        Assert.assertEquals(expected, noteMapper.mapNoteDtoToEntity(noteDto).getUserId());
    }

    @Test
    public void mapNoteDtoToEntity_expects_DateIsMapped() {
        LocalDateTime expected = LocalDateTime.now();
        noteDto.setDate(expected);
        Assert.assertEquals(expected, noteMapper.mapNoteDtoToEntity(noteDto).getDate());
    }

    // Entity to Entity


}