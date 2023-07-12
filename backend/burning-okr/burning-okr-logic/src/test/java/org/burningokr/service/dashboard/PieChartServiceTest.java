package org.burningokr.service.dashboard;

import org.burningokr.model.dashboard.ChartCreationOptions;
import org.burningokr.model.dashboard.ChartInformationTypeEnum;
import org.burningokr.model.dashboard.PieChartOptionsDto;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.service.topicDraft.OkrTopicDraftService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PieChartServiceTest {
  @Mock
  private OkrTopicDraftService topicDraftService;

  @InjectMocks
  private PieChartService pieChartService;

  @Test()
  public void buildProgressChart_shouldBuildPieChartWithOneDraft() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    Collection<Long> teamIds = List.of(10L, 20L);
    chartCreationOptions.setTeamIds(teamIds);
    chartCreationOptions.setTitle("TestTitle");

    OkrTopicDraft okrTopicDraft1 = new OkrTopicDraft();
    okrTopicDraft1.setCurrentStatus(OkrTopicDraftStatusEnum.draft);

    when(topicDraftService.getAllTopicDrafts()).thenReturn(List.of(okrTopicDraft1));

    PieChartOptionsDto result = pieChartService.buildTopicDraftOverviewChart(chartCreationOptions);

    assertNotNull(result);
    assertEquals("TestTitle", result.getTitle());
    assertEquals(4, result.getSeries().length);
    assertEquals(1, result.getSeries()[0]);
    assertEquals(0, result.getSeries()[1]);
    assertEquals(0, result.getSeries()[2]);
    assertEquals(0, result.getSeries()[3]);
    assertEquals(4, result.getValueLabels().length);
    assertEquals("Draft", result.getValueLabels()[0]);
    assertEquals("Submitted", result.getValueLabels()[1]);
    assertEquals("Approved", result.getValueLabels()[2]);
    assertEquals("Rejected", result.getValueLabels()[3]);
  }

  @Test()
  public void buildProgressChart_shouldBuildPieChartWithOneOfEachStatus() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    Collection<Long> teamIds = List.of(10L, 20L);
    chartCreationOptions.setTeamIds(teamIds);
    chartCreationOptions.setTitle("TestTitle");

    OkrTopicDraft okrTopicDraft1 = new OkrTopicDraft();
    okrTopicDraft1.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    OkrTopicDraft okrTopicDraft2 = new OkrTopicDraft();
    okrTopicDraft2.setCurrentStatus(OkrTopicDraftStatusEnum.approved);
    OkrTopicDraft okrTopicDraft3 = new OkrTopicDraft();
    okrTopicDraft3.setCurrentStatus(OkrTopicDraftStatusEnum.rejected);
    OkrTopicDraft okrTopicDraft4 = new OkrTopicDraft();
    okrTopicDraft4.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);

    when(topicDraftService.getAllTopicDrafts()).thenReturn(List.of(okrTopicDraft1, okrTopicDraft2, okrTopicDraft3, okrTopicDraft4));

    PieChartOptionsDto result = pieChartService.buildTopicDraftOverviewChart(chartCreationOptions);

    assertNotNull(result);
    assertEquals("TestTitle", result.getTitle());
    assertEquals(4, result.getSeries().length);
    assertEquals(1, result.getSeries()[0]);
    assertEquals(1, result.getSeries()[1]);
    assertEquals(1, result.getSeries()[2]);
    assertEquals(1, result.getSeries()[3]);
    assertEquals(4, result.getValueLabels().length);
    assertEquals("Draft", result.getValueLabels()[0]);
    assertEquals("Submitted", result.getValueLabels()[1]);
    assertEquals("Approved", result.getValueLabels()[2]);
    assertEquals("Rejected", result.getValueLabels()[3]);
  }

  @Test()
  public void buildProgressChart_shouldBuildPieChartWithNoneStatus() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    Collection<Long> teamIds = List.of(10L, 20L);
    chartCreationOptions.setTeamIds(teamIds);
    chartCreationOptions.setTitle("TestTitle");

    when(topicDraftService.getAllTopicDrafts()).thenReturn(new ArrayList<>());

    PieChartOptionsDto result = pieChartService.buildTopicDraftOverviewChart(chartCreationOptions);

    assertNotNull(result);
    assertEquals("TestTitle", result.getTitle());
    assertEquals(4, result.getSeries().length);
    assertEquals(0, result.getSeries()[0]);
    assertEquals(0, result.getSeries()[1]);
    assertEquals(0, result.getSeries()[2]);
    assertEquals(0, result.getSeries()[3]);
    assertEquals(4, result.getValueLabels().length);
    assertEquals("Draft", result.getValueLabels()[0]);
    assertEquals("Submitted", result.getValueLabels()[1]);
    assertEquals("Approved", result.getValueLabels()[2]);
    assertEquals("Rejected", result.getValueLabels()[3]);
  }

  @Test()
  public void buildProgressChart_shouldBuildPieChartWithMuchOfOne() {
    ChartCreationOptions chartCreationOptions = new ChartCreationOptions();
    chartCreationOptions.setChartType(ChartInformationTypeEnum.LINE_PROGRESS);
    Collection<Long> teamIds = List.of(10L, 20L);
    chartCreationOptions.setTeamIds(teamIds);
    chartCreationOptions.setTitle("TestTitle");

    OkrTopicDraft okrTopicDraft1 = new OkrTopicDraft();
    okrTopicDraft1.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    OkrTopicDraft okrTopicDraft2 = new OkrTopicDraft();
    okrTopicDraft2.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    OkrTopicDraft okrTopicDraft3 = new OkrTopicDraft();
    okrTopicDraft3.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    OkrTopicDraft okrTopicDraft4 = new OkrTopicDraft();
    okrTopicDraft4.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);

    when(topicDraftService.getAllTopicDrafts()).thenReturn(List.of(okrTopicDraft1, okrTopicDraft2, okrTopicDraft3, okrTopicDraft4));

    PieChartOptionsDto result = pieChartService.buildTopicDraftOverviewChart(chartCreationOptions);

    assertNotNull(result);
    assertEquals("TestTitle", result.getTitle());
    assertEquals(4, result.getSeries().length);
    assertEquals(3, result.getSeries()[0]);
    assertEquals(1, result.getSeries()[1]);
    assertEquals(0, result.getSeries()[2]);
    assertEquals(0, result.getSeries()[3]);
    assertEquals(4, result.getValueLabels().length);
    assertEquals("Draft", result.getValueLabels()[0]);
    assertEquals("Submitted", result.getValueLabels()[1]);
    assertEquals("Approved", result.getValueLabels()[2]);
    assertEquals("Rejected", result.getValueLabels()[3]);
  }
}
