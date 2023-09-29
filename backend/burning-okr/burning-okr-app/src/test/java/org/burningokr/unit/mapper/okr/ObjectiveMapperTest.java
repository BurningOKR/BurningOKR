package org.burningokr.unit.mapper.okr;

import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.mapper.okr.ObjectiveMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteObjective;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectiveMapperTest {

  private Objective objective;
  private ObjectiveDto objectiveDto;
  private ObjectiveMapper objectiveMapper;

  @BeforeEach
  public void reset() {
    this.objectiveDto = new ObjectiveDto();
    this.objective = new Objective();
    OkrDepartment parentOkrDepartment = new OkrDepartment();
    this.objective.setParentOkrUnit(parentOkrDepartment);
    Objective parentObjective = new Objective();
    this.objective.setParentObjective(parentObjective);
    this.objectiveMapper = new ObjectiveMapper();
  }

  // region Tests EntityToDto
  @Test
  public void test_mapEntityToDto_expects_idIsMapped() {
    Long expected = 7L;
    objective.setId(7L);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expected, objectiveDto.getId());
  }

  @Test
  public void test_mapEntityToDto_expects_parentCompanyIsMapped() {
    Long expected = 1L;
    OkrCompany parentOkrCompany = new OkrCompany();
    parentOkrCompany.setId(expected);
    objective.setParentOkrUnit(parentOkrCompany);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expected, objectiveDto.getParentUnitId());
  }

  @Test
  public void test_mapEntityToDto_expects_parentDepartmentIsMapped() {
    Long expected = 2L;
    OkrDepartment parentOkrDepartment = new OkrDepartment();
    parentOkrDepartment.setId(expected);
    objective.setParentOkrUnit(parentOkrDepartment);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expected, objectiveDto.getParentUnitId());
  }

  @Test
  public void test_mapEntityToDto_expects_parentObjectiveIsNullFromNull() {
    objective.setParentObjective(null);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertNull(objectiveDto.getParentObjectiveId());
  }

  @Test
  public void test_mapEntityToDto_expects_parentObjectiveIsMappedNotNullFromNotNull() {
    Long expected = 4L;
    Objective parentObjective = new Objective();
    parentObjective.setId(expected);
    objective.setParentObjective(parentObjective);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expected, objectiveDto.getParentObjectiveId());
  }

  @Test
  public void test_mapEntityToDto_expects_titleIsMapped() {
    String expected = "test";
    objective.setName(expected);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expected, objectiveDto.getTitle());
  }

  @Test
  public void test_mapEntityToDto_expects_descriptionIsMapped() {
    String expected = "test";
    objective.setDescription(expected);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expected, objectiveDto.getDescription());
  }

  @Test
  public void test_mapEntityToDto_expects_remarkIsMapped() {
    String expected = "test";
    objective.setRemark(expected);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expected, objectiveDto.getRemark());
  }

  @Test
  public void mapEntityToDto_expects_notesAreMapped() {
    Collection<NoteObjective> noteObjectives = new ArrayList<>();
    NoteObjective noteObjective = new NoteObjective();
    noteObjective.setId(1L);
    noteObjectives.add(noteObjective);
    objective.setNotes(noteObjectives);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(noteObjective.getId(), objectiveDto.getNoteIds().toArray()[0]);
  }

  @Test
  public void mapEntityToDto_expectsContactPersonIsMapped() {
    String expectedId = "1337";
    objective.setContactPersonId(expectedId);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expectedId, objectiveDto.getContactPersonId());
  }

  private void mapEntityToDto_isActiveTest(boolean isActive) {
    objective.setActive(isActive);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(isActive, objectiveDto.getIsActive());
  }

  @Test
  public void mapEntityToDto_expectsIsActiveTrue() {
    mapEntityToDto_isActiveTest(true);
  }

  @Test
  public void mapEntityToDto_expectsIsActiveFalse() {
    mapEntityToDto_isActiveTest(false);
  }

  @Test
  public void test_mapEntityToDto_expects_subobjectivesSizeIsEqual() {
    int expected = 4;
    Collection<Objective> subobjectives = new ArrayList<>();
    for (int i = 1; i <= expected; i++) {
      subobjectives.add(new Objective());
    }
    objective.setSubObjectives(subobjectives);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expected, objectiveDto.getSubObjectiveIds().size());
  }

  @Test
  public void test_mapEntityToDto_expects_keyResultsSizeIsEqual() {
    int expected = 7;
    Collection<KeyResult> keyResults = new ArrayList<>();
    for (int i = 1; i <= expected; i++) {
      keyResults.add(new KeyResult());
    }
    objective.setKeyResults(keyResults);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expected, objectiveDto.getKeyResultIds().size());
  }

  @Test
  public void test_mapEntityToDto_expects_reviewIsMapped() {
    String expected = "test";
    objective.setReview(expected);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    assertEquals(expected, objectiveDto.getReview());
  }

  @Test
  public void mapEntitiesToDtos_shouldMapNoteEntitiesToDtos() {
    objective.setId(12L);
    Collection<Objective> expected = new ArrayList<>() {
      {
        add(objective);
        add(objective);
      }
    };
    Collection<ObjectiveDto> actual = objectiveMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getId());
  }

  @Test
  public void mapEntitiesToDtos_shouldHandleEmptyList() {
    Collection<Objective> expected = new ArrayList<>() {};
    Collection<ObjectiveDto> actual = objectiveMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
  }
  // endregion

  // region Tests DtoToEntity
  @Test
  public void test_mapDtoToEntity_expects_idIsMapped() {
    Long expected = 7L;
    objectiveDto.setId(7L);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertEquals(expected, objective.getId());
  }

  @Test
  public void mapDtoToEntity_expects_notesAreMapped() {
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertArrayEquals(
      new ArrayList<NoteObjective>().toArray(), objective.getNotes().toArray());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentUnitIsNull() {
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertNull(objective.getParentOkrUnit());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentUnitIsMapped() {
    Long expected = 15L;
    objectiveDto.setParentUnitId(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertEquals(expected, objective.getParentOkrUnit().getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentObjectiveIsNull() {
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertNull(objective.getParentObjective());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentObjectiveIsMapped() {
    Long expected = 14L;
    objectiveDto.setParentObjectiveId(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertEquals(expected, objective.getParentObjective().getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_titleIsMapped() {
    String expected = "test";
    objectiveDto.setTitle(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertEquals(expected, objective.getName());
  }

  @Test
  public void test_mapDtoToEntity_expects_descriptionIsMapped() {
    String expected = "desc";
    objectiveDto.setDescription(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertEquals(expected, objective.getDescription());
  }

  @Test
  public void test_mapDtoToEntity_expects_remarkIsMapped() {
    String expected = "remark";
    objectiveDto.setRemark(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertEquals(expected, objective.getRemark());
  }

  @Test
  public void mapDtoToEntity_expectsContactPersonIdIsMapped() {
    String expected = "1337123";
    objectiveDto.setContactPersonId(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertEquals(expected, objective.getContactPersonId());
  }

  private void mapDtoToEntity_isActiveTest(boolean expectedIsActive) {
    objectiveDto.setIsActive(expectedIsActive);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertEquals(expectedIsActive, objective.isActive());
  }

  @Test
  public void mapDtoToEntity_expectsIsActiveTrue() {
    mapDtoToEntity_isActiveTest(true);
  }

  @Test
  public void mapDtoToEntity_expectsIsActiveFalse() {
    mapDtoToEntity_isActiveTest(false);
  }

  @Test
  public void test_mapDtoToEntity_expects_subobjectivesNotNull() {
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertNotNull(objective.getSubObjectives());
  }

  @Test
  public void test_mapDtoToEntity_expects_keyResultsNotNull() {
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertNotNull(objective.getKeyResults());
  }

  @Test
  public void test_mapDtoToEntity_expects_reviewIsMapped() {
    String expected = "bad review";
    objectiveDto.setReview(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    assertEquals(expected, objective.getReview());
  }

  @Test
  public void mapDtosToEntities_shouldMapNoteDtosToEntities() {
    objectiveDto.setId(12L);
    Collection<ObjectiveDto> expected = new ArrayList<>() {
      {
        add(objectiveDto);
        add(objectiveDto);
      }
    };
    Collection<Objective> actual = objectiveMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getId());
  }

  @Test
  public void mapDtosToEntities_shouldHandleEmptyList() {
    Collection<ObjectiveDto> expected = new ArrayList<>() {};
    Collection<Objective> actual = objectiveMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
  }
  // endregion
}