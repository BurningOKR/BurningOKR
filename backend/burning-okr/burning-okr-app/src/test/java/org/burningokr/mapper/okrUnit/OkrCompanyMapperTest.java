package org.burningokr.mapper.okrUnit;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.OkrUnitHistory;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OkrCompanyMapperTest {

  private OkrCompany okrCompany;
  private OkrCompanyDto okrCompanyDto;
  private OkrCompanyMapper okrCompanyMapper;

  @Before
  public void reset() {
    this.okrCompany = new OkrCompany();
    this.okrCompanyDto = new OkrCompanyDto();
    this.okrCompanyMapper = new OkrCompanyMapper();

    Cycle cycle = new Cycle();
    cycle.setId(14L);
    okrCompany.setCycle(cycle);

    OkrUnitHistory<OkrCompany> history = new OkrUnitHistory<>();
    history.setId(24L);
    okrCompany.setHistory(history);
  }

  // region EntityToDto
  @Test
  public void test_mapEntityToDto_expects_idIsMapped() {
    Long expected = 5L;
    okrCompany.setId(expected);
    okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);
    Assert.assertEquals(expected, okrCompanyDto.getOkrUnitId());
  }

  @Test
  public void test_mapEntityToDto_expects_nameIsMapped() {
    String expected = "test";
    okrCompany.setName(expected);
    okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);
    Assert.assertEquals(expected, okrCompanyDto.getUnitName());
  }

  @Test
  public void test_mapEntityToDto_expects_labelIsMapped() {
    String expected = "t1e2s3t";
    okrCompany.setLabel(expected);
    okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);
    Assert.assertEquals(expected, okrCompanyDto.getLabel());
  }

  @Test
  public void test_mapEntityToDto_expects_departmentsSizeIsEqual4() {
    int expected = 4;
    Collection<OkrChildUnit> departments = new ArrayList<>();
    for (int i = 1; i <= expected; i++) {
      departments.add(new OkrDepartment());
    }
    okrCompany.setOkrChildUnits(departments);
    okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);
    Assert.assertEquals(expected, okrCompanyDto.getOkrChildUnitIds().size());
  }

  @Test
  public void test_mapEntityToDto_expects_objectiveSizeIsEqual() {
    int expected = 4;
    Collection<Objective> objectives = new ArrayList<>();
    for (int i = 1; i <= expected; i++) {
      objectives.add(new Objective());
    }
    okrCompany.setObjectives(objectives);
    okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);
    Assert.assertEquals(expected, okrCompanyDto.getObjectiveIds().size());
  }

  @Test
  public void test_mapEntityToDto_expects_CycleIsMapped() {
    Long expected = 14L;
    Cycle cycle = new Cycle();
    cycle.setId(expected);
    okrCompany.setCycle(cycle);
    okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);
    Assert.assertEquals(expected, okrCompanyDto.getCycleId());
  }

  @Test
  public void test_mapEntityToDto_expects_HistoryIsMapped() {
    Long expected = 27L;
    OkrUnitHistory<OkrCompany> history = new OkrUnitHistory<>();
    history.setId(expected);
    okrCompany.setHistory(history);
    okrCompanyDto = okrCompanyMapper.mapEntityToDto(okrCompany);
    Assert.assertEquals(expected, okrCompanyDto.getHistoryId());
  }
  // endregion

  // region DtoToEntity
  @Test
  public void test_mapDtoToEntity_expects_idIsMapped() {
    Long expected = 5L;
    okrCompanyDto.setOkrUnitId(expected);
    okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);
    Assert.assertEquals(expected, okrCompany.getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_nameIsMapped() {
    String expected = "test";
    okrCompanyDto.setUnitName(expected);
    okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);
    Assert.assertEquals(expected, okrCompany.getName());
  }

  @Test
  public void test_mapDtoToEntity_expects_labelIsMapped() {
    String expected = "test2503";
    okrCompanyDto.setLabel(expected);
    okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);
    Assert.assertEquals(expected, okrCompany.getLabel());
  }

  @Test
  public void test_mapDtoToEntity_expects_departmentsNotNull() {
    okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);
    Assert.assertNotNull(okrCompany.getOkrChildUnits());
  }

  @Test
  public void test_mapDtoToEntity_expects_objectivesNotNull() {
    okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);
    Assert.assertNotNull(okrCompany.getObjectives());
  }

  @Test
  public void test_mapDtoToEntity_expects_CycleIdIsMapped_NotNull() {
    Long expected = 14L;
    okrCompanyDto.setCycleId(expected);
    okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);
    Assert.assertEquals(expected, okrCompany.getCycle().getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_CycleIdIsMapped_Null() {
    okrCompanyDto.setCycleId(null);
    okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);
    Assert.assertNull(okrCompany.getCycle());
  }

  @Test
  public void test_mapDtoToEntity_expects_HistoryIdIsMapped_NotNull() {
    Long expected = 24L;
    okrCompanyDto.setHistoryId(expected);
    okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);
    Assert.assertEquals(expected, okrCompany.getHistory().getId());
  }

  @Test
  public void test_mapDtoToEntity_expects_HistoryIdIsMapped_Null() {
    okrCompanyDto.setHistoryId(null);
    okrCompany = okrCompanyMapper.mapDtoToEntity(okrCompanyDto);
    Assert.assertNull(okrCompany.getHistory());
  }
  // endregion
}
