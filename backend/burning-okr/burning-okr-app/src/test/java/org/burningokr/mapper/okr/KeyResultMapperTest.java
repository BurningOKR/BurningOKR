package org.burningokr.mapper.okr;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.Unit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KeyResultMapperTest {

  private KeyResult keyResult;
  private KeyResultDto keyResultDto;
  private KeyResultMapper keyResultMapper;

  @Before
  public void init() {
    this.keyResultDto = new KeyResultDto();
    this.keyResult = new KeyResult();
    Objective parentObjective = new Objective();
    this.keyResult.setParentObjective(parentObjective);
    this.keyResultMapper = new KeyResultMapper();
  }

  // region EntityToDto
  @Test
  public void test_mapEntityToDto_expectsIdIsMapped() {
    Long expected = 15L;
    keyResult.setId(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    Assert.assertEquals(expected, keyResultDto.getId());
  }

  @Test
  public void test_mapEntitiyToDto_expectsParentObjectiveIsMapped() {
    Long expected = 5L;
    Objective parentObjective = new Objective();
    parentObjective.setId(expected);
    keyResult.setParentObjective(parentObjective);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    Assert.assertEquals(expected, keyResultDto.getParentObjectiveId());
  }

  @Test
  public void test_mapEntityToDto_expectsTitleIsMapped() {
    String expected = "Beef";
    keyResult.setName(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    Assert.assertEquals(expected, keyResultDto.getTitle());
  }

  @Test
  public void test_mapEntityToDto_expectsDescriptionIsMapped() {
    String expected = "Beef";
    keyResult.setDescription(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    Assert.assertEquals(expected, keyResultDto.getDescription());
  }

  @Test
  public void test_mapEntityToDto_expectsStartValueIsMapped() {
    long expected = 1337L;
    keyResult.setStartValue(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    Assert.assertEquals(expected, keyResultDto.getStartValue());
  }

  @Test
  public void test_mapEntityToDto_expectsCurrentValueIsSet() {
    long expected = 25L;
    keyResult.setCurrentValue(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    Assert.assertEquals(expected, keyResultDto.getCurrentValue());
  }

  @Test
  public void test_mapEntityToDto_expectsTargetValueIsSet() {
    long expected = 15L;
    keyResult.setTargetValue(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    Assert.assertEquals(expected, keyResultDto.getTargetValue());
  }

  @Test
  public void test_mapEntityToDto_expectsNotesSizeIsEqual() {
    int expected = 5;
    Collection<Note> notes = new ArrayList<>();
    for (int i = 1; i <= expected; i++) {
      notes.add(new Note());
    }
    keyResult.setNotes(notes);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    Assert.assertEquals(expected, keyResultDto.getNoteIds().size());
  }

  @Test
  public void test_mapEntityToDto_expectsEnumIsMappedToZero() {
    Unit expected = Unit.NUMBER;
    keyResult.setUnit(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);

    Assert.assertEquals(expected, keyResultDto.getUnit());
  }

  @Test
  public void test_mapEntityToDto_expectsEnumIsMappedToOne() {
    Unit expected = Unit.PERCENT;
    keyResult.setUnit(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);

    Assert.assertEquals(expected, keyResultDto.getUnit());
  }

  @Test
  public void test_mapEntityToDto_expectsEnumIsMappedToTwo() {
    Unit expected = Unit.EURO;
    keyResult.setUnit(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);

    Assert.assertEquals(expected, keyResultDto.getUnit());
  }
  // endregion

  // region DtoToEntity
  @Test
  public void test_mapDtoToEntity_expectsIdIsMapped() {
    Long expected = 15L;
    keyResultDto.setId(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertEquals(expected, keyResult.getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentIsNull() {
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertNull(keyResult.getParentObjective());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentIsMapped() {
    Long expected = 1337L;
    keyResultDto.setParentObjectiveId(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertEquals(expected, keyResult.getParentObjective().getId());
  }

  @Test
  public void test_mapDtoToEntity_expectsTitleIsMapped() {
    String expected = "Beef";
    keyResultDto.setTitle(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertEquals(expected, keyResult.getName());
  }

  @Test
  public void test_mapDtoToEntity_expectsDescriptionIsMapped() {
    String expected = "Beef";
    keyResultDto.setDescription(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertEquals(expected, keyResult.getDescription());
  }

  @Test
  public void test_mapDtoToEntity_expectsStartValueIsMapped() {
    long expected = 1337L;
    keyResultDto.setStartValue(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertEquals(expected, keyResult.getStartValue());
  }

  @Test
  public void test_mapDtoToEntity_expectsCurrentValueIsMapped() {
    long expected = 25L;
    keyResultDto.setCurrentValue(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertEquals(expected, keyResult.getCurrentValue());
  }

  @Test
  public void test_mapDtoToEntity_expectsTargetValueIsMapped() {
    long expected = 15L;
    keyResultDto.setTargetValue(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertEquals(expected, keyResult.getTargetValue());
  }

  @Test
  public void test_mapDtoToEntity_expectsNotesNotNull() {
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertNotNull(keyResult);
  }

  @Test
  public void test_mapDtoToEntity_expectsEnumIsMappedToZero() {
    Unit expected = Unit.NUMBER;
    keyResultDto.setUnit(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertEquals(expected, keyResult.getUnit());
  }

  @Test
  public void test_mapDtoToEntity_expectsEnumIsMappedToOne() {
    Unit expected = Unit.EURO;
    keyResultDto.setUnit(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertEquals(expected, keyResult.getUnit());
  }

  @Test
  public void test_mapDtoToEntity_expectsEnumIsMappedToTwo() {
    Unit expected = Unit.PERCENT;
    keyResultDto.setUnit(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    Assert.assertEquals(expected, keyResult.getUnit());
  }
  // endregion
}
