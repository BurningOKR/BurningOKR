package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.StructureDto;
import org.burningokr.model.okrUnits.OkrCompany;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//TODO (F. L. 26.06.2023) add Tests
public class StructureMapperTest {

  private StructureMapper structureMapper;
  private OkrCompany company;

  @BeforeEach
  public void setUp() {
    structureMapper = new StructureMapper();
    company = new OkrCompany();
  }

  @Test
  public void mapCompanyToStructureDto_shouldMapId() {
    long expected = 1L;
    company.setId(expected);

    StructureDto actual = structureMapper.mapCompanyToStructureDto(company);

    assertEquals(expected, actual.getOkrUnitId());
  }

  @Test
  public void mapCompanyToStructureDto_shouldMapName() {
    String expected = "Company1";
    company.setName(expected);

    StructureDto actual = structureMapper.mapCompanyToStructureDto(company);

    assertEquals(expected, actual.getUnitName());
  }
}
