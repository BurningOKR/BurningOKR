package org.burningokr.dto.validators;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrUnitHistory;
import org.burningokr.repositories.cycle.CycleRepository;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.service.util.DateMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CycleDtoValidator {

  private final CycleRepository cycleRepository;
  private final CompanyRepository companyRepository;
  private final DateMapper dateMapper;

  public void validateCycleDto(CycleDto cycleDto) throws InvalidDtoException {
    checkForContentExceptions(cycleDto);
    checkForSemanticExceptionsWithOtherCyclesInDatabase(cycleDto);
  }

  private void checkForContentExceptions(CycleDto cycleDto) throws InvalidDtoException {
    String exceptionString = "";
    var startDate = dateMapper.mapDateStringToDate(cycleDto.getPlannedStartDate());
    var endDate = dateMapper.mapDateStringToDate(cycleDto.getPlannedEndDate());

    if (cycleDto.getName().isEmpty()) {
      exceptionString += "Cycle name can not be empty.\n";
    }
    if (!startDate.isBefore(endDate)) {
      exceptionString += "Cycle planned end date has to be AFTER planned start date.\n";
    }
    if (cycleDto.getCycleState() == CycleState.ACTIVE
      && startDate.isAfter(LocalDate.now())) {
      exceptionString +=
        "Cycle with ACTIVE state but planned start date in the future can not be created.\n";
    }
    if (cycleDto.getCycleState() == CycleState.PREPARATION
      && startDate.isBefore(LocalDate.now())) {
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
    var startDate = dateMapper.mapDateStringToDate(cycleDto.getPlannedStartDate());
    var endDate = dateMapper.mapDateStringToDate(cycleDto.getPlannedEndDate());

    OkrCompany targetOkrCompany = companyRepository.findByIdOrThrow(cycleDto.getCompanyId());
    OkrUnitHistory<OkrCompany> targetOkrUnitHistory = targetOkrCompany.getCompanyHistory();

    if (isDateWithinOtherCycleInCompanyHistory(
      cycleDto.getId(), startDate, targetOkrUnitHistory)) {
      exceptionString +=
        "Cycle planned start can not be inside another cycle of the same company.\n";
    }
    if (isDateWithinOtherCycleInCompanyHistory(
      cycleDto.getId(), endDate, targetOkrUnitHistory)) {
      exceptionString += "Cycle planned end can not be inside another cycle of the same company.\n";
    }
    if (isCycleDtoDateRangeEnvelopingOtherCycleDateRangeInCompanyHistory(
      cycleDto, targetOkrUnitHistory)) {
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
    Long cycleDtoId, LocalDate dateToCheck, OkrUnitHistory<OkrCompany> okrUnitHistory
  ) {
    List<Cycle> collidingCycles =
      cycleRepository.findByCompanyHistoryAndDateBetweenPlannedTimeRange(
        okrUnitHistory, dateToCheck);

    return (isCycleListFilledWithCyclesWithoutMatchingId(collidingCycles, cycleDtoId));
  }

  private boolean isCycleDtoDateRangeEnvelopingOtherCycleDateRangeInCompanyHistory(
    CycleDto cycleDto, OkrUnitHistory<OkrCompany> okrUnitHistory
  ) {
    var startDate = dateMapper.mapDateStringToDate(cycleDto.getPlannedStartDate());
    var endDate = dateMapper.mapDateStringToDate(cycleDto.getPlannedEndDate());

    List<Cycle> collidingCycles =
      cycleRepository.findByCompanyHistoryAndPlannedTimeRangeBetweenDates(
        okrUnitHistory, startDate, endDate);

    return (isCycleListFilledWithCyclesWithoutMatchingId(collidingCycles, cycleDto.getId()));
  }

  private boolean isCycleListFilledWithCyclesWithoutMatchingId(
    List<Cycle> cycleList, Long cycleId
  ) {
    if (cycleId == null) {
      return !cycleList.isEmpty();
    }

    return !(cycleList.isEmpty()
      || cycleList.size() == 1 && cycleList.get(0).getId().longValue() == cycleId.longValue());
  }
}
