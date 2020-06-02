package org.burningokr.mapper.structure;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.structure.CompanyDto;
import org.burningokr.model.cycles.CompanyHistory;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CompanyMapperTest {

  private Company company;
  private CompanyDto companyDto;
  private CompanyMapper companyMapper;

  @Before
  public void reset() {
    this.company = new Company();
    this.companyDto = new CompanyDto();
    this.companyMapper = new CompanyMapper();

    Cycle cycle = new Cycle();
    cycle.setId(14L);
    company.setCycle(cycle);

    CompanyHistory history = new CompanyHistory();
    history.setId(24L);
    company.setHistory(history);
  }

  // region EntityToDto
  @Test
  public void test_mapEntityToDto_expects_idIsMapped() {
    Long expected = 5L;
    company.setId(expected);
    companyDto = companyMapper.mapEntityToDto(company);
    Assert.assertEquals(expected, companyDto.getStructureId());
  }

  @Test
  public void test_mapEntityToDto_expects_nameIsMapped() {
    String expected = "test";
    company.setName(expected);
    companyDto = companyMapper.mapEntityToDto(company);
    Assert.assertEquals(expected, companyDto.getStructureName());
  }

  @Test
  public void test_mapEntityToDto_expects_labelIsMapped() {
    String expected = "t1e2s3t";
    company.setLabel(expected);
    companyDto = companyMapper.mapEntityToDto(company);
    Assert.assertEquals(expected, companyDto.getLabel());
  }

  @Test
  public void test_mapEntityToDto_expects_departmentsSizeIsEqual4() {
    int expected = 4;
    Collection<SubStructure> departments = new ArrayList<>();
    for (int i = 1; i <= expected; i++) {
      departments.add(new Department());
    }
    company.setSubStructures(departments);
    companyDto = companyMapper.mapEntityToDto(company);
    Assert.assertEquals(expected, companyDto.getSubStructureIds().size());
  }

  @Test
  public void test_mapEntityToDto_expects_objectiveSizeIsEqual() {
    int expected = 4;
    Collection<Objective> objectives = new ArrayList<>();
    for (int i = 1; i <= expected; i++) {
      objectives.add(new Objective());
    }
    company.setObjectives(objectives);
    companyDto = companyMapper.mapEntityToDto(company);
    Assert.assertEquals(expected, companyDto.getObjectiveIds().size());
  }

  @Test
  public void test_mapEntityToDto_expects_CycleIsMapped() {
    Long expected = 14L;
    Cycle cycle = new Cycle();
    cycle.setId(expected);
    company.setCycle(cycle);
    companyDto = companyMapper.mapEntityToDto(company);
    Assert.assertEquals(expected, companyDto.getCycleId());
  }

  @Test
  public void test_mapEntityToDto_expects_HistoryIsMapped() {
    Long expected = 27L;
    CompanyHistory history = new CompanyHistory();
    history.setId(expected);
    company.setHistory(history);
    companyDto = companyMapper.mapEntityToDto(company);
    Assert.assertEquals(expected, companyDto.getHistoryId());
  }
  // endregion

  // region DtoToEntity
  @Test
  public void test_mapDtoToEntity_expects_idIsMapped() {
    Long expected = 5L;
    companyDto.setStructureId(expected);
    company = companyMapper.mapDtoToEntity(companyDto);
    Assert.assertEquals(expected, company.getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_nameIsMapped() {
    String expected = "test";
    companyDto.setStructureName(expected);
    company = companyMapper.mapDtoToEntity(companyDto);
    Assert.assertEquals(expected, company.getName());
  }

  @Test
  public void test_mapDtoToEntity_expects_labelIsMapped() {
    String expected = "test2503";
    companyDto.setLabel(expected);
    company = companyMapper.mapDtoToEntity(companyDto);
    Assert.assertEquals(expected, company.getLabel());
  }

  @Test
  public void test_mapDtoToEntity_expects_departmentsNotNull() {
    company = companyMapper.mapDtoToEntity(companyDto);
    Assert.assertNotNull(company.getSubStructures());
  }

  @Test
  public void test_mapDtoToEntity_expects_objectivesNotNull() {
    company = companyMapper.mapDtoToEntity(companyDto);
    Assert.assertNotNull(company.getObjectives());
  }

  @Test
  public void test_mapDtoToEntity_expects_CycleIdIsMapped_NotNull() {
    Long expected = 14L;
    companyDto.setCycleId(expected);
    company = companyMapper.mapDtoToEntity(companyDto);
    Assert.assertEquals(expected, company.getCycle().getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_CycleIdIsMapped_Null() {
    companyDto.setCycleId(null);
    company = companyMapper.mapDtoToEntity(companyDto);
    Assert.assertNull(company.getCycle());
  }

  @Test
  public void test_mapDtoToEntity_expects_HistoryIdIsMapped_NotNull() {
    Long expected = 24L;
    companyDto.setHistoryId(expected);
    company = companyMapper.mapDtoToEntity(companyDto);
    Assert.assertEquals(expected, company.getHistory().getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_HistoryIdIsMapped_Null() {
    companyDto.setHistoryId(null);
    company = companyMapper.mapDtoToEntity(companyDto);
    Assert.assertNull(company.getHistory());
  }
  // endregion
}
