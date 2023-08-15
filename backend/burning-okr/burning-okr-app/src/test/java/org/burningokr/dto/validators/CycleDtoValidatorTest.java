package org.burningokr.dto.validators;

import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrCompanyHistory;
import org.burningokr.repositories.cycle.CycleRepository;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.service.util.DateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CycleDtoValidatorTest {

  private final Long cycleDtoId = 100L;
  @Mock
  private CycleRepository cycleRepository;
  @Mock
  private CompanyRepository companyRepository;
  @InjectMocks
  private CycleDtoValidator cycleDtoValidator;

  @BeforeEach
  public void reset() {
    OkrCompanyHistory targetOkrUnitHistory = new OkrCompanyHistory();
    OkrCompany targetOkrCompany = new OkrCompany();
    targetOkrCompany.setCompanyHistory(targetOkrUnitHistory);

    DateMapper dateMapper = new DateMapper();
    cycleDtoValidator = new CycleDtoValidator(cycleRepository, companyRepository, dateMapper);

    List<Cycle> repositoryReturnCyclesThatAreEnvelopedFromSameCompanyHistory = new ArrayList<>();
    List<Cycle> repositoryReturnCyclesThatOverlapFromSameCompanyHistory = new ArrayList<>();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(targetOkrCompany);
    when(cycleRepository.findByCompanyHistoryAndDateBetweenPlannedTimeRange(any(), any()))
            .thenReturn(repositoryReturnCyclesThatOverlapFromSameCompanyHistory);
    when(cycleRepository.findByCompanyHistoryAndPlannedTimeRangeBetweenDates(any(), any(), any()))
            .thenReturn(repositoryReturnCyclesThatAreEnvelopedFromSameCompanyHistory);
  }

  private CycleDto getValidCycleDto() {
    CycleDto cycleDto = new CycleDto();
    cycleDto.setName("ExampleDto");
    cycleDto.setId(100L);
    cycleDto.setCycleState(CycleState.PREPARATION);
    cycleDto.setPlannedStartDate(LocalDate.of(2020, 3, 1).plusDays(5).toString());
    cycleDto.setPlannedEndDate(LocalDate.of(2020, 3, 1).plusDays(10).toString());

    Collection<Long> companyList = new ArrayList<>();
    companyList.add(555L);
    cycleDto.setCompanyIds(companyList);

    return cycleDto;
  }

//TODO (F. L. 27.06.2023) add tests
//
//  @Test
//  public void validateCycleDto_expectedNoExceptionThrown() throws InvalidDtoException {
//    CycleDto cycleDto = getValidCycleDto();
//    cycleDtoValidator.validateCycleDto(cycleDto);
//  }
//
//  @Test
//  public void validateCycleDto_DtoNameEmpty_expectedInvalidDtoException() {
//    CycleDto cycleDto = getValidCycleDto();
//    cycleDto.setName("");
//
//    try {
//      cycleDtoValidator.validateCycleDto(cycleDto);
//      fail();
//    } catch (Exception ex) {
//      assertEquals(ex.getClass(), InvalidDtoException.class);
//    }
//  }
//
//  @Test
//  public void validateCycleDto_DtoTimeRangeNegative_expectedInvalidDtoException() {
//    CycleDto cycleDto = getValidCycleDto();
//    LocalDate timestamp = LocalDate.of(2020, 3, 1);
//    cycleDto.setPlannedStartDate(timestamp.plusDays(5).toString());
//    cycleDto.setPlannedEndDate(timestamp.minusDays(2).toString());
//    try {
//      cycleDtoValidator.validateCycleDto(cycleDto);
//      fail();
//    } catch (Exception ex) {
//      assertEquals(ex.getClass(), InvalidDtoException.class);
//    }
//  }
//
//  @Test
//  public void validateCycleDto_DtoTimeRangeZero_expectedInvalidDtoException() {
//    CycleDto cycleDto = getValidCycleDto();
//    LocalDate timestamp = LocalDate.of(2020, 3, 1);
//    cycleDto.setPlannedStartDate(timestamp.toString());
//    cycleDto.setPlannedEndDate(timestamp.toString());
//
//    try {
//      cycleDtoValidator.validateCycleDto(cycleDto);
//      fail();
//    } catch (Exception ex) {
//      assertEquals(ex.getClass(), InvalidDtoException.class);
//    }
//  }
//
//  @Test
//  public void validateCycleDto_DtoActiveStatusPlannedStartInFuture_expectedInvalidDtoException() {
//    CycleDto cycleDto = getValidCycleDto();
//    cycleDto.setCycleState(CycleState.ACTIVE);
//    cycleDto.setPlannedStartDate(LocalDate.of(2020, 3, 1).plusDays(5).toString());
//    try {
//      cycleDtoValidator.validateCycleDto(cycleDto);
//      fail();
//    } catch (Exception ex) {
//      assertEquals(ex.getClass(), InvalidDtoException.class);
//    }
//  }
//
//  @Test
//  public void
//  validateCycleDto_DtoPreparationStatusPlannedStartInPast_expectedInvalidDtoException() {
//    CycleDto cycleDto = getValidCycleDto();
//    cycleDto.setCycleState(CycleState.PREPARATION);
//    cycleDto.setPlannedStartDate(LocalDate.of(2020, 3, 1).minusDays(5).toString());
//    try {
//      cycleDtoValidator.validateCycleDto(cycleDto);
//      fail();
//    } catch (Exception ex) {
//      assertEquals(ex.getClass(), InvalidDtoException.class);
//    }
//  }
//
//  @Test
//  public void validateCycleDto_DtoClosedStatus_expectedInvalidDtoException() {
//    CycleDto cycleDto = getValidCycleDto();
//    cycleDto.setCycleState(CycleState.CLOSED);
//    try {
//      cycleDtoValidator.validateCycleDto(cycleDto);
//      fail();
//    } catch (Exception ex) {
//      assertEquals(ex.getClass(), InvalidDtoException.class);
//    }
//  }
//
//  @Test
//  public void validateCycleDto_DtoEnvelopsOtherCycle_expectedInvalidDtoException() {
//    CycleDto cycleDto = getValidCycleDto();
//    Cycle envelopedCycle = new Cycle();
//    envelopedCycle.setId(cycleDtoId + 100L);
//    repositoryReturnCyclesThatAreEnvelopedFromSameCompanyHistory.add(envelopedCycle);
//    try {
//      cycleDtoValidator.validateCycleDto(cycleDto);
//      fail();
//    } catch (Exception ex) {
//      assertEquals(ex.getClass(), InvalidDtoException.class);
//    }
//  }
//
//  @Test
//  public void validateCycleDto_DtoEnvelopsSameCycle_expectedNoExceptionThrown()
//          throws InvalidDtoException {
//    CycleDto cycleDto = getValidCycleDto();
//    Cycle envelopedCycle = new Cycle();
//    envelopedCycle.setId(cycleDtoId);
//    repositoryReturnCyclesThatAreEnvelopedFromSameCompanyHistory.add(envelopedCycle);
//
//    cycleDtoValidator.validateCycleDto(cycleDto);
//  }
//
//  @Test
//  public void validateCycleDto_DtoDateOverlapsWithOtherCycle_expectedInvalidDtoException() {
//    CycleDto cycleDto = getValidCycleDto();
//    Cycle envelopedCycle = new Cycle();
//    envelopedCycle.setId(cycleDtoId + 100L);
//    repositoryReturnCyclesThatOverlapFromSameCompanyHistory.add(envelopedCycle);
//    try {
//      cycleDtoValidator.validateCycleDto(cycleDto);
//      fail();
//    } catch (Exception ex) {
//      assertEquals(ex.getClass(), InvalidDtoException.class);
//    }
//  }
//
//  @Test
//  public void validateCycleDto_DtoDateOverlapsSameCycle_expectedNoExceptionThrown()
//          throws InvalidDtoException {
//    CycleDto cycleDto = getValidCycleDto();
//    Cycle envelopedCycle = new Cycle();
//    envelopedCycle.setId(cycleDtoId);
//    repositoryReturnCyclesThatOverlapFromSameCompanyHistory.add(envelopedCycle);
//
//    cycleDtoValidator.validateCycleDto(cycleDto);
//  }
}
