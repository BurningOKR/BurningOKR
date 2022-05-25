package org.burningokr.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.dashboard.PieChartOptionsDto;
import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.service.topicDraft.OkrTopicDraftService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class PieChartService {
  private final OkrTopicDraftService topicDraftService;

  public PieChartOptionsDto buildTopicDraftOverviewChart(ChartCreationOptions chartCreationOptions) {
    String[] possibleStates = {"Draft", "Submitted", "Approved", "Rejected"};
    Double[] stateCount = new Double[4];
    Collection<OkrTopicDraft> topicDrafts = topicDraftService.getAllTopicDrafts();
    stateCount[0] = (double) topicDrafts.stream()
      .filter(okrTopicDraft -> okrTopicDraft.getCurrentStatus() == OkrTopicDraftStatusEnum.draft)
      .toArray().length;
    stateCount[1] = (double) topicDrafts.stream()
      .filter(okrTopicDraft -> okrTopicDraft.getCurrentStatus() == OkrTopicDraftStatusEnum.submitted)
      .toArray().length;
    stateCount[2] = (double) topicDrafts.stream()
      .filter(okrTopicDraft -> okrTopicDraft.getCurrentStatus() == OkrTopicDraftStatusEnum.approved)
      .toArray().length;
    stateCount[3] = (double) topicDrafts.stream()
      .filter(okrTopicDraft -> okrTopicDraft.getCurrentStatus() == OkrTopicDraftStatusEnum.rejected)
      .toArray().length;

    PieChartOptionsDto pieChartOptionsDto = new PieChartOptionsDto();
    pieChartOptionsDto.setTitle(chartCreationOptions.getTitle());
    pieChartOptionsDto.setChart(chartCreationOptions.getChartType().ordinal());
    pieChartOptionsDto.setValueLabels(possibleStates);

    pieChartOptionsDto.setValueLabels(possibleStates);
    pieChartOptionsDto.setSeries(stateCount);
    pieChartOptionsDto.setChart(ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW.ordinal());

    return pieChartOptionsDto;
  }
}
