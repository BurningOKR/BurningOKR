// TODO fix test
//package org.burningokr.mapper.cycle;
//
//import org.burningokr.dto.cycle.CycleDto;
//import org.burningokr.mapper.CycleMapper;
//import org.burningokr.model.cycles.Cycle;
//import org.burningokr.model.cycles.CycleState;
//import org.burningokr.model.okrUnits.OkrCompany;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collection;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class CycleMapperTest {
//
//  private Cycle cycle;
//  private CycleDto cycleDto;
//  private CycleMapper cycleMapper;
//
//  private LocalDate expectedDatePlanned = LocalDate.now();
//  private LocalDate expectedDateFactual = LocalDate.now().plusDays(1337);
//
//  @BeforeEach
//  public void init() {
//    initCycle();
//    initCycleDto();
//    cycleMapper = new CycleMapper();
//  }
//
//  // region Initialization
//  private void initCycle() {
//    cycle = new Cycle();
//    cycle.setId(1337L);
//    cycle.setName("Test-Zyklus");
//    cycle.setCycleState(CycleState.PREPARATION);
//    cycle.setCompanies(defaultCompany());
//    cycle.setPlannedStartDate(expectedDatePlanned);
//    cycle.setPlannedEndDate(expectedDatePlanned.plusDays(5));
//    cycle.setFactualStartDate(expectedDateFactual);
//    cycle.setFactualEndDate(expectedDateFactual.plusDays(5));
//    cycle.setVisible(false);
//  }
//
//  private void initCycleDto() {
//    cycleDto = new CycleDto();
//    cycleDto.setId(69L);
//    cycleDto.setName("Test-Zyklus-DTO");
//    cycleDto.setCycleState(CycleState.PREPARATION);
//    cycleDto.setCompanyIds(defaultCompanyDto());
//    cycleDto.setPlannedStartDate(expectedDatePlanned.plusDays(5));
//    cycleDto.setPlannedEndDate(expectedDatePlanned.plusDays(10));
//    cycleDto.setIsVisible(false);
//  }
//
//  private Collection<OkrCompany> defaultCompany() {
//    OkrCompany okrCompany = new OkrCompany();
//    okrCompany.setId(1337L);
//    Collection<OkrCompany> companies =
//      new ArrayList<OkrCompany>() {
//        {
//          add(okrCompany);
//        }
//      };
//    return companies;
//  }
//
//  private Collection<Long> defaultCompanyDto() {
//    Collection<Long> companyIds = new ArrayList<>();
//    companyIds.add(7331L);
//    return companyIds;
//  }
//  // endregion
//
//  // region EntityToDto
//  @Test
//  public void test_mapEntityToDto_expectsIdIsMapped() {
//    cycleDto = cycleMapper.mapEntityToDto(cycle);
//    assertEquals(cycle.getId(), cycleDto.getId());
//  }
//
//  @Test
//  public void test_mapEntityToDto_expectsNameIsMapped() {
//    cycleDto = cycleMapper.mapEntityToDto(cycle);
//    assertEquals(cycle.getName(), cycleDto.getName());
//  }
//
//  @Test
//  public void test_mapEntityToDto_expectsPlannedStartDateIsMapped() {
//    cycleDto = cycleMapper.mapEntityToDto(cycle);
//    assertEquals(cycle.getPlannedStartDate(), cycleDto.getPlannedStartDate());
//  }
//
//  @Test
//  public void test_mapEntityToDto_expectsPlannedEndDateIsMapped() {
//    cycleDto = cycleMapper.mapEntityToDto(cycle);
//    assertEquals(cycle.getPlannedEndDate(), cycleDto.getPlannedEndDate());
//  }
//
//  @Test
//  public void test_mapEntityToDto_expectsCompaniesAreMapped() {
//    cycleDto = cycleMapper.mapEntityToDto(cycle);
//    assertEquals(cycle.getCompanies().size(), cycleDto.getCompanyIds().size());
//  }
//
//  @Test
//  public void test_mapEntityToDto_expectsStateActiveIsMapped() {
//    CycleState expected = CycleState.ACTIVE;
//    cycle.setCycleState(expected);
//    cycleDto = cycleMapper.mapEntityToDto(cycle);
//    assertEquals(expected, cycleDto.getCycleState());
//  }
//
//  @Test
//  public void test_mapEntityToDto_expectsStateClosedIsMapped() {
//    CycleState expected = CycleState.CLOSED;
//    cycle.setCycleState(expected);
//    cycleDto = cycleMapper.mapEntityToDto(cycle);
//    assertEquals(expected, cycleDto.getCycleState());
//  }
//
//  @Test
//  public void test_mapEntityToDto_expectsStatePreperationIsMapped() {
//    cycleDto.setCycleState(CycleState.ACTIVE);
//
//    CycleState expected = CycleState.PREPARATION;
//    cycle.setCycleState(expected);
//    cycleDto = cycleMapper.mapEntityToDto(cycle);
//    assertEquals(expected, cycleDto.getCycleState());
//  }
//
//  @Test
//  public void test_mapEntityToDto_expectsIsVisibleTrueIsMapped() {
//    cycle.setVisible(true);
//
//    CycleDto cycleDto = cycleMapper.mapEntityToDto(cycle);
//    assertEquals(cycle.isVisible(), cycleDto.getIsVisible());
//  }
//
//  @Test
//  public void test_mapEntityToDto_expectsIsVisibleFalseIsMapped() {
//    cycle.setVisible(false);
//
//    CycleDto cycleDto = cycleMapper.mapEntityToDto(cycle);
//    assertEquals(cycle.isVisible(), cycleDto.getIsVisible());
//  }
//
//  // endregion
//
//  // region DtoToEntity
//  @Test
//  public void test_mapDtoToEntity_expectsIdIsMapped() {
//    cycle = cycleMapper.mapDtoToEntity(cycleDto);
//    assertEquals(cycleDto.getId(), cycle.getId());
//  }
//
//  @Test
//  public void test_mapDtoToEntity_expectsNameIsMapped() {
//    cycle = cycleMapper.mapDtoToEntity(cycleDto);
//    assertEquals(cycleDto.getName(), cycle.getName());
//  }
//
//  @Test
//  public void test_mapDtoToEntity_expectsPlannedStartDateIsMapped() {
//    cycle = cycleMapper.mapDtoToEntity(cycleDto);
//    assertEquals(cycleDto.getPlannedStartDate(), cycle.getPlannedStartDate());
//  }
//
//  @Test
//  public void test_mapDtoToEntity_expectsPlannedEndDateIsMapped() {
//    cycle = cycleMapper.mapDtoToEntity(cycleDto);
//    assertEquals(cycleDto.getPlannedEndDate(), cycle.getPlannedEndDate());
//  }
//
//  @Test
//  public void test_mapDtoToEntity_expectsStateActiveIsMapped() {
//    CycleState expected = CycleState.ACTIVE;
//    cycleDto.setCycleState(expected);
//    cycle = cycleMapper.mapDtoToEntity(cycleDto);
//    assertEquals(expected, cycle.getCycleState());
//  }
//
//  @Test
//  public void test_mapDtoToEntity_expectsStateClosedIsMapped() {
//    CycleState expected = CycleState.CLOSED;
//    cycleDto.setCycleState(expected);
//    cycle = cycleMapper.mapDtoToEntity(cycleDto);
//    assertEquals(expected, cycle.getCycleState());
//  }
//
//  @Test
//  public void test_mapDtoToEntity_expectsStatePreperationIsMapped() {
//    cycle.setCycleState(CycleState.ACTIVE);
//
//    CycleState expected = CycleState.PREPARATION;
//    cycleDto.setCycleState(expected);
//    cycle = cycleMapper.mapDtoToEntity(cycleDto);
//    assertEquals(expected, cycle.getCycleState());
//  }
//
//  @Test
//  public void test_mapDtoToEntity_expectsIsVisibleTrueIsMapped() {
//    cycleDto.setIsVisible(true);
//
//    Cycle cycle = cycleMapper.mapDtoToEntity(cycleDto);
//
//    assertEquals(cycleDto.getIsVisible(), cycle.isVisible());
//  }
//
//  @Test
//  public void test_mapDtoToEntity_expectsIsVisibleFalseIsMapped() {
//    cycleDto.setIsVisible(false);
//
//    Cycle cycle = cycleMapper.mapDtoToEntity(cycleDto);
//
//    assertEquals(cycleDto.getIsVisible(), cycle.isVisible());
//  }
//  // endregion
//
//}
