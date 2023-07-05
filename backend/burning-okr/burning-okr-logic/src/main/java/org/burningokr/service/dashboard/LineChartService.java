package org.burningokr.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.dashboard.LineChartLineKeyValues;
import org.burningokr.model.dashboard.LineChartOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;
import org.burningokr.model.dashboard.DashboardCreation;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.dashboard.helper.ProgressHelper;
import org.burningokr.service.okr.KeyResultHistoryService;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LineChartService {
  private final CompanyService companyService;
  private final KeyResultHistoryService keyResultHistoryService;

  public LineChartOptionsDto buildProgressChart(ChartCreationOptions chartCreationOptions, DashboardCreation dashboardCreation) {
    LineChartOptionsDto lineChartOptionsDto = new LineChartOptionsDto();
    Collection<LineChartLineKeyValues> lineChartLineKeyValuesList = new ArrayList<>();
    Collection<Objective> objectives = new ArrayList<>();
    Collection<KeyResult> keyResults = new ArrayList<>();
    Collection<OkrDepartment> teams;

    lineChartOptionsDto.setId(chartCreationOptions.getId());
    lineChartOptionsDto.setTitle(chartCreationOptions.getTitle());
    lineChartOptionsDto.setChartType(ChartInformationTypeEnum.LINE_PROGRESS.ordinal());

    teams = getDepartments(
      dashboardCreation.getCompanyId(),
      chartCreationOptions.getTeamIds()
    );

    for (OkrDepartment team : teams) {
      objectives.addAll(team.getObjectives());
    }

    for (Objective objective : objectives) {
      keyResults.addAll(objective.getKeyResults());
    }

    if (keyResults.size() == 0) {
      return getNoValuesFoundLineChartOptionsDto(chartCreationOptions, lineChartOptionsDto);
    }

    LocalDate startDate = keyResultHistoryService.findOldestKeyResultHistoryForKeyResultList(keyResults)
      .getDateChanged();
    LocalDate today = LocalDate.now();
    long numberOfDays = ChronoUnit.DAYS.between(startDate, today) + 1; // +1 because we also include the startDate


    for (OkrDepartment team : teams) {
      LineChartLineKeyValues lineChartLineKeyValues = new LineChartLineKeyValues();
      lineChartLineKeyValues.setName(team.getName());
      lineChartLineKeyValues.setData(ProgressHelper.getProgressForTeam(team, startDate, numberOfDays));
      lineChartLineKeyValuesList.add(lineChartLineKeyValues);
    }

    if (chartCreationOptions.getTeamIds().size() == 0) {
      lineChartOptionsDto.setSeries(Collections.singletonList(ProgressHelper.getProgressForCompany(
        lineChartLineKeyValuesList,
        numberOfDays
      )));
    } else {
      lineChartOptionsDto.setSeries(lineChartLineKeyValuesList);
    }
    lineChartOptionsDto.setXAxisCategories(getProgressXAxis(startDate, numberOfDays));
    return lineChartOptionsDto;
  }

  private Collection<OkrDepartment> getDepartments(Long companyId, Collection<Long> teamIds) {
    OkrCompany company = companyService.findById(companyId);
    return BranchHelper.collectDepartments(teamIds, company);
  }

  private Collection<String> getProgressXAxis(LocalDate startDate, long numberOfDays) {
    Collection<String> xAxis = new ArrayList<>();
    LocalDate currentDate = startDate;
    for (int i = 0; i < numberOfDays; i++) {
      xAxis.add(currentDate.toString());
      currentDate = currentDate.plusDays(1);
    }
    return xAxis;
  }

  private LineChartOptionsDto getNoValuesFoundLineChartOptionsDto(ChartCreationOptions chartCreationOptions, LineChartOptionsDto lineChartOptionsDto) {
    ArrayList<LineChartLineKeyValues> lineChartKeyValuesList = new ArrayList<>();

    LineChartLineKeyValues lineChartLineKeyValuesNoValues = new LineChartLineKeyValues();
    lineChartLineKeyValuesNoValues.setName(chartCreationOptions.getTitle());

    lineChartLineKeyValuesNoValues.setName(chartCreationOptions.getTitle());
    lineChartLineKeyValuesNoValues.setData(new ArrayList<>(Collections.singletonList(50.0)));

    lineChartKeyValuesList.add(lineChartLineKeyValuesNoValues);

    lineChartOptionsDto.setSeries(lineChartKeyValuesList);
    lineChartOptionsDto.setXAxisCategories(Stream.of("Keine Werte vorhanden").collect(Collectors.toList()));

    return lineChartOptionsDto;
  }
}
