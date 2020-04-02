package org.burningokr.dto.validators;

import java.time.LocalDate;
import java.util.List;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.model.cycles.CompanyHistory;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.structures.Company;
import org.burningokr.repositories.cycle.CycleRepository;
import org.burningokr.repositories.structre.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CycleDtoValidator {

  private CycleRepository cycleRepository;
  private CompanyRepository companyRepository;

  @Autowired
  public CycleDtoValidator(CycleRepository cycleRepository, CompanyRepository companyRepository) {
    this.cycleRepository = cycleRepository;
    this.companyRepository = companyRepository;
  }

  public void validateCycleDto(CycleDto cycleDto) throws InvalidDtoException {
    checkForContentExceptions(cycleDto);
    checkForSemanticExceptionsWithOtherCyclesInDatabase(cycleDto);
  }

  private void checkForContentExceptions(CycleDto cycleDto) throws InvalidDtoException {
    String exceptionString = "";

    if (cycleDto.getName().isEmpty()) {
      exceptionString += "Cycle name can not be empty.\n";
    }
    if (!cycleDto.getPlannedStartDate().isBefore(cycleDto.getPlannedEndDate())) {
      exceptionString += "Cycle planned end date has to be AFTER planned start date.\n";
    }
    if (cycleDto.getCycleState() == CycleState.ACTIVE
        && cycleDto.getPlannedStartDate().isAfter(LocalDate.now())) {
      exceptionString +=
          "Cycle with ACTIVE state but planned start date in the future can not be created.\n";
    }
    if (cycleDto.getCycleState() == CycleState.PREPARATION
        && cycleDto.getPlannedStartDate().isBefore(LocalDate.now())) {
      exceptionString +=
          "Cycle with PREPARATION state but planned start date in the past can not be created.\n";
    }
    if (cycleDto.getCycleState() == CycleState.CLOSED) {
      exceptionString += "Cycle with CLOSED state can not be created.\n";
    }

    handleExceptionString(exceptionString);
  }

  private void checkForSemanticExceptionsWithOtherCyclesInDatabase(CycleDto cycleDto)
      throws InvalidDtoException {
    String exceptionString = "";

    Company targetCompany = companyRepository.findByIdOrThrow(cycleDto.getCompanyId());
    CompanyHistory targetCompanyHistory = targetCompany.getHistory();

    if (isDateWithinOtherCycleInCompanyHistory(
        cycleDto.getId(), cycleDto.getPlannedStartDate(), targetCompanyHistory)) {
      exceptionString +=
          "Cycle planned start can not be inside another cycle of the same company.\n";
    }
    if (isDateWithinOtherCycleInCompanyHistory(
        cycleDto.getId(), cycleDto.getPlannedEndDate(), targetCompanyHistory)) {
      exceptionString += "Cycle planned end can not be inside another cycle of the same company.\n";
    }
    if (isCycleDtoDateRangeEnvelopingOtherCycleDateRangeInCompanyHistory(
        cycleDto, targetCompanyHistory)) {
      exceptionString += "Cycle can not completely envelop another cycle of the same company.\n";
    }

    handleExceptionString(exceptionString);
  }

  private void handleExceptionString(String exceptionString) throws InvalidDtoException {
    if (!exceptionString.isEmpty()) {
      throw new InvalidDtoException(exceptionString);
    }
  }

  private boolean isDateWithinOtherCycleInCompanyHistory(
      Long cycleDtoId, LocalDate dateToCheck, CompanyHistory companyHistory) {
    List<Cycle> collidingCycles =
        cycleRepository.findByCompanyHistoryAndDateBetweenPlannedTimeRange(
            companyHistory, dateToCheck);

    return (isCycleListFilledWithCyclesWithoutMatchingId(collidingCycles, cycleDtoId));
  }

  private boolean isCycleDtoDateRangeEnvelopingOtherCycleDateRangeInCompanyHistory(
      CycleDto cycleDto, CompanyHistory companyHistory) {
    List<Cycle> collidingCycles =
        cycleRepository.findByCompanyHistoryAndPlannedTimeRangeBetweenDates(
            companyHistory, cycleDto.getPlannedStartDate(), cycleDto.getPlannedEndDate());

    return (isCycleListFilledWithCyclesWithoutMatchingId(collidingCycles, cycleDto.getId()));
  }

  private boolean isCycleListFilledWithCyclesWithoutMatchingId(
      List<Cycle> cycleList, Long cycleId) {
    return !(cycleList.isEmpty()
        || cycleList.size() == 1 && cycleList.get(0).getId().longValue() == cycleId.longValue());
  }
}
