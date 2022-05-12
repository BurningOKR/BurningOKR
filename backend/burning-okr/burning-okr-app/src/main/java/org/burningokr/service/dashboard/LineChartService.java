package org.burningokr.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.dashboard.LineChartLineKeyValues;
import org.burningokr.dto.dashboard.LineChartOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LineChartService {
  private final CompanyService companyService;
  private final KeyResultHistoryService keyResultHistoryService;

  public LineChartOptionsDto buildProgressChart(ChartCreationOptions chartCreationOptions) {
    OkrCompany company = companyService.findById(chartCreationOptions.getDashboardCreation().getCompanyId());
    ArrayList<OkrDepartment> allTeamsOfCompany = new ArrayList<OkrDepartment>(BranchHelper.collectDepartments(company));
    ArrayList<OkrDepartment> teams = new ArrayList<>();

    ArrayList<Objective> objectives = new ArrayList<>();
    ArrayList<KeyResult> keyResults = new ArrayList<>();

    for (OkrDepartment team : allTeamsOfCompany) {
      if (allTeamsOfCompany.stream().anyMatch(companyTeam -> Objects.equals(companyTeam.getId(), team.getId()))) {
        teams.add(team);
        objectives.addAll(team.getObjectives());
      }
    }

    for (Objective objective : objectives) {
      keyResults.addAll(objective.getKeyResults());
    }


    // company.getOkrChildUnits().forEach(department -> department.getObjectives().forEach(objective -> keyResults.addAll(objective.getKeyResults())));

    if (keyResults.size() == 0) {
      return getNoValuesFoundLineChartOptionsDto(chartCreationOptions);
    }

    LocalDate startDate = keyResultHistoryService.findOldestKeyResultHistoryForKeyResultList(keyResults)
      .getDateChanged();
    LocalDate lastDate = keyResultHistoryService.findNewestKeyResultHistoryForKeyResultList(keyResults)
      .getDateChanged();
    long numberOfDays = ChronoUnit.DAYS.between(startDate, lastDate);

    ArrayList<LineChartLineKeyValues> lineChartLineKeyValuesList = new ArrayList<>();

    if (chartCreationOptions.getTeamIds().size() > 0) {
      for (long teamId : chartCreationOptions.getTeamIds()) {
        OkrDepartment team = teams.stream()
          .filter(okrDepartment -> okrDepartment.getId() == teamId)
          .findFirst()
          .orElseThrow(
            () ->
              new EntityNotFoundException(
                "Entity mit id " + teamId + " konnte nicht gefunden werden"));

        LineChartLineKeyValues lineChartLineKeyValues = new LineChartLineKeyValues();
        lineChartLineKeyValues.setName(team.getName());
        lineChartLineKeyValues.setNumber(getProgressForTeam(team, startDate, numberOfDays));
        lineChartLineKeyValuesList.add(lineChartLineKeyValues);
      }
    }

    LineChartOptionsDto lineChartOptionsDto = new LineChartOptionsDto();
    lineChartOptionsDto.setSeries(lineChartLineKeyValuesList);
    lineChartOptionsDto.setXAxisCategories(getProgressXAxis(startDate, numberOfDays));
    return new LineChartOptionsDto();
  }

  private ArrayList<Double> getProgressForTeam(OkrChildUnit okrDepartment, LocalDate startDate, long numberOfDays) {
    ArrayList<Double> teamProgress = new ArrayList<>();
    ArrayList<ArrayList<Double>> objectiveProgressLists = new ArrayList<>();
    ArrayList<Objective> objectives = new ArrayList<Objective>(okrDepartment.getObjectives());
    ArrayList<String> xAxis = new ArrayList<>();

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
        keyResultProgress.add(
          (double) keyResultHistory.getTargetValue() - keyResultHistory.getStartValue() /
            keyResultHistory.getCurrentValue() - keyResultHistory.getStartValue() * 100);
      } else {
        int size = keyResultProgress.size();
        if (size > 0) {
          keyResultProgress.add(keyResultProgress.get(size - 1));
        } else {
          keyResultProgress.add(null); // Adding null to filter out days without any key-result progress in later calculations
        }
      }
      currentDate = currentDate.plusDays(1);
    }
    return keyResultProgress;
  }

  private ArrayList<String> getProgressXAxis(LocalDate startDate, long numberOfDays) {
    ArrayList<String> xAxis = new ArrayList<>();
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
    lineChartLineKeyValuesNoValues.setNumber(Stream.of(50.0).collect(Collectors.toList()));

    lineChartKeyValuesList.add(lineChartLineKeyValuesNoValues);

    LineChartOptionsDto lineChartOptionsDto = new LineChartOptionsDto();
    lineChartOptionsDto.setSeries(lineChartKeyValuesList);

    lineChartOptionsDto.setXAxisCategories(Stream.of("Keine Werte vorhanden").collect(Collectors.toList()));

    return lineChartOptionsDto;
  }
}
