package org.burningokr.model.okrUnits;

import org.burningokr.model.okrUnits.okrUnitHistories.OkrBranchHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class OkrBranchTest {

  private OkrBranch original;
  private OkrBranch parentBranch;
  private OkrBranch childBranch;

  @BeforeEach
  public void init() {
    original = new OkrBranch();
    original.setId(4L);
    original.setName("originalName");
    original.setLabel("originalLabel");

    OkrBranchHistory history = new OkrBranchHistory();
    history.setUnits(Collections.singleton(original));

    original.setHistory(history);

    parentBranch = new OkrBranch();
    parentBranch.setId(5L);

    childBranch = new OkrBranch();
    childBranch.setParentOkrUnit(original);

    original.setParentOkrUnit(parentBranch);
    original.setOkrChildUnits(Collections.singleton(childBranch));
  }

  @Test
  public void getCopyWithoutRelation_ExpectNotToBeNull() {
    OkrBranch copy = original.getCopyWithoutRelations();
    assertNotNull(copy);
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    OkrBranch copy = original.getCopyWithoutRelations();

    assertNotEquals(original.getId(), copy.getId());
  }

  @Test
  public void getCopyWithoutRelation_ExpectNameToBeEquals() {
    OkrBranch copy = original.getCopyWithoutRelations();
    assertEquals(original.getName(), copy.getName());
  }

  @Test
  public void getCopyWithoutRelation_ExpectLabelToBeEquals() {
    OkrBranch copy = original.getCopyWithoutRelations();
    assertEquals(original.getLabel(), copy.getLabel());
  }

  @Test
  public void getCopyWithoutRelation_ExpectIsActiveToBeEquals() {
    OkrBranch copy = original.getCopyWithoutRelations();
    assertEquals(original.isActive(), copy.isActive());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    OkrBranch copy = original.getCopyWithoutRelations();

    assertNull(copy.getParentOkrUnit());
    assertEquals(0, copy.getOkrChildUnits().size());
  }

  @Test
  public void getCopyWithoutRelation_ExpectHistoryToBeEquals() {
    OkrBranch copy = original.getCopyWithoutRelations();

    assertSame(original.getHistory(), copy.getHistory());
  }
}
