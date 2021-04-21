package org.burningokr.model.okr;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.okr.histories.OkrTopicDraftHistory;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrBranchHistory;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class OkrTopicDraft extends OkrTopicDescription {

  @ManyToOne private OkrUnit parentUnit;

  @ManyToOne private OkrTopicDraftHistory history;
}
