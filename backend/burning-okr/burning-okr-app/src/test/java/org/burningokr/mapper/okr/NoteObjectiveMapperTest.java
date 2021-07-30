package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.NoteObjectiveDto;
import org.burningokr.model.okr.NoteObjective;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

// ToDo (C.K implement)
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

    }

    @Test
    public void mapDtoToEntity_expects_TextIsMapped() {

    }

    @Test
    public void mapDtoToEntity_expects_DateIsMapped() {

    }

    @Test
    public void mapDtoToEntity_expects_UserIdIsMapped() {

    }

    @Test
    public void mapDtoToEntity_expects_ParentObjectiveIsMapped() {

    }
}