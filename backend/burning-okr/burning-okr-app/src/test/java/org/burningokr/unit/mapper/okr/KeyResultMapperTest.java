package org.burningokr.unit.mapper.okr;

import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.KeyResultMilestoneDto;
import org.burningokr.mapper.okr.KeyResultMapper;
import org.burningokr.mapper.okr.KeyResultMilestoneMapper;
import org.burningokr.model.okr.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class KeyResultMapperTest {

  private KeyResult keyResult;
  private KeyResultDto keyResultDto;
  private KeyResultMapper keyResultMapper;

  @BeforeEach
  public void init() {
    this.keyResultDto = new KeyResultDto();
    this.keyResult = new KeyResult();
    Objective parentObjective = new Objective();
    this.keyResult.setParentObjective(parentObjective);
    this.keyResultMapper = new KeyResultMapper(new KeyResultMilestoneMapper());
  }

  // region EntityToDto
  @Test
  public void mapEntityToDto_expectsIdIsMapped() {
    Long expected = 15L;
    keyResult.setId(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    assertEquals(expected, keyResultDto.getId());
  }

  @Test
  public void mapEntitiyToDto_shouldMapParentObjective() {
    Long expected = 5L;
    Objective parentObjective = new Objective();
    parentObjective.setId(expected);
    keyResult.setParentObjective(parentObjective);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    assertEquals(expected, keyResultDto.getParentObjectiveId());
  }

  @Test
  public void mapEntityToDto_shouldMapTitle() {
    String expected = "Beef";
    keyResult.setName(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    assertEquals(expected, keyResultDto.getTitle());
  }

  @Test
  public void mapEntityToDto_shouldMapDescription() {
    String expected = "Beef";
    keyResult.setDescription(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    assertEquals(expected, keyResultDto.getDescription());
  }

  @Test
  public void mapEntityToDto_shouldMapStartValue() {
    long expected = 1337L;
    keyResult.setStartValue(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    assertEquals(expected, keyResultDto.getStartValue());
  }

  @Test
  public void mapEntityToDto_shouldMapCurrentValue() {
    long expected = 25L;
    keyResult.setCurrentValue(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    assertEquals(expected, keyResultDto.getCurrentValue());
  }

  @Test
  public void mapEntityToDto_shouldMapTargetValue() {
    long expected = 15L;
    keyResult.setTargetValue(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    assertEquals(expected, keyResultDto.getTargetValue());
  }

  @Test
  public void mapEntityToDto_shouldMapNotes() {
    int expected = 5;
    Collection<NoteKeyResult> notesKeyResult = new ArrayList<>() {
      {
        add(new NoteKeyResult());
        add(new NoteKeyResult());
        add(new NoteKeyResult());
        add(new NoteKeyResult());
        add(new NoteKeyResult());
      }
    };
    keyResult.setNotes(notesKeyResult);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);
    assertEquals(expected, keyResultDto.getNoteIds().size());
  }

  @Test
  public void mapEntityToDto_shouldMapEnumToZero() {
    Unit expected = Unit.NUMBER;
    keyResult.setUnit(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);

    assertEquals(expected, keyResultDto.getUnit());
  }

  @Test
  public void mapEntityToDto_shouldMapEnumToOne() {
    Unit expected = Unit.PERCENT;
    keyResult.setUnit(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);

    assertEquals(expected, keyResultDto.getUnit());
  }

  @Test
  public void mapEntityToDto_shouldMapEnumToTwo() {
    Unit expected = Unit.EURO;
    keyResult.setUnit(expected);
    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);

    assertEquals(expected, keyResultDto.getUnit());
  }

  @Test
  public void mapEntityToDto_shouldMapEmptyKeyResultMilestones() {
    Collection<KeyResultMilestone> milestones = new ArrayList<>();

    keyResult.setMilestones(milestones);

    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);

    assertTrue(keyResultDto.getKeyResultMilestoneDtos().isEmpty());
  }

  @Test
  public void mapEntityToDto_shouldMapKeyResultMilestones() {
    Long expectedId = 18L;

    Collection<KeyResultMilestone> milestones = new ArrayList<>();
    KeyResultMilestone milestone = new KeyResultMilestone();
    milestone.setId(expectedId);
    milestone.setParentKeyResult(keyResult);
    milestones.add(milestone);

    keyResult.setMilestones(milestones);

    keyResultDto = keyResultMapper.mapEntityToDto(keyResult);

    assertFalse(keyResultDto.getKeyResultMilestoneDtos().isEmpty());
    assertTrue(
      keyResultDto.getKeyResultMilestoneDtos().stream()
        .anyMatch(keyResultDto -> keyResultDto.getId().equals(expectedId)));
  }

  @Test
  public void mapEntitiesToDtos_shouldMapKeyResultEntitiesToDtos() {
    keyResult.setId(12L);
    Collection<KeyResult> expected = new ArrayList<>() {
      {
        add(keyResult);
        add(keyResult);
      }
    };
    Collection<KeyResultDto> actual = keyResultMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getId());
  }
  // endregion

  // region DtoToEntity
  @Test
  public void mapDtoToEntity_shouldMapId() {
    Long expected = 15L;
    keyResultDto.setId(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertEquals(expected, keyResult.getId());
  }

  @Test
  public void mapDtoToEntity_shouldExpectThatParentObjectiveIsNull() {
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertNull(keyResult.getParentObjective());
  }

  @Test
  public void mapDtoToEntity_shouldMapParentObjective() {
    Long expected = 1337L;
    keyResultDto.setParentObjectiveId(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertEquals(expected, keyResult.getParentObjective().getId());
  }

  @Test
  public void mapDtoToEntity_shouldMapTitle() {
    String expected = "Beef";
    keyResultDto.setTitle(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertEquals(expected, keyResult.getName());
  }

  @Test
  public void mapDtoToEntity_shouldMapDescription() {
    String expected = "Beef";
    keyResultDto.setDescription(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertEquals(expected, keyResult.getDescription());
  }

  @Test
  public void mapDtoToEntity_shouldMapStartValue() {
    long expected = 1337L;
    keyResultDto.setStartValue(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertEquals(expected, keyResult.getStartValue());
  }

  @Test
  public void mapDtoToEntity_shouldMapCurrentValue() {
    long expected = 25L;
    keyResultDto.setCurrentValue(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertEquals(expected, keyResult.getCurrentValue());
  }

  @Test
  public void mapDtoToEntity_shouldMapTargetValue() {
    long expected = 15L;
    keyResultDto.setTargetValue(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertEquals(expected, keyResult.getTargetValue());
  }

  @Test
  public void mapDtoToEntity_shouldExpectNotesNotNull() {
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertNotNull(keyResult);
  }

  @Test
  public void mapDtoToEntity_shouldMapEnumToZero() {
    Unit expected = Unit.NUMBER;
    keyResultDto.setUnit(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertEquals(expected, keyResult.getUnit());
  }

  @Test
  public void mapDtoToEntity_shouldMapEnumToOne() {
    Unit expected = Unit.EURO;
    keyResultDto.setUnit(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertEquals(expected, keyResult.getUnit());
  }

  @Test
  public void mapDtoToEntity_shouldMapEnumToTwo() {
    Unit expected = Unit.PERCENT;
    keyResultDto.setUnit(expected);
    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);
    assertEquals(expected, keyResult.getUnit());
  }

  @Test
  public void mapDtoToentity_shouldMapEmptyKeyResultMilestones() {
    Collection<KeyResultMilestoneDto> milestonesDtos = new ArrayList<>();

    keyResultDto.setKeyResultMilestoneDtos(milestonesDtos);

    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);

    assertTrue(keyResultDto.getKeyResultMilestoneDtos().isEmpty());
  }

  @Test
  public void mapDtoToEntity_shouldMapKeyResultMilestones() {
    Long expectedId = 18L;

    Collection<KeyResultMilestoneDto> milestoneDtos = new ArrayList<>();
    KeyResultMilestoneDto milestoneDto = new KeyResultMilestoneDto();
    keyResult.setId(10L);
    milestoneDto.setId(expectedId);
    milestoneDto.setParentKeyResultId(keyResult.getId());
    milestoneDtos.add(milestoneDto);

    keyResultDto.setKeyResultMilestoneDtos(milestoneDtos);

    keyResult = keyResultMapper.mapDtoToEntity(keyResultDto);

    assertFalse(keyResult.getMilestones().isEmpty());
    assertTrue(
      keyResult.getMilestones().stream()
        .anyMatch(keyResult -> keyResult.getId().equals(expectedId)));
  }

  @Test
  public void mapDtosToEntities_shouldMapKeyResultDtosToEntities() {
    keyResultDto.setId(12L);
    Collection<KeyResultDto> expected = new ArrayList<>() {
      {
        add(keyResultDto);
        add(keyResultDto);
      }
    };
    Collection<KeyResult> actual = keyResultMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getId());
  }
  // endregion
}
