package org.burningokr.dto.validators;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.model.cycles.CompanyHistory;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.structures.Company;
import org.burningokr.repositories.cycle.CycleRepository;
import org.burningokr.repositories.structre.CompanyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CycleDtoValidatorTest {

  @Mock private CycleRepository cycleRepository;

  @Mock private CompanyRepository companyRepository;

  @InjectMocks private CycleDtoValidator cycleDtoValidator;

  private Long cycleDtoId = 100L;

  private CompanyHistory targetCompanyHistory;
  private Company targetCompany;

  private List<Cycle> repositoryReturnCyclesThatOverlapFromSameCompanyHistory;
  private List<Cycle> repositoryReturnCyclesThatAreEnvelopedFromSameCompanyHistory;

  @Before
  public void reset() {
    targetCompanyHistory = new CompanyHistory();
    targetCompany = new Company();
    targetCompany.setHistory(targetCompanyHistory);

    repositoryReturnCyclesThatAreEnvelopedFromSameCompanyHistory = new ArrayList<>();
    repositoryReturnCyclesThatOverlapFromSameCompanyHistory = new ArrayList<>();

    when(companyRepository.findByIdOrThrow(anyLong())).thenReturn(targetCompany);
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
    cycleDto.setPlannedStartDate(LocalDate.now().plusDays(5));
    cycleDto.setPlannedEndDate(LocalDate.now().plusDays(10));

    Collection<Long> companyList = new ArrayList<>();
    companyList.add(555L);
    cycleDto.setCompanyIds(companyList);

    return cycleDto;
  }

  // - - -
  // - - - Testing for Content Exceptions
  // - - -

  @Test
  public void validateCycleDto_expectedNoExceptionThrown() throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    cycleDtoValidator.validateCycleDto(cycleDto);
  }

  @Test(expected = InvalidDtoException.class)
  public void validateCycleDto_DtoNameEmpty_expectedInvalidDtoException()
      throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    cycleDto.setName("");
    cycleDtoValidator.validateCycleDto(cycleDto);
  }

  @Test(expected = InvalidDtoException.class)
  public void validateCycleDto_DtoTimeRangeNegative_expectedInvalidDtoException()
      throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    LocalDate timestamp = LocalDate.now();
    cycleDto.setPlannedStartDate(timestamp.plusDays(5));
    cycleDto.setPlannedEndDate(timestamp.minusDays(2));
    cycleDtoValidator.validateCycleDto(cycleDto);
  }

  @Test(expected = InvalidDtoException.class)
  public void validateCycleDto_DtoTimeRangeZero_expectedInvalidDtoException()
      throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    LocalDate timestamp = LocalDate.now();
    cycleDto.setPlannedStartDate(timestamp);
    cycleDto.setPlannedEndDate(timestamp);
    cycleDtoValidator.validateCycleDto(cycleDto);
  }

  @Test(expected = InvalidDtoException.class)
  public void validateCycleDto_DtoActiveStatusPlannedStartInFuture_expectedInvalidDtoException()
      throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    cycleDto.setCycleState(CycleState.ACTIVE);
    cycleDto.setPlannedStartDate(LocalDate.now().plusDays(5));
    cycleDtoValidator.validateCycleDto(cycleDto);
  }

  @Test(expected = InvalidDtoException.class)
  public void validateCycleDto_DtoPreparationStatusPlannedStartInPast_expectedInvalidDtoException()
      throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    cycleDto.setCycleState(CycleState.PREPARATION);
    cycleDto.setPlannedStartDate(LocalDate.now().minusDays(5));
    cycleDtoValidator.validateCycleDto(cycleDto);
  }

  @Test(expected = InvalidDtoException.class)
  public void validateCycleDto_DtoClosedStatus_expectedInvalidDtoException()
      throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    cycleDto.setCycleState(CycleState.CLOSED);
    cycleDtoValidator.validateCycleDto(cycleDto);
  }

  // - - -
  // - - - Testing for Semantic Exceptions from collision with other cycles in database
  // - - -

  @Test(expected = InvalidDtoException.class)
  public void validateCycleDto_DtoEnvelopsOtherCycle_expectedInvalidDtoException()
      throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    Cycle envelopedCycle = new Cycle();
    envelopedCycle.setId(cycleDtoId + 100L);
    repositoryReturnCyclesThatAreEnvelopedFromSameCompanyHistory.add(envelopedCycle);

    cycleDtoValidator.validateCycleDto(cycleDto);
  }

  @Test
  public void validateCycleDto_DtoEnvelopsSameCycle_expectedNoExceptionThrown()
      throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    Cycle envelopedCycle = new Cycle();
    envelopedCycle.setId(cycleDtoId);
    repositoryReturnCyclesThatAreEnvelopedFromSameCompanyHistory.add(envelopedCycle);

    cycleDtoValidator.validateCycleDto(cycleDto);
  }

  @Test(expected = InvalidDtoException.class)
  public void validateCycleDto_DtoDateOverlapsWithOtherCycle_expectedInvalidDtoException()
      throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    Cycle envelopedCycle = new Cycle();
    envelopedCycle.setId(cycleDtoId + 100L);
    repositoryReturnCyclesThatOverlapFromSameCompanyHistory.add(envelopedCycle);

    cycleDtoValidator.validateCycleDto(cycleDto);
  }

  @Test
  public void validateCycleDto_DtoDateOverlapsSameCycle_expectedNoExceptionThrown()
      throws InvalidDtoException {
    CycleDto cycleDto = getValidCycleDto();
    Cycle envelopedCycle = new Cycle();
    envelopedCycle.setId(cycleDtoId);
    repositoryReturnCyclesThatOverlapFromSameCompanyHistory.add(envelopedCycle);

    cycleDtoValidator.validateCycleDto(cycleDto);
  }
}
