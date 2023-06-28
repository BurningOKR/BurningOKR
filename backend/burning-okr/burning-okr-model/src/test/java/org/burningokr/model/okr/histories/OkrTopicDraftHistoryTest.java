package org.burningokr.model.okr.histories;

import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OkrTopicDraftHistoryTest {

  private OkrTopicDraftHistory okrTopicDraftHistory;
  private OkrTopicDraft unit1;
  private OkrTopicDraft unit2;


  @BeforeEach
  public void init() {
    OkrTopicDraftHistory history1 = new OkrTopicDraftHistory();
    history1.setId(10L);
    OkrTopicDraftHistory history2 = new OkrTopicDraftHistory();
    history2.setId(11L);
    unit1 = new OkrTopicDraft();
    unit1.setHistory(history1);
    unit1.setParentUnit(null);
    unit1.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    unit2 = new OkrTopicDraft();
    unit2.setHistory(history2);
    unit2.setParentUnit(null);
    unit2.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    Collection<OkrTopicDraft> units = new ArrayList<>();
    units.add(unit1);
    units.add(unit2);
    okrTopicDraftHistory = new OkrTopicDraftHistory();
    okrTopicDraftHistory.setUnits(units);
  }

  @Test
  public void clearUnits_shouldClearUnits() {
    Collection<OkrTopicDraft> emptyUnits = new ArrayList<>();
    okrTopicDraftHistory.clearUnits();
    assertEquals(emptyUnits, okrTopicDraftHistory.getUnits());
  }

  @Test
  public void removeUnit_shouldRemoveOneUnitFromUnits() {
    Collection<OkrTopicDraft> expectedUnits = new ArrayList<>();
    expectedUnits.add(unit1);
    okrTopicDraftHistory.removeUnit(unit2);
    assertEquals(expectedUnits, okrTopicDraftHistory.getUnits());
  }
}
