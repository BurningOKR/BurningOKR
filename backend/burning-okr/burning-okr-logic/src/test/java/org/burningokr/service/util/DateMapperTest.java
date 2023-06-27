package org.burningokr.service.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DateMapperTest {

    private DateMapper dateMapper;

    @BeforeEach
    public void setUp() {
        dateMapper = new DateMapper();
    }

    @Test
    public void mapDateStringToDate_shouldMapStringToLocalDate() {
        LocalDate expected = LocalDate.of(2020, 3, 1);

        LocalDate actual = dateMapper.mapDateStringToDate(expected.toString());

        assertEquals(expected, actual);
    }

    @Test
    public void mapDateStringToDate_shouldNotMapWhenStringIsNull() {
        String expected = null;

        LocalDate actual = dateMapper.mapDateStringToDate(expected);

        assertNull(actual);
    }

    @Test
    public void mapDateStringToDate_shouldNotMapWhenStringIsEmpty() {
        String expected = "";

        LocalDate actual = dateMapper.mapDateStringToDate(expected);

        assertNull(actual);
    }

    @Test
    public void mapDateStringToDate_shouldNotMapWhenStringIsNonsense() {
        String expected = "dAWDsdd3a22";

        LocalDate actual = dateMapper.mapDateStringToDate(expected);

        assertNull(actual);
    }

}
