package org.burningokr.mapper.okr;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.NoteObjective;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObjectiveMapperTest {

  private Objective objective;
  private ObjectiveDto objectiveDto;
  private ObjectiveMapper objectiveMapper;

  @Before
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
    Assert.assertEquals(expected, objectiveDto.getId());
  }

  @Test
  public void test_mapEntityToDto_expects_parentCompanyIsMapped() {
    Long expected = 1L;
    OkrCompany parentOkrCompany = new OkrCompany();
    parentOkrCompany.setId(expected);
    objective.setParentOkrUnit(parentOkrCompany);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertEquals(expected, objectiveDto.getParentUnitId());
  }

  @Test
  public void test_mapEntityToDto_expects_parentDepartmentIsMapped() {
    Long expected = 2L;
    OkrDepartment parentOkrDepartment = new OkrDepartment();
    parentOkrDepartment.setId(expected);
    objective.setParentOkrUnit(parentOkrDepartment);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertEquals(expected, objectiveDto.getParentUnitId());
  }

  @Test
  public void test_mapEntityToDto_expects_parentObjectiveIsNullFromNull() {
    objective.setParentObjective(null);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertNull(objectiveDto.getParentObjectiveId());
  }

  @Test
  public void test_mapEntityToDto_expects_parentObjectiveIsMappedNotNullFromNotNull() {
    Long expected = 4L;
    Objective parentObjective = new Objective();
    parentObjective.setId(expected);
    objective.setParentObjective(parentObjective);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertEquals(expected, objectiveDto.getParentObjectiveId());
  }

  @Test
  public void test_mapEntityToDto_expects_titleIsMapped() {
    String expected = "test";
    objective.setName(expected);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertEquals(expected, objectiveDto.getTitle());
  }

  @Test
  public void test_mapEntityToDto_expects_descriptionIsMapped() {
    String expected = "test";
    objective.setDescription(expected);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertEquals(expected, objectiveDto.getDescription());
  }

  @Test
  public void test_mapEntityToDto_expects_remarkIsMapped() {
    String expected = "test";
    objective.setRemark(expected);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertEquals(expected, objectiveDto.getRemark());
  }

  @Test
  public void mapEntityToDto_expects_notesAreMapped() {
    Collection<NoteObjective> noteObjectives = new ArrayList<>();
    NoteObjective noteObjective = new NoteObjective();
    noteObjective.setId(1L);
    noteObjectives.add(noteObjective);
    objective.setNotes(noteObjectives);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertEquals(noteObjective.getId(), objectiveDto.getNoteIds().toArray()[0]);
  }

  @Test
  public void mapEntityToDto_expectsContactPersonIsMapped() {
    String expectedId = "1337";
    objective.setContactPersonId(expectedId);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertEquals(expectedId, objectiveDto.getContactPersonId());
  }

  private void mapEntityToDto_isActiveTest(boolean isActive) {
    objective.setActive(isActive);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertEquals(isActive, objectiveDto.getIsActive());
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
    Assert.assertEquals(expected, objectiveDto.getSubObjectiveIds().size());
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
    Assert.assertEquals(expected, objectiveDto.getKeyResultIds().size());
  }

  @Test
  public void test_mapEntityToDto_expects_reviewIsMapped() {
    String expected = "test";
    objective.setReview(expected);
    objectiveDto = objectiveMapper.mapEntityToDto(objective);
    Assert.assertEquals(expected, objectiveDto.getReview());
  }
  // endregion

  // region Tests DtoToEntity
  @Test
  public void test_mapDtoToEntity_expects_idIsMapped() {
    Long expected = 7L;
    objectiveDto.setId(7L);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertEquals(expected, objective.getId());
  }

  @Test
  public void mapDtoToEntity_expects_notesAreMapped() {
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertArrayEquals(
        new ArrayList<NoteObjective>().toArray(), objective.getNotes().toArray());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentUnitIsNull() {
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertNull(objective.getParentOkrUnit());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentUnitIsMapped() {
    Long expected = 15L;
    objectiveDto.setParentUnitId(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertEquals(expected, objective.getParentOkrUnit().getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentObjectiveIsNull() {
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertNull(objective.getParentObjective());
  }

  @Test
  public void test_mapDtoToEntity_expects_expectsParentObjectiveIsMapped() {
    Long expected = 14L;
    objectiveDto.setParentObjectiveId(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertEquals(expected, objective.getParentObjective().getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_titleIsMapped() {
    String expected = "test";
    objectiveDto.setTitle(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertEquals(expected, objective.getName());
  }

  @Test
  public void test_mapDtoToEntity_expects_descriptionIsMapped() {
    String expected = "desc";
    objectiveDto.setDescription(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertEquals(expected, objective.getDescription());
  }

  @Test
  public void test_mapDtoToEntity_expects_remarkIsMapped() {
    String expected = "remark";
    objectiveDto.setRemark(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertEquals(expected, objective.getRemark());
  }

  @Test
  public void mapDtoToEntity_expectsContactPersonIdIsMapped() {
    String expected = "1337123";
    objectiveDto.setContactPersonId(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertEquals(expected, objective.getContactPersonId());
  }

  private void mapDtoToEntity_isActiveTest(boolean expectedIsActive) {
    objectiveDto.setIsActive(expectedIsActive);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertEquals(expectedIsActive, objective.isActive());
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
    Assert.assertNotNull(objective.getSubObjectives());
  }

  @Test
  public void test_mapDtoToEntity_expects_keyResultsNotNull() {
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertNotNull(objective.getKeyResults());
  }

  @Test
  public void test_mapDtoToEntity_expects_reviewIsMapped() {
    String expected = "bad review";
    objectiveDto.setReview(expected);
    objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    Assert.assertEquals(expected, objective.getReview());
  }
  // endregion
}
