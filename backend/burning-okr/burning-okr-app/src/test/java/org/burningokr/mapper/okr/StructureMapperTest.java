package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.StructureDto;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    OkrChildUnit okrDepartment = new OkrDepartment();
    OkrChildUnit okrBranch = new OkrBranch();
    Collection<OkrChildUnit> okrChildUnits = new ArrayList<>();
    okrChildUnits.add(okrDepartment);
    okrChildUnits.add(okrBranch);
    company.setOkrChildUnits(okrChildUnits);

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

  @Test
  public void mapCompaniesToStructureDtos_shouldMapCompaniesToStructureDtos() {
    company.setId(12L);
    Collection<OkrCompany> expected = new ArrayList<>() {
      {
        add(company);
        add(company);
      }
    };
    Collection<StructureDto> actual = structureMapper.mapCompaniesToStructureDtos(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getOkrUnitId());

  }

  @Test
  public void mapCompaniesToStructureDtos_shouldHandleEmptyList() {
    Collection<OkrCompany> expected = new ArrayList<>() {};
    Collection<StructureDto> actual = structureMapper.mapCompaniesToStructureDtos(expected);
    assertEquals(expected.size(), actual.size());

  }
}
