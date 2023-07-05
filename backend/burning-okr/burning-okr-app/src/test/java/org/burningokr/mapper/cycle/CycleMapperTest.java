package org.burningokr.mapper.cycle;

import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.util.DateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CycleMapperTest {

  private Cycle cycle;
  private CycleDto cycleDto;
  private CycleMapper cycleMapper;

  private final LocalDate expectedDatePlanned = LocalDate.of(2020, 3, 1);
  private final LocalDate expectedDateFactual = LocalDate.of(2020, 3, 1).plusDays(1337);

  @BeforeEach
  public void init() {
    initCycle();
    initCycleDto();
    DateMapper dateMapper = new DateMapper();
    cycleMapper = new CycleMapper(dateMapper);
  }

  private void initCycle() {
    cycle = new Cycle();
    cycle.setId(1337L);
    cycle.setName("Test-Zyklus");
    cycle.setCycleState(CycleState.PREPARATION);
    cycle.setCompanies(defaultCompany());
    cycle.setPlannedStartDate(expectedDatePlanned);
    cycle.setPlannedEndDate(expectedDatePlanned.plusDays(5));
    cycle.setFactualStartDate(expectedDateFactual);
    cycle.setFactualEndDate(expectedDateFactual.plusDays(5));
    cycle.setVisible(false);
  }

  private void initCycleDto() {
    cycleDto = new CycleDto();
    cycleDto.setId(69L);
    cycleDto.setName("Test-Zyklus-DTO");
    cycleDto.setCycleState(CycleState.PREPARATION);
    cycleDto.setCompanyIds(defaultCompanyDto());
    cycleDto.setPlannedStartDate(expectedDatePlanned.plusDays(5).toString());
    cycleDto.setPlannedEndDate(expectedDatePlanned.plusDays(10).toString());
    cycleDto.setIsVisible(false);
  }

  private Collection<OkrCompany> defaultCompany() {
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setId(1337L);
    return new ArrayList<>() {
      {
        add(okrCompany);
      }
    };
  }

  private Collection<Long> defaultCompanyDto() {
    Collection<Long> companyIds = new ArrayList<>();
    companyIds.add(7331L);
    return companyIds;
  }

  @Test
  public void mapEntityToDto_shouldMapId() {
    cycleDto = cycleMapper.mapEntityToDto(cycle);

    assertEquals(cycle.getId(), cycleDto.getId());
  }

  @Test
  public void mapEntityToDto_shouldMapName() {
    cycleDto = cycleMapper.mapEntityToDto(cycle);

    assertEquals(cycle.getName(), cycleDto.getName());
  }

  @Test
  public void mapEntityToDto_shouldMapPlannedStartDate() {
    cycleDto = cycleMapper.mapEntityToDto(cycle);

    assertEquals(cycle.getPlannedStartDate().toString(), cycleDto.getPlannedStartDate());
  }

  @Test
  public void mapEntityToDto_shouldMapPlannedEndDate() {
    cycleDto = cycleMapper.mapEntityToDto(cycle);

    assertEquals(cycle.getPlannedEndDate().toString(), cycleDto.getPlannedEndDate());
  }

  @Test
  public void mapEntityToDto_shouldMapCompanies() {
    cycleDto = cycleMapper.mapEntityToDto(cycle);

    assertEquals(cycle.getCompanies().size(), cycleDto.getCompanyIds().size());
  }

  @Test
  public void mapEntityToDto_shouldMapCycleStateActive() {
    CycleState expected = CycleState.ACTIVE;
    cycle.setCycleState(expected);

    cycleDto = cycleMapper.mapEntityToDto(cycle);

    assertEquals(expected, cycleDto.getCycleState());
  }

  @Test
  public void mapEntityToDto_shouldMapCycleStateClosed() {
    CycleState expected = CycleState.CLOSED;
    cycle.setCycleState(expected);

    cycleDto = cycleMapper.mapEntityToDto(cycle);

    assertEquals(expected, cycleDto.getCycleState());
  }

  @Test
  public void mapEntityToDto_shouldMapCycleStatePreparation() {
    cycleDto.setCycleState(CycleState.ACTIVE);
    CycleState expected = CycleState.PREPARATION;
    cycle.setCycleState(expected);

    cycleDto = cycleMapper.mapEntityToDto(cycle);

    assertEquals(expected, cycleDto.getCycleState());
  }

  @Test
  public void mapEntityToDto_shouldMapIsVisibleTrue() {
    cycle.setVisible(true);

    CycleDto cycleDto = cycleMapper.mapEntityToDto(cycle);

    assertEquals(cycle.isVisible(), cycleDto.getIsVisible());
  }

  @Test
  public void mapEntityToDto_shouldMapIsVisibleFalse() {
    cycle.setVisible(false);

    CycleDto cycleDto = cycleMapper.mapEntityToDto(cycle);

    assertEquals(cycle.isVisible(), cycleDto.getIsVisible());
  }

  @Test
  public void mapEntitiesToDtos_shouldMapCycleEntitiesToDtos() {
    cycle.setId(12L);
    Collection<Cycle> expected = new ArrayList<>() {
      {
        add(cycle);
        add(cycle);
      }
    };
    Collection<CycleDto> actual = cycleMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getId());
  }

  @Test
  public void mapDtoToEntity_shouldMapId() {
    cycle = cycleMapper.mapDtoToEntity(cycleDto);

    assertEquals(cycleDto.getId(), cycle.getId());
  }

  @Test
  public void mapDtoToEntity_shouldMapName() {
    cycle = cycleMapper.mapDtoToEntity(cycleDto);

    assertEquals(cycleDto.getName(), cycle.getName());
  }

  @Test
  public void mapDtoToEntity_shouldMapPlannedStartDate() {
    cycle = cycleMapper.mapDtoToEntity(cycleDto);

    assertEquals(cycleDto.getPlannedStartDate(), cycle.getPlannedStartDate().toString());
  }

  @Test
  public void mapDtoToEntity_shouldMapPlannedEndDate() {
    cycle = cycleMapper.mapDtoToEntity(cycleDto);

    assertEquals(cycleDto.getPlannedEndDate(), cycle.getPlannedEndDate().toString());
  }

  @Test
  public void mapDtoToEntity_shouldMapCycleStateActive() {
    CycleState expected = CycleState.ACTIVE;
    cycleDto.setCycleState(expected);

    cycle = cycleMapper.mapDtoToEntity(cycleDto);

    assertEquals(expected, cycle.getCycleState());
  }

  @Test
  public void mapDtoToEntity_shouldMapCycleStateClosed() {
    CycleState expected = CycleState.CLOSED;
    cycleDto.setCycleState(expected);

    cycle = cycleMapper.mapDtoToEntity(cycleDto);

    assertEquals(expected, cycle.getCycleState());
  }

  @Test
  public void mapDtoToEntity_shouldMapCycleStatePreparation() {
    cycle.setCycleState(CycleState.ACTIVE);
    CycleState expected = CycleState.PREPARATION;
    cycleDto.setCycleState(expected);

    cycle = cycleMapper.mapDtoToEntity(cycleDto);

    assertEquals(expected, cycle.getCycleState());
  }

  @Test
  public void mapDtoToEntity_shouldMapIsVisibleTrue() {
    cycleDto.setIsVisible(true);

    Cycle cycle = cycleMapper.mapDtoToEntity(cycleDto);

    assertEquals(cycleDto.getIsVisible(), cycle.isVisible());
  }

  @Test
  public void mapDtoToEntity_shouldMapIsVisibleFalse() {
    cycleDto.setIsVisible(false);

    Cycle cycle = cycleMapper.mapDtoToEntity(cycleDto);

    assertEquals(cycleDto.getIsVisible(), cycle.isVisible());
  }

  @Test
  public void mapDtosToEntities_shouldMapCycleDtosToEntities() {
    cycleDto.setId(12L);
    Collection<CycleDto> expected = new ArrayList<>() {
      {
        add(cycleDto);
        add(cycleDto);
      }
    };
    Collection<Cycle> actual = cycleMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getId());
  }
}
