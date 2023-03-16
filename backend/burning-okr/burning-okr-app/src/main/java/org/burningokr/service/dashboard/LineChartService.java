package org.burningokr.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.dashboard.LineChartLineKeyValues;
import org.burningokr.dto.dashboard.LineChartOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.histories.KeyResultHistory;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.okr.KeyResultHistoryService;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LineChartService {
  private final CompanyService companyService;
  private final KeyResultHistoryService keyResultHistoryService;

  public LineChartOptionsDto buildProgressChart(ChartCreationOptions chartCreationOptions) {
    LineChartOptionsDto lineChartOptionsDto = new LineChartOptionsDto();
    Collection<LineChartLineKeyValues> lineChartLineKeyValuesList = new ArrayList<>();
    Collection<Objective> objectives = new ArrayList<>();
    Collection<KeyResult> keyResults = new ArrayList<>();
    Collection<OkrDepartment> teams;

    teams = getTeamsForChart(
      chartCreationOptions.getDashboardCreation().getCompanyId(),
      chartCreationOptions.getTeamIds()
    );

    for (OkrDepartment team : teams) {
      objectives.addAll(team.getObjectives());
    }

    for (Objective objective : objectives) {
      keyResults.addAll(objective.getKeyResults());
    }

    if (keyResults.size() == 0) {
      return getNoValuesFoundLineChartOptionsDto(chartCreationOptions);
    }

    LocalDate startDate = keyResultHistoryService.findOldestKeyResultHistoryForKeyResultList(keyResults)
      .getDateChanged();
    LocalDate today = LocalDate.now();
    long numberOfDays = ChronoUnit.DAYS.between(startDate, today) + 1; // +1 because we also include the startDate


    for (OkrDepartment team : teams) {
      LineChartLineKeyValues lineChartLineKeyValues = new LineChartLineKeyValues();
      lineChartLineKeyValues.setName(team.getName());
      lineChartLineKeyValues.setData(getProgressForTeam(team, startDate, numberOfDays));
      lineChartLineKeyValuesList.add(lineChartLineKeyValues);
    }

    if (chartCreationOptions.getTeamIds().size() == 0) {
      lineChartOptionsDto.setSeries(Collections.singletonList(getProgressForCompany(
        lineChartLineKeyValuesList,
        numberOfDays
      )));
    } else {
      lineChartOptionsDto.setSeries(lineChartLineKeyValuesList);
    }
    lineChartOptionsDto.setId(chartCreationOptions.getId());
    lineChartOptionsDto.setTitle(chartCreationOptions.getTitle());
    lineChartOptionsDto.setXAxisCategories(getProgressXAxis(startDate, numberOfDays));
    lineChartOptionsDto.setChartType(ChartInformationTypeEnum.LINE_PROGRESS.ordinal());
    return lineChartOptionsDto;
  }

  private Collection<OkrDepartment> getTeamsForChart(Long companyId, Collection<Long> teamIds) {
    ArrayList<OkrDepartment> allTeamsOfCompany;

    OkrCompany company = companyService.findById(companyId);
    allTeamsOfCompany = new ArrayList<OkrDepartment>(BranchHelper.collectDepartments(company));

    if (teamIds.size() == 0) {
      return allTeamsOfCompany;
    } else {
      Collection<OkrDepartment> teams = new ArrayList<>();
      for (OkrDepartment team : allTeamsOfCompany) {
        if (teamIds.stream().anyMatch(teamId -> team.getId().equals(teamId))) {
          teams.add(team);
        }
      }
      return teams;
    }
  }

  private LineChartLineKeyValues getProgressForCompany(
    Collection<LineChartLineKeyValues> teamProgressLineChartValueList,
    long numberOfDays
  ) {
    LineChartLineKeyValues avgCompanyProgressLineChartKeyValues = new LineChartLineKeyValues();
    ArrayList<Double> companyProgress = new ArrayList<>();

    for (int i = 0; i < numberOfDays; i++) {
      double sumDay = 0;
      int numberOfTeamsThatDay = 0;
      for (LineChartLineKeyValues teamProgress : teamProgressLineChartValueList) {
        if (teamProgress.getData().get(i) != null) {
          sumDay += teamProgress.getData().get(i);
          numberOfTeamsThatDay++;
        }
      }
      if (numberOfTeamsThatDay > 0) {
        companyProgress.add(sumDay / numberOfTeamsThatDay);
      } else {
        companyProgress.add(null);
      }
    }

    avgCompanyProgressLineChartKeyValues.setName("Durchschnitt der Firma");
    avgCompanyProgressLineChartKeyValues.setData(companyProgress);

    return avgCompanyProgressLineChartKeyValues;
  }


  private ArrayList<Double> getProgressForTeam(OkrChildUnit okrDepartment, LocalDate startDate, long numberOfDays) {
    ArrayList<Double> teamProgress = new ArrayList<>();
    ArrayList<ArrayList<Double>> objectiveProgressLists = new ArrayList<>();
    ArrayList<Objective> objectives = new ArrayList<Objective>(okrDepartment.getObjectives());

    for (Objective objective : objectives) {
      objectiveProgressLists.add(getProgressForObjective(objective, startDate, numberOfDays));
    }

    for (int i = 0; i < numberOfDays; i++) {
      double currentDayValue = 0;
      int numberOfObjectives = 0;

      for (ArrayList<Double> objectiveProgressList : objectiveProgressLists) {
        if (objectiveProgressList.get(i) != null) {
          currentDayValue += objectiveProgressList.get(i);
          numberOfObjectives++;
        }
      }
      if (numberOfObjectives > 0) {
        currentDayValue = currentDayValue / numberOfObjectives;
        teamProgress.add(currentDayValue);
      } else {
        teamProgress.add(null); // Adding null to later filter out when objectives didn't have progress yet (Meaning no Key-Result) in later calculations
      }
    }
    return teamProgress;
  }

  private ArrayList<Double> getProgressForObjective(Objective objective, LocalDate startDate, long numberOfDays) {
    ArrayList<Double> objectiveProgress = new ArrayList<>();
    ArrayList<ArrayList<Double>> keyResultsProgressLists = new ArrayList<>();
    ArrayList<KeyResult> keyResults = new ArrayList<>(objective.getKeyResults());

    for (KeyResult keyResult : keyResults) {
      keyResultsProgressLists.add(getKeyResultProgressOfKeyResult(keyResult, startDate, numberOfDays));
    }

    for (int i = 0; i < numberOfDays; i++) {
      double currentDayValue = 0;
      int numberOfKeyResults = 0;

      for (ArrayList<Double> keyResultProgressList : keyResultsProgressLists) {
        if (keyResultProgressList.get(i) != null) {
          currentDayValue += keyResultProgressList.get(i);
          numberOfKeyResults++;
        }
      }
      if (numberOfKeyResults > 0) {
        currentDayValue = currentDayValue / numberOfKeyResults;
        objectiveProgress.add(currentDayValue);
      } else {
        objectiveProgress.add(null); // Adding null to later filter out when objectives didn't have progress yet (Meaning no Key-Result) in later calculations
      }
    }
    return objectiveProgress;
  }

  /**
   * @param keyResult    KeyResult Objet of which zu get the progress-history
   * @param startDate    LocalDate from which it beginns
   * @param numberOfDays long Number of Days it's supposed to calculate the progress.
   * @return List of doubles(percentage) which contain the progress for every single day since startdate until numberOfDays after startDate is reached.
   * If first value of keyResultHistory is after startDate, all values until startDate will be 0.0
   */
  private ArrayList<Double> getKeyResultProgressOfKeyResult(
    KeyResult keyResult,
    LocalDate startDate,
    long numberOfDays
  ) {
    ArrayList<Double> keyResultProgress = new ArrayList<>();
    ArrayList<KeyResultHistory> keyResultHistories = new ArrayList<>(keyResult.getKeyResultHistory());
    keyResultHistories.sort(Comparator.comparing(KeyResultHistory::getDateChanged));
    LocalDate currentDate = startDate;

    for (long i = 0; i < numberOfDays; i++) {
      KeyResultHistory keyResultHistory = null;
      for (KeyResultHistory krh : keyResultHistories) {
        if (krh.getDateChanged().equals(currentDate)) {
          keyResultHistory = krh; // Can't use stream filter because java doesn't like it when variables used in lambdas aren't final
        }
      }
      if (keyResultHistory != null) {
        double target = keyResultHistory.getTargetValue() - keyResultHistory.getStartValue();
        double currentValue = keyResultHistory.getCurrentValue() - keyResultHistory.getStartValue();
        if (currentValue == 0) {
          keyResultProgress.add(0.0);
        } else {
          keyResultProgress.add(100 / target * currentValue);
        }
      } else {
        int size = keyResultProgress.size();
        if (size > 0) {
          keyResultProgress.add(keyResultProgress.get(size - 1));
        } else {
          keyResultProgress.add(null); // Adding null to filter out days without any key-result progress for keyResult in later calculations
        }
      }
      currentDate = currentDate.plusDays(1);
    }
    return keyResultProgress;
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

  private LineChartOptionsDto getNoValuesFoundLineChartOptionsDto(ChartCreationOptions chartCreationOptions) {
    ArrayList<LineChartLineKeyValues> lineChartKeyValuesList = new ArrayList<>();

    LineChartLineKeyValues lineChartLineKeyValuesNoValues = new LineChartLineKeyValues();
    lineChartLineKeyValuesNoValues.setName(chartCreationOptions.getTitle());
    lineChartLineKeyValuesNoValues.setData(new ArrayList<>(Collections.singletonList(50.0)));

    lineChartKeyValuesList.add(lineChartLineKeyValuesNoValues);

    LineChartOptionsDto lineChartOptionsDto = new LineChartOptionsDto();
    lineChartOptionsDto.setSeries(lineChartKeyValuesList);

    lineChartOptionsDto.setXAxisCategories(Stream.of("Keine Werte vorhanden").collect(Collectors.toList()));

    return lineChartOptionsDto;
  }
}
