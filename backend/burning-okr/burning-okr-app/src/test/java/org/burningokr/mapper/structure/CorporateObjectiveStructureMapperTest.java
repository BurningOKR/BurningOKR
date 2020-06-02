package org.burningokr.mapper.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.burningokr.dto.structure.CorporateObjectiveStructureDto;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CorporateObjectiveStructureMapperTest {

  private CorporateObjectiveStructure entity;
  private CorporateObjectiveStructureDto dto;
  private CorporateObjectiveStructureMapper mapper;

  @Before
  public void init() {
    entity = new CorporateObjectiveStructure();
    dto = new CorporateObjectiveStructureDto();
    mapper = new CorporateObjectiveStructureMapper();
  }

  @Test
  public void test_mapEntityToDto_expectIdIsMapped() {
    Long expectedId = 5L;
    entity.setId(expectedId);
    dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(expectedId, dto.getStructureId());
  }

  @Test
  public void test_mapEntityToDto_expectNameIsMapped() {
    String expectedName = "myNameIs";
    entity.setName(expectedName);
    dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(expectedName, dto.getStructureName());
  }

  @Test
  public void test_mapEntityToDto_expectParentStructureIdIsMapped() {
    Long expectedParentStructureId = 42L;
    Department parentStructure = new Department();
    parentStructure.setId(expectedParentStructureId);
    entity.setParentStructure(parentStructure);
    dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(expectedParentStructureId, dto.getParentStructureId());
  }

  @Test
  public void test_mapEntityToDto_expectDepartmentIdsAreMapped() {
    int expectedDepartmentCount = 4;
    Collection<SubStructure> departments = new ArrayList<>();
    for (int i = 1; i <= expectedDepartmentCount; i++) {
      Department department = new Department();
      department.setId((long) i);
      departments.add(department);
    }
    entity.setSubStructures(departments);
    dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(expectedDepartmentCount, dto.getSubStructureIds().size());
    Assert.assertEquals(1L, dto.getSubStructureIds().toArray()[0]);
    Assert.assertEquals(2L, dto.getSubStructureIds().toArray()[1]);
    Assert.assertEquals(3L, dto.getSubStructureIds().toArray()[2]);
    Assert.assertEquals(4L, dto.getSubStructureIds().toArray()[3]);
  }

  @Test
  public void test_mapEntityToDto_expectObjectiveIdsAreMapped() {
    int expectedObjectiveCount = 3;
    Collection<Objective> objectives = new ArrayList<>();
    for (int i = 1; i <= expectedObjectiveCount; i++) {
      Objective objective = new Objective();
      objective.setId((long) i);
      objectives.add(objective);
    }
    entity.setObjectives(objectives);
    dto = mapper.mapEntityToDto(entity);
    Assert.assertEquals(expectedObjectiveCount, dto.getObjectiveIds().size());
    Assert.assertEquals(1L, dto.getObjectiveIds().toArray()[0]);
    Assert.assertEquals(2L, dto.getObjectiveIds().toArray()[1]);
    Assert.assertEquals(3L, dto.getObjectiveIds().toArray()[2]);
  }

  @Test
  public void test_mapDtoToEntity_expectIdIsMapped() {
    Long expectId = 4L;
    dto.setStructureId(expectId);
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertEquals(expectId, entity.getId());
  }

  @Test
  public void test_mapDtoToEntity_expectNameIsMapped() {
    String expectedName = "myNameIS";
    dto.setStructureName(expectedName);
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertEquals(expectedName, entity.getName());
  }

  @Test
  public void test_mapDtoToEntity_expectParentStructureIsNull() {
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertNull(entity.getParentStructure());
  }

  @Test
  public void test_mapDtoToEntity_expectParentStructureIsNull2() {
    dto.setParentStructureId(12L);
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertNull(entity.getParentStructure());
  }

  @Test
  public void test_mapDtoToEntity_expectObjectiveListEmpty() {
    dto.setObjectiveIds(Arrays.asList(1L, 2L));
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertTrue(entity.getObjectives().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectObjectiveListEmpty2() {
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertTrue(entity.getObjectives().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectDepartmentListEmpty() {
    dto.setSubStructureIds(Arrays.asList(1L, 2L));
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertTrue(entity.getSubStructures().isEmpty());
  }

  @Test
  public void test_mapDtoToEntity_expectDepartmentListEmpty2() {
    entity = mapper.mapDtoToEntity(dto);
    Assert.assertTrue(entity.getSubStructures().isEmpty());
  }

  @Test
  public void test_mapEntitiesToDots_expectAllAreMapped() {
    Collection<CorporateObjectiveStructure> entities =
        Arrays.asList(
            new CorporateObjectiveStructure(),
            new CorporateObjectiveStructure(),
            new CorporateObjectiveStructure());
    Collection<CorporateObjectiveStructureDto> dtos = mapper.mapEntitiesToDtos(entities);
    Assert.assertEquals(entities.size(), dtos.size());
  }
}
