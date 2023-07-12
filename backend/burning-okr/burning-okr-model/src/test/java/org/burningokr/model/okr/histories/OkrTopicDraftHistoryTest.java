package org.burningokr.model.okr.histories;

import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;


@ExtendWith(MockitoExtension.class)
public class OkrTopicDraftHistoryTest {


  @BeforeEach
  public void init() {
    OkrTopicDraftHistory history1 = new OkrTopicDraftHistory();
    history1.setId(10L);
    OkrTopicDraftHistory history2 = new OkrTopicDraftHistory();
    history2.setId(11L);
    OkrTopicDraft unit1 = new OkrTopicDraft();
    unit1.setHistory(history1);
    unit1.setParentUnit(null);
    unit1.setCurrentStatus(OkrTopicDraftStatusEnum.draft);
    OkrTopicDraft unit2 = new OkrTopicDraft();
    unit2.setHistory(history2);
    unit2.setParentUnit(null);
    unit2.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);
    Collection<OkrTopicDraft> units = new ArrayList<>();
    units.add(unit1);
    units.add(unit2);
    OkrTopicDraftHistory okrTopicDraftHistory = new OkrTopicDraftHistory();
    okrTopicDraftHistory.setUnits(units);
  }
}
