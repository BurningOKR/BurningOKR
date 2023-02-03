package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.StructureDto;
import org.burningokr.model.okrUnits.OkrCompany;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StructureMapperTest {

  private StructureMapper mapper;
  private OkrCompany company1;

  @Before
  public void setUp() {
    mapper = new StructureMapper();
    company1 = new OkrCompany();
  }

  @Test
  public void test_mapCompanyToDto_expected_name_Company1() {
    company1.setName("Company1");

    StructureDto actual = mapper.mapCompanyToStructureDto(company1);

    assertEquals("Company1", actual.getUnitName());
  }
}
