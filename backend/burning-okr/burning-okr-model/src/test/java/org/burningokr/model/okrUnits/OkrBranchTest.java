package org.burningokr.model.okrUnits;

import org.burningokr.model.okrUnits.okrUnitHistories.OkrBranchHistory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class OkrBranchTest {

  private OkrBranch original;
  private OkrBranch parentBranch;
  private OkrBranch childBranch;

  @Before
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
    Assert.assertNotNull(copy);
  }

  @Test
  public void getCopyWithoutRelations_expectedIdNotCopied() {
    OkrBranch copy = original.getCopyWithoutRelations();

    Assert.assertNotEquals(original.getId(), copy.getId());
  }

  @Test
  public void getCopyWithoutRelation_ExpectNameToBeEquals() {
    OkrBranch copy = original.getCopyWithoutRelations();
    Assert.assertEquals(original.getName(), copy.getName());
  }

  @Test
  public void getCopyWithoutRelation_ExpectLabelToBeEquals() {
    OkrBranch copy = original.getCopyWithoutRelations();
    Assert.assertEquals(original.getLabel(), copy.getLabel());
  }

  @Test
  public void getCopyWithoutRelation_ExpectIsActiveToBeEquals() {
    OkrBranch copy = original.getCopyWithoutRelations();
    Assert.assertEquals(original.isActive(), copy.isActive());
  }

  @Test
  public void getCopyWithoutRelations_expectedNoRelations() {
    OkrBranch copy = original.getCopyWithoutRelations();

    Assert.assertNull(copy.getParentOkrUnit());
    Assert.assertEquals(0, copy.getOkrChildUnits().size());
  }

  @Test
  public void getCopyWithoutRelation_ExpectHistoryToBeEquals() {
    OkrBranch copy = original.getCopyWithoutRelations();

    Assert.assertSame(original.getHistory(), copy.getHistory());
  }
}
